package app.serviceconnector.activities

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import app.serviceconnector.services.DemoBoundForegroundService
import app.serviceconnector.services.DemoBoundIntentService
import app.serviceconnector.services.DemoBoundService

class ServiceSelectionActivity : ListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listView.adapter = ArrayAdapter(
            application,
            android.R.layout.simple_list_item_1,
            arrayOf(
                "Bound Service Demo",
                "Bound Foreground Service Demo",
                "Bound Intent Service Demo"
            )
        )

        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                var service = ""
                when (position) {
                    0 -> service = DemoBoundService::class.java.name
                    1 -> service = DemoBoundForegroundService::class.java.name
                    2 -> service = DemoBoundIntentService::class.java.name
                }
                val intent = Intent(application, ServiceLauncherActivity::class.java)
                intent.putExtra("SERVICE", service)
                startActivity(intent)
                //finish()
            }
    }
}
