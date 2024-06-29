package com.thoxia.odin.skyblock.command;

import com.thoxia.odin.skyblock.SkyBlockPlugin;
import com.thoxia.odin.skyblock.api.player.SPlayer;
import com.thoxia.odin.skyblock.api.util.ChatUtils;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.Command;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Command(value = "island", alias = "is")
public class AcceptCommand extends BaseCommand {

    private final SkyBlockPlugin plugin;

    @SubCommand("accept")
    public void acceptCommand(Player player) {
        SPlayer sPlayer = plugin.getPlayerManager().getPlayer(player);
        if (sPlayer == null) {
            player.sendMessage(ChatUtils.format("<red>Your data is still loading, please try again."));
            return;
        }

        plugin.getInviteManager().getInvite(player.getUniqueId()).whenComplete((islandId, ex) -> {
            if (ex != null) {
                plugin.log("An exception was found on redis!", ex);
                return;
            }

            if (islandId == null) {
                player.sendMessage(ChatUtils.format("<red>You do not have an invite. -_-"));
                return;
            }

            player.sendMessage(ChatUtils.format("<green>Invite accepted. Teleporting..."));
            plugin.getInviteManager().acceptInvite(sPlayer, islandId);
        });
    }

}
