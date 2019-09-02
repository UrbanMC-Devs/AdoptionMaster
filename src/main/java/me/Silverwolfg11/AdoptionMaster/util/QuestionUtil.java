package me.Silverwolfg11.AdoptionMaster.util;

import me.Silverwolfg11.JSONMessage.JSONMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class QuestionUtil {

    private static HashMap<UUID, Runnable[]> questionMap = new HashMap<>();


    /* public void playerConsent(Player target, String q, Runnable accept, Runnable deny) {



        List<Option> options = new ArrayList<>();

        options.add(new Option("yes", accept));
        options.add(new Option("no", deny));

        LinkedQuestion question = new LinkedQuestion(QuestionManager.getNextQuestionId(), Collections.singletonList(target.getName()), q, options);

        try {
            questioner.getQuestionManager().appendLinkedQuestion(question);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (String line : questioner.formatQuestion(question, "Adoption"))
            target.sendMessage(line);
    } */

    public static void playerConsent(Player target, String q, Runnable accept, Runnable deny) {

        Runnable[] runnables = new Runnable[2];
        runnables[0] = accept;
        runnables[1] = deny;

        questionMap.put(target.getUniqueId(), runnables);

        JSONMessage message = JSONMessage.create();

        message.then("[").color(ChatColor.GRAY).then("Question").color(ChatColor.DARK_AQUA).then("] ").color(ChatColor.GRAY)
                .then(q).color(ChatColor.AQUA).then("\n")
                .then("[Yes]").runCommand("/adopt accept").color(ChatColor.GREEN).tooltip(JSONMessage.create("Click to accept!").color(ChatColor.YELLOW))
                .then(" ")
                .then("[No]").runCommand("/adopt deny").color(ChatColor.RED).tooltip(JSONMessage.create("Click to deny!").color(ChatColor.DARK_RED));


        message.send(target);
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
