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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.payasyoustay.payasyoustay.object.DeviceContent;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class DeviceActivity extends AppCompatActivity {

    private DeviceContent.Device mDevice;
    private String mRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("position")) {
            int position = bundle.getInt("position");
            mRole = bundle.getString("role");
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
        if (mRole.equals("guest")) {
            remainingTimeView.setText(mDevice.remainingTime.toString() + " h");
        }
        else {
            LinearLayout manageCredit = (LinearLayout) findViewById(R.id.manageCredit);
            manageCredit.setVisibility(View.INVISIBLE);
        }


    }

    public void addCredit(View view) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new TimePickerDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setTitle("Add Credit for " + mDevice.name);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.payment, null);
        final NumberPicker hourPicker = (NumberPicker)dialogView.findViewById(R.id.time_hour);
        hourPicker.setMaxValue(48);
        final NumberPicker minutePicker = (NumberPicker)dialogView.findViewById(R.id.time_minute);
        minutePicker.setMaxValue(59);
        final TextView priceView = (TextView) dialogView.findViewById(R.id.price);
        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updatePrice(hourPicker.getValue(), minutePicker.getValue(), priceView);
            }
        });
        minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updatePrice(hourPicker.getValue(), minutePicker.getValue(), priceView);
            }
        });

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
                    Integer timeCredit = hourPicker.getValue() * 60 + minutePicker.getValue();
                    json.put("timeCredit", timeCredit / 60);

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
                    new Api(json).execute("creditcardpay");

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

    public void refresh(MenuItem item) {
        new Api().execute("getCounters");
        TextView remainingTimeView = (TextView)findViewById(R.id.device_remaing_time);
        if (remainingTimeView != null && mRole.equals("guest")) {
            remainingTimeView.setText(mDevice.remainingTime.toString() + " h");
        }
        else {
            LinearLayout manageCredit = (LinearLayout) findViewById(R.id.manageCredit);
            manageCredit.setVisibility(View.INVISIBLE);
        }
    }

    public void activate(View view) {
        new Api().execute("activate");
    }

    public void stop(View view) {
        new Api().execute("stop");
    }

    public void updatePrice(int hour, int minute, TextView priceView) {

        // Time Credit
        Integer timeCredit = hour * 60 + minute;

        // Price amount
        Double amount = timeCredit / 60 * mDevice.pricePerHour;
        priceView.setText(String.format("%.2f $", amount));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device, menu);
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
        public Api() {
            mJson = new JSONObject();
        }

        protected JSONObject doInBackground(String... services) {
            try {

                if (services[0].equals("creditcardpay")) {
                    RestClient restClient = new RestClient(mJson);
                    JSONObject json = restClient.post("/ws/api/v1/pay/creditcardpay");
                    return json;
                }
                else if(services[0].equals("activate")) {
                    RestClient restClient = new RestClient();
                    JSONObject json = restClient.get("/ws/api/v1/dev/deviceActivate/" + mDevice.id + "/" + mDevice.remainingTime.toString());
                }
                else if(services[0].equals("stop")) {
                    RestClient restClient = new RestClient();
                    JSONObject json = restClient.get("/ws/api/v1/dev/deviceDeactivate/" + mDevice.id);
                }
                else if (services[0].equals("getCounters")) {
                    RestClient restClient = new RestClient();
                    JSONObject jsonCoutners = restClient.get("/ws/api/v1/counter/listCounters");
                    DeviceContent.updateCounters(jsonCoutners);
                    return jsonCoutners;
                }
                return new JSONObject();
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
