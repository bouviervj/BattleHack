package com.payasyoustay.payasyoustay.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class HouseContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<House> ITEMS = new ArrayList<House>();


    static {
        // Add 3 sample items.
        addItem(new House("Cap Cod", "282 Bradford St, Provincetown, MA 02657"));
        addItem(new House("New York City", "Times Square Manhattan New York, NY 10036"));
    }

    private static void addItem(House item) {
        ITEMS.add(item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class House {
        public String name;
        public String address;

        public House(String name, String address) {
            this.name = name;
            this.address = address;
        }

        @Override
        public String toString() {
            return name + "\n" + address;
        }
    }
}
