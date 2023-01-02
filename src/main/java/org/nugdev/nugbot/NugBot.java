package org.nugdev.nugbot;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.nugdev.nugbot.command.Commands;
import org.slf4j.Logger;

public class NugBot extends ListenerAdapter {
    public static final Logger LOG = JDALogger.getLog("Nug Bot");
    private static final String[] HELLOS = {"hi", "hello", "hola", "bonjour", "ciao", "hey", "yo", "sup"};
    public static String DISCORD_TOKEN; //private env
    public static String POOP_STASH_ID; //public env
    public static JDA BOT;

    static {
        Dotenv privateEnv = Dotenv.load();
        DISCORD_TOKEN = privateEnv.get("DISCORD_TOKEN");

        Dotenv publicEnv = Dotenv.configure().filename("public.env").load();
        POOP_STASH_ID = publicEnv.get("POOP_STASH_ID");
    }

    public static void main(String[] args) {
        if (DISCORD_TOKEN == null) {
            NugBot.LOG.error("Must specify discord bot token.");
            System.exit(0);
        }
        JDABuilder.createDefault(DISCORD_TOKEN)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .addEventListeners(new NugBot(), new Commands())
                .build();
    }

    @Override
    public void onReady(ReadyEvent event) {
        BOT = event.getJDA();
        BOT.getPresence().setActivity(Activity.playing(" with you"));
        BOT.getPresence().setStatus(OnlineStatus.ONLINE);
        LOG.info("Nug Bot started");
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.isFromType(ChannelType.TEXT) || !event.isFromGuild()) return;
        if (event.getAuthor().isBot()) return;

        String content = event.getMessage().getContentRaw();

        if (!content.contains(BOT.getSelfUser().getAsMention())) return;

        boolean found = false;
        for (String hello : HELLOS) {
            if (content.toLowerCase().contains(hello)) {
                found = true;
                event.getMessage().reply(hello + " :)").queue();
            }
        }
        if(!found){
            event.getMessage().addReaction(Emoji.fromUnicode("U+1F44B")).queue();
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        final String message = String.format("Welcome %s to %s",
                event.getMember().getUser().getAsMention(),
                event.getGuild().getName());

        final TextChannel channel = event.getGuild().getTextChannelById(POOP_STASH_ID);
        channel.sendMessage(message).queue();
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        final String message = String.format("Goodbye %s",
                event.getMember().getUser().getAsTag());

        final TextChannel channel = event.getGuild().getTextChannelById(POOP_STASH_ID);
        channel.sendMessage(message).queue();
    }
}