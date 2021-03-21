package me.lxc.thesieutoc.internal;

import me.lxc.thesieutoc.TheSieuToc;
import me.lxc.thesieutoc.utils.CalculateTop;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
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
        CalculateTop.appendToCache(DornorLogElement.parse(content.toString()));

        TheSieuToc.pluginDebug.debug("| Dornor Log [> " + content);
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(logFile, true), StandardCharsets.UTF_8)) {
            writer.append(content.toString());
            writer.append('\n');
            writer.flush();
        } catch (IOException e) {
            TheSieuToc.getInstance().getLogger().log(Level.SEVERE, "An error occurred ", e);
        }
    }

    public void createFile() {
        try {
            if (logFile.exists()) return;
            if (!logFile.getParentFile().mkdir() || !logFile.createNewFile()) throw new IOException();
        } catch (IOException e) {
            TheSieuToc.getInstance().getLogger().log(Level.SEVERE, "An error occurred ", e);
        }
    }
}
