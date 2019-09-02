package me.Silverwolfg11.AdoptionMaster.listeners;

import at.pcgamingfreaks.MarriageMaster.Bukkit.API.Events.DivorcedEvent;
import me.Silverwolfg11.AdoptionMaster.AdoptionMaster;
import me.Silverwolfg11.AdoptionMaster.Messages;
import me.Silverwolfg11.AdoptionMaster.data.DataManager;
import me.Silverwolfg11.AdoptionMaster.objects.Adoption;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class DivorceListener implements Listener {

    AdoptionMaster plugin;

    public DivorceListener(AdoptionMaster plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommand(DivorcedEvent event) {

        Adoption adoption = DataManager.locateAdoptionByParent(event.getPlayer1().getUUID());

        if (adoption == null)
            return;

        voidAdoption(adoption);
    }

    private void voidAdoption(Adoption adopt) {

        Player temp = Bukkit.getPlayer(adopt.getPartner1());

        if (temp != null && temp.isOnline())
            temp.sendMessage(Messages.getString("children-disowned"));

        temp = Bukkit.getPlayer(adopt.getPartner2());

        if (temp != null)
            temp.sendMessage(Messages.getString("children-disowned"));

        for (UUID id : adopt.getChildren()) {
            temp = Bukkit.getPlayer(id);

            if (temp != null && temp.isOnline())
                temp.sendMessage(Messages.getString("parents-disowned"));
        }

        AdoptionMaster.adoptions.remove(adopt);
        DataManager.saveAdoptions();
    }

}
