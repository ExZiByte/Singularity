package me.exzibyte.Listeners.Moderation;

import me.exzibyte.Utilities.Colors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.Instant;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

public class Mute extends ListenerAdapter {

    Colors colors = new Colors();

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("mute")) return;
        if (!event.isFromGuild()) return;
        EmbedBuilder eb = new EmbedBuilder();
        EmbedBuilder log = new EmbedBuilder();
        if (!event.getMember().hasPermission(Permission.VOICE_MUTE_OTHERS)) {
            eb.setDescription("Insufficient Permission!\nYou require the permission to Ban members from this guild.\n\n If you believe this message was shown in error contact the guild owner.");
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
        if (event.getGuild().getRolesByName("muted", true).isEmpty()) {
            event.getGuild().createRole().setColor(0x95a5a6).setName("Muted").setMentionable(false).queue((role) -> {
                role.getManager().revokePermissions(EnumSet.of(Permission.MESSAGE_SEND, Permission.MESSAGE_MANAGE, Permission.MESSAGE_SEND_IN_THREADS, Permission.REQUEST_TO_SPEAK, Permission.MESSAGE_TTS, Permission.CREATE_INSTANT_INVITE, Permission.CREATE_PRIVATE_THREADS, Permission.CREATE_PUBLIC_THREADS)).queue();
            });

            eb.setDescription("Created a muted role as the server didn't have one.");
            eb.setColor(colors.successGreen);
            eb.setFooter("Singularity created mute role | Oxryn");

            log.setDescription("Created a muted role as the server didn't have one.");
            log.setColor(colors.successGreen);
            log.setTimestamp(Instant.now());
            log.setFooter("Singularity created mute role | Log | Oxryn");

            event.getChannel().sendMessageEmbeds(eb.build()).queue((msg) -> {
                msg.delete().queueAfter(2, TimeUnit.MINUTES);
                eb.clear();
                event.getGuild().getTextChannelCache().getElementById(967202671075418132L).sendMessageEmbeds(log.build()).queue((__) -> {
                    log.clear();
                });
            });
        }

        if (reason == null && proof == null) {
            eb.setDescription(String.format("Muted %s for No reason specified", event.getOption("member").getAsMember().getAsMention()));
            eb.setColor(colors.successGreen);
            eb.setFooter("Singularity Member Muted | Oxryn");

            log.setDescription(String.format("%s muted %s for \n\n```\nNo reason specified\n```", event.getMember().getAsMention(), event.getOption("member").getAsMember().getAsMention()));
            log.setColor(colors.warningYellow);
            log.setTimestamp(Instant.now());
            log.setFooter("Singularity Member Muted | Log | Oxryn");

            event.replyEmbeds(eb.build()).queue((msg) -> {
                msg.deleteOriginal().queueAfter(2, TimeUnit.SECONDS);
                eb.clear();
                event.getGuild().getTextChannelCache().getElementById(967202671075418132L).sendMessageEmbeds(log.build()).queue((__) -> {
                    log.clear();
                    event.getGuild().addRoleToMember(event.getMember().getId(), event.getGuild().getRolesByName("Muted", false).get(0)).reason(String.format("Reason: No reason specified | Executor: %s", event.getMember().getUser().getAsTag())).queue();
                });
            });
        }
        if (reason != null && proof == null) {
            eb.setDescription(String.format("Muted %s for %s", event.getOption("member").getAsMember().getAsMention(), reason.getAsString()));
            eb.setColor(colors.successGreen);
            eb.setFooter("Singularity Member Muted | Oxryn");

            log.setDescription(String.format("%s muted %s for \n\n```\n%s\n```", event.getMember().getAsMention(), event.getOption("member").getAsMember().getAsMention(), reason.getAsString()));
            log.setColor(colors.warningYellow);
            log.setTimestamp(Instant.now());
            log.setFooter("Singularity Member Muted | Log | Oxryn");

            event.replyEmbeds(eb.build()).queue((msg) -> {
                msg.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                eb.clear();
                event.getGuild().getTextChannelCache().getElementById(967202671075418132L).sendMessageEmbeds(log.build()).queue((__) -> {
                    log.clear();
                    event.getGuild().addRoleToMember(event.getMember().getId(), event.getGuild().getRolesByName("Muted", false).get(0)).reason(String.format("Reason: %s | Executor: %s", reason.getAsString(), event.getMember().getUser().getAsTag())).queue();
                });
            });
        }
        if (reason == null && proof != null) {
            eb.setDescription(String.format("Muted %s for no reason specified", event.getOption("member").getAsMember().getAsMention()));
            eb.setImage(proof.getAsAttachment().getUrl());
            eb.setColor(colors.successGreen);
            eb.setFooter("Singularity Member Muted | Oxryn");

            log.setDescription(String.format("%s muted %s for \n\n```\nNo Reason Specified\n```", event.getMember().getAsMention(), event.getOption("member").getAsMember().getAsMention()));
            log.setImage(proof.getAsAttachment().getUrl());
            log.setColor(colors.warningYellow);
            log.setTimestamp(Instant.now());
            log.setFooter("Singularity Member Muted | Log | Oxryn");

            event.replyEmbeds(eb.build()).queue((msg) -> {
                msg.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                eb.clear();
                event.getGuild().getTextChannelCache().getElementById(967202671075418132L).sendMessageEmbeds(log.build()).queue((__) -> {
                    log.clear();
                    event.getGuild().addRoleToMember(event.getMember().getId(), event.getGuild().getRolesByName("Muted", false).get(0)).reason(String.format("Reason: %s | Executor: %s", reason.getAsString(), event.getMember().getUser().getAsTag())).queue();
                });
            });
        }

        if (reason != null && proof != null) {
            eb.setDescription(String.format("Muted %s for %s", event.getOption("member").getAsMember().getAsMention(), reason.getAsString()));
            eb.setImage(proof.getAsAttachment().getUrl());
            eb.setColor(colors.successGreen);
            eb.setFooter("Singularity Member Muted | Oxryn");

            log.setDescription(String.format("%s muted %s for \n\n```\n%s\n```", event.getMember().getAsMention(), event.getOption("member").getAsMember().getAsMention(), reason.getAsString()));
            log.setImage(proof.getAsAttachment().getUrl());
            log.setColor(colors.warningYellow);
            log.setTimestamp(Instant.now());
            log.setFooter("Singularity Member Muted | Log | Oxryn");

            event.replyEmbeds(eb.build()).queue((msg) -> {
                msg.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                eb.clear();
                event.getGuild().getTextChannelCache().getElementById(967202671075418132L).sendMessageEmbeds(log.build()).queue((__) -> {
                    log.clear();
                    event.getGuild().addRoleToMember(event.getMember().getId(), event.getGuild().getRolesByName("Muted", false).get(0)).reason(String.format("Reason: %s | Executor: %s", reason.getAsString(), event.getMember().getUser().getAsTag())).queue();
                });
            });
        }
    }

}
