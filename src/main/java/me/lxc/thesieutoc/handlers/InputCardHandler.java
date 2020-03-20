package me.lxc.thesieutoc.handlers;

import me.lxc.thesieutoc.TheSieuToc;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InputCardHandler {
    private static List<Player> inputStepOne = new ArrayList<>();
    private static List<Player> inputStepTwo = new ArrayList<>();
    private static HashMap<Player, LocalCardInfo> cards = new HashMap<>();

    public static void triggerStepOne(Player player, String type, int amount) {
        if(!stepOne(player)) {
            player.sendMessage(TheSieuToc.getInstance().getMessages().inputSerial);
            inputStepOne.add(player);
            cards.put(player, new LocalCardInfo(type, amount, "", ""));
        }
    }

    public static boolean stepOne(Player player) {
        return inputStepOne.contains(player);
    }

    public static void unTriggerStep1(Player player) {
        inputStepOne.remove(player);
    }

    public static void triggerStepTwo(Player player, String serial) {
        player.sendMessage(TheSieuToc.getInstance().getMessages().inputSerial);
        inputStepTwo.add(player);
        final LocalCardInfo info = cards.get(player);
        cards.replace(player, new LocalCardInfo(info.type, info.amount, serial, ""));
    }

    public static boolean stepTwo(Player player) {
        return inputStepTwo.contains(player);
    }

    public static void unTriggerStep2(Player player) {
        inputStepTwo.remove(player);
    }

    public static LocalCardInfo lastStep(Player player, String pin) {
        final LocalCardInfo info = cards.get(player);
        cards.remove(player);
        return new LocalCardInfo(info.type, info.amount, info.serial, pin);
    }

    public static class LocalCardInfo {
        public String type;
        public int amount;
        public String serial;
        public String pin;

        public LocalCardInfo(String type, int amount, String serial, String pin){
            this.type = type;
            this.amount = amount;
            this.serial = serial;
            this.pin = pin;
        }
    }
}
