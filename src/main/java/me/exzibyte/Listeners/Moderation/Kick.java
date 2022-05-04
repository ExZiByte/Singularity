package me.exzibyte.Listeners.Moderation;

import me.exzibyte.Utilities.Colors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class Kick extends ListenerAdapter {

    Colors colors = new Colors();

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("kick")) return;
        if (!event.isFromGuild()) return;
        EmbedBuilder eb = new EmbedBuilder();
        EmbedBuilder log = new EmbedBuilder();
        if (!event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
            eb.setDescription("Insufficient Permission!\nYou require the permission to Kick members from this guild.\n\n If you believe this message was shown in error contact the guild owner.");
            eb.setColor(colors.warningYellow);
            eb.setFooter("Singularity Insufficient Permissions | Oxryn");

            event.replyEmbeds(eb.build()).queue((msg) -> {
                msg.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                eb.clear();
            });
            return;
        }

        var reason = event.getOption("reason");
        var proof = event.getOption("proof");

        if (event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
            if (reason == null && proof == null) {
                eb.setDescription(String.format("Kicked %s for No Reason Specified", event.getOption("member").getAsMember().getAsMention()));
                eb.setColor(colors.successGreen);
                eb.setFooter("Singularity Member Kicked | Oxryn");

                log.setDescription(String.format("%s Kicked member: %s\n\nReason:\n```\nNo Reason\n```", event.getMember().getAsMention(), event.getOption("member").getAsMember().getAsMention()));
                log.setColor(colors.warningYellow);
                log.setTimestamp(Instant.now());
                log.setFooter("Singularity Member Kicked | Log | Oxryn");


                event.replyEmbeds(eb.build()).queue((msg) -> {
                    eb.clear();
                    msg.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                    event.getGuild().getTextChannelCache().getElementById(967202671075418132L).sendMessageEmbeds(log.build()).queue((msg2) -> {
                        log.clear();
                        event.getGuild().kick(event.getOption("member").getAsMember(), String.format("No Reason | Kick Executor: %s", event.getMember().getUser().getAsTag())).queue();
                    });
                });
            }
            if (reason != null && proof == null) {
                eb.setDescription(String.format("Kicked %s for %s", event.getOption("member").getAsMember().getAsMention(), reason.getAsString()));
                eb.setColor(colors.successGreen);
                eb.setFooter("Singularity Member Kicked | Oxryn");

                log.setDescription(String.format("%s Kicked member: %s\n\nReason:\n```\n%s\n```", event.getMember().getAsMention(), event.getOption("member").getAsMember().getAsMention(), reason.getAsString()));
                log.setColor(colors.warningYellow);
                log.setTimestamp(Instant.now());
                log.setFooter("Singularity Member Kicked | Log | Oxryn");

                event.replyEmbeds(eb.build()).queue((msg) -> {
                    eb.clear();
                    msg.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                    event.getGuild().getTextChannelCache().getElementById(967202671075418132L).sendMessageEmbeds(log.build()).queue((msg2) -> {
                        log.clear();
                        event.getGuild().kick(event.getOption("member").getAsMember(), String.format("%s | Kick Executor: %s", reason.getAsString(), event.getMember().getUser().getAsTag())).queue();
                    });
                });
            }
            if (reason == null && proof != null) {
                eb.setDescription(String.format("Kicked %s for %s", event.getOption("member").getAsMember().getAsMention(), "No Reason Specified"));
                eb.setImage(proof.getAsAttachment().getUrl());
                eb.setColor(colors.successGreen);
                eb.setFooter("Singularity Member Kicked | Oxryn");

                log.setDescription(String.format("%s Kicked member: %s\n\nReason:\n```\n%s\n```", event.getMember().getAsMention(), event.getOption("member").getAsMember().getAsMention(), "No Reason Specified"));
                log.setImage(proof.getAsAttachment().getUrl());
                log.setColor(colors.warningYellow);
                log.setTimestamp(Instant.now());
                log.setFooter("Singularity Member Kicked | Log | Oxryn");

                event.replyEmbeds(eb.build()).queue((msg) -> {
                    eb.clear();
                    msg.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                    event.getGuild().getTextChannelCache().getElementById(967202671075418132L).sendMessageEmbeds(log.build()).queue((msg2) -> {
                        log.clear();
                        event.getGuild().kick(event.getOption("member").getAsMember(), String.format("%s | Kick Executor: %s", "No Reason Specified", event.getMember().getUser().getAsTag())).queue();
                    });
                });
            }
            if (reason != null && proof != null) {
                eb.setDescription(String.format("Kicked %s for %s", event.getOption("member").getAsMember().getAsMention(), reason.getAsString()));
                eb.setImage(proof.getAsAttachment().getUrl());
                eb.setColor(colors.successGreen);
                eb.setFooter("Singularity Member Kicked | Oxryn");

                log.setDescription(String.format("%s Kicked member: %s\n\nReason:\n```\n%s\n```", event.getMember().getAsMention(), event.getOption("member").getAsMember().getAsMention(), reason.getAsString()));
                log.setImage(proof.getAsAttachment().getUrl());
                log.setColor(colors.warningYellow);
                log.setTimestamp(Instant.now());
                log.setFooter("Singularity Member Kicked | Log | Oxryn");

                event.replyEmbeds(eb.build()).queue((msg) -> {
                    eb.clear();
                    msg.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                    event.getGuild().getTextChannelCache().getElementById(967202671075418132L).sendMessageEmbeds(log.build()).queue((msg2) -> {
                        log.clear();
                        event.getGuild().kick(event.getOption("member").getAsMember(), String.format("%s | Kick Executor: %s", reason.getAsString(), event.getMember().getUser().getAsTag())).queue();
                    });
                });
            }
        }
    }

}
