package org.infai.ses.senergy.operators.valuemapper;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;

public class IntervalRule {
    @JsonProperty("from")
    public Object from;

    @JsonProperty("to")
    public Object to;


    private final double min;
    private final double max;
    private final boolean leftOpen;
    private final boolean rightOpen;

    IntervalRule() throws ParseException {
        String interval = from.toString().replaceAll("\\s+", "");
        if (interval.length() < 5) {
            throw new ParseException("unexpected lenght of interval", interval.length());
        }
        char minBracket = interval.charAt(0);
        char maxBracket = interval.charAt(interval.length() - 1);
        String intervalValues = interval.substring(1, interval.length() - 1);
        String[] parts = intervalValues.split(",");
        if (parts.length != 2) {
            throw new ParseException("expect one ',' to separate min and max of interval", interval.length());
        }

        switch (minBracket) {
            case '(':
            case ']':
                this.leftOpen = true;
                break;
            case '[':
                this.leftOpen = false;
                break;
            default:
                throw new ParseException("expect '[', ']' or '(' for opening bracket", 0);
        }

        switch (maxBracket) {
            case ')':
            case '[':
                this.rightOpen = true;
                break;
            case ']':
                this.rightOpen = false;
                break;
            default:
                throw new ParseException("expect '[', ']' or ')' for closing bracket", interval.length());
        }


        if (parts[0].equals("*")) {
            this.min = Double.NEGATIVE_INFINITY;
        } else {
            this.min = Double.parseDouble(parts[0]);
        }

        if (parts[1].equals("*")) {
            this.max = Double.POSITIVE_INFINITY;
        } else {
            this.max = Double.parseDouble(parts[1]);
        }
    }

    public boolean triggers(Object value) {
        if (value instanceof Integer) {
            value = ((Integer) value).doubleValue();
        }
        if (value instanceof String) {
            value = Double.parseDouble((String) value);
        }
        double actual = (double) value;
        if (this.leftOpen && this.min >= actual) {
            return false;
        }
        if (!this.leftOpen && this.min > actual) {
            return false;
        }

        if (this.rightOpen && this.max <= actual) {
            return false;
        }
        if (!this.rightOpen && this.max < actual) {
            return false;
        }
        return true;
    }
}
