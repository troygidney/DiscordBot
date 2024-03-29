package DiscordBot.commands;

import DiscordBot.Embeds;
import DiscordBot.main;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class RamCommand extends Command {

    public RamCommand() {
        this.name = "ram";
        this.category = main.roleCategories.get("player");
        this.help = "Displays a tutorial on how to allocate more ram";
    }

    @Override
    protected void execute(CommandEvent event) {
        if (event.getChannel().getName().contains("-chat")) return;
        event.reply(Embeds.ramInfo().build());
    }

}