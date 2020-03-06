package me.lxc.thesieutoc.internal;

import me.lxc.artxeapi.data.ArtxeYAML;
import me.lxc.artxeapi.utils.ArtxeNumber;
import me.lxc.thesieutoc.TheSieuToc;
import me.lxc.thesieutoc.tasks.CardCheckTask;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class Commands extends BukkitCommand {

    public Commands(String name, String description, String usageMessage, List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        final ArtxeYAML msg = TheSieuToc.getInstance().getMessages();
        final boolean isPlayer = sender instanceof Player;
        final Player player = isPlayer ? (Player) sender : null;
        final boolean hasAPIInfo = TheSieuToc.getInstance().hasAPIInfo;

        switch (args.length){
            case 0:
                if(isPlayer){
                    if(hasAPIInfo) {
                        chooseCard(player, msg);
                        return true;
                    } else return false;
                } else {
                    sender.sendMessage(msg.getConfig().getString("Messages.Only-Player"));
                    return false;
                }
            case 1:
                switch (args[0].toLowerCase()){
                    case "reload":
                        return reload(sender, args.length, msg);
                    case "choose":
                        if(hasAPIInfo) {
                            if (isPlayer) {
                                chooseCard(player, msg);
                                return true;
                            } else {
                                sender.sendMessage(msg.getConfig().getString("Messages.Only-Player"));
                                return false;
                            }
                        } else return false;
                    case "check":
                        if(hasAPIInfo) {
                            return check(sender, args.length, msg);
                        } else return false;
                    default:
                        sender.sendMessage(msg.getConfig().getString("Messages.Invalid-Command"));
                        return false;
                }
            case 2:
                switch (args[0].toLowerCase()){
                    case "choose":
                        if(isPlayer) {
                            if(hasAPIInfo) {
                                if (isValidCard(args[1])) {
                                    String type = args[1];
                                    chooseAmount(player, type, msg);
                                    return true;
                                } else {
                                    sender.sendMessage(msg.getConfig().getString("Messages.Invalid-Card-Type"));
                                    return false;
                                }
                            } else return false;
                        } else {
                            sender.sendMessage(msg.getConfig().getString("Messages.Only-Player"));
                            return false;
                        }
                    case "reload":
                        return reload(sender, args.length, msg);
                    case "check":
                        if(hasAPIInfo) {
                            return check(sender, args.length, msg);
                        } else return false;
                    default:
                        sender.sendMessage(msg.getConfig().getString("Messages.Invalid-Command"));
                        return false;
                }
            case 3:
                switch (args[0].toLowerCase()){
                    case "choose":
                        if(isPlayer) {
                            if(hasAPIInfo) {
                                if (isValidCard(args[1])) {
                                    if (ArtxeNumber.isInteger(args[2])) {
                                        if (isValidAmount(Integer.parseInt(args[2]))) {
                                            
                                            //CardHandler.triggerInputSignCard(player, args[1], Integer.parseInt(args[2]), msg);
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    } else {
                                        sender.sendMessage(msg.getConfig().getString("Messages.Not-Number").replaceAll("(?ium)[{]0[}]", args[2]));
                                        return false;
                                    }
                                } else {
                                    sender.sendMessage(msg.getConfig().getString("Messages.Invalid-Card-Type"));
                                    return false;
                                }
                            } else return false;
                        } else {
                            sender.sendMessage(msg.getConfig().getString("Messages.Only-Player"));
                            return false;
                        }
                    case "reload":
                        return reload(sender, args.length, msg);
                    case "check":
                        if(hasAPIInfo) {
                            return check(sender, args.length, msg);
                        } else return false;
                    default:
                        sender.sendMessage(msg.getConfig().getString("Messages.Invalid-Command"));
                        return false;
                }
            default:
                sender.sendMessage(msg.getConfig().getString("Messages.Too-Many-Args"));
                return false;
        }
    }

    private boolean reload(CommandSender sender, int arg, ArtxeYAML msg){
        if(sender.hasPermission("napthe.admin.reload")){
            if(arg == 1){
                TheSieuToc.getInstance().loadData();
                sender.sendMessage(msg.getConfig().getString("Messages.Reloaded"));
                return true;
            } else {
                sender.sendMessage(msg.getConfig().getString("Messages.Too-Many-Args"));
                return false;
            }
        } else {
            sender.sendMessage(msg.getConfig().getString("Messages.No-Permission"));
            return false;
        }
    }

    private boolean check(CommandSender sender, int arg, ArtxeYAML msg){
        if(sender.hasPermission("napthe.admin.check")){
            if(arg == 1){
                CardCheckTask.check(TheSieuToc.getInstance());
                sender.sendMessage(msg.getConfig().getString("Messages.Checked"));
                return true;
            } else {
                sender.sendMessage(msg.getConfig().getString("Messages.Too-Many-Args"));
                return false;
            }
        } else {
            sender.sendMessage(msg.getConfig().getString("Messages.No-Permission"));
            return false;
        }
    }

    private void chooseCard(Player player, ArtxeYAML msg){
        for(String card : TheSieuToc.cardList){
            String text = msg.getConfig().getString("Chat-Gui.Card-Type.Text").replaceAll("(?ium)[{]Card_Type[}]", card);
            String hover = splitListToLine(msg.getConfig().getStringList("Chat-Gui.Card-Type.Hover")).replaceAll("(?ium)[{]Card_Type[}]", card);
            BaseComponent[] message = new ComponentBuilder(text)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, MessageFormat.format("/napthe choose {0}", card)))
                    .create();
            player.spigot().sendMessage(message);
        }
    }

    private void chooseAmount(Player player, String type, ArtxeYAML msg){
        for(Integer amount : TheSieuToc.getInstance().getAmountList()){
            String text = msg.getConfig().getString("Chat-Gui.Card-Amount.Text").replaceAll("(?ium)[{]Card_Amount[}]", amount.toString());
            String hover = splitListToLine(msg.getConfig().getStringList("Chat-Gui.Card-Amount.Hover")).replaceAll("(?ium)[{]Card_Amount[}]", amount.toString());
            BaseComponent[] message = new ComponentBuilder(text)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, MessageFormat.format("/napthe choose {0} {1}", type, amount.toString())))
                    .create();
            player.spigot().sendMessage(message);
        }
    }

    private String splitListToLine(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i<list.size() ; i++) {
            String line = list.get(i);
            sb.append(ChatColor.translateAlternateColorCodes('&', line));
            if(i<list.size()-1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return new ArrayList<>();
    }

    static boolean isValidCard(String type){
        return TheSieuToc.cardList.stream().anyMatch(type::equalsIgnoreCase);
    }

    static boolean isValidAmount(int a){
        for (int amount : TheSieuToc.getInstance().getAmountList()) {
            if (amount == a) {
                return true;
            }
        }
        return false;
    }

}