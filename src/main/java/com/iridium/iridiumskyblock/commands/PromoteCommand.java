package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.IslandRank;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Command which promotes a user in the Island rank system.
 */
public class PromoteCommand extends Command {

    /**
     * The default constructor.
     */
    public PromoteCommand() {
        super(Collections.singletonList("promote"), "Promote a user", "", true);
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Promotes a user in the Island rank system.
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = IridiumSkyblockAPI.getInstance().getUser(player);
        Optional<Island> island = user.getIsland();

        if (island.isPresent()) {
            OfflinePlayer targetPlayer = Bukkit.getServer().getOfflinePlayer(args[1]);
            User targetUser = IridiumSkyblockAPI.getInstance().getUser(targetPlayer);

            if (island.get().equals(targetUser.getIsland().orElse(null))) {
                IslandRank nextRank = IslandRank.getByLevel(targetUser.getIslandRank().getLevel() + 1);
                if (nextRank != null && nextRank.getLevel() < user.getIslandRank().getLevel() && IridiumSkyblock.getInstance().getIslandManager().getIslandPermission(island.get(), IridiumSkyblockAPI.getInstance().getUser(player), IridiumSkyblock.getInstance().getPermissions().promote)) {
                    targetUser.setIslandRank(nextRank);
                    for (User member : island.get().getMembers()) {
                        Player p = Bukkit.getPlayer(member.getUuid());
                        if (p != null) {
                            if (p.equals(player)) {
                                p.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().promotedPlayer.replace("%player%", targetUser.getName()).replace("%rank%", nextRank.name()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                            } else {
                                p.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userPromotedPlayer.replace("%promoter%", player.getName()).replace("%player%", targetUser.getName()).replace("%rank%", nextRank.name()).replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                            }
                        }
                    }
                } else {
                    player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().cannotPromoteUser.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                }
            } else {
                player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().userNotInYourIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            }
        } else {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().dontHaveIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
        }
    }

    /**
     * Handles tab-completion for this command.
     *
     * @param commandSender The CommandSender which tries to tab-complete
     * @param command       The command
     * @param label         The label of the command
     * @param args          The arguments already provided by the sender
     * @return The list of tab completions for this command
     */
    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        return null;
    }

}
