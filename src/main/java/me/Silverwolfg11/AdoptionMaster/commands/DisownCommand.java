package me.Silverwolfg11.AdoptionMaster.commands;

import me.Silverwolfg11.AdoptionMaster.AdoptionMaster;
import me.Silverwolfg11.AdoptionMaster.Messages;
import me.Silverwolfg11.AdoptionMaster.data.DataManager;
import me.Silverwolfg11.AdoptionMaster.objects.Adoption;
import me.Silverwolfg11.AdoptionMaster.objects.CommandObj;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DisownCommand extends CommandObj {

    public DisownCommand() {
        super("disown", true);
    }

    @Override
    public void execute(Player p, String[] args) {

        if (args.length != 1) {
            propMsg(p, "disown-usage");
            return;
        }

        Adoption adopt = DataManager.locateAdoptionByParent(p.getUniqueId());

        if (adopt == null) {
            propMsg(p, "no-children");
            return;
        }

        OfflinePlayer child = Bukkit.getOfflinePlayer(args[0]);

        if (child == null) {
            propMsg(p, "child-doesnt-exist");
            return;
        }

        if (!adopt.containsChild(child.getUniqueId())) {
            propMsg(p, "not-child");
            return;
        }

        adopt.removeChild(child.getUniqueId());

        p.sendMessage(Messages.getString("child-disowned", child.getName()));

        OfflinePlayer tempPlayer = (p.getUniqueId() == adopt.getPartner1()) ? Bukkit.getOfflinePlayer(adopt.getPartner2()) : Bukkit.getOfflinePlayer(adopt.getPartner1());

        if(tempPlayer != null)
            if (tempPlayer.isOnline())
                tempPlayer.getPlayer().sendMessage(Messages.getString("child-disowned", child.getName()));

        if (child.isOnline())
            child.getPlayer().sendMessage(Messages.getString("parent-disowned", p.getName(), (tempPlayer == null ? "null" : tempPlayer.getName())));

        if (!adopt.getChildren().isEmpty())
            for (UUID id : adopt.getChildren()) {
                tempPlayer = Bukkit.getOfflinePlayer(id);

                if(tempPlayer == null) continue;

                if(!tempPlayer.isOnline()) continue;

                tempPlayer.getPlayer().sendMessage(Messages.getString("sibling-disowned", p.getName()));
            }


        if (adopt.getChildren().isEmpty())
            AdoptionMaster.adoptions.remove(adopt);

        save();

    }

}
