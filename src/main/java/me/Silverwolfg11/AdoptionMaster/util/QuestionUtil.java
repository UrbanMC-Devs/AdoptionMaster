package me.Silverwolfg11.AdoptionMaster.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class QuestionUtil {

    private static HashMap<UUID, Runnable[]> questionMap = new HashMap<>();

    public static void playerConsent(Player target, String q, Runnable accept, Runnable deny) {

        Runnable[] runnables = new Runnable[2];
        runnables[0] = accept;
        runnables[1] = deny;

        questionMap.put(target.getUniqueId(), runnables);

        ComponentBuilder builder = new ComponentBuilder("[").color(net.md_5.bungee.api.ChatColor.GRAY);

        builder.append("Question").color(ChatColor.DARK_AQUA).append("] ").color(ChatColor.GRAY).append(q).color(ChatColor.AQUA).append("\n");
        builder.append("[Yes]").color(ChatColor.GREEN)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/adopt accept"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to accept!").color(ChatColor.YELLOW).create()));
        builder.append(" ");
        builder.append("[No]").color(ChatColor.RED)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/adopt deny"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to deny!").color(ChatColor.RED).create()));

        target.spigot().sendMessage(builder.create());
    }

    public static boolean hasQuestion(Player p) {
        return questionMap.containsKey(p.getUniqueId());
    }

    public static void answerQuestion(Player p, boolean accepted) {
        if (!questionMap.containsKey(p.getUniqueId())) return;

        int arg = accepted ? 0 : 1;

        Runnable run = questionMap.get(p.getUniqueId())[arg];

        questionMap.remove(p.getUniqueId());

        run.run();
    }

}
