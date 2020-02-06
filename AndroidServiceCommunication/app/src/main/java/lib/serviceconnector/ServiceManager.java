package lib.serviceconnector;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

import static lib.serviceconnector.ServiceManager.MessageReceiver.*;

import java.lang.ref.WeakReference;

/**
 * Created by Lenovo7 on 8/23/2015.
 */
public final class ServiceManager {

    private Intent mServiceIntent;
    private WeakReference<DialServiceConnection> mDialServiceConnWfr;
    private Messenger mSenderToService;
    private Activity mActivity;
    private MessageReceiver mMessageReceiver;
    private final Messenger mMessengerReceiver = new Messenger(new ServiceReceiverHandler());

    public interface MessageReceiver {

        int SERVICE_CONNECTED = 1;
        int SERVICE_DISCONNECT = 2;
        int SERVICE_STOP = 3;
        int SERVICE_SENT_DATA = 10;

        void onMessageReceivedFromService(Message message);

        void onMessageReceivedFromService(Object object);
    }

    /***
     * @param activity
     * @param messageReceiver
     */
    public ServiceManager(Activity activity, MessageReceiver messageReceiver) {
        mActivity = activity;
        mMessageReceiver = messageReceiver;
    }

    /***
     * @param className
     */
    public void startService(Class className) {

        mDialServiceConnWfr = new WeakReference<DialServiceConnection>(new DialServiceConnection());
        mServiceIntent = new Intent(mActivity, className);
        mActivity.startService(mServiceIntent);
        mActivity.bindService(mServiceIntent, mDialServiceConnWfr.get(), Context.BIND_AUTO_CREATE);
    }

    /***
     * @param message
     */
    public void sendMessageToService(Message message) {
        try {
            if (mSenderToService != null) {
                message.replyTo = mMessengerReceiver;
                mSenderToService.send(message);
            } else {
                Toast.makeText(mActivity, "Sender is undefined", Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /***
     * @param message
     */
    public void sendMessageToService(String message) {
        try {
            if (mSenderToService != null) {
                Message msg = new Message();
                msg.obj = message;
                msg.replyTo = mMessengerReceiver;
                mSenderToService.send(msg);
            } else {
                Toast.makeText(mActivity, "Sender is undefined", Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private class DialServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName className, IBinder service) {
            mSenderToService = new Messenger(service);
            Message msg = Message.obtain(null, SERVICE_CONNECTED);
            sendMessageToService(msg);
        }

        /***
         * @param className
         */
        public void onServiceDisconnected(ComponentName className) {
            //Toast.makeText(mActivity.get, "SERVICE_CONNECTED", Toast.LENGTH_SHORT).show();
        }
    }

    private class ServiceReceiverHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case SERVICE_SENT_DATA:
                        if (mMessageReceiver != null) {
                            mMessageReceiver.onMessageReceivedFromService(msg.obj);
                        }
                        break;
                    case SERVICE_DISCONNECT:
                        disconnectedToService();
                        break;
                    case SERVICE_STOP:
                        stopService();
                        break;
                    default:
                        if (mMessageReceiver != null) {
                            mMessageReceiver.onMessageReceivedFromService(msg);
                        }
                        break;
                }
            }
        }
    }

    public void disconnectedToService() {
        try {
            sendMessageToService(Message.obtain(null, SERVICE_DISCONNECT));
            mActivity.unbindService(mDialServiceConnWfr.get());
            mDialServiceConnWfr.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopService() {
        try {
            sendMessageToService(Message.obtain(null, SERVICE_STOP));
            mActivity.unbindService(mDialServiceConnWfr.get());
            mActivity.stopService(mServiceIntent);
            mDialServiceConnWfr.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
