package com.felkertech.n.munch.Objects;

import java.util.ArrayList;

/**
 * Created by N on 4/7/2015.
 */
public class Hist {
    ArrayList<HistItem> history;
    public Hist() {
        history = new ArrayList<>();
    }
    public void insert(HistItem hi) {
        history.add(hi);
    }
    public String toString() {
        String out = "[";
        for(HistItem hi: history) {
            out += hi.toString();
        }
        return out+"]";
    }
    public class HistItem {
        private int id;
        private long time;
        private int amount;
        public HistItem(int id, long time, int amount) {
            this.id = id;
            this.time = time;
            this.amount = amount;
        }
        public String toString() {
            return "{\"id:\""+id+", \"time\": "+time+", \"amount\": "+amount+"}";
        }
    }
}
