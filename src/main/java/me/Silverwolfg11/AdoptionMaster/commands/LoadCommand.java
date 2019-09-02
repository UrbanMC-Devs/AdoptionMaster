package me.Silverwolfg11.AdoptionMaster.commands;

import me.Silverwolfg11.AdoptionMaster.AdoptionMaster;
import me.Silverwolfg11.AdoptionMaster.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoadCommand implements CommandExecutor {

    private AdoptionMaster plugin;

    public LoadCommand(AdoptionMaster plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!sender.hasPermission("adoption.load")) {
            propMsg(sender, "no-perm");
            return true;
        }

        plugin.data.loadAdoptions();

        propMsg(sender, "load-adoptions");

        return true;
    }

    private void propMsg(CommandSender sender, String property) {
        sender.sendMessage((sender instanceof Player ? Messages.getString(property) : ChatColor.stripColor(Messages.getString(property))));
    }

}
