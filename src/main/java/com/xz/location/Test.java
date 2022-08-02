package com.xz.location;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        String str[] = {"113.421912,23.073626", "经度113.42.072 纬度23.079.58", "经：113.4051 纬：23.0741", "X:113.55688, Y:23.20839", "北纬23°09'43''东经113°31'35.36\""};
        //String pattern[]={"(?<longitude>[1-9]+(\\.\\d+))\\D+(?<latitude>[1-9]+(\\.\\d+))$","(?<longitude>[1-9]+(\\.\\d+)(\\.\\d+))\\D+(?<latitude>[1-9]+(\\.\\d+)(\\.\\d+))$"};
        Pattern p0 = Pattern.compile("(?<longitude>[1-9]+(\\.\\d+))\\D+(?<latitude>[1-9]+(\\.\\d+))$", Pattern.CASE_INSENSITIVE);
        Pattern p1 = Pattern.compile("(?<longitude>[1-9]+(\\.\\d+)(\\.\\d+))\\D+(?<latitude>[1-9]+(\\.\\d+)(\\.\\d+))$", Pattern.CASE_INSENSITIVE);
        Pattern p2 = Pattern.compile("(?<longitude>[1-9]+°\\d{1,2}'\\d{1,2}(\\.\\d+)?[''\"])\\D+(?<latitude>[1-9]+°\\d{1,2}'\\d{1,2}(\\.\\d+)?[''\"])$", Pattern.CASE_INSENSITIVE);
        Pattern pattern[] = {p0, p1, p2};
        for (String s : str) {
            System.out.println("-------------------------");
            System.out.println(s);
            for (Pattern p : pattern) {
                Matcher m = p.matcher(s);
                if (m.find()) {
                    System.out.println("m.groupCount() = " + m.groupCount());
                    System.out.println("longitude = " + m.group("longitude"));
                    System.out.println("latitude = " + m.group("latitude"));
                    break;
                    //
               /* for (int i = 0; i < m.groupCount(); i++)
                    System.out.println("m.group(" + i + ") = " + m.group(i));
                */
                }
            }
        }
    }

}
