package me.lxc.thesieutoc.internal;

import me.lxc.thesieutoc.TheSieuToc;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;

public class DonorLog {
    public File logFile;

    public DonorLog(File logFile) {
        this.logFile = logFile;
    }

    public boolean writeLog(Player p, String serial, String pin, String cardType, int amount, boolean success, String notes) {
        createFile();
        Date dt = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        String name = p.getName();
        df.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        try (FileWriter fw = new FileWriter(logFile, true); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.append(
                    df.format(dt)).
                    append(" | NAME ").append(name).
                    append(" | SERIAL ").append(serial).
                    append(" | PIN ").append(pin).
                    append(" | TYPE ").append(cardType).
                    append(" | AMOUNT ").append(String.valueOf(amount)).
                    append(" | SUCCESS ").append(String.valueOf(success)).
                    append(" | NOTES ").append(notes);
            bw.newLine();
        } catch (IOException e) {
            TheSieuToc.getInstance().getLogger().log(Level.SEVERE, "An error occurred ", e);
        }
        return true;
    }

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
