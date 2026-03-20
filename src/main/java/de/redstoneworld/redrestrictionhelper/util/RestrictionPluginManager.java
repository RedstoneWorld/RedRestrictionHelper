package de.redstoneworld.redrestrictionhelper.util;

import de.redstoneworld.redrestrictionhelper.enums.RestrictionPluginStatus;
import de.redstoneworld.redrestrictionhelper.enums.RestrictionPlugins;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class RestrictionPluginManager {

    private static final Set<RestrictionPlugins> PLUGIN_LIST = Arrays.stream(RestrictionPlugins.values())
            .collect(Collectors.toSet());
    
    private static final Set<String> PLUGIN_NAME_LIST = Arrays.stream(RestrictionPlugins.values())
            .map(RestrictionPlugins::getName)
            .collect(Collectors.toSet());
    
    
    public static Set<RestrictionPlugins> getPluginList() {
        return PLUGIN_LIST;
    }

    public static Set<String> getPluginNameList() {
        return PLUGIN_NAME_LIST;
    }

    public static RestrictionPlugins getPlugin(String pluginName) {
        for (RestrictionPlugins plugin : RestrictionPlugins.values()) {
            if (plugin.getName().equalsIgnoreCase(pluginName)) return plugin;
        }
        return null;
    }

    public static boolean isPluginEnabled(RestrictionPlugins plugin) {
        return isPluginEnabled(plugin.getName());
    }

    public static boolean isPluginEnabled(String pluginName) {
        if (Bukkit.getPluginManager().isPluginEnabled(pluginName)) return true;
        return false;
    }

    public static boolean isPluginInstalled(RestrictionPlugins plugin) {
        return isPluginInstalled(plugin.getName());
    }

    public static boolean isPluginInstalled(String pluginName) {
        if (Bukkit.getPluginManager().getPlugin(pluginName) != null) return true;
        return false;
    }

    public static RestrictionPluginStatus getState(RestrictionPlugins plugin) {
        if (isPluginInstalled(plugin)) {
            if (isPluginEnabled(plugin)) {
                return RestrictionPluginStatus.ENABLED;
            }
            return RestrictionPluginStatus.NOT_ENABLED;
        }
        return RestrictionPluginStatus.NOT_INSTALLED;
    }

    public static boolean serverHasRestrictionPlugins() {
        
        boolean isInstalled = false;
        
        for (RestrictionPlugins plugin : RestrictionPlugins.values()) {
            if (plugin == RestrictionPlugins.RED_RESTRICTION_HELPER) continue;
            
            if (isPluginEnabled(plugin)) {
                isInstalled = true;
                break;
            }
        }
        
        return isInstalled;
    }

}
