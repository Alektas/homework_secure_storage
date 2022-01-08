package com.otus.securehomework.data.utils.encryption

private const val ALGORITHM = "AES"
private const val KEY_SIZE = 256
private const val TRANSFORMATION = "AES/CBC/PKCS5PADDING"

data class EncryptionParams(
    val algorithm: String = ALGORITHM,
    val keySize: Int = KEY_SIZE,
    val transformation: String = TRANSFORMATION,
)
