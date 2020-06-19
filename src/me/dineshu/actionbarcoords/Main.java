package me.dineshu.actionbarcoords;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main extends JavaPlugin
{
    public static ColorGUI actionBarGUI = new ColorGUI();
    public static String prefix = "[ActionBarCoords] ";

    public static ChatColor[] chatColors = {ChatColor.DARK_RED, ChatColor.RED,ChatColor.GOLD, ChatColor.YELLOW, ChatColor.DARK_GREEN, ChatColor.GREEN, ChatColor.AQUA,
            ChatColor.DARK_AQUA, ChatColor.ITALIC, ChatColor.DARK_BLUE, ChatColor.BLUE, ChatColor.LIGHT_PURPLE,
            ChatColor.DARK_PURPLE, ChatColor.WHITE, ChatColor.GRAY, ChatColor.DARK_GRAY, ChatColor.BLACK, ChatColor.BOLD};

    public static String[] stringColors = {"Dark Red", "Red", "Gold", "Yellow", "Dark Green", "Green", "Aqua", "Cyan", "Italic",
            "Dark Blue", "Blue", "Pink", "Magenta", "White", "Light Gray", "Dark Gray", "Black", "Bold"};

    public static HashMap<UUID, PlayerColor> playerColorHashMap = new HashMap<UUID, PlayerColor>();
    public static Map<ChatColor, String> chatColorConvertMap = new HashMap<ChatColor, String>();

    @Override

    // Function: onEnable()
    // Purpose: creates the main directory and loads up the ChatColor/StrColor Map
    //          Attempts to load pre-existing playerColorData from disk
    //          Registers the colorGUI and Coords (includes cmds, EventListeners)
    public void onEnable()
    {
        createMainDir();
        loadChatColorConvertMap();

        OfflinePlayer[] players = Bukkit.getOfflinePlayers();
        for(OfflinePlayer player : players)
        {
            PlayerColor playerColorData = new PlayerColor();

            try
            {
                playerColorData.loadData(player.getUniqueId().toString());
                playerColorHashMap.put(player.getUniqueId(), playerColorData);
            }
            catch (NullPointerException e)
            {
                playerColorHashMap.put(player.getUniqueId(), new PlayerColor());
                getServer().getConsoleSender().sendMessage(Main.prefix + ChatColor.RED + "Error in loading player color data for: " + player.getUniqueId().toString());
            }
        }

        getServer().getPluginManager().registerEvents(new Coords(), this);
        getServer().getPluginManager().registerEvents(actionBarGUI, this);
        this.getCommand("coords").setExecutor(new Coords());
    }

    @Override

    // Function: onDisable
    // Purpose: Saves the in-memory cached playerColorData onto the disk through YML Files
    public void onDisable()
    {
        for (Map.Entry<UUID, PlayerColor> record : playerColorHashMap.entrySet())
        {
            String UUID = record.getKey().toString();
            PlayerColor playerColorData = record.getValue();

            boolean saveConfirm = playerColorData.saveData(UUID);
            if (!saveConfirm)
                getServer().getConsoleSender().sendMessage(Main.prefix + ChatColor.RED + "Error in saving player color data for: " + UUID);
        }
    }

    // Function: createMainDir
    // Purpose: creates the main directory which stores playerColor YML files & recognizes if the directory exists
    public void createMainDir()
    {
        File ActionBarCoordsMainDir = new File("./plugins/ActionBarCoords");

        // If the ActionBarDirectory doesn't exist then create the directory
        try
        {
            if(!ActionBarCoordsMainDir.exists())
            {
                if (ActionBarCoordsMainDir.mkdir())
                    getServer().getConsoleSender().sendMessage(Main.prefix + ChatColor.GREEN + "Plugin Directory Successfully Created!");
                else
                    getServer().getConsoleSender().sendMessage(Main.prefix + ChatColor.RED + "Plugin Directory Creation Failed!");
            }
            else
                getServer().getConsoleSender().sendMessage(Main.prefix + ChatColor.GREEN + "Plugin Directory Successfully Recognized!");
        }
        catch (SecurityException e)
        {
            getServer().getConsoleSender().sendMessage(Main.prefix + ChatColor.RED + "OS Security prevented plugin from creating main directory.");
        }
    }

    public void loadChatColorConvertMap()
    {
        for (int colorPos = 0; colorPos < chatColors.length; colorPos++)
        {
            chatColorConvertMap.put(chatColors[colorPos], stringColors[colorPos]);
        }
    }

}
