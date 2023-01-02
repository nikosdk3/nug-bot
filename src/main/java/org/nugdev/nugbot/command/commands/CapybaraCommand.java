package org.nugdev.nugbot.command.commands;

import kong.unirest.Unirest;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.nugdev.nugbot.command.Command;

public class CapybaraCommand extends Command {
    public CapybaraCommand() {
        super("capybara", "pulls up");
    }

    @Override
    public SlashCommandData build(SlashCommandData data) {
        return data;
    }

    @Override
    public void run(SlashCommandInteractionEvent event)
    {
        Unirest.get("https://api.capy.lol/v1/capybara?json=true").asJsonAsync(response->{
            event.reply(response.getBody().getObject().getJSONObject("data").getString("url")).queue();
        });
    }
}
