package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumskyblock.gui.BlockValueSelectGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * Command which shows all the valuable blocks and spawners.
 *
 * @see com.iridium.iridiumskyblock.configs.BlockValues
 */
public class BlockValueCommand extends Command {

    /**
     * The default constructor.
     */
    public BlockValueCommand() {
        super(Collections.singletonList("blockvalues"), "Show the values of blocks", "", true);

    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     * Shows all the valuable blocks and spawners.
     *
     * @param sender    The CommandSender which executes this command
     * @param arguments The arguments used with this command. They contain the sub-command
     */
    @Override
    public void execute(CommandSender sender, String[] arguments) {
        Player player = (Player) sender;
        player.openInventory(new BlockValueSelectGUI().getInventory());
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
        // We currently don't want to tab-completion here
        // Return a new List so it isn't a list of online players
        return Collections.emptyList();
    }

}
