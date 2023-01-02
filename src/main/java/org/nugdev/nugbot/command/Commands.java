package org.nugdev.nugbot.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.nugdev.nugbot.NugBot;
import org.nugdev.nugbot.command.commands.CapybaraCommand;
import org.nugdev.nugbot.command.commands.CatCommand;
import org.nugdev.nugbot.command.commands.DogCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Commands extends ListenerAdapter {
    private static final Map<String, Command> commands = new HashMap<>();

    public static void add(Command command) {
        commands.put(command.name, command);
    }

    @Override
    public void onReady(ReadyEvent event) {
        add(new CapybaraCommand());
        add(new CatCommand());
        add(new DogCommand());

        List<CommandData> commandData = new ArrayList<>();

        for (Command command : commands.values()) {
            commandData.add(command.build(new CommandDataImpl(command.name, command.desc)));
        }

        NugBot.BOT.updateCommands().addCommands(commandData).complete();

        NugBot.LOG.info("Loaded {} commands", commands.size());
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Command command = commands.get(event.getName());
        if(command == null) return;

        command.run(event);
    }
}
