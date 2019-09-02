package me.Silverwolfg11.AdoptionMaster.listeners;

import at.pcgamingfreaks.MarriageMaster.Bukkit.API.MarriagePlayer;
import com.google.common.collect.Lists;
import me.Silverwolfg11.AdoptionMaster.AdoptionMaster;
import me.Silverwolfg11.AdoptionMaster.Messages;
import me.Silverwolfg11.AdoptionMaster.data.DataManager;
import me.Silverwolfg11.AdoptionMaster.objects.Adoption;
import me.Silverwolfg11.AdoptionMaster.util.QuestionUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.UUID;

public class InteractListener implements Listener {

    private AdoptionMaster plugin;

    public InteractListener(AdoptionMaster plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent e) {

        //Check if the player has the interact meta data.
        if (!e.getPlayer().hasMetadata("adoptMasterInteract"))
            return;

        if (!e.getHand().equals(EquipmentSlot.HAND)) {
            cancel(e);
            return;
        }

        Entity entity = e.getRightClicked();

        if (!(entity instanceof Player)) {
            cancel(e);
            return;
        }

        Player victim = (Player) entity;

        //Make sure the interacted entity is not an NPC
        if (checkIfNPC(victim, e.getPlayer())) return;

        //Make sure the interacted player is not already being adopted by someone else.
        if (victim.hasMetadata("adoptMasterAdoptProg")) {
            errorMessage(e, "adoptInProgress");
            return;
        }

        MarriagePlayer partner = AdoptionMaster.getPartner(e.getPlayer());

        if (partner == null) {
            errorMessage(e, "not-married");
            return;
        }


        //Prevent players form adopting their partner.
        if (victim.getUniqueId().equals(partner.getUUID())) {
            errorMessage(e, "adopting-partner");
            return;
        }

        Adoption tempadopt = DataManager.locateAdoptionByChild(e.getPlayer().getUniqueId());

        //Make sure that the interacted player is not already the player's parent.
        if (tempadopt != null && tempadopt.isParent(victim.getUniqueId())) {
            errorMessage(e, "adoptparent");
            return;
        }

        tempadopt = DataManager.locateAdoptionByChild(victim.getUniqueId());

        //Check if there is an adoption. Make sure the player didn't already adopt the child.
        if (tempadopt != null) {
            if (tempadopt.isParent(e.getPlayer().getUniqueId())) {
                errorMessage(e, "adopttwice");
                return;
            }

            MarriagePlayer victimPartner = AdoptionMaster.getPartner(victim);

            //Prevent the player from adopting the victim's partner.
            if (victimPartner != null && tempadopt.containsChild(victimPartner.getUUID())) {
                errorMessage(e, "adopt-victims-partner");
                return;
            }

            errorMessage(e, "adoptnap");
            return;
        }

        //Make sure the player's partner is also online.
        if (!partner.isOnline()) {
            errorMessage(e, "partner-not-online");
            return;
        }

        Player partnerPlayer = partner.getPlayerOnline();

        if (checkIfPartnerNPC(partnerPlayer)) {
            errorMessage(e, "partner-not-online");
            return;
        }

        AdoptionMaster.removeInteractMetaData(e.getPlayer());

        sendPropMessage(e.getPlayer(), "adoptQ-partner");

        modifyAdoptsProg(victim, false);

       int taskId = adoptDelay(victim);

       //Sends Question to Partner
        QuestionUtil.playerConsent(partnerPlayer, Messages.getString("adoptQ-partnerQ", victim.getName()),
                //Accept Runnable
                () -> {
                    sendPropMessage(partnerPlayer, "adoptQ-partner-accept");

                    sendPropMessage(Lists.newArrayList(e.getPlayer(), partnerPlayer), "adoptQ-child");

                    childConsent(e.getPlayer(), partnerPlayer, victim, taskId);
                }, //Deny Runnable
                () -> {
                        sendPropMessage(partnerPlayer, "adoptQ-partner-deny");

                        sendPropMessage(e.getPlayer(), "adoptQ-partner-denied");

                        modifyAdoptsProg(victim, true);
                    }
                );
    }

