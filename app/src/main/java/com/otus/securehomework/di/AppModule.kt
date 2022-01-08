package com.otus.securehomework.di

import android.content.Context
import com.otus.securehomework.data.dto.LoginResponse
import com.otus.securehomework.data.dto.User
import com.otus.securehomework.data.repository.AuthRepository
import com.otus.securehomework.data.repository.UserRepository
import com.otus.securehomework.data.source.local.UserPreferences
import com.otus.securehomework.data.source.network.AuthApi
import com.otus.securehomework.data.source.network.UserApi
import com.otus.securehomework.data.utils.encryption.EncryptionParams
import com.otus.securehomework.data.utils.encryption.Encryptor
import com.otus.securehomework.data.utils.encryption.EncryptorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.ResponseBody
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    @Singleton
    fun bindEncryptor(impl: EncryptorImpl): Encryptor

    companion object {

        @Provides
        fun provideEncryptionParams(): EncryptionParams = EncryptionParams()

        @Singleton
        @Provides
        fun provideRemoteDataSource(): RemoteDataSource {
            return RemoteDataSource()
        }

        @Provides
        fun provideAuthApi(
            remoteDataSource: RemoteDataSource,
            @ApplicationContext context: Context,
            encryptor: Encryptor,
            userPreferences: UserPreferences,
        ): AuthApi {
            return object : AuthApi {
                override suspend fun login(email: String, password: String): LoginResponse {
                    return LoginResponse(
                        User(
                            -1,
                            "Name",
                            "test@mail.com",
                            "now",
                            "now",
                            "now",
                            "qwerty",
                            "wasd"
                        )
                    )
                }

                override suspend fun logout(): ResponseBody {
                    TODO("Not yet implemented")
                }

            }
            return remoteDataSource.buildApi(AuthApi::class.java, context, encryptor, userPreferences)
        }

        @Provides
        fun provideUserApi(
            remoteDataSource: RemoteDataSource,
            @ApplicationContext context: Context,
            encryptor: Encryptor,
            userPreferences: UserPreferences,
        ): UserApi {
            return remoteDataSource.buildApi(UserApi::class.java, context, encryptor, userPreferences)
        }

        @Singleton
        @Provides
        fun provideUserPreferences(
            @ApplicationContext context: Context,
            encryptor: Encryptor,
        ): UserPreferences {
            return UserPreferences(context, encryptor)
        }

        @Provides
        fun provideAuthRepository(
            authApi: AuthApi,
            userPreferences: UserPreferences
        ): AuthRepository {
            return AuthRepository(authApi, userPreferences)
        }

        @Provides
        fun provideUserRepository(
            userApi: UserApi
        ): UserRepository {
            return UserRepository(userApi)
        }
    }

}