package me.sky.kingdoms.utils;

public class ValueFormat {
    public static String format(long count) {
        if (count < 1000000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000000));
        return String.format("%.1f %c",
                count / Math.pow(1000000, exp),
                "MGTPE".charAt(exp - 1));
    }
}