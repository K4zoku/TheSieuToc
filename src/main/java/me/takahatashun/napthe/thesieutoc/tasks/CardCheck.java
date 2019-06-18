package me.takahatashun.napthe.thesieutoc.tasks;

import me.takahatashun.napthe.thesieutoc.NapThe;
import me.takahatashun.napthe.thesieutoc.internal.CardHandler;
import org.bukkit.scheduler.BukkitRunnable;

public class CardCheck extends BukkitRunnable {
    private NapThe main;

    public CardCheck(NapThe main){
        this.main = main;
        this.runTaskTimer(main, 0L, main.getSettings().Callback_Refresh);
    }

    @Override
    public void run(){
        CardHandler.cardChecker(NapThe.getInstance().queue, main.getLanguage());
    }
}
