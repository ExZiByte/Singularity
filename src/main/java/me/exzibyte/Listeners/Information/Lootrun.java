package me.exzibyte.Listeners.Information;

import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;

import java.util.ArrayList;
import java.util.List;

public class Lootrun extends ListenerAdapter {

    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event){
        if(!event.getName().equals("lr")) return;

        List<Command.Choice> regionChoices = new ArrayList<>();

        regionChoices.add(new Command.Choice("Ragni", "regionRagni"));
        regionChoices.add(new Command.Choice("Troms", "regionTroms"));
        regionChoices.add(new Command.Choice("Silent Expanse", "regionSE"));
        regionChoices.add(new Command.Choice("Rodoroc", "regionRodoroc"));
        regionChoices.add(new Command.Choice("Sky Islands", "regionSI"));
        regionChoices.add(new Command.Choice("Light Forest", "regionLF"));
        regionChoices.add(new Command.Choice("Corkus", "regionCorkus"));
        regionChoices.add(new Command.Choice("Unleveled", "regionUnleveled"));

        if(event.getFocusedOption().getName().equals("region")){
            event.replyChoices(regionChoices).queue();
        }
    }

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        if(!event.getName().equals("lr")) return;

    }

}
