package me.exzibyte.Listeners.Moderation;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import me.exzibyte.Singularity;
import me.exzibyte.Utilities.Colors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import org.bson.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Filters.eq;

public class Clear extends ListenerAdapter {

    Colors colors = new Colors();
    private final Singularity singularity;

    public Clear(Singularity singularity) {
        this.singularity = singularity;
    }

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        if(!event.getName().equals("clear")) return;
        if(!event.isFromGuild()) return;
        EmbedBuilder eb = new EmbedBuilder();
        EmbedBuilder log = new EmbedBuilder();
        if(!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            eb.setDescription("Insufficient Permission!\nYou require the permission to manage message for this guild.\n\n If you believe this message was shown in error contact the guild owner.");
            eb.setColor(colors.warningYellow);
            eb.setFooter("Singularity Insufficient Permissions | Oxryn");

            event.replyEmbeds(eb.build()).queue((msg) -> {
                msg.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
                eb.clear();
            });
            return;
        };
        RestAction<List<Message>> history = event.getTextChannel().getHistory().retrievePast(event.getOption("amount").getAsInt());
        List<BasicDBObject> messages = new ArrayList<>();

        for (Message msg: history.complete()){
            if(msg.getAuthor().isBot() || msg.getAuthor().isSystem()) continue;
            messages.add(new BasicDBObject("message", msg.getContentDisplay()).append("authorId", msg.getAuthor().getIdLong()));
        }

        Document textChannelDoc = new Document("textChannelID", event.getTextChannel().getId()).append("messages", messages);

        MongoCollection<Document> messagesLog = singularity.getDatabase().getCollection("messsages");
        if(messagesLog.find(eq("textChannelID", event.getTextChannel().getId())).first() != null) {
            messagesLog.findOneAndReplace(eq("textChannelID", event.getTextChannel().getId()), textChannelDoc);
        } else {
            messagesLog.insertOne(textChannelDoc);
        }
        eb.setDescription(String.format("Cleared **%s** messages from %s", event.getOption("amount").getAsInt(), event.getChannel().getAsMention()));
        eb.setColor(colors.successGreen);
        eb.setFooter("Singularity Messages Cleared | Oxryn");

        log.setDescription(String.format("%s cleared **%s** messages from %s", event.getMember().getAsMention(), event.getOption("amount").getAsInt(), event.getChannel().getAsMention()));
        log.setColor(colors.warningYellow);
        log.setTimestamp(Instant.now());
        log.setFooter("Singularity Messages Cleared | Log | Oxryn");

        event.getTextChannel().getHistory().retrievePast(event.getOption("amount").getAsInt()).queue((hist) -> {
            event.getGuild().getTextChannelCache().getElementById(event.getChannel().getId()).deleteMessages(hist).queue();
            event.replyEmbeds(eb.build()).queue((msg) -> {
                eb.clear();
                event.getHook().deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
            });
        });
    }

}
