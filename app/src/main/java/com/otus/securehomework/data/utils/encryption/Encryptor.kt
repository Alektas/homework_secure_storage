package com.otus.securehomework.data.utils.encryption

interface Encryptor {
    fun encrypt(plainText: String): EncryptionOutput
    fun decrypt(encryption: String, initVector: ByteArray): String
}