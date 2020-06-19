package me.dineshu.actionbarcoords;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import java.util.UUID;
import static org.bukkit.Bukkit.*;

// Class: Coords
// Purpose: Contains the listener to update player ActionBar with current location
//          Contains the command to grab player's current location
//          Handles the command input from player & console
//          Creates ActionBar Profiles for new players

public class Coords implements CommandExecutor, Listener
{
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args)
    {
        if (label.equalsIgnoreCase("coords"))
        {
            // If Console Types Command (Regardless of parameters)
            if (!(commandSender instanceof Player))
            {
                getServer().getConsoleSender().sendMessage(Main.prefix + ChatColor.RED + "Console cannot use this command.");
                return true;
            }
            // If Player Types Command
            else
            {
                Player sender = (Player) commandSender;
                UUID senderID = sender.getUniqueId();

                // When player only types: /coords
                // or one parameter
                if (args.length == 0)
                {
                    TextComponent playerStringLoc = new TextComponent(getPlayerLocation(sender));
                    playerStringLoc.setColor(Main.playerColorHashMap.get(senderID).getColor());
                    playerStringLoc.setBold(Main.playerColorHashMap.get(senderID).getBold());
                    playerStringLoc.setItalic(Main.playerColorHashMap.get(senderID).getItalic());
                    sender.spigot().sendMessage(playerStringLoc);
                    return true;
                }

                // When player types the cmd with multiple parameters
                else if (args.length >= 1)
                {
                    // Check the parameters following: /coords {param1}...
                    if (args[0].equalsIgnoreCase("colors"))
                    {
                        TextComponent changeColorStmt = new TextComponent("Changing Colors!");
                        changeColorStmt.setColor(Main.playerColorHashMap.get(senderID).getColor());
                        changeColorStmt.setBold(Main.playerColorHashMap.get(senderID).getBold());
                        changeColorStmt.setItalic(Main.playerColorHashMap.get(senderID).getItalic());
                        sender.spigot().sendMessage(changeColorStmt);
                        Main.actionBarGUI.openInventory(sender);
                        return true;
                    }
                    // If false / unrecognizable param then return error
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "Invalid Subcommand!");
                        return false;
                    }
                }
            }
        }
        return false;
    }

    // Function: onMove
    // Desc: Listens for player movement and updates their actionbar with current location
    @EventHandler
    public void onMove(final PlayerMoveEvent e)
    {
        Player p = e.getPlayer();
        UUID playerUUID = e.getPlayer().getUniqueId();
        String playerStringLoc = getPlayerLocation(e.getPlayer());

        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(playerStringLoc)
                .color(Main.playerColorHashMap.get(playerUUID).getColor())
                .bold(Main.playerColorHashMap.get(playerUUID).getBold())
                .italic(Main.playerColorHashMap.get(playerUUID).getItalic())
                .create());
    }

    // Function: onPlayerJoin
    // Desc: Listens for new players and creates an ActionBarCoords Profile
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e)
    {
        if (Main.playerColorHashMap.containsKey(e.getPlayer().getUniqueId()))
            return;
        else
        {
            UUID pID = e.getPlayer().getUniqueId();
            Main.playerColorHashMap.put(pID, new PlayerColor());
        }
    }

    // Function: getPlayerLocation
    // Desc: Returns a template-based string of a player's location
    // Used: In ActionBar & Chat
    public String getPlayerLocation (Player player)
    {
        Location playerLoc = player.getLocation();
        return "X: " + (int) playerLoc.getX() + " Y: " + (int) playerLoc.getY() + " Z: " + (int)playerLoc.getZ();
    }
}