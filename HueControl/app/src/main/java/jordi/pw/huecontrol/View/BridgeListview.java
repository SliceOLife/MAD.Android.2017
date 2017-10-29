package jordi.pw.huecontrol.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import jordi.pw.huecontrol.Adapters.BridgeAdapter;
import jordi.pw.huecontrol.Models.Bridge;
import jordi.pw.huecontrol.R;

public class BridgeListview extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ArrayList<Bridge> availableBridges;
    private ListView bridgeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_listview);

        bridgeList = (ListView) findViewById(R.id.listviewBridge);
        bridgeList.setOnItemClickListener(this);

        // Load sample bridges
        loadSampleBridges();

        final BridgeAdapter bridgeAdapter = new BridgeAdapter(getApplicationContext(), LayoutInflater.from(getApplicationContext()), availableBridges);
        bridgeList.setAdapter(bridgeAdapter);

        bridgeAdapter.notifyDataSetChanged();
    }

    private void loadSampleBridges() {
        availableBridges = new ArrayList<>();

        this.availableBridges.add(new Bridge("Emulator", "10.0.2.2:8000", "newdeveloper"));
        this.availableBridges.add(new Bridge("LA134", "192.168.1.179", "M4MLKGnNIs-FIcksCcAGGGt-Kjb3hXpEkMUFEIco"));
        this.availableBridges.add(new Bridge("Avans Ground Floor", "145.48.205.33", "iYrmsQq1wu5FxF9CPqpJCnm1GpPVylKBWDUsNDhB"));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getApplicationContext(), BulbListview.class);
        Bridge bridge = this.availableBridges.get(i);

        // Pass bridge into bulb list view.
        intent.putExtra("bridge", bridge);

        startActivity(intent);
    }
}