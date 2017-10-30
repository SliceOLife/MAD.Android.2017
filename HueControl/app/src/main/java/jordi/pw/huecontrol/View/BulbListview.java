package jordi.pw.huecontrol.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import jordi.pw.huecontrol.Adapters.BridgeAdapter;
import jordi.pw.huecontrol.Adapters.BulbAdapter;
import jordi.pw.huecontrol.Interfaces.BulbsLoadedCallback;
import jordi.pw.huecontrol.Models.Bridge;
import jordi.pw.huecontrol.Models.Bulb;
import jordi.pw.huecontrol.R;

public class BulbListview extends AppCompatActivity implements AdapterView.OnItemClickListener, BulbsLoadedCallback{
    private ListView bulbList;
    private BulbAdapter adapter;
    private Bridge bridge;

    private static final String TAG = "BulbListview";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulb_listview);

        bulbList = (ListView) findViewById(R.id.listviewBulb);
        bulbList.setOnItemClickListener(this);

        // Retrieve bridge from intent
        this.bridge = getIntent().getParcelableExtra("bridge");

        this.adapter = new BulbAdapter(getApplicationContext(), LayoutInflater.from(getApplicationContext()), this.bridge.getBulbs());
        bulbList.setAdapter(this.adapter);


        // Call bulb retrieval on bridge and pass callback
        requestBulbs();
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestBulbs();
    }

    private void requestBulbs() {
        try {
            bridge.getAvailableBulbs(this);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getApplicationContext(), BulbDetail.class);
        Bulb bulb = this.bridge.getBulbs().get(i);

        // Pass bulb into detail view
        intent.putExtra("bulb", bulb);

        startActivity(intent);
    }

    @Override
    public void onBulbsLoaded() {
        // Implement bulb adapter
        this.adapter.notifyDataSetChanged();
    }
}
