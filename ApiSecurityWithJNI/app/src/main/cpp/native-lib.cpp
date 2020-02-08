#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_bhaskar_util_DddGameAppSecret_getApiKeyForEncryptionJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string key = "E$6%C7CF8&8044BA25@96BFC#367FF%hD00aqE0Dq4";
    return env->NewStringUTF(key.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_bhaskar_util_DddGameAppSecret_getApiKeyForDecryptionJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string key = "D&8C%C3&F46$E128@1E0&BA70we928$C45#E295F3A";
    return env->NewStringUTF(key.c_str());
}

/*extern "C" JNIEXPORT jstring JNICALL
Java_com_bhaskar_darwin_ui_dbkbc_constants_KbcApiConstants_getApiKeyForEncryptionJNI(
        JNIEnv* env,
        jobject *//* this *//*) {
    std::string key = "E$6%C7CF8&8044BA25@96BFC#367FF%hD00aqE0Dq4";
    return env->NewStringUTF(key.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_bhaskar_darwin_ui_dbkbc_constants_KbcApiConstants_getApiKeyForDecryptionJNI(
        JNIEnv* env,
        jobject *//* this *//*) {
    std::string key = "D&8C%C3&F46$E128@1E0&BA70we928$C45#E295F3A";
    return env->NewStringUTF(key.c_str());
}*/
