package me.Silverwolfg11.AdoptionMaster.commands;

import me.Silverwolfg11.AdoptionMaster.AdoptionMaster;
import me.Silverwolfg11.AdoptionMaster.objects.CommandObj;
import me.Silverwolfg11.AdoptionMaster.util.QuestionUtil;
import org.bukkit.entity.Player;

public class AdoptCommand extends CommandObj {

    public AdoptCommand() {
        super("adopt", true);
    }

    @Override
    public void execute(Player p, String[] args) {

        if (args.length == 1) {
            String ad = args[0];

            if(!ad.equalsIgnoreCase("accept") && !ad.equalsIgnoreCase("deny"))
                return;

            if (!QuestionUtil.hasQuestion(p)) {
                propMsg(p, "no-question");
                return;
            }

            QuestionUtil.answerQuestion(p, ad.equalsIgnoreCase("accept"));
            return;
        }

        AdoptionMaster.addInteractMetaData(p);

        propMsg(p, "pre-adopt");
    }
}
