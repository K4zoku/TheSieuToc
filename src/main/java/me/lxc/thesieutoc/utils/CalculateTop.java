package me.lxc.thesieutoc.utils;

import me.lxc.thesieutoc.TheSieuToc;
import me.lxc.thesieutoc.internal.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalculateTop {
    private static final String DAY = "dd/MM/yyyy";
    private static final String MONTH = "MM/yyyy";
    private static final String YEAR = "yyyy";
    private static final String TOTAL = "total";

    private static long numberOfDonors;
    private static long serverTotal = 0;

    private static List<String> getLogContent() throws Exception {
        File log = TheSieuToc.getInstance().getDonorLog().logFile;
        Scanner s = new Scanner(log);
        List<String> logContent = new ArrayList<>();
        while (s.hasNextLine()) {
            logContent.add(s.nextLine());
        }
        s.close();
        return logContent;
    }
    public static Map<String, Integer> execute(String type) throws Exception {
        List<String> log = getLogContent();
        List<String> matchDate = new ArrayList<>();
        Map<String, Integer> top;
        if (type.contains(TOTAL)) {
            top = getSuccess(log);
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
            top = getSuccess(matchDate);
        }
        top = SortDesc(top);
        numberOfDonors = top.size();
        return top;
    }
    private static Map<String, Integer> getSuccess(List<String> inputarray) {
        serverTotal = 0;
        Map<String, Integer> s = new HashMap<>();
        for (String line : inputarray) {
            String[] data = line.split("[|]",8);
            boolean success = Boolean.parseBoolean(data[6].replaceFirst(" SUCCESS ","").trim());
            if (success) {
                String name = data[1].replaceFirst(" NAME ","").trim();
                int amount = Integer.parseInt(data[5].replaceFirst(" AMOUNT ","").trim());
                if (s.containsKey(name))
                    s.replace(name, s.get(name) + amount);
                else s.put(name, amount);
                serverTotal += amount;
            }
        }
        return s;
    }

    private static Map<String, Integer> SortDesc(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list =
                new LinkedList<>(map.entrySet());
        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static void printTop(CommandSender sender, Map<String, Integer> top, int limit) {
        final Messages msg = TheSieuToc.getInstance().getMessages();
        sender.sendMessage(msg.calculating);
        int i = 0;
        String playerName = sender.getName();
        if (top.isEmpty()) {
            sender.sendMessage(msg.emptyTop);
        } else {
            sender.sendMessage(msg.topMessage.
                    replaceAll("(?ium)[{]Number_Of_Donors[}]", String.valueOf(numberOfDonors)).
                    replaceAll("(?ium)[{]Server_Total[}]", String.valueOf(serverTotal)).
                    replaceAll("(?ium)[{]Top[}]", String.valueOf(Math.min(top.size(), limit)))
            );
            String yourTop = msg.yourTop;
            for (Map.Entry<String, Integer> entry : top.entrySet()) {
                i++;
                String name = entry.getKey().trim();
                String amount = String.valueOf(entry.getValue()).trim();
                if (i <= limit) {
                    sender.sendMessage(msg.topFormat.
                            replaceAll("(?ium)[{]Player_Rank[}]", String.valueOf(i)).
                            replaceAll("(?ium)[{]Player[}]", name).
                            replaceAll("(?ium)[{]Player_Total[}]", amount)
                    );
                }
                if (name.equals(playerName) && sender instanceof Player) {
                    yourTop = yourTop.
                            replaceAll("(?ium)[{]Player_Rank[}]", String.valueOf(i)).
                            replaceAll("(?ium)[{]Player[}]", name).
                            replaceAll("(?ium)[{]Player_Total[}]", amount);
                }
            }

            if (top.containsKey(playerName) && sender instanceof Player) {
                sender.sendMessage(yourTop);
            }
        }
    }
}
