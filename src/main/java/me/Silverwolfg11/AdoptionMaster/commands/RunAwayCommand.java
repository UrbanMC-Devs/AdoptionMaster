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

public class RunAwayCommand extends CommandObj {
    public RunAwayCommand() {
        super("runaway", false);
    }

    @Override
    public void execute(Player p, String[] args) {
        Adoption adopt = DataManager.locateAdoptionByChild(p.getUniqueId());

        if (adopt == null) {
            propMsg(p, "no-parents");
            return;
        }

        adopt.removeChild(p.getUniqueId());

        propMsg(p, "ran-away");

        Player parent = Bukkit.getPlayer(adopt.getPartner1());

        if (parent != null)
            if (parent.isOnline())
                parent.sendMessage(Messages.getString("child-ranaway", p.getName()));

        parent = Bukkit.getPlayer(adopt.getPartner2());

        if (parent != null)
            if (parent.isOnline())
                parent.sendMessage(Messages.getString("child-ranaway", p.getName()));

        if (!adopt.getChildren().isEmpty()) {
            OfflinePlayer temp;
            for (UUID id : adopt.getChildren()) {
                temp = Bukkit.getOfflinePlayer(id);

                if (temp == null) continue;

                if (!temp.isOnline()) continue;

                temp.getPlayer().sendMessage(Messages.getString("sibling-ranaway", p.getName()));
            }
        }

        if (adopt.getChildren().isEmpty())
            AdoptionMaster.adoptions.remove(adopt);

        save();
    }
}
