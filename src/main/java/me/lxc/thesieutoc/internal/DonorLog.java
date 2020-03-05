package me.lxc.thesieutoc.internal;

import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DonorLog {
    private File donorLog;

    public DonorLog(Settings settings) {
        this.donorLog = settings.Donor_Log_File;
    }

    public boolean writeLog(Player p, String serial, String pin, String cardType, int amount, boolean success, String notes) {
        if (!(donorLog.exists())) {
            if (donorLog.getParentFile().mkdir()) {
                try {
                    if (!donorLog.createNewFile()) {
                        return false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Date dt = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        String name = p.getName();
        df.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        try (FileWriter fw = new FileWriter(donorLog, true); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.append(
                    df.format(dt)).
                    append(" | NAME ").append(name).
                    append(" | SERIAL ").append(serial).
                    append(" | PIN ").append(pin).
                    append(" | TYPE ").append(cardType).
                    append(" | AMOUNT ").append(String.valueOf(amount)).
                    append(" | SUCCESS ").append(String.valueOf(success)).
                    append(" | NOTES ").append(notes.replaceAll("(?i)[§&][0-9A-FK-OR]", ""));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
