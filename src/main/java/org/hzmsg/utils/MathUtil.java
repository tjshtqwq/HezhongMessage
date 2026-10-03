package org.hzmsg.utils;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {
    public static String format1(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.toString();
    }
}
