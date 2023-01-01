package net.danh.litesack.API.Utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class Number {

    private static final String[] suffix = new String[]{"", "K", "M", "B", "T", "Q"};
    private static final int MAX_LENGTH = 4;

    public static String format(int number) {
        String r = new DecimalFormat("##0E0").format(number);
        r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
        while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")) {
            r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
        }
        return r;
    }

    public static String settingFormat(int number) {
        if (File.getSetting().getBoolean("FORMAT.NUMBER.TYPE")) {
            return format(number);
        } else {
            return intToSuffixedNumber(number);
        }
    }

    public static double getRandomDouble(double min, double max) {
        if (max > min) {
            return ThreadLocalRandom.current().nextDouble(min, max);
        } else {
            return min;
        }
    }

    public static int getRandomInteger(int min, int max) {
        if (max > min + 2) {
            return ThreadLocalRandom.current().nextInt(min, max);
        } else {
            return min;
        }
    }

    public static double getDouble(String s) {
        try {
            if (!s.contains("-")) {
                return BigDecimal.valueOf(Double.parseDouble(s)).doubleValue();
            } else {
                return getRandomDouble(BigDecimal.valueOf(Double.parseDouble(s.split("-")[0])).doubleValue(), BigDecimal.valueOf(Double.parseDouble(s.split("-")[1])).doubleValue());
            }
        } catch (NumberFormatException | NullPointerException e) {
            return 0d;
        }
    }

    public static int getInteger(String s) {
        try {
            if (!s.contains("-")) {
                return BigDecimal.valueOf(Long.parseLong(s)).intValue();
            } else {
                return getRandomInteger(BigDecimal.valueOf(Long.parseLong(s.split("-")[0])).intValue(), BigDecimal.valueOf(Long.parseLong(s.split("-")[1])).intValue());
            }
        } catch (NumberFormatException | NullPointerException e) {
            return 0;
        }
    }

    public static String intToSuffixedNumber(int number) {
        if (number < 1000) {
            return "" + number;
        } else {
            int exponent = (int) (Math.log(number) / Math.log(1000));
            return String.format("%.2f%c", number / Math.pow(1000, exponent), "kMBTQ".charAt(exponent - 1));
        }
    }

    public static String longToSuffixedNumber(long number) {
        if (number < 1000) {
            return "" + number;
        } else {
            int exponent = (int) (Math.log(number) / Math.log(1000));
            return String.format("%.2f%c", number / Math.pow(1000, exponent), "kMBTQ".charAt(exponent - 1));
        }
    }

    public static String doubleToSuffixedNumber(double number) {
        if (number < 1000) {
            return String.format("%.2f", number);
        } else {
            int exponent = (int) (Math.log(number) / Math.log(1000));
            return String.format("%.2f%c", number / Math.pow(1000, exponent), "kMBTQ".charAt(exponent - 1));
        }
    }


}
