package org.nugdev.nugbot.command.commands;

import kong.unirest.Unirest;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.nugdev.nugbot.command.Command;

public class CatCommand extends Command {
    public CatCommand(){
        super("cat","el gato");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data;
    }

    @Override
    public void run(SlashCommandInteractionEvent event) {
        Unirest.get("https://aws.random.cat/meow").asJsonAsync(response -> {
           event.reply(response.getBody().getObject().getString("file")).queue();
        });
    }
}
