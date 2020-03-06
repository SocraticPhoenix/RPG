package com.gmail.socraticphoenix.rpg.modifiers;

import java.util.ArrayList;
import java.util.List;

public class SortedList<T extends Comparable<T>> extends ArrayList<T> {

    public SortedList(List<T> val) {
        super();
        addAll(val);
    }

    public SortedList() {
        super();
    }

    @Override
    public boolean add(T o) {
        if (this.isEmpty()) {
            return super.add(o);
        } if (this.get(this.size() - 1).compareTo(o) <= 0) {
            return super.add(o);
        } else {
            for (int i = 0; i < this.size(); i++) {
                if (this.get(i).compareTo(o) >= 0) {
                    this.add(i, o);
                    return true;
                }
            }

            return super.add(o);
        }
    }

}
