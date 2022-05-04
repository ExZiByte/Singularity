package me.exzibyte.Listeners.Test;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.ArrayList;
import java.util.List;

public class Test extends ListenerAdapter {

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        if(!event.getName().equals("test")) return;
        if(!event.isFromGuild()) return;

        List<ItemComponent> buttons = new ArrayList<>();

        buttons.add(Button.of(ButtonStyle.SUCCESS, "guildMemberYes", "Yes", Emoji.fromUnicode("\u2705")));
        buttons.add(Button.of(ButtonStyle.DANGER, "guildMemberNo", "No", Emoji.fromUnicode("\u2716")));

        event.reply(String.format("%s, is this person part of the Oxryn Wynncraft Guild", event.getMember().getAsMention())).addActionRow(buttons).queue();
    }

}
