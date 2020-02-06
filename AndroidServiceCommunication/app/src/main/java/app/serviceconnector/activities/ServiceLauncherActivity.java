package app.serviceconnector.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import app.serviceconnector.R;
import lib.serviceconnector.ServiceManager;

public final class ServiceLauncherActivity extends Activity implements View.OnClickListener {

    private ServiceManager mServiceManager;
    private Class service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        mServiceManager = new ServiceManager(this, new ServiceManager.MessageReceiver() {
            @Override
            public void onMessageReceivedFromService(Object object) {
                if (object != null) {
                    Toast.makeText(getApplicationContext(), object + "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMessageReceivedFromService(Message message) {
                if (message != null) {
                    Toast.makeText(getApplicationContext(), message + "", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.btn_send).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String serviceName = bundle.getString("SERVICE");
            try {
                service = Class.forName(serviceName);
                mServiceManager.startService(service);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        mServiceManager.sendMessageToService("Message sent from UI to " + service.getSimpleName());
    }

    @Override
    protected void onDestroy() {
        mServiceManager.disconnectedToService();
        super.onDestroy();
    }
}
