package com.payasyoustay.payasyoustay.object;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DeviceContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<Device> ITEMS = new ArrayList<Device>();


    /*static {
        // Add 3 sample items.
        addItem(new Device("Living room's light", "light", 0.5, 60));
        addItem(new Device("AC", "ac", 1.0, 30));
    }*/

    public static void update(JSONObject json) throws JSONException {
        ITEMS.clear();
        Iterator<?> keys = json.keys();

        while( keys.hasNext() ) {
            String key = (String)keys.next();
            for (int i = 0 ; i < json.getJSONArray(key).length() ; i++) {
                JSONObject device = json.getJSONArray(key).getJSONObject(i);
                Log.d("DeviceContent", device.toString());
                addItem(new Device(device.getString("ID"), device.getString("NAME"), device.getString("TYPE"), 0.5, 50));

            }
        }
    }

    private static void addItem(Device item) {
        ITEMS.add(item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class Device {
        public String id;
        public String name;
        public String type;
        public Integer remainingTime;
        public Double pricePerHour;

        public Device(String id, String name, String type, Double pricePerSecond, Integer remainingTime) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.pricePerHour = pricePerSecond;
            this.remainingTime = remainingTime;

        }

        @Override
        public String toString() {
            return name + "\n" + remainingTime.toString() + " m remaining";
        }
    }
}
