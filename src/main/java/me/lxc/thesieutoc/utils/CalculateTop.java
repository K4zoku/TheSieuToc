package me.lxc.thesieutoc.utils;

import me.lxc.thesieutoc.TheSieuToc;
import me.lxc.thesieutoc.internal.Messages;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalculateTop {
    private static final String DAY = "dd/MM/yyyy";
    private static final String MONTH = "MM/yyyy";
    private static final String YEAR = "yyyy";
    private static final String TOTAL = "total";

    public long numberOfDonors;


    private static ArrayList<String> getLogContent() throws Exception {
        File log = TheSieuToc.getInstance().getDonorLog().logFile;
        Scanner s = new Scanner(log);
        ArrayList<String> lines = new ArrayList<>();
        while (s.hasNextLine()) {
            lines.add(s.nextLine());
        }
        s.close();
        return lines;
    }
    public static Map<String, Double> execute(String type) throws Exception {
        ArrayList<String> log = getLogContent();
        ArrayList<String> matchDate = new ArrayList<>();
        Map<String,Double> top = new HashMap<>();
        if (type.contains(TOTAL)) {
            top = getSuccess(top, log);
        } else {
            String format;
            switch (type) {
                case "month":
                    format = MONTH;
                    break;
                case "year":
                    format = YEAR;
                    break;
                case "day":
                default:
                    format = DAY;
                    break;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date date = new Date();
            for (String line : log) {
                String[] ss = line.split("[|]");
                if (ss[0].trim().contains(dateFormat.format(date))) {
                    matchDate.add(line);
                }
            }
            top = getSuccess(top, matchDate);
        }
        top = SortDesc(top);
        return top;
    }
    private static Map<String,Double> getSuccess(Map<String,Double> inputmap, ArrayList<String> inputarray){
        for (String line : inputarray) {
            String[] data = line.split("[|]",8);
            String name = data[1].replaceFirst(" NAME ","");
            double amount = Double.parseDouble(data[5].replaceFirst(" AMOUNT ","").trim());
            boolean success = Boolean.parseBoolean(data[6].replaceFirst(" SUCCESS ","").trim());
            if (success) {
                if (!(inputmap.containsKey(name))) inputmap.put(name,amount);
                else inputmap.replace(name,inputmap.get(name)+amount);
            }
        }
        return inputmap;
    }
    private static Map<String,Double> SortAsc(Map<String,Double> map) {
        List<Map.Entry<String,Double>> list = new LinkedList<>(map.entrySet());
        list.sort(Comparator.comparing(o -> (o.getValue())));

        Map<String,Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String,Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    private static Map<String,Double> SortDesc(Map<String, Double> map) {
        List<Map.Entry<String,Double>> list =
                new LinkedList<>(map.entrySet());
        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));
        Map<String,Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String,Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    private static void printTop(Player player, Map<String, Double> top) {
        final Messages msg = TheSieuToc.getInstance().getMessages();
        player.sendMessage(msg.calculating);
        int i = 0;
        String playerName = player.getName();
        String topFormat = msg.topFormat;
        String format = msg.yourTop;
        if (top.isEmpty()) {
            player.sendMessage(msg.emptyTop);
        } else {
            for (Map.Entry<String, Double> entry : top.entrySet()) {
                i++;
                String name = entry.getKey();
                String amount = String.valueOf(entry.getValue());
                if (i < 10) {
                    player.sendMessage(topFormat.
                            replaceAll("(?ium)\\{OrdinalNumber}", String.valueOf(i)).
                            replaceAll("(?ium)\\{PlayerName}", name).
                            replaceAll("(?ium)\\{TotalAmount}", amount)
                    );
                }
                if (name.contains(playerName)) {
                    player.sendMessage(format.
                            replaceAll("(?ium)\\{OrdinalNumber}", String.valueOf(i)).
                            replaceAll("(?ium)\\{PlayerName}", name).
                            replaceAll("(?ium)\\{TotalAmount}", amount)
                    );
                }
            }
        }
    }
}
