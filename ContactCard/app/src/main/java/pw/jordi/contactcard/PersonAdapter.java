package pw.jordi.contactcard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jordi on 9/11/17.
 */

public class PersonAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflator;
    ArrayList mPersonArrayList;

    public PersonAdapter(Context context, LayoutInflater layoutInflater, ArrayList<Person> personArrayList) {
        mContext = context;
        mInflator = layoutInflater;
        mPersonArrayList = personArrayList;
    }

    @Override
    public int getCount() {
        int size = mPersonArrayList.size();
        Log.i("getCount()", "=" + size);
        return size;
    }

    @Override
    public Object getItem(int position) {
        Log.i("getItem()", "");
        return mPersonArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        // Create new of gebruik een al bestaande (recycled by Android)
        if (convertView == null) {

            //
            convertView = mInflator.inflate(R.layout.personlist_row, null);

            //
            viewHolder = new ViewHolder();
            viewHolder.avatarView = convertView.findViewById(R.id.ivAvatarRow);
            viewHolder.name = convertView.findViewById(R.id.tvNameRow);

            //
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // En nu de viewHolder invullen met de juiste persons
        Person person = (Person) mPersonArrayList.get(position);

        viewHolder.name.setText(String.format("%s %s", person.firstName, person.lastName));
        viewHolder.avatarView.setImageBitmap(person.avatar);

        return convertView;
    }

    private static class ViewHolder {
        public ImageView avatarView;
        public TextView name;
    }
}

