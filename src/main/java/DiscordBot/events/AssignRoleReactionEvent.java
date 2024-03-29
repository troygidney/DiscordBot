package DiscordBot.events;

import DiscordBot.main;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;


public class AssignRoleReactionEvent implements EventListener {

    @Override
    public void onEvent(@NotNull GenericEvent e) {
        if (e instanceof GuildMessageReactionAddEvent) {
            GuildMessageReactionAddEvent event = (GuildMessageReactionAddEvent) e;
            String messageID = event.getMessageId();

            if (!main.userData.get("reactrolemessages").containsKey(messageID)) return;
//            String emoteName = event.getReactionEmote().getName().replace("poke", "");

            String emoteID = event.getReactionEmote().getId();
            if (!main.emoteRankID.containsKey(emoteID)) return;


            Member member = event.getMember();
            String roleID = main.emoteRankID.get(emoteID);
            Role resolvedRole = event.getGuild().getRoleById(roleID);

            event.getGuild().addRoleToMember(member, resolvedRole).queue();
        }
    }

}
