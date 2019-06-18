package me.takahatashun.napthe.thesieutoc.utils;

public class Numberic {
    public static boolean isInteger(String inp){
        try {
            Integer.parseInt(inp);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
}
