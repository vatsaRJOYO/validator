package com.vatsa.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Counter {
    private int valid;
    private int total;
    private List<String> messages;

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

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
        this.total += counter.getTotal();
        this.valid += counter.getValid();
        this.messages.addAll(counter.getMessages());

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
        target.add(source);
        return target;
    }

    public static Counter reduceOnPolicy(Counter counter, ReductionPolicy reductionPolicy) {


        if (ReductionPolicy.isBinary(reductionPolicy)) {
            int valid = counter.getValid();
            if (reductionPolicy == ReductionPolicy.BINARY_AT_LEAST_ONE) {
                valid = (counter.valid > 0) ? 1 : 0;
            } else if (reductionPolicy == ReductionPolicy.BINARY_MAJORITY) {
                valid = (counter.valid << 1 >= counter.total) ? 1 : 0;
            } else if (reductionPolicy == ReductionPolicy.BINARY_ALL) {
                valid = (counter.valid == counter.total) ? 1 : 0;
            }
            counter.setValid(valid);
            counter.setTotal(1);
        }
        return counter;
    }
}
