package me.Silverwolfg11.AdoptionMaster.commands;

import me.Silverwolfg11.AdoptionMaster.data.DataManager;
import me.Silverwolfg11.AdoptionMaster.objects.Adoption;
import me.Silverwolfg11.AdoptionMaster.objects.CommandObj;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FamilyCommand extends CommandObj {


    public FamilyCommand() {
        super("family", false);
    }

    @Override
    public void execute(Player p, String[] args) {

        if(args.length > 1) {
            propMsg(p, "family-usage");
            return;
        }

        String message = null;

        if(args.length == 1) {
            OfflinePlayer objec = Bukkit.getOfflinePlayer(args[0]);

            if(objec == null) {
                propMsg(p, "player-null");
                return;
            }

            if(objec.getLastPlayed() == 0) {
                propMsg(p, "player-null");
                return;
            }

            message = familyMessage(objec.getUniqueId(), false);
        }

        else
            message = familyMessage(p.getUniqueId(), true);


        message = ChatColor.translateAlternateColorCodes('&', message);

        p.sendMessage(message);

    }


    private String familyMessage(UUID p, boolean personal) {
        StringBuilder builder = new StringBuilder();

        OfflinePlayer temp;

        Adoption adopt = DataManager.locateAdoptionByChild(p);

        String pronoun = personal ? "Your " : "Their ";

        if (adopt != null) {
            builder.append("&9");
            builder.append(pronoun);
            builder.append("Parents: ");

            temp = Bukkit.getOfflinePlayer(adopt.getPartner1());

            builder.append(temp == null ? "null" : temp.getName())
                    .append(" + ");

            temp = Bukkit.getOfflinePlayer(adopt.getPartner2());

            builder.append(temp == null ? "null" : temp.getName());

            if (!adopt.getChildren().isEmpty() && adopt.getChildren().size() > 1) {

                builder.append("\n&2");
                builder.append(pronoun);
                builder.append("Siblings: ");

                for (UUID id : adopt.getChildren()) {
                    if (id.equals(p))
                        continue;

                    temp = Bukkit.getOfflinePlayer(id);

                    builder.append(temp == null ? "null" : temp.getName())
                            .append(",");
                }

                builder.deleteCharAt(builder.length() - 1);
            }
        }

        adopt = DataManager.locateAdoptionByParent(p);

        if (adopt != null && !adopt.getChildren().isEmpty()) {
            if (builder.length() != 0)
                builder.append("\n");

            builder.append("&3");
            builder.append(pronoun);
            builder.append("Children: ");

            for (UUID id : adopt.getChildren()) {
                temp = Bukkit.getOfflinePlayer(id);

                if (temp == null)
                    builder.append("null");
                else
                    builder.append(temp.getName());

                builder.append(",");
            }

            builder.deleteCharAt(builder.length() - 1);
        }

        if (builder.length() == 0) {
            builder.append("&4");
            builder.append(personal ? "You" : "They");
            builder.append(" have no family!");
        }

        return builder.toString().trim();
    }
}
