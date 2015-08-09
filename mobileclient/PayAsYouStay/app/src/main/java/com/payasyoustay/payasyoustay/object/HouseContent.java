package com.payasyoustay.payasyoustay.object;

import java.util.ArrayList;
import java.util.List;

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
    public static List<House> ITEMS_HOST = new ArrayList<House>();
    public static List<House> ITEMS_GUEST = new ArrayList<House>();


    static {
        // Add 3 sample items.
        addItem(new House("Cap Cod", "282 Bradford St, Provincetown, MA 02657"), true, false);
        addItem(new House("New York City", "Times Square Manhattan New York, NY 10036"), true, true);
    }

    private static void addItem(House item, boolean owner, boolean guest) {
        if (owner) {
            ITEMS_HOST.add(item);
        }
        if (guest) {
            ITEMS_GUEST.add(item);
        }
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
