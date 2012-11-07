package com.pixelvent.bukkit.shout;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventListener implements Listener
{
	public EventListener()
	{
		Shout.instance.getServer().getPluginManager().registerEvents(this, Shout.instance);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
	}
}