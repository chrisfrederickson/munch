package com.felkertech.n.munch.Objects;

public class HistItem {
    private int id;
    private long time;
    private float amount;
    public HistItem(int id, long time, float amount) {
        this.id = id;
        this.time = time;
        this.amount = amount;
    }
    public String toString() {
        return "{\"id:\""+id+", \"time\": "+time+", \"amount\": "+amount+"}";
    }
}
