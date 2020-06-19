package me.dineshu.actionbarcoords;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.UUID;

// Class: ColorGUI
// Purpose: Initializes the ColorGUI and opens ColorGUI when called
//          Assigns the color preferences clicked by the player to their playerColorData and ActionBarCoords
public class ColorGUI implements Listener
{
    private final Inventory colorInv;

    // Function: ColorGUI()
    // Purpose: Initializes a blank version of the ColorGUI
    public ColorGUI()
    {
        colorInv = Bukkit.createInventory(null, 18, "Actionbar Color");
        initItems(colorInv);
    }

    // Function: initItems(Inv inv)
    // Purpose: Initalizes the color icons within the ColorGUI
    public void initItems(Inventory inv)
    {
        String[] colorStrings = {ChatColor.DARK_RED + "Dark Red", ChatColor.RED + "Red", ChatColor.GOLD + "Gold", ChatColor.YELLOW + "Yellow", ChatColor.DARK_GREEN + "Dark Green", ChatColor.GREEN + "Green", ChatColor.AQUA + "Aqua", ChatColor.DARK_AQUA + "Cyan", ChatColor.ITALIC + "Italic",
                ChatColor.DARK_BLUE + "Dark Blue", ChatColor.BLUE + "Blue", ChatColor.LIGHT_PURPLE + "Pink", ChatColor.DARK_PURPLE + "Magenta", ChatColor.WHITE + "White", ChatColor.GRAY + "Light Gray", ChatColor.DARK_GRAY + "Dark Gray", ChatColor.BLACK + "Black", ChatColor.BOLD + "Bold"};

        Material[] colorItems = {Material.RED_WOOL, Material.RED_STAINED_GLASS, Material.YELLOW_TERRACOTTA, Material.YELLOW_WOOL, Material.GREEN_TERRACOTTA, Material.LIME_WOOL, Material.LIGHT_BLUE_WOOL, Material.CYAN_WOOL, Material.STICK,
                Material.BLUE_WOOL, Material.LIGHT_BLUE_TERRACOTTA, Material.PINK_WOOL, Material.MAGENTA_WOOL, Material.WHITE_WOOL, Material.LIGHT_GRAY_STAINED_GLASS, Material.GRAY_STAINED_GLASS, Material.BLACK_WOOL, Material.REDSTONE_BLOCK};

        for (int item = 0; item < colorStrings.length; item++)
        {
            inv.addItem(createGUIItem(colorItems[item], colorStrings[item]));
        }
    }

    // Function: createGUIItem(Mat mat, Str str)
    // Purpose: creates each icon and it's label within the ColorGUI
    protected ItemStack createGUIItem(final Material material, final String name)
    {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);

        item.setItemMeta(meta);

        return item;
    }

    // Opens the ColorGUI when the player enters: ./coords colors
    public void openInventory(final HumanEntity ent)
    {
        ent.openInventory(colorInv);
    }

    // Handles whenever a player clicks on a color
    // and sets the cached playerColorData appropriate to user input
    // and changes ActionBarCoords color
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e)
    {
        if (e.getInventory() != colorInv) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        final Player p = (Player) e.getWhoClicked();

        UUID pID = e.getWhoClicked().getUniqueId();

        String colorItemName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
        Main.playerColorHashMap.replace(pID, colorClicked(pID, e.getRawSlot()));

        p.spigot().sendMessage(ChatMessageType.CHAT, new ComponentBuilder("Your color is now: ")
                .append(colorItemName)
                    .color(Main.playerColorHashMap.get(pID).getColor())
                    .bold(Main.playerColorHashMap.get(pID).getBold())
                    .italic(Main.playerColorHashMap.get(pID).getItalic())
                .create());
    }

    // Stops players from dragging the icons around in the ColorGUI
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e)
    {
        if (e.getInventory() == colorInv)
        {
            e.setCancelled(true);
        }
    }

    // Function: colorClicked
    // Purpose: Retrieves the playerColorData from the colorIcon that the player clicked upon
    public PlayerColor colorClicked(UUID pID, int slotNum)
    {
        PlayerColor playerColor = Main.playerColorHashMap.get(pID);

        switch (slotNum)
        {
            case 8:
                if (playerColor.getItalic())
                    playerColor.setItalic(false);
                else
                    playerColor.setItalic(true);
                break;
            case 17:
                if (playerColor.getBold())
                    playerColor.setBold(false);
                else
                    playerColor.setBold(true);
                break;
            default:
                    playerColor.setColor(Main.chatColors[slotNum]);
        }
        return playerColor;
    }
}