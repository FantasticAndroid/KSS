package lib.serviceconnector;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

import static lib.serviceconnector.ServiceManager.MessageReceiver.*;
/**
 * Created by Lenovo7 on 8/23/2015.
 */
public abstract class BoundService extends Service {

    // Target we publish for clients to send messages to IncomingHandler.
    protected final Messenger mReceivingMessenger = new Messenger(new IncomingHandler());

    protected Messenger mSenderMessenger;

    protected abstract void onMessageReceivedFromUI(Message message);

    protected boolean mIsServiceBounded = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private class IncomingHandler extends Handler { // Handler of incoming messages from clients.
        @Override
        public void handleMessage(Message message) {

            if (message.what == SERVICE_CONNECTED) {
                mSenderMessenger = message.replyTo;
            }
            onMessageReceivedFromUI(message);
        }
    }

    /***
     * @param message
     */
    protected final void sendMessageToUI(String message) {
        try {
            mSenderMessenger.send(Message.obtain(null, SERVICE_SENT_DATA, message));
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /***
     * @param message
     */
    protected final void sendMessageToUI(Message message) {
        try {
            mSenderMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        mIsServiceBounded = true;
        Toast.makeText(getApplication(), "onBind", Toast.LENGTH_SHORT).show();
        return mReceivingMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(getApplication(), "onUnbind", Toast.LENGTH_SHORT).show();
        mIsServiceBounded = false;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplication(), "onDestroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
}
