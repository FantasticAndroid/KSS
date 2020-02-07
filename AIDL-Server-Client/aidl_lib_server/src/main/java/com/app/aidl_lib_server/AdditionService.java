package com.app.aidl_lib_server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class AdditionService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return mIMyAidlInterfaceBinder;
    }

    private final IMyAidlInterface.Stub mIMyAidlInterfaceBinder = new IMyAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }

        @Override
        public int add(int num1, int num2) throws RemoteException {
            return num1 + num2;
        }

        @Override
        public MyRect getNewRect(int top, int left, int right, int bottom) throws RemoteException {
            return new MyRect(top, left, right, bottom);
        }

        @Override
        public void getNewRectangle(int top, int left, int right, int bottom, IResponseCallback callback) throws RemoteException {
            callback.newRect(new MyRect(top, left, right, bottom));
        }
    };
}
