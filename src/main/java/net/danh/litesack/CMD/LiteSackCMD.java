package net.danh.litesack.CMD;

import net.danh.litesack.API.CMD.CMDBase;
import net.danh.litesack.API.Data.Player.PlayerData;
import net.danh.litesack.API.Data.Sack.SackData;
import net.danh.litesack.API.Utils.Chat;
import net.danh.litesack.API.Utils.File;
import net.danh.litesack.API.Utils.Number;
import net.xconfig.bukkit.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LiteSackCMD extends CMDBase {
    public LiteSackCMD() {
        super("LiteSack");
    }

    @Override
    public void execute(CommandSender c, String[] args) {
        if (c instanceof Player) {
            Player p = (Player) c;
            if (p.hasPermission("ls.withdraw")) {
                if (args.length == 4) {
                    if (args[0].equalsIgnoreCase("withdraw")) {
                        if (SackData.getSackList().contains(args[1])) {
                            if (SackData.getItemList(args[1]).contains(args[2])) {
                                if (PlayerData.removeSackData(p, args[1], args[2], args[3])) return;
                            }
                        }
                    }
                }
            }
            if (p.hasPermission("ls.admin")) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("bypass")) {
                        if (!PlayerData.bypass.containsKey(p)) {
                            PlayerData.bypass.put(p, true);
                            p.sendMessage(TextUtils.colorize("&cBypass complete!"));
                            return;
                        } else {
                            if (!PlayerData.bypass.get(p)) {
                                PlayerData.bypass.put(p, true);
                                p.sendMessage(TextUtils.colorize("&cBypass complete!"));
                                return;
                            }
                            if (PlayerData.bypass.get(p)) {
                                PlayerData.bypass.put(p, false);
                                p.sendMessage(TextUtils.colorize("&cun-bypass complete!"));
                                return;
                            }
                        }
                    }
                }
            }
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
                if (c.hasPermission("ls.admin")) {
                    Chat.sendCommandSenderMessage(c, File.getMessage().getStringList("COMMAND.HELP.ADMIN"));
                }
                Chat.sendCommandSenderMessage(c, File.getMessage().getStringList("COMMAND.HELP.MEMBER"));
            }
        }
        if (c.hasPermission("ls.admin")) {
            if (args.length == 5) {
                if (args[0].equalsIgnoreCase("sackID")) {
                    Player p = Bukkit.getPlayer(args[3]);
                    if (p != null) {
                        Integer amount = Number.getInteger(args[4]);
                        if (args[1].equalsIgnoreCase("set")) {
                            SackData.setStorageSack(p, args[2], amount);
                            Chat.debug(c, "&c sackID = " + args[2] + ", Player = " + args[3] + ", Action = " + args[1] + ", Amount = " + args[4]);
                        }
                        if (args[1].equalsIgnoreCase("add")) {
                            SackData.addStorageSack(p, args[2], amount);
                            Chat.debug(c, "&c sackID = " + args[2] + ", Player = " + args[3] + ", Action = " + args[1] + ", Amount = " + args[4]);
                        }
                        if (args[1].equalsIgnoreCase("remove")) {
                            SackData.removeStorageSack(p, args[2], amount);
                            Chat.debug(c, "&c sackID = " + args[2] + ", Player = " + args[3] + ", Action = " + args[1] + ", Amount = " + args[4]);
                        }
                    }
                }
            }
            if (args.length == 6) {
                if (args[0].equalsIgnoreCase("item")) {
                    Player p = Bukkit.getPlayer(args[4]);
                    if (p != null) {
                        Integer amount = Number.getInteger(args[5]);
                        if (args[1].equalsIgnoreCase("set")) {
                            SackData.setSackItem(p, args[2], args[3], amount);
                            Chat.debug(c, "&c sackID = " + args[2] + ", item = " + args[3] + ", Player = " + args[4] + ", Action = " + args[1] + ", Amount = " + args[5]);
                        }
                        if (args[1].equalsIgnoreCase("add")) {
                            SackData.addSackItem(p, args[2], args[3], amount);
                            Chat.debug(c, "&c sackID = " + args[2] + ", item = " + args[3] + ", Player = " + args[4] + ", Action = " + args[1] + ", Amount = " + args[5]);
                        }
                        if (args[1].equalsIgnoreCase("remove")) {
                            SackData.removeSackItem(p, args[2], args[3], amount);
                            Chat.debug(c, "&c sackID = " + args[2] + ", item = " + args[3] + ", Player = " + args[4] + ", Action = " + args[1] + ", Amount = " + args[5]);
                        }
                    }
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (args[1].equalsIgnoreCase("file")) {
                        File.reloadFiles(c);
                    }
                    if (args[1].equalsIgnoreCase("data")) {
                        SackData.loadSack(c);
                    }
                }
            }
            if (args.length == 5) {
                if (args[0].equalsIgnoreCase("withdraw")) {
                    if (SackData.getSackList().contains(args[1])) {
                        if (SackData.getItemList(args[1]).contains(args[2])) {
                            Player p = Bukkit.getPlayer(args[4]);
                            if (p != null) {
                                PlayerData.removeSackData(p, args[1], args[2], args[3]);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<String> TabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("ls.admin")) {
                commands.add("reload");
                commands.add("sackID");
                commands.add("item");
                commands.add("bypass");
            }
            commands.add("help");
            commands.add("withdraw");
            StringUtil.copyPartialMatches(args[0], commands, completions);
        }
        if (args.length == 2) {
            if (sender.hasPermission("ls.admin")) {
                if (args[0].equalsIgnoreCase("reload")) {
                    StringUtil.copyPartialMatches(args[1], Arrays.asList("file", "data"), completions);
                }
                if (args[0].equalsIgnoreCase("sackID") || args[0].equalsIgnoreCase("item")) {
                    StringUtil.copyPartialMatches(args[1], Arrays.asList("set", "add", "remove"), completions);
                }
            }
            if (args[0].equalsIgnoreCase("withdraw")) {
                StringUtil.copyPartialMatches(args[1], SackData.getSackList(), completions);
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("withdraw")) {
                if (SackData.getSackList().contains(args[1])) {
                    StringUtil.copyPartialMatches(args[2], SackData.getItemList(args[1]), completions);
                }
            }
            if (sender.hasPermission("ls.admin")) {
                if (args[0].equalsIgnoreCase("sackID") || args[0].equalsIgnoreCase("item")) {
                    if (Arrays.asList("set", "add", "remove").contains(args[1])) {
                        StringUtil.copyPartialMatches(args[2], SackData.getSackList(), completions);
                    }
                }
            }
        }
        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("withdraw")) {
                if (SackData.getSackList().contains(args[1])) {
                    if (SackData.getItemList(args[1]).contains(args[2])) {
                        StringUtil.copyPartialMatches(args[3], Collections.singletonList("<number>"), completions);
                    }
                }
            }
            if (sender.hasPermission("ls.admin")) {
                if (args[0].equalsIgnoreCase("sackID")) {
                    if (Arrays.asList("set", "add", "remove").contains(args[1])) {
                        if (SackData.getSackList().contains(args[2])) {
                            StringUtil.copyPartialMatches(args[3], Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()), completions);
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("item")) {
                    if (Arrays.asList("set", "add", "remove").contains(args[1])) {
                        if (SackData.getSackList().contains(args[2])) {
                            StringUtil.copyPartialMatches(args[3], SackData.getItemList(args[2]), completions);
                        }
                    }
                }
            }
        }
        if (args.length == 5) {
            if (sender.hasPermission("ls.admin")) {
                if (args[0].equalsIgnoreCase("sackID")) {
                    if (Arrays.asList("set", "add", "remove").contains(args[1])) {
                        if (SackData.getSackList().contains(args[2])) {
                            if (Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()).contains(args[3])) {
                                StringUtil.copyPartialMatches(args[4], Collections.singletonList("<number>"), completions);
                            }
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("item")) {
                    if (Arrays.asList("set", "add", "remove").contains(args[1])) {
                        if (SackData.getSackList().contains(args[2])) {
                            if (SackData.getItemList(args[2]).contains(args[3])) {
                                StringUtil.copyPartialMatches(args[4], Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()), completions);
                            }
                        }
                    }
                }
            }
        }
        if (args.length == 6) {
            if (sender.hasPermission("ls.admin")) {
                if (args[0].equalsIgnoreCase("item")) {
                    if (Arrays.asList("set", "add", "remove").contains(args[1])) {
                        if (SackData.getSackList().contains(args[2])) {
                            if (SackData.getItemList(args[2]).contains(args[3])) {
                                if (Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()).contains(args[4])) {
                                    StringUtil.copyPartialMatches(args[5], Collections.singletonList("<number>"), completions);
                                }
                            }
                        }
                    }
                }
            }
        }
        Collections.sort(completions);
        return completions;
    }
}
