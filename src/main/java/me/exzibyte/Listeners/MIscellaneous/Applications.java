package me.exzibyte.Listeners.MIscellaneous;

import com.mongodb.client.MongoCollection;
import com.mongodb.util.JSON;
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

import static com.mongodb.client.model.Filters.eq;

public class Applications extends ListenerAdapter {

    private final Singularity singularity;
    Logging logging = new Logging();
    Colors colors = new Colors();

    public Applications(Singularity singularity) {
        this.singularity = singularity;
    }

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("applications")) return;
        EmbedBuilder eb = new EmbedBuilder();
        if (event.isFromGuild()) {
            if (event.getMember().getIdLong() == 290660792632606734L || event.getMember().getIdLong() == 79693184417931264L) {
                if (event.getOptions().isEmpty()) {
                    StringBuilder builder = new StringBuilder();
                    for (Document doc : singularity.getDatabase().getCollection("applications").find().limit(20)) {
                        builder.append(String.format("\n[%o] Application for %s", doc.getInteger("applicationID"), doc.getString("minecraftName")));
                    }
                    eb.setDescription(builder.toString());
                    eb.setFooter("List of Applications for Oxryn | Oxryn");

                    event.replyEmbeds(eb.build()).queue();
                }
                if (event.getOptions().size() == 1) {
                    if (event.getOptions().get(0).getName().equals("application-number")){
                        JSONParser parser = new JSONParser();
                        MongoCollection<Document> applications = singularity.getDatabase().getCollection("applications");
                        try{
                            var applicationNumber = event.getOption("application-number").getAsLong();
                            var document = applications.find(eq("applicationID", applicationNumber)).first();

                            if (document == null) {
                                event.reply("Application number invalid.").setEphemeral(true).queue();
                                return;
                            }

                            var minecraftName = document.getString("minecraftName");

                            HttpRequest request = HttpRequest.newBuilder()
                                    .uri(new URI(String.format("https://api.wynncraft.com/v2/player/%s/stats", minecraftName))).GET().build();
                            HttpClient client = HttpClient.newHttpClient();
                            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                            JSONObject fullJSON = (JSONObject) parser.parse(response.body());
                            JSONArray dataArray = (JSONArray) parser.parse(fullJSON.get("data").toString());
                            JSONObject playerObj = (JSONObject) dataArray.get(0);
                            JSONObject metaObj = (JSONObject) parser.parse(playerObj.get("meta").toString());
                            JSONObject tagObj = (JSONObject) parser.parse(metaObj.get("tag").toString());
                            JSONObject globalObj = (JSONObject) parser.parse(playerObj.get("global").toString());
                            JSONObject totalLevelObj = (JSONObject) parser.parse(globalObj.get("totalLevel").toString());
                            JSONArray classArray = (JSONArray) parser.parse(playerObj.get("classes").toString());
                            JSONObject highestClassObj = (JSONObject) parser.parse(classArray.get(0).toString());

                            String discordName = event.getGuild().getMemberById(document.getString("memberID")).getEffectiveName();
                            eb.setTitle(String.format("Application for %s | %s", minecraftName, discordName), String.format("https://wynncraft.com/stats/player/%s", minecraftName));
                            eb.addField("Total Level", totalLevelObj.get("combat").toString(), true);
                            eb.addField("Highest Level | Class", String.format("%s | %s", highestClassObj.get("level"), highestClassObj.get("name").toString()), true);
                            eb.addField("Rank", tagObj.get("value").toString(), true);
                            switch(tagObj.get("value").toString().toLowerCase()){
                                case "vip":
                                    eb.setColor(colors.vip);
                                    break;
                                case "vip+":
                                    eb.setColor(colors.vipplus);
                                    break;
                                case "hero":
                                    eb.setColor(colors.hero);
                                    break;
                                case "champion":
                                    eb.setColor(colors.champion);
                                    break;
                                default:
                                    eb.setColor(colors.member);
                                    break;
                            }
                            eb.setFooter("Oxryn Guild Application | Oxryn | Data from Wynncraft API");

                            event.replyEmbeds(eb.build()).queue();
                        } catch (Exception e){
                            logging.error(this.getClass(), e.toString());
                            e.printStackTrace();
                        }
                    }
                    if (event.getOptions().get(0).getName().equals("delete")){
                        MongoCollection<Document> applications = singularity.getDatabase().getCollection("applications");
                        applications.findOneAndDelete(eq("applicationID", event.getOption("delete").getAsInt()));
                        event.reply(String.format("Succesfully removed application %d from the database", event.getOption("delete").getAsInt())).queue();
                    }
                }
            }
        }
    }
}

