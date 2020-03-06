package me.lxc.artxeapi.utils;

public class ArtxeNumber {
    public static boolean isInteger(Object o){
        try {
            Integer.parseInt(o.toString());
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
}
