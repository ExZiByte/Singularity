package me.exzibyte.Listeners.MIscellaneous;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertOneOptions;
import me.exzibyte.Singularity;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class Apply extends ListenerAdapter {

    private final Singularity singularity;
    private Integer documentCount;
    public Apply(Singularity singularity) {
        this.singularity = singularity;
    }

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        this.documentCount = Integer.parseInt(String.valueOf(singularity.getDatabase().getCollection("applications").countDocuments()));
        if (!event.getName().equals("apply")) return;

        if (singularity.getDatabase().getCollection("applications").find(eq("memberID", event.getMember().getUser().getId())).first() != null) {
            event.reply("You have already applied. Please wait till someone reviews it. If you messed up on your application you can contact ExZiByte#9472 for help").queue();
        } else {
            TextInput ign = TextInput.create("ign", "What is your in-game name?", TextInputStyle.SHORT).build();
            TextInput guildFound = TextInput.create("guildFound", "How did you find Oxryn?", TextInputStyle.SHORT).build();
            TextInput availability = TextInput.create("availability", "How often and how long do you play?", TextInputStyle.SHORT).build();
            TextInput age = TextInput.create("age", "How old are you? Put 0 if not comfortable.", TextInputStyle.SHORT).build();
            TextInput interests = TextInput.create("interests", "What are you interests in game with Oxryn?", TextInputStyle.SHORT).build();

            Modal modal = Modal.create("application", "Application for Oxryn")
                    .addActionRow(ign)
                    .addActionRow(guildFound)
                    .addActionRow(availability)
                    .addActionRow(age)
                    .addActionRow(interests).build();
            event.replyModal(modal).queue();
        }
    }

    public void onModalInteraction(ModalInteractionEvent event) {
        if (!event.getModalId().equals("application")) return;

        MongoCollection<Document> applications = singularity.getDatabase().getCollection("applications");

        Document application = new Document("applicationID", documentCount++)
                .append("memberID", event.getMember().getUser().getId())
                .append("memberName", event.getMember().getUser().getAsTag())
                .append("minecraftName", event.getValue("ign").getAsString())
                .append("guildFound", event.getValue("guildFound").getAsString())
                .append("availability", event.getValue("availability").getAsString())
                .append("age", event.getValue("age").getAsString())
                .append("interests", event.getValue("interests").getAsString());

        applications.insertOne(application, new InsertOneOptions().bypassDocumentValidation(false));

        event.reply("Thank you for applying! Someone will review your application shortly and get back to you with a decision").queue();

    }

}
