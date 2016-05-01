package com.ap.common.core;

import java.util.OptionalInt;

public class ComparableId implements Comparable<ComparableId> {

    private OptionalInt id;
    double output;

    public ComparableId(double output) {
        this.output = output;
    }

    public ComparableId(int id, double output) {
        this.id = OptionalInt.of(id);
        this.output = output;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ComparableId)) {
            return false;
        }
        ComparableId that = (ComparableId) obj;
        return this.getId() == that.getId();
    }

    public int compareTo(ComparableId o) {
        if (output < o.output) {
            return 1;
        } else if (output > o.output) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        String object = "ID: " + getId() + ", ComparableId: " + output;
        return object;
    }

    public int getId() {
        return id.getAsInt();
    }

}