    private void childConsent(Player p, Player partner, Player child, int taskId) {

        if(Bukkit.getScheduler().isCurrentlyRunning(taskId) || Bukkit.getScheduler().isQueued(taskId))
            Bukkit.getScheduler().cancelTask(taskId);

        modifyAdoptsProg(child, false);

        adoptDelay(child);

        QuestionUtil.playerConsent(child, Messages.getString("adoptQ-childQ", p.getName(), partner.getName()),
                //Accept Runnable
                () -> {
                adoptChild(p, partner, child);

                modifyAdoptsProg(child, true);

        }, () -> {
                sendPropMessage(Lists.newArrayList(p, partner), "adoptQ-child-denied");

                sendPropMessage(child, "adoptQ-child-deny");

                modifyAdoptsProg(child, true);
        });
    }

    private void adoptChild(Player p, Player partner, Player victim) {
        Adoption adopt = DataManager.locateAdoptionByParent(p.getUniqueId());

        if (adopt == null)
            adopt = DataManager.createAdoption(p.getUniqueId(), partner.getUniqueId());

        if (adopt.containsChild(victim.getUniqueId()))
            return;


        sendPropMessage(Lists.newArrayList(p, partner), "adopted", victim.getDisplayName());

        sendPropMessage(victim, "got-adopted", p.getDisplayName(), partner.getDisplayName());

        if (!adopt.getChildren().isEmpty()) {
            OfflinePlayer temp;
            for (UUID id : adopt.getChildren()) {
                temp = Bukkit.getOfflinePlayer(id);

                if (temp == null) continue;

                if (!temp.isOnline()) continue;

                sendPropMessage(temp.getPlayer(), "new-sibling", victim.getDisplayName());
            }
        }

        adopt.addChild(victim.getUniqueId());

        DataManager.saveAdoptions();

    }

    private void errorMessage(PlayerInteractAtEntityEvent event, String property) {
        sendPropMessage(event.getPlayer(), property);
        AdoptionMaster.removeInteractMetaData(event.getPlayer());
    }

    private void sendPropMessage(Player p, String property, Object... args) {
        p.sendMessage(Messages.getString(property, args));
    }

    private void sendPropMessage(ArrayList<Player> players, String property, Object... args) {
        for (Player p : players) {
            p.sendMessage(Messages.getString(property, args));
        }
    }

    private void cancel(PlayerInteractAtEntityEvent e) {
        AdoptionMaster.removeInteractMetaData(e.getPlayer());
        sendPropMessage(e.getPlayer(), "adopt-canceled");
    }

    private boolean checkIfNPC(Player victim, Player adopter) {
        if (victim.hasMetadata("NPC")) {
            sendPropMessage(adopter, "is-statue");
            AdoptionMaster.removeInteractMetaData(adopter);
            return true;
        }

        return false;
    }

    private boolean checkIfPartnerNPC(Player p) {
        return p.hasMetadata("NPC");
    }


    private void modifyAdoptsProg(Player p, boolean remove) {
        if (!p.hasMetadata("adoptMasterAdoptProg")) {
            if (remove) return;

            addAdoptProgMeta(p);
            return;
        }

        if (remove)
            removeAdoptProgMeta(p);
    }

    private int adoptDelay(Player child) {
        return Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> removeAdoptProgMeta(child), 2400);
    }

    private void addAdoptProgMeta(Player p) {
        p.setMetadata("adoptMasterAdoptProg", new FixedMetadataValue(plugin, true));
    }

    private void removeAdoptProgMeta(Player p) {
        if (p.hasMetadata("adoptMasterAdoptProg"))
            p.removeMetadata("adoptMasterAdoptProg", plugin);
    }
}
