package com.pixelvent.bukkit.shout;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;
// Import required packages
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Shout extends JavaPlugin
{
	public static Shout instance;
	
	public Logger log;
	public PluginDescriptionFile desc;
	public FileConfiguration config;
	public FileConfiguration publicConfig;
	public EventListener eventListener;
	
	public ArrayList<String> announcementsList = new ArrayList<String>();
	
	// On plugin enable
	public void onEnable()
	{
		Shout.instance = this;
		
		log = getLogger();
		desc = getDescription();
		
		// Get config.yml resource
		config = YamlConfiguration.loadConfiguration(this.getResource("config.yml"));
		publicConfig = getConfig();
		
		eventListener = new EventListener();
				
		setupCommands();
		setupScheduledTasks();

		File configFile = new File(getDataFolder(), "config.yml");
		if (!configFile.isDirectory())
		{
			if (!configFile.exists())
			{
				try
				{
					YamlConfiguration.loadConfiguration(getResource("publicconfig.yml")).save(configFile);
				}
				catch (Exception e)
				{
				}
			}
		}

		reloadPublicConfig();
	}
	
	// On plugin disable
	public void onDisable()
	{
		getServer().getScheduler().cancelTasks(this);
	}

	private void setupCommands()
	{
		CustomCommandExecutor cce = new CustomCommandExecutor();
		
		
	}

	private void setupScheduledTasks()
	{
		
	}
	
	// Reload config
	private void reloadPublicConfig()
	{
		reloadConfig();
	    this.publicConfig = getConfig();

	    this.announcementsList.clear();
	    this.announcementsList = ((ArrayList<String>)this.publicConfig.getStringList("messages"));
	}
	
	// Gets the chat prefix as defined in config.yml
	public String getChatPrefix()
	{
		return ChatColor.getByChar(config.getString("settings.announcePrefix")) + "[" + config.getString("settings.announceColor") + "]" + ChatColor.RESET + " ";
	}
}