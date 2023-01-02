package org.nugdev.nugbot.command.commands;

import kong.unirest.Unirest;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.nugdev.nugbot.command.Command;

public class DogCommand extends Command{
    public DogCommand(){
        super("dog", "dawg");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data;
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        MessageChannel channel = event.getChannel();
        Unirest.get("https://some-random-api.ml/animal/dog").asJsonAsync(response -> {
            event.reply(response.getBody().getObject().getString("image")).queue();
            channel.sendMessage(response.getBody().getObject().getString("fact")).queue();
        });
    }
}
