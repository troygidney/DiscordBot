package DiscordBot.commands.moderation;

import DiscordBot.TimeUtils;
import DiscordBot.main;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

// TODO Clean up class
public class MuteCommand extends Command {
    int counter = 0;

    public MuteCommand() {
        this.name = "mute";
        this.aliases = new String[] { "m", "tm", "tempmute" };
        this.guildOnly = true;
        this.help = "Mute a specified user";
        this.category = main.roleCategories.get("staff");
        this.requiredRole = main.requiredRoles.get("staff");
        this.hidden = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] args = event.getArgs().split(" ");
        if (event.getArgs().isEmpty()){
            event.replyError("You must supply a user, a time and a reason!");
            return;
        }
        if (args.length == 1) {
            event.replyError("You must supply a time and a reason!");
            return;
        }
        if (args.length == 2) {
            event.replyError("You must supply a reason!");
        }

        try{event.getGuild().getMemberById(args[0].replaceAll("<@", "").replaceAll(">", "").replaceAll("!", ""));} catch (NumberFormatException e) {
            event.replyError("Invalid User!");
            return;
        }

        Member member = event.getGuild().getMemberById(args[0].replaceAll("<@", "").replaceAll(">", "").replaceAll("!", ""));
        if (member.hasPermission(Permission.KICK_MEMBERS)) {
            event.replyError("You cannot mute another staff member!");
            return;
        }

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                counter ++;
                if (counter == 2);
                event.getGuild().removeRoleFromMember(member, event.getGuild().getRoleById("707675201181057084")).queue();
            }
        };

        long time1 = TimeUtils.timeAmount(args[1]);
        switch (TimeUtils.timeUnit(args[1])) {
            case DAYS : time1*=24;
            case HOURS : time1*=60;
            case MINUTES : time1*=60;
            case SECONDS : time1*=1000;
        }
        timer.schedule(task, 0, time1);

        Array.set(args, 0, "");
        String reason = Arrays.toString(Arrays.copyOfRange(args, 2, args.length)).replaceAll(",","");
        String time = (args[1]);
        Guild server = event.getGuild();

        event.reply(muteUser(member, time, reason).setFooter("Muted by " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getAvatarUrl()).build());
        event.getGuild().addRoleToMember(member, event.getGuild().getRoleById("707675201181057084")).queue();

        TextChannel staffLogs = server.getTextChannelById("322915404043517952");
        staffLogs
                .sendMessage((muteUser(member, time, reason)
                .setFooter("Muted by " + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getAvatarUrl())
                .build()))
                .queue();
    }

    public EmbedBuilder muteUser(Member user, String time, String reason) {
        EmbedBuilder userMuteMessage = new EmbedBuilder();
        userMuteMessage
                .setTitle("User Muted!")
                .setColor(Color.red)
                .setThumbnail(user.getUser().getAvatarUrl())
                .getDescriptionBuilder()
                .append("Muted user: " + user.getAsMention() + " (" + user.getUser().getId() + ")\nMute time: " + time + "\nReason: " + reason);
        return userMuteMessage;
    }


}
