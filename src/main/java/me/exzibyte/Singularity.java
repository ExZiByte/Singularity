package me.exzibyte;

import me.exzibyte.Listeners.Information.Lootrun;
import me.exzibyte.Listeners.MIscellaneous.*;
import me.exzibyte.Listeners.Moderation.*;
import me.exzibyte.Listeners.Test.Test;
import me.exzibyte.Utilities.Config;
import me.exzibyte.Utilities.Database;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

public class Singularity {

    private final JDABuilder singularity;
    private final Config config;
    private final Database database;


    private Singularity() throws LoginException{
        this.config = new Config(this);
        config.load();
        this.database = new Database(this);
        database.connect();
        this.singularity = JDABuilder.createDefault(getConfig().get("token"));

        singularity.setActivity(Activity.competing("in the war against all other guilds."));
        singularity.setStatus(OnlineStatus.DO_NOT_DISTURB);
        singularity.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS);
        singularity.setMemberCachePolicy(MemberCachePolicy.ALL);
        singularity.setChunkingFilter(ChunkingFilter.ALL);

        singularity.addEventListeners(
                new Ready(),
                new Harvest(this),
                new Apply(this),
                new Applications(this),
                new Link(this),

                new Ban(),
                new Clear(this),
                new Kick(),
                new Mute(),
                new Undo(this),

                new Test(),
                new Lootrun()
        );

        singularity.build();
    }

    public static void main(String[] args) throws LoginException {
        new Singularity();
    }

    public Config getConfig() {
        return config;
    }

    public Database getDatabase(){
        return database;
    }

}
