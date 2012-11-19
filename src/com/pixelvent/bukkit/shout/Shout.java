package com.pixelvent.bukkit.shout;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

// Import required packages
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.pixelvent.bukkit.shout.commands.ShoutCommandExecutor;

public class Shout extends JavaPlugin
{
	public static Shout instance;
	
	public Logger log;
	public PluginDescriptionFile desc;
	public FileConfiguration config;
	public FileConfiguration publicConfig;
	public EventListener eventListener;
	
	public int announcementScheduledTaskID;
	
	public ArrayList<String> announcementsList = new ArrayList<String>();
	
	public int currentAnnouncementIndex = 0;
	
	// On plugin enable
	public void onEnable()
	{
		Shout.instance = this;
		
		log = getLogger();
		desc = getDescription();
		
		// Get config.yml resource
		config = YamlConfiguration.loadConfiguration(this.getResource("config.yml"));
		publicConfig = getConfig();
		
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
		
		eventListener = new EventListener();
				
		setupCommands();
		setupScheduledTasks();
	}
	
	// On plugin disable
	public void onDisable()
	{
		getServer().getScheduler().cancelTasks(this);
	}

	private void setupCommands()
	{
		getCommand("shout").setExecutor(new ShoutCommandExecutor());
	}

	private void setupScheduledTasks()
	{
		
	}
	
	private void setupAnnouncementScheduledTask()
	{
		announcementScheduledTaskID = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				if(publicConfig.getBoolean("settings.random"))
				{
					getServer().broadcastMessage(getChatPrefix() + ChatColor.getByChar(publicConfig.getString("settings.announceColor")) + ChatColor.translateAlternateColorCodes('&', announcementsList.get(new Random().nextInt(announcementsList.size()))));
				}
				else
				{
					getServer().broadcastMessage(getChatPrefix() + ChatColor.getByChar(publicConfig.getString("settings.announceColor")) + ChatColor.translateAlternateColorCodes('&', announcementsList.get(currentAnnouncementIndex)));
					
					currentAnnouncementIndex++;
					
					if(currentAnnouncementIndex >= announcementsList.size())
					{
						currentAnnouncementIndex = 0;
					}
				}
			}
		}, publicConfig.getInt("settings.announceInterval") * 20, publicConfig.getInt("settings.announceInterval") * 20);
	}
	
	// Reload config
	public void reloadPublicConfig()
	{
		reloadConfig();
	    this.publicConfig = getConfig();
	    
	    if(!publicConfig.getBoolean("settings.enabled"))
	    {
	    	log.warning("Plugin is disabled in config!");
	    	getServer().getPluginManager().disablePlugin(this);
	    }
	    
	    currentAnnouncementIndex = 0;

	    announcementsList.clear();
	    announcementsList = ((ArrayList<String>)publicConfig.getStringList("messages"));
	    
	    getServer().getScheduler().cancelTask(announcementScheduledTaskID);
	    setupAnnouncementScheduledTask();
	}
	
	// Gets the chat prefix as defined in config.yml
	public String getChatPrefix()
	{
		return ChatColor.getByChar(publicConfig.getString("settings.announceColor")) + "[" + publicConfig.getString("settings.announcePrefix") + "]" + ChatColor.RESET + " ";
	}
}