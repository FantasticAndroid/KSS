#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL

Java_com_android_ndk_AppSecret_getKeyForEncryptionJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string key = "This is your secure Key for Encryption";
    return env->NewStringUTF(key.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_android_ndk_AppSecret_getKeyForDecryptionJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string key = "This is your secure Key for Decryption";
    return env->NewStringUTF(key.c_str());
}