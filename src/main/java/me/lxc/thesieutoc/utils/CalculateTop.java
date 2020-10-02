package me.lxc.thesieutoc.utils;

import javafx.util.Pair;
import me.lxc.thesieutoc.TheSieuToc;
import me.lxc.thesieutoc.internal.DornorLogElement;
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

    private static Pair<Date, List<DornorLogElement>> logCache;

    public static void clearCache() {
        logCache = null;
    }

    public static void appendToCache(DornorLogElement dornor) {
        List<DornorLogElement> l = new ArrayList<>();
        if (logCache != null) {
            l = logCache.getValue();
            l.add(dornor);
            logCache = new Pair<>(logCache.getKey(), l);
        } else {
            l.add(dornor);
            logCache = new Pair<>(new Date(), l);
        }
    }

    private static List<DornorLogElement> getLogContent() throws Exception {
        List<DornorLogElement> logContent = new ArrayList<>();
        if (logCache == null || logCache.getKey().before(new Date())) {
            TheSieuToc.pluginDebug.debug("Loading log from file...");
            File log = TheSieuToc.getInstance().getDonorLog().logFile;
            Scanner s = new Scanner(log);
            while (s.hasNextLine()) {
                logContent.add(DornorLogElement.getFromLine(s.nextLine()));
            }
            s.close();
            Date expire = new Date(System.currentTimeMillis() + TheSieuToc.getInstance().getSettings().cacheTTL);
            logCache = new Pair<>(expire, logContent);
        } else {
            long start = System.currentTimeMillis();
            long end = logCache.getKey().getTime();
            long ttl = (end - start) / 1000;

            TheSieuToc.pluginDebug.debug("Loading log from cache... (TTL: " + ttl + ")");
            logContent = logCache.getValue();
        }
        return logContent;
    }

    public static Map<String, Integer> execute(String type) throws Exception {
        List<DornorLogElement> log = getLogContent();
        Map<String, Integer> top;
        if (type == null || (type.equalsIgnoreCase(TOTAL) || type.isEmpty())) {
            top = getSuccess(log);
        } else {
            List<DornorLogElement> matchDate = new ArrayList<>();
            SimpleDateFormat dateFormat;
            switch (type.toLowerCase()) {
                case "month":
                    dateFormat = new SimpleDateFormat(MONTH);
                    break;
                case "year":
                    dateFormat = new SimpleDateFormat(YEAR);
                    break;
                case "day":
                default:
                    dateFormat = new SimpleDateFormat(DAY);
                    break;
            }
            String now = dateFormat.format(new Date());
            for (DornorLogElement dornor : log) {
                if (dateFormat.format(dornor.getDate()).contains(now)) {
                    matchDate.add(dornor);
                }
            }
            top = getSuccess(matchDate);
        }
        top = SortDesc(top);
        numberOfDonors = top.size();
        return top;
    }

    private static Map<String, Integer> getSuccess(List<DornorLogElement> inputarray) {
        serverTotal = 0;
        Map<String, Integer> s = new HashMap<>();
        for (DornorLogElement dornor : inputarray) {
            if (dornor.isSuccess()) {
                String name = dornor.getPlayerName();
                int amount = dornor.getCardInfo().amount;

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
            int i = 0;
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
