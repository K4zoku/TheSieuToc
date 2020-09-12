package me.lxc.thesieutoc.internal;

import me.lxc.thesieutoc.TheSieuToc;
import me.lxc.thesieutoc.utils.CalculateTop;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;

public class DonorLog {
    public File logFile;

    public DonorLog(File logFile) {
        this.logFile = logFile;
    }

    public void writeLog(Player p, String serial, String pin, String cardType, int amount, boolean success, String notes) {
        writeLog(p.getName(), serial, pin, cardType, amount, success, notes);
    }

    public void writeLog(String p, String serial, String pin, String cardType, int amount, boolean success, String notes) {
        createFile();
        Date dt = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        StringBuilder content = new StringBuilder();
        content.append(df.format(dt));
        content.append(" | NAME ");
        content.append(p);
        content.append(" | SERIAL ");
        content.append(serial);
        content.append(" | PIN ");
        content.append(pin);
        content.append(" | TYPE ");
        content.append(cardType);
        content.append(" | AMOUNT ");
        content.append(amount);
        content.append(" | SUCCESS ");
        content.append(success);
        content.append(" | NOTES ");
        content.append(notes);
        try {
            CalculateTop.appendToCache(DornorLogElement.getFromLine(content.toString()));
        } catch (ParseException e) {
            TheSieuToc.getInstance().getLogger().log(Level.SEVERE, "An error occurred ", e);
        }
        TheSieuToc.pluginDebug.debug("| Dornor Log [> " + content);
        try (FileWriter fw = new FileWriter(logFile, true); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.append(content.toString());
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            TheSieuToc.getInstance().getLogger().log(Level.SEVERE, "An error occurred ", e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void createFile() {
        try {
            if (!(logFile.exists())) {
                logFile.getParentFile().mkdir();
                logFile.createNewFile();
            }
        } catch (IOException e) {
            TheSieuToc.getInstance().getLogger().log(Level.SEVERE, "An error occurred ", e);
        }
    }
}
