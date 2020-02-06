package lib.serviceconnector;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import static lib.serviceconnector.ServiceManager.MessageReceiver.*;
/**
 * Created by Lenovo7 on 8/23/2015.
 */
public abstract class BoundIntentService extends IntentService {

    // Target we publish for clients to send messages to IncomingHandler.
    protected final Messenger mReceivingMessenger = new Messenger(new IncomingHandler());

    protected Messenger mSenderMessenger;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BoundIntentService(String name) {
        super(name);
    }

    protected abstract void onMessageReceivedFromUI(Message message);

    protected boolean mIsServiceBounded = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public final int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
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
        return mReceivingMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mIsServiceBounded = false;
        return super.onUnbind(intent);
    }
}
