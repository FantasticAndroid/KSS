// IMyAidlInterface.aidl
package com.app.aidl_lib_server;

import com.app.aidl_lib_server.MyRect;
import com.app.aidl_lib_server.IResponseCallback;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

            int add(int num1, int num2);

            MyRect getNewRect(int top,int left, int right, int bottom);

            void getNewRectangle(int top,int left, int right, int bottom,in IResponseCallback callback);
}
