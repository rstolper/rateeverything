package rateeverything.item.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by roman on 1/18/2015.
 */
public enum Rating {
    UNRATED("Unrated", 0),
    NO("No", 1),
    MAYBE("Maybe", 2),
    YES("Yes", 3);

    private String displayText;
    private int numericValue;
    private static Map<String, Rating> displayTextToEnum;
    private static Map<Integer, Rating> numericValueToEnum;

    static {
        displayTextToEnum = new HashMap<>();
        numericValueToEnum = new HashMap<>();
        for (Rating r : Rating.values()) {
            displayTextToEnum.put(r.getDisplayText(), r);
            numericValueToEnum.put(r.getNumericValue(), r);
        }
    }

    Rating (String displayText, int numericValue) {
        this.displayText = displayText;
        this.numericValue = numericValue;
    }

    public String getDisplayText() {
        return displayText;
    }

    public int getNumericValue() {
        return numericValue;
    }

    public static Rating valueOfDisplayText(String displayText) {
        return displayTextToEnum.get(displayText);
    }

    public static Rating valueOfNumericValue(int numericValue) {
        return numericValueToEnum.get(numericValue);
    }
}
