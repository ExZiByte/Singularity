package me.exzibyte.Listeners.MIscellaneous;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class Ready extends ListenerAdapter {

    public void onReady(ReadyEvent event){

        event.getJDA().getPresence().setActivity(Activity.watching("lootrunning"));
        event.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);

        event.getJDA().getGuildById(916886277587079210L).upsertCommand("harvest", "Developer knows what this does if you aren't him go away").queue();
        event.getJDA().getGuildById(916886277587079210L).upsertCommand("test", "Developer knows what this does if you aren't him go away").queue();

        event.getJDA().upsertCommand("apply", "Apply to the Oxryn Wynncraft guild").queue();
        event.getJDA().upsertCommand("applications", "List applications for the Oxryn Wynncraft Guild")
                .addOption(OptionType.INTEGER, "application-number", "The application number to pull up", false, false)
                .addOption(OptionType.INTEGER, "delete", "Delete an application", false, false).queue();
        event.getJDA().upsertCommand("ban", "Ban someone from the server")
                .addOption(OptionType.MENTIONABLE, "member", "The member you wish to ban", true, false)
                .addOption(OptionType.STRING, "reason", "The reason for banning the member", false, false)
                .addOption(OptionType.ATTACHMENT, "proof", "Proof of the reason for the ban", false, false).queue();
        event.getJDA().upsertCommand("clear", "Clear multiple messages at the same time")
                .addOption(OptionType.INTEGER, "amount", "The amount of message to delete", true, false).queue();
        event.getJDA().upsertCommand("undo", "Undo cleared messages").queue();
        event.getJDA().upsertCommand("mute", "Mute someone on the server")
                .addOption(OptionType.MENTIONABLE, "member", "The member you wish to mute", true, false)
                .addOption(OptionType.STRING, "reason", "The reason for muting the member", false, false)
                .addOption(OptionType.ATTACHMENT, "proof", "Proof of the reason for the mute", false, false).queue();
        event.getJDA().upsertCommand("link", "Link Your Discord and Minecraft Accounts")
                .addOption(OptionType.STRING, "minecraft-name", "Your Minecraft Account name", false, false)
                .addOption(OptionType.STRING, "remove", "Remove your account link. You can put a funny message here.", false, false).queue();
        event.getJDA().upsertCommand("lr", "Mark a world that was looted")
                .addOption(OptionType.NUMBER, "world", "The world that was looted", true, false)
                .addOption(OptionType.STRING, "region", "What part of the map was looted", true, true).queue();
        event.getJDA().upsertCommand("lrlist", "List already looted worlds/regions. Not 100% accurate").queue();
    }


}
