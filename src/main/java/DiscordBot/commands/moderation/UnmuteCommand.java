package DiscordBot.commands.moderation;

import DiscordBot.main;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.lang.reflect.Array;

public class UnmuteCommand extends Command {

    public UnmuteCommand() {
        this.name = "unmute";
        this.aliases = new String[] { "um" };
        this.guildOnly = true;
        this.help = "Unmute a specified user";
        this.category = main.roleCategories.get("staff");
        this.requiredRole = main.requiredRoles.get("staff");
        this.hidden = true;
    }

    @Override
    protected void execute(CommandEvent event) {

        String[] args = event.getArgs().split(" ");
        if (event.getArgs().isEmpty()) {
            event.replyError("You must supply a user and a reason!");
            return;
        }

        try{event.getGuild().getMemberById(args[0].replaceAll("<@", "").replaceAll(">", "").replaceAll("!", ""));} catch (NumberFormatException e) {
            event.replyError("Invalid User!");
            return;
        }

        Member member = event.getGuild().getMemberById(args[0].replaceAll("<@", "").replaceAll(">", "").replaceAll("!", ""));

        Array.set(args, 0, "");
        String reason = String.join(" ", args);
        Guild server = event.getGuild();

        event.reply(muteUser(member, reason).setFooter("Unmuted by " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getAvatarUrl()).build());
        event.getGuild().removeRoleFromMember(member, event.getGuild().getRoleById("707675201181057084")).submit();

        TextChannel staffLogs = server.getTextChannelById("322915404043517952");
        staffLogs
                .sendMessage((muteUser(member, reason)
                .setFooter("Unmuted by " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getAvatarUrl())
                .build()))
                .queue();
    }

    public EmbedBuilder muteUser(Member user, String reason) {
        EmbedBuilder userUnmuteMessage = new EmbedBuilder();
        userUnmuteMessage
                .setTitle("User Unmuted!")
                .setColor(Color.green)
                .setThumbnail(user.getUser().getAvatarUrl())
                .getDescriptionBuilder()
                .append("Unmuted user: " + user.getAsMention() + " (" + user.getUser().getId() + ")");
        return userUnmuteMessage;
    }

}
