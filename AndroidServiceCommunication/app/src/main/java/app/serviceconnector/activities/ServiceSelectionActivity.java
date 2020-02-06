package app.serviceconnector.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import app.serviceconnector.services.DemoBoundForgroundService;
import app.serviceconnector.services.DemoBoundIntentService;
import app.serviceconnector.services.DemoBoundService;

public final class ServiceSelectionActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView lv = getListView();

        lv.setAdapter(new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, new String[]{
                "Bound Service Demo", "Bound Foreground Service Demo", "Bound Intent Service Demo"
        }));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String service = "";
                switch (position) {
                    case 0:
                        service = DemoBoundService.class.getName();
                        break;
                    case 1:
                        service = DemoBoundForgroundService.class.getName();
                        break;
                    case 2:
                        service = DemoBoundIntentService.class.getName();
                        break;
                }
                Intent intent = new Intent(getApplication(), ServiceLauncherActivity.class);
                intent.putExtra("SERVICE", service);
                startActivity(intent);
                finish();
            }
        });
    }
}
