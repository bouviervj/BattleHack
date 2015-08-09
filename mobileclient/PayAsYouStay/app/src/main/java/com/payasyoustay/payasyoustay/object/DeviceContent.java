package com.payasyoustay.payasyoustay.object;

import java.util.ArrayList;
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


    static {
        // Add 3 sample items.
        addItem(new Device("Living room's light", "light", 60));
        addItem(new Device("AC", "ac", 30));
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

        public Device(String name, String type, Integer remainingTime) {
            this.id = "";
            this.name = name;
            this.type = type;
            this.remainingTime = remainingTime;
        }

        @Override
        public String toString() {
            return name + "\n" + remainingTime.toString() + " s remaining";
        }
    }
}
