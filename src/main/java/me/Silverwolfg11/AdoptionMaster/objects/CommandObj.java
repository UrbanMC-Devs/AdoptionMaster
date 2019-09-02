package me.Silverwolfg11.AdoptionMaster.objects;

import me.Silverwolfg11.AdoptionMaster.AdoptionMaster;
import me.Silverwolfg11.AdoptionMaster.Messages;
import me.Silverwolfg11.AdoptionMaster.data.DataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class CommandObj implements CommandExecutor {

    private String permission;
    private boolean marriedOnly;

    public CommandObj(String permission, boolean marriedOnly) {
        this.permission = permission;
        this.marriedOnly = marriedOnly;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command label, String s, String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.getString("not-player"));
            return true;
        }

        if(!sender.hasPermission("adoption." + permission)) {
            sender.sendMessage(Messages.getString("no-perm"));
            return true;
        }

        Player p = (Player) sender;

        if(AdoptionMaster.getPartner(p) == null && marriedOnly && args.length != 1) {
            p.sendMessage(Messages.getString("not-married"));
            return true;
        }

        execute(p, args);

        return true;
    }

    public abstract void execute(Player p, String[] args);


    public void propMsg(Player p, String property) {
        p.sendMessage(Messages.getString(property));
    }

    public void save() {
        DataManager.saveAdoptions();
    }
}
