package de.redstoneworld.redrestrictionhelper.analyze;

import de.redstoneworld.redrestrictionhelper.enums.RestrictionPlugins;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class TestEventExecuter {
    
    private static final boolean IS_FOLIA = isFoliaServer();
    
    private final Plugin bukkitPlugin;
    private final Location location;
    private final Event testEvent;
    private final boolean pluginFilter;
    
    
    public TestEventExecuter(Plugin bukkitPlugin, Location location, Event testEvent, boolean pluginFilter) {
        this.bukkitPlugin = bukkitPlugin;
        this.location = location;
        this.testEvent = testEvent;
        this.pluginFilter = pluginFilter;
        
        callEvent();
    }
    
    public void callEvent() {

        if (IS_FOLIA) {
            
            Bukkit.getRegionScheduler().execute(bukkitPlugin, location, this::callBukkitEvent);
            return;
        }
        
        callBukkitEvent();
    }
    
    private void callBukkitEvent() {
        
        if (pluginFilter) {
    
            Arrays.stream(testEvent.getHandlers().getRegisteredListeners())
                    .filter(listener -> RestrictionPlugins.RESTRICTION_PLUGIN_NAMES.contains(listener.getPlugin().getName()))
                    .filter(listener -> listener.getPlugin().isEnabled())
                    .anyMatch(listener -> {
                        try {
                            listener.callEvent(testEvent);
                        } catch (EventException e) {
                            return true; // "anyMatch" stopps by "true"
                        }
                        return false;
                    });
        } else {
            
            Bukkit.getPluginManager().callEvent(testEvent);
        }
    }
    
    private static boolean isFoliaServer() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }
    
    public Location getLocation() {
        return location;
    }

    public boolean isPluginFilter() {
        return pluginFilter;
    }

}