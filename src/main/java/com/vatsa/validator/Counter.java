package com.vatsa.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Counter {
    int valid;
    int total;
    List<String> messages;

    public Counter() {
        this.total = 0;
        this.valid = 0;
        messages = new ArrayList<>();
    }

    public Counter(int valid, int total) {
        this.total = total;
        this.valid = valid;
        messages = new ArrayList<>();
    }

    public Counter(int valid, int total, String message) {
        this.total = total;
        this.valid = valid;
        messages = new ArrayList<>();
        if (Objects.nonNull(message) && !message.isEmpty()) messages.add(message);
    }

    @Override
    public String toString() {
        return String.format("%s valid=%d total=%d messages=%s", super.toString(), this.valid, this.total, this.messages.toString());
    }

    public Counter add(Counter counter) {
        this.total += counter.total;
        this.valid += counter.valid;

        return this;
    }

    public void update(int valid, int total) {
        this.total = total;
        this.valid = valid;
        messages = new ArrayList<>();
    }

    public void update(int valid, int total, String message) {
        this.total = total;
        this.valid = valid;
        messages = new ArrayList<>();
        if (Objects.nonNull(message) && !message.isEmpty()) messages.add(message);
    }

    public static Counter combine(Counter source, Counter target) {
        target.total += source.total;
        target.valid += source.valid;
        target.messages.addAll(source.messages);
        return target;
    }

    public static Counter reduceOnPolicy(Counter counter, ReductionPolicy reductionPolicy) {


        if (ReductionPolicy.isBinary(reductionPolicy)) {
            if (reductionPolicy == ReductionPolicy.BINARY_AT_LEAST_ONE) {
                counter.valid = (counter.valid > 0) ? 1 : 0;
            } else if (reductionPolicy == ReductionPolicy.BINARY_MAJORITY) {
                counter.valid = (counter.valid << 1 >= counter.total) ? 1 : 0;
            } else if (reductionPolicy == ReductionPolicy.BINARY_ALL) {
                counter.valid = (counter.valid == counter.total) ? 1 : 0;
            }
            counter.total = 1;
        }

        return counter;
    }
}
