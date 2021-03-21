package me.lxc.thesieutoc.utils;

import com.google.common.base.Strings;
import me.lxc.thesieutoc.TheSieuToc;
import me.lxc.thesieutoc.internal.DornorLogElement;
import me.lxc.thesieutoc.internal.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class CalculateTop {
    private static final String DAY = "dd/MM/yyyy";
    private static final String MONTH = "MM/yyyy";
    private static final String YEAR = "yyyy";
    private static final String TOTAL = "total";

    private static final TTLCache<List<DornorLogElement>> cache;
    private static long dornorCount;
    private static long totalIncome = 0;

    static {
        cache = new TTLCache<>(CalculateTop::loadFromFile, TheSieuToc.getInstance().getSettings().cacheTTL);
    }

    public static void clearCache() {
        cache.update();
    }

    public static void appendToCache(DornorLogElement dornor) {
        List<DornorLogElement> l = cache.get();
        l.add(dornor);
        cache.forceUpdate(l);
    }

    private static List<DornorLogElement> loadFromFile() {
        return DornorLogElement.loadFromFile(TheSieuToc.getInstance().getDonorLog().logFile);
    }

    public static Map<String, Integer> execute(String type) {
        List<DornorLogElement> log = cache.get();
        Map<String, Integer> top;
        if (Strings.isNullOrEmpty(type) || type.equalsIgnoreCase(TOTAL)) {
            top = getSuccess(log);
        } else {
            String format;
            switch (type.toLowerCase()) {
                case "month":
                    format = MONTH;
                    break;
                case "day":
                    format = DAY;
                    break;
                case "year":
                default:
                    format = YEAR;
                    break;
            }
            final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            final String now = dateFormat.format(new Date());
            final List<DornorLogElement> matchDate = log.stream()
                .filter(dornor -> dateFormat.format(dornor.getDate()).contains(now))
                .collect(Collectors.toList());
            top = getSuccess(matchDate);
        }
        top = sort(top);
        dornorCount = top.size();
        return top;
    }

    private static Map<String, Integer> getSuccess(List<DornorLogElement> inputarray) {
        totalIncome = 0;
        Map<String, Integer> s = new HashMap<>();
        for (DornorLogElement dornor : inputarray) {
            if (!dornor.isSuccess()) continue;
            final String name = dornor.getPlayerName();
            final int amount = dornor.getCardInfo().amount;
            s.compute(name, (n, a) -> (a == null) ? amount : a + amount);
            totalIncome += amount;
        }
        return s;
    }

    private static Map<String, Integer> sort(Map<String, Integer> map) {
        return map.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static void printTop(CommandSender sender, Map<String, Integer> top, int limit) {
        final Messages msg = TheSieuToc.getInstance().getMessages();

        if (top.isEmpty()) {
            sender.sendMessage(msg.emptyTop);
            return;
        }

        sender.sendMessage(msg.calculating);
        final String name = sender.getName();

        sender.sendMessage(msg.topMessage.
            replaceAll("(?ium)[{]Number_Of_Donors[}]", String.valueOf(dornorCount)).
            replaceAll("(?ium)[{]Server_Total[}]", String.valueOf(totalIncome)).
            replaceAll("(?ium)[{]Top[}]", String.valueOf(Math.min(top.size(), limit)))
        );

        String yourTop = msg.yourTop;
        int i = 0;
        for (Map.Entry<String, Integer> entry : top.entrySet()) {
            i++;
            String donor = entry.getKey().trim();
            String amount = String.valueOf(entry.getValue()).trim();
            if (i <= limit) {
                sender.sendMessage(msg.topFormat
                    .replaceAll("(?ium)[{]Player_Rank[}]", String.valueOf(i))
                    .replaceAll("(?ium)[{]Player[}]", donor)
                    .replaceAll("(?ium)[{]Player_Total[}]", amount)
                );
            }
            if (donor.equals(name)) {
                yourTop = yourTop
                    .replaceAll("(?ium)[{]Player_Rank[}]", String.valueOf(i))
                    .replaceAll("(?ium)[{]Player[}]", donor)
                    .replaceAll("(?ium)[{]Player_Total[}]", amount);
            }
        }

        if (top.containsKey(name) && sender instanceof Player) {
            sender.sendMessage(yourTop);
        }

    }
}
