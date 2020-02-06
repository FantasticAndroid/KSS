package com.app.aidl_lib_server;

import com.app.aidl_lib_server.MyRect;

interface IResponseCallback {
    void newRect(in MyRect rect);
}
