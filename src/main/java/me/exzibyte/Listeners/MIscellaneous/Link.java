package me.exzibyte.Listeners.MIscellaneous;

import com.mongodb.client.MongoCollection;
import me.exzibyte.Singularity;
import me.exzibyte.Utilities.Colors;
import me.exzibyte.Utilities.Logging;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Filters.eq;

public class Link extends ListenerAdapter {

    private final Singularity singularity;
    Colors colors = new Colors();
    Logging logging = new Logging();

    public Link(Singularity singularity) {
        this.singularity = singularity;
    }

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("link")) return;
        if (!event.isFromGuild()) return;

        EmbedBuilder eb = new EmbedBuilder();
        MongoCollection<Document> members = singularity.getDatabase().getCollection("members");
        boolean presentInGuild = false;
        JSONParser parser = new JSONParser();
        if(event.getOption("minecraft-name") != null) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI(String.format("https://api.wynncraft.com/v2/player/%s/stats", event.getOption("minecraft-name").getAsString()))).GET().build();
                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                JSONObject fullJSON = (JSONObject) parser.parse(response.body());
                JSONArray dataArray = (JSONArray) parser.parse(fullJSON.get("data").toString());
                JSONObject playerObj = (JSONObject) dataArray.get(0);
                JSONObject guildInfo = (JSONObject) playerObj.get("guild");
                if(guildInfo.get("name").equals("Oxryn")) {
                    presentInGuild = true;
                }
            } catch (Exception e) {
                logging.error(this.getClass(), e.getMessage());
            }
        }

        if(event.getOption("minecraft-name") != null && !presentInGuild){

            eb.setDescription("It seems you are not in the Oxryn Wynncraft Guild. You can apply to join if you haven't already by typing /apply and filling out the information requested");
            eb.setColor(colors.warningYellow);
            eb.setTimestamp(Instant.now());
            eb.setFooter("Member not in Oxryn Wynncraft Guild");

            event.replyEmbeds(eb.build()).queue((msg) -> {
                eb.clear();
                event.getHook().deleteOriginal().queueAfter(2, TimeUnit.MINUTES);
            });
            return;
        }
        if (event.getOption("remove") != null) {
            String minecraftName = members.find(eq("id", event.getMember().getIdLong())).first().getString("minecraftName");
            Document updatedMemberDoc = new Document("id", event.getMember().getIdLong())
                    .append("memberName", event.getMember().getUser().getAsTag())
                    .append("minecraftName", "");
            members.findOneAndReplace(eq("id", event.getMember().getIdLong()), updatedMemberDoc);

            eb.setDescription(String.format("Successfully unlinked your Minecraft account [%s] from your Discord account", minecraftName));
            eb.setColor(colors.successGreen);
            eb.setTimestamp(Instant.now());
            eb.setFooter("Minecraft/Discord Account Unlink | Oxryn");

            event.replyEmbeds(eb.build()).queue((msg) -> {
                eb.clear();
                event.getHook().deleteOriginal().queueAfter(2, TimeUnit.MINUTES);
            });
            return;
        }

        if(!members.find(eq("id", Objects.requireNonNull(event.getMember()).getIdLong())).first().get("minecraftName").toString().equals("")) {
            eb.setDescription("You have already linked your Minecraft and Discord accounts. If you need to update your minecraft name please use /link remove");
            eb.setColor(colors.warningYellow);
            eb.setTimestamp(Instant.now());
            eb.setFooter("Minecraft/Discord Account Linked Already | Oxryn");

            event.replyEmbeds(eb.build()).queue((msg) -> {
                eb.clear();
                event.getHook().deleteOriginal().queueAfter(2, TimeUnit.MINUTES);
            });
            return;
        }

        Document updatedMemberDoc = new Document("id", event.getMember().getUser().getIdLong())
                .append("memberName", event.getMember().getUser().getAsTag())
                .append("minecraftName", event.getOption("minecraft-name").getAsString());
        members.findOneAndReplace(eq("id", event.getMember().getIdLong()), updatedMemberDoc);

        eb.setDescription(String.format("Successfully linked your Minecraft account [%s] with your Discord account", event.getOption("minecraft-name").getAsString()));
        eb.setColor(colors.successGreen);
        eb.setTimestamp(Instant.now());
        eb.setFooter("Minecraft/Discord Account Link | Oxryn");

        event.replyEmbeds(eb.build()).queue((msg) -> {
            eb.clear();
            event.getHook().deleteOriginal().queueAfter(2, TimeUnit.MINUTES);
        });
    }
}
