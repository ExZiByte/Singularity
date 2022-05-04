package me.exzibyte.Listeners.MIscellaneous;

import com.mongodb.client.MongoCollection;
import me.exzibyte.Singularity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Harvest extends ListenerAdapter {

    private final Singularity singularity;

    public Harvest(Singularity singularity) {
        this.singularity = singularity;
    }


    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (!event.getName().equals("harvest")) return;

        if(event.getMember().getUser().getIdLong() == 79693184417931264L){
            MongoCollection<Document> members = singularity.getDatabase().getCollection("members");
            List<Document> memberDocs = new ArrayList<>();

            for(Member member : event.getGuild().getMembers()){
                if(!member.getUser().isBot()){
                    Document memberDoc = new Document("id", member.getUser().getIdLong())
                            .append("memberName", member.getUser().getAsTag())
                            .append("minecraftName", "");
                    memberDocs.add(memberDoc);
                }
            }
            members.insertMany(memberDocs);
            event.reply("Done").setEphemeral(true).queue();
        }
    }
}
