package me.exzibyte.Listeners.Moderation;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import me.exzibyte.Singularity;
import me.exzibyte.Utilities.Logging;
import me.exzibyte.Utilities.Webhooks;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Filters.eq;

public class Undo extends ListenerAdapter {

    private final Singularity singularity;
    Logging logging = new Logging();

    public Undo(Singularity singularity) {
        this.singularity = singularity;
    }

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("undo")) return;

        if (event.getTextChannel().retrieveWebhooks().complete().isEmpty()) {
            event.getTextChannel().createWebhook("Bogo Binted").queue((webhook) -> {
                webhook.getManager().setName("Bogo Binted").queue();
            });
        }


        FindIterable<Document> iterable = singularity.getDatabase().getCollection("messsages").find(eq("textChannelID", event.getTextChannel().getId()));
        MongoCursor<Document> cursor = iterable.cursor();
        HashMap<String, HashMap<String, Long>> messages = new HashMap<>();
        HashMap<String, Long> messageInfo = new HashMap<>();
        while(cursor.hasNext()){
            messageInfo.put(cursor.next().getString("message"), cursor.next().getLong("authorId"));
            messages.put("message", messageInfo);
        }

        System.out.println(messages);

//        for (Webhook webhook : Objects.requireNonNull(event.getGuild()).retrieveWebhooks().complete()) {
//            if (!webhook.getName().equals("Bogo Binted")) continue;
//            Webhooks webhooks = new Webhooks(webhook.getUrl());
//            webhooks.setContent(messages.toString());
//            webhooks.setAvatarUrl(event.getGuild().getMemberById(cursor.next().getLong("authorId")).getEffectiveAvatarUrl());
//            webhooks.setUsername(event.getGuild().getMemberById(cursor.next().getLong("authorId")).getEffectiveName());
//            try {
//                webhooks.execute();
//            } catch (IOException e) {
//
//            }
//        }
        event.reply("This is just a message to allow discord to not be pissed off").setEphemeral(true).queue();
    }
}
