package com.payasyoustay.payasyoustay;

import android.app.ListActivity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.payasyoustay.payasyoustay.object.DeviceContent;
import com.payasyoustay.payasyoustay.object.HouseContent;

import java.util.List;

public class DeviceListActivity extends ListActivity implements AbsListView.OnItemClickListener {

    private HouseContent.House mHouse;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devise_list);

        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt("position");
        mHouse = HouseContent.ITEMS.get(position);

        mAdapter = new DeviceArrayAdapter(this, DeviceContent.ITEMS);
        setListAdapter(mAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_house, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public class DeviceArrayAdapter extends ArrayAdapter<DeviceContent.Device> {
        private final Context context;
        private final List<DeviceContent.Device> values;

        public DeviceArrayAdapter(Context context, List<DeviceContent.Device> values) {
            super(context, R.layout.device_row, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.device_row, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.device);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
            DeviceContent.Device device = values.get(position);
            textView.setText(device.toString());


            if (device.type == "ac") {
                imageView.setImageResource(R.drawable.ac);
            } else {
                imageView.setImageResource(R.drawable.light);
            }
            return rowView;
        }
    }
}