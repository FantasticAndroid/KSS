# AIDL-Server-Client

## What is AIDL-Server-Client?
1. Its a demonstration of Android Interface Definition Language (AIDL) used for InterProcess Communication means to make communication between two apps. To know more about AIDL (https://developer.android.com/guide/components/aidl)

2. “aidl_lib_server” module contains the Service with Stub. Stub is generated via aidl implementation written in “aidl” package. Stub methods are needed to override by desired logic to be export for the client app.

3. Developer should not add implementation logic for these overridden methods while attaching “aidl_lib_server” to “aidl_lib_client” because of “aidl_lib_client” only need “aidl_lib_server” just to compile app only.

4. Developer must have the same package structure for “aidl_lib_server” and “aidl_app_server”.

