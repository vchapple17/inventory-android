package com.example.valchapple.hybrid_android.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int position) {
        int status = position % 2;
        String serial = "DMPPP293BDSF" + String.valueOf(position);
        String model = "iPad " + String.valueOf(position % 25);
        String color = "Black";
        return new DummyItem(String.valueOf(position),
                status,
                serial,
                model,
                color); 
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final int status;
        public final String serial;
        public final String model;
        public final String color;

        public DummyItem(String id, int status, String serial, String model, String color) {
            this.id = id;
            this.status = status;
            this.serial = serial;
            this.model = model;
            this.color = color;
        }

        @Override
        public String toString() {
            return serial;
        }
    }
}
