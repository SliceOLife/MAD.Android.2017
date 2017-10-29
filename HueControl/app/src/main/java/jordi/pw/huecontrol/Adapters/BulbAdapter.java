package jordi.pw.huecontrol.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import jordi.pw.huecontrol.Models.Bulb;
import jordi.pw.huecontrol.R;

/**
 * Created by jordi on 10/27/17.
 */

public class BulbAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Bulb> bulbs;

    public BulbAdapter(Context context, LayoutInflater inflater, ArrayList<Bulb> bulbs) {
        this.context = context;
        this.inflater = inflater;
        this.bulbs = bulbs;
    }

    @Override
    public int getCount() {
        return bulbs.size();
    }

    @Override
    public Object getItem(int i) {
        return bulbs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = inflater.inflate(R.layout.listview_row_bulb, null);

            viewHolder = new ViewHolder();
            viewHolder.name = view.findViewById(R.id.textviewBulbName);
            viewHolder.state = view.findViewById(R.id.switchBulbState);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Bulb bulb = bulbs.get(i);
        viewHolder.name.setText(bulb.getName());
        viewHolder.state.setChecked(bulb.isState());
        viewHolder.state.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("BULBADAPTER", "HEY IM CHANGED B");
                bulb.toggle();
            }
        });

        return view;
    }

    private static class ViewHolder {
        public TextView name;
        public Switch state;
    }
}
