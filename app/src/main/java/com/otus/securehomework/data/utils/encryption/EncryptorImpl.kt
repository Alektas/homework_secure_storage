package com.otus.securehomework.data.utils.encryption

import android.util.Log
import java.security.AlgorithmParameters
import java.security.Key
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject

class EncryptorImpl @Inject constructor(
    private val params: EncryptionParams
) : Encryptor {

    private fun generateKey(): Key =
        KeyGenerator.getInstance(params.algorithm)
            .apply { init(params.keySize) }
            .generateKey()
            .also { Log.d("Encryptor", "generate key = ${it.encoded.contentToString()}") }

    override fun encrypt(plainText: String): EncryptionOutput {
        Log.d("Encryptor", "plain = $plainText")
        val cipher = Cipher.getInstance(params.transformation)
            .apply { init(Cipher.ENCRYPT_MODE, generateKey()) }

        return with(cipher) {
            EncryptionOutput(
                encryption = doFinal(plainText.toByteArray()).decodeToString()
                    .also { Log.d("Encryptor", "plain = $plainText, encrypt = $it, iv = $iv") },
                initVector = iv
            )
        }
    }

    override fun decrypt(encryption: String, initVector: ByteArray): String {
        Log.d("Encryptor", "encryption = $encryption")
        return Cipher.getInstance(params.transformation)
            .apply { init(Cipher.DECRYPT_MODE, generateKey(), IvParameterSpec(initVector)) }
            .doFinal(encryption.toByteArray())
            .toString()
            .also { Log.d("Encryptor", "encryption = $encryption, decrypt = $it") }
    }

}
