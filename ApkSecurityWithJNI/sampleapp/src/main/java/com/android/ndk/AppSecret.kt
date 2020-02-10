package com.android.ndk

class AppSecret {
    companion object {
        @JvmStatic
        external fun getKeyForEncryptionJNI(): String

        @JvmStatic
        external fun getKeyForDecryptionJNI(): String
    }
}