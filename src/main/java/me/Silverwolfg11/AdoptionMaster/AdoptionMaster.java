package me.Silverwolfg11.AdoptionMaster;

import at.pcgamingfreaks.MarriageMaster.Bukkit.API.MarriageMasterPlugin;
import at.pcgamingfreaks.MarriageMaster.Bukkit.API.MarriagePlayer;
import me.Silverwolfg11.AdoptionMaster.commands.*;
import me.Silverwolfg11.AdoptionMaster.data.DataManager;
import me.Silverwolfg11.AdoptionMaster.listeners.DivorceListener;
import me.Silverwolfg11.AdoptionMaster.listeners.InteractListener;
import me.Silverwolfg11.AdoptionMaster.objects.Adoption;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.logging.Level;

public class AdoptionMaster extends JavaPlugin {

    private static AdoptionMaster instance;

    private static MarriageMasterPlugin marriage;

    public static ArrayList<Adoption> adoptions;

    public DataManager data;

    public void onEnable() {
        instance = this;

        checkDependency();

        data = new DataManager();

        data.loadData();

        registerCommands();

        registerListener();
    }

    public void onDisable() {
        DataManager.saveAdoptions();
    }

    private void checkDependency() {
        marriage = (MarriageMasterPlugin) Bukkit.getPluginManager().getPlugin("MarriageMaster");

        if (marriage == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Missing MarriageMaster dependency! Disabling plugin...");
            setEnabled(false);
        }
    }


    private void registerCommands() {
        registerCommand("adopt", new AdoptCommand());
        registerCommand("disown", new DisownCommand());
        registerCommand("family", new FamilyCommand());
        registerCommand("runaway", new RunAwayCommand());
        registerCommand("adoptreload", new LoadCommand(this));

    }

    private void registerCommand(String commandName, CommandExecutor command) {
        getCommand(commandName).setExecutor(command);
    }

    private void registerListener() {

        Bukkit.getPluginManager().registerEvents(new InteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new DivorceListener(this), this);

    }

    public static MarriagePlayer getPartner(Player p) {
        return marriage.getPlayerData(p.getUniqueId()).getPartner();
    }



    public static void addInteractMetaData(Player p) {
        if (!p.hasMetadata("adoptMasterInteract"))
            p.setMetadata("adoptMasterInteract", new FixedMetadataValue(instance, true));
    }

    public static void removeInteractMetaData(Player p) {
        if (p.hasMetadata("adoptMasterInteract"))
            p.removeMetadata("adoptMasterInteract", instance);
    }
}
