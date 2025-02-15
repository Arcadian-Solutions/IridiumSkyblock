package com.iridium.iridiumskyblock.gui;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.PlaceholderBuilder;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.InventoryUtils;
import com.iridium.iridiumskyblock.utils.ItemStackUtils;
import com.iridium.iridiumskyblock.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * GUI which displays all members of an Island and allows quick rank management.
 */
public class MembersGUI implements GUI {

    private final Island island;
    private final HashMap<Integer, User> members;

    /**
     * The default constructor.
     *
     * @param island The Island this GUI belongs to
     */
    public MembersGUI(@NotNull Island island) {
        this.island = island;
        this.members = new HashMap<>();
    }

    /**
     * Builds and returns this inventory.
     *
     * @return The new inventory
     */
    @NotNull
    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, 27, StringUtils.color("&7Island Members"));

        InventoryUtils.fillInventory(inventory);

        int i = 0;
        for (User member : island.getMembers()) {
            inventory.setItem(i, ItemStackUtils.makeItem(IridiumSkyblock.getInstance().getInventories().islandMember, new PlaceholderBuilder().applyPlayerPlaceholders(member).build()));
            members.put(i, member);
            i++;
        }

        return inventory;
    }

    /**
     * Called when there is a click in this GUI.
     * Cancelled automatically.
     *
     * @param event The InventoryClickEvent provided by Bukkit
     */
    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        if (members.containsKey(event.getSlot())) {
            User user = members.get(event.getSlot());
            if (event.getClick().equals(ClickType.LEFT)) {
                Bukkit.getServer().dispatchCommand(event.getWhoClicked(), "is demote " + user.getName());
            } else if (event.getClick().equals(ClickType.RIGHT)) {
                Bukkit.getServer().dispatchCommand(event.getWhoClicked(), "is promote " + user.getName());
            }
            event.getWhoClicked().openInventory(getInventory());
        }
    }

}
