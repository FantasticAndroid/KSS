# ApkSecurityWithJNI

## What is ApkSecurityWithJNI?
Data security can be compromised if you are reading any KEY ex. 'API Key' as statically in your app source code since APK' source code can be easily decompiled by using multiple tools like [dex2rar](https://github.com/pxb1988/dex2jar) and [JD-gui](https://github.com/java-decompiler/jd-gui).

So its much better way to read these KEYs from cpp source (using NDK) and not from JAVA source.

ApkSecurityWithJNI having "lib" as a library module with a “sample” module to demonstrate how to use this "lib".

## Idea of Implementation
You need at least two files in pair 1) cpp file having methods to return your secure data, and 2) relative Kotlin/Java class to call these cpp methods.

As you can see in “lib” method name in cpp must be written in a proper format i.e. “Java_full_package_name_classname_methodname” where the full package name and class name of the relative Kotlin/Java class to call these cpp methods.

Import these classes in your main app i.e. “sample” module and load the generated So by calling System.loadLibrary("native-lib") in application class or activity class before calling AppSecret methods.

### In “lib”
```kotlin
class AppSecret {
    companion object {
        @JvmStatic
        external fun getKeyForEncryptionJNI(): String

        @JvmStatic
        external fun getKeyForDecryptionJNI(): String
    }
}

```cpp
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

### “In sample module”
```kotlin
class MainApp:Application() {
    override fun onCreate() {
        super.onCreate()
        initNativeLib()
    }

    private fun initNativeLib(){
       System.loadLibrary("native-lib")
    }
}

