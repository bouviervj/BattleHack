package com.payasyoustay.payasyoustay;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.payasyoustay.payasyoustay.object.DeviceContent;

import org.json.JSONObject;

public class DeviceActivity extends AppCompatActivity {

    DeviceContent.Device mDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("position")) {
            int position = bundle.getInt("position");
            mDevice = DeviceContent.ITEMS.get(position);
        }
        setContentView(R.layout.activity_device);
        ImageView imageView = (ImageView)findViewById(R.id.device_icon);
        if (mDevice.type.equals("ac")) {
            imageView.setImageResource(R.drawable.ac);
        }
        else {
            imageView.setImageResource(R.drawable.light);
        }
        TextView deviceNameView = (TextView)findViewById(R.id.device_name);
        deviceNameView.setText(mDevice.name);
        TextView priceView = (TextView)findViewById(R.id.price_per_second);
        priceView.setText(mDevice.pricePerHour.toString() + "$/h");
        TextView remainingTimeView = (TextView)findViewById(R.id.device_remaing_time);
        remainingTimeView.setText(mDevice.remainingTime.toString() + " m");

    }

    public void addCredit(View view) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new TimePickerDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setTitle("Add Credit for " + mDevice.name);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.payment, null);
        NumberPicker hourPicker = (NumberPicker)dialogView.findViewById(R.id.time_hour);
        hourPicker.setMaxValue(48);
        NumberPicker minutePicker = (NumberPicker)dialogView.findViewById(R.id.time_minute);
        minutePicker.setMaxValue(60);
        NumberPicker expMonthPicker = (NumberPicker)dialogView.findViewById(R.id.exp_month);
        expMonthPicker.setMaxValue(12);
        expMonthPicker.setMinValue(1);
        expMonthPicker.setValue(9);
        NumberPicker expYearPicker = (NumberPicker)dialogView.findViewById(R.id.exp_year);
        expYearPicker.setMaxValue(2050);
        expYearPicker.setMinValue(2015);
        builder.setView(dialogView);
        builder.setPositiveButton("Pay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    JSONObject json = new JSONObject();
                    //Device Name
                    json.put("DeviceID", mDevice.name);

                    // Time Credit
                    NumberPicker hourPicker = (NumberPicker)dialogView.findViewById(R.id.time_hour);
                    NumberPicker minutePicker = (NumberPicker)dialogView.findViewById(R.id.time_minute);
                    Integer timeCredit = hourPicker.getValue() + minutePicker.getValue() / 60;
                    json.put("timeCredit", timeCredit);

                    // Price amount
                    Double amount = timeCredit / 60 * mDevice.pricePerHour;
                    json.put("amountToPay", String.format("%.2f", amount));

                    // Credit card number
                    TextView ccnView = (TextView)dialogView.findViewById(R.id.ccn);
                    json.put("creditCardNumber", ccnView.getText());

                    // CC Exp
                    NumberPicker expMonthPicker = (NumberPicker)dialogView.findViewById(R.id.exp_month);
                    NumberPicker expYearPicker = (NumberPicker)dialogView.findViewById(R.id.exp_year);
                    String expiration = String.format("%02d", expMonthPicker.getValue()) + "/" + Integer.toString(expYearPicker.getValue());
                    json.put("creditCardExpiration", expiration);

                    Log.d("DeviceActivity", "Json POST " + json.toString());

                    // Post
                    new Api(json).execute("/ws/api/v1/pay/creditcardpay");

                }
                catch(Exception e) {
                    Log.d("DeviceActivity", e.toString());
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_device, menu);
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
    public class Api extends AsyncTask<String, Void, JSONObject> {

        JSONObject mJson;

        public Api(JSONObject json) {
            mJson = json;
        }

        protected JSONObject doInBackground(String... urls) {
            try {

                RestClient restClient = new RestClient(mJson);
                JSONObject json = restClient.post(urls[0]);
                return json;
            } catch (Exception e) {
                Log.e("DeviceList", e.toString());
            }
            return new JSONObject();
        }

        protected void onPostExecute(JSONObject result) {
            Log.d("DeviceList", "onPostExecute");
        }
    }
}
