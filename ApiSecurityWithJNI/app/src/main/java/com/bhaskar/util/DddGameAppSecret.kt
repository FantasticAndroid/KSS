package com.bhaskar.util

class DddGameAppSecret {
    companion object {
        @JvmStatic
        external fun getApiKeyForEncryptionJNI(): String

        @JvmStatic
        external fun getApiKeyForDecryptionJNI(): String
    }
}