package jordi.pw.huecontrol.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import jordi.pw.huecontrol.Models.Bridge;
import jordi.pw.huecontrol.R;

/**
 * Created by jordi on 10/27/17.
 */

public class BridgeAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Bridge> bridges;

    public BridgeAdapter(Context context, LayoutInflater inflater, ArrayList<Bridge> bridges) {
        this.context = context;
        this.inflater = inflater;
        this.bridges = bridges;
    }

    @Override
    public int getCount() {
        return bridges.size();
    }

    @Override
    public Object getItem(int i) {
        return bridges.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.listview_row_bridge, null);

            viewHolder = new ViewHolder();
            viewHolder.name = view.findViewById(R.id.textviewBridgeName);
            viewHolder.endpoint = view.findViewById(R.id.textviewBridgeEndpoint);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Bridge bridge = bridges.get(i);
        viewHolder.name.setText(bridge.getName());
        viewHolder.endpoint.setText(bridge.getEndpoint());

        return view;
    }

    private static class ViewHolder {
        public TextView name;
        public TextView endpoint;
    }
}
