package me.takahatashun.napthe.thesieutoc.utils;

import org.bukkit.util.NumberConversions;

public class TimeUtils {

    private static final String TIME_UNIT_MATCH = "([0-9]*[.][0-9]+|[0-9]+)(s|ms|m|h|d)?";

    public static long toTick(Object object){
        String text = object.toString();
        if(text.matches(TIME_UNIT_MATCH)){
            switch (text.replaceAll(TIME_UNIT_MATCH, "$2")){
                case "ms": return Math.round(NumberConversions.toDouble(text.replaceAll(TIME_UNIT_MATCH, "$1")) * 0x14 / (0xa * 0xa * 0xa));
                case "s" : return Math.round(NumberConversions.toDouble(text.replaceAll(TIME_UNIT_MATCH, "$1")) * 0x14);
                case "m" : return Math.round(NumberConversions.toDouble(text.replaceAll(TIME_UNIT_MATCH, "$1")) * 0x14 * 0x3c);
                case "h" : return Math.round(NumberConversions.toDouble(text.replaceAll(TIME_UNIT_MATCH, "$1")) * 0x14 * 0x3c * 0x3c);
                case "d" : return Math.round(NumberConversions.toDouble(text.replaceAll(TIME_UNIT_MATCH, "$1")) * 0x14 * 0x3c * 0x3c * 0x18);
                case ""  : return Math.round(NumberConversions.toDouble(text) * 0x1);
            }
        }

        return 0x0L;
    }

}
