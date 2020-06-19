package me.dineshu.actionbarcoords;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

// Class: PlayerColor
// Purpose: Custom DataObject that contains the playerColorData for each player
//          Saves and Loads each player's playerColorData (Handled through YML flat-file based data)
public class PlayerColor implements Serializable
{
    private ChatColor pColor;
    private Boolean pBold;
    private Boolean pItalic;

    public PlayerColor()
    {
        setColor(ChatColor.GREEN);
        setBold(true);
        setItalic(false);
    }

    public void setColor(ChatColor color)
    {
        this.pColor = color;
    }

    public void setBold(boolean bold)
    {
        this.pBold = bold;
    }

    public void setItalic(boolean italic)
    {
        this.pItalic = italic;
    }

    public ChatColor getColor()
    {
        return pColor;
    }

    public String transformColorToStr(ChatColor color)
    {
        return Main.chatColorConvertMap.get(color);
    }

    public ChatColor transformStrToColor(String color)
    {
        for (Map.Entry<ChatColor, String> colorMapRecord : Main.chatColorConvertMap.entrySet())
        {
            if (color.equals(colorMapRecord.getValue()))
            {
                return colorMapRecord.getKey();
            }
        }

        // Return Default Color if conversion failed
        return new PlayerColor().getColor();
    }

    public boolean getBold()
    {
        return pBold;
    }

    public boolean getItalic()
    {
        return pItalic;
    }

    public boolean saveData(String UUID)
    {
        File playerColorData = new File("./plugins/ActionBarCoords/" + UUID + ".yml");
        YamlConfiguration playerColorYML = YamlConfiguration.loadConfiguration(playerColorData);
        try
        {
            playerColorYML.set("color", transformColorToStr(getColor()));
            playerColorYML.set("bold", getBold());
            playerColorYML.set("italic", getItalic());
            playerColorYML.save(playerColorData);
            return true;
        }
        catch (IOException | SecurityException e)
        {
            return false;
        }
    }

    public PlayerColor loadData(String UUID)
    {
        try
        {
            File playerColorData = new File("./plugins/ActionBarCoords/" + UUID + ".yml");
            YamlConfiguration playerColorYML = YamlConfiguration.loadConfiguration(playerColorData);

            setColor(transformStrToColor(playerColorYML.get("color").toString()));
            setBold(Boolean.parseBoolean(playerColorYML.get("bold").toString()));
            setItalic(Boolean.parseBoolean(playerColorYML.get("italic").toString()));
            return this;
        }
        catch (NullPointerException | SecurityException e)
        {
            return null;
        }
    }
}