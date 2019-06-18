package me.takahatashun.napthe.thesieutoc.data;

import me.takahatashun.napthe.thesieutoc.NapThe;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DonorLog {

    public DonorLog(Player player, String type, String serial, String pin, int amount, boolean success, String notes) {
        File logFile = NapThe.getInstance().getSettings().LogFile;
        if(!logFile.exists()) {
            try {
                if (!logFile.createNewFile()) {
                    return;
                }
            } catch (IOException e){
                e.printStackTrace();
                return;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            String currentTime = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date());
            writer.append(currentTime);
            writer.append(" | NAME ");
            writer.append(player.getName());
            writer.append(" | SERIAL ");
            writer.append(serial);
            writer.append(" | PIN ");
            writer.append(pin);
            writer.append(" | TYPE ");
            writer.append(type);
            writer.append(" | AMOUNT ");
            writer.append(String.valueOf(amount));
            writer.append(" | SUCCESS ");
            writer.append(String.valueOf(success));
            writer.append(" | NOTES ");
            writer.append(notes);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
