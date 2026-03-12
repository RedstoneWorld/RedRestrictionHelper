package de.redstoneworld.redrestrictionhelper.analyze;

import de.redstoneworld.redrestrictionhelper.RestrictionCheck;
import org.bukkit.plugin.Plugin;

public abstract class RestrictionPluginCheck {
    
    private final Plugin plugin;
    
    
    public RestrictionPluginCheck(Plugin plugin) {
        this.plugin = plugin;
    }
    
    public Result runCheck(RestrictionCheck check) {
        return null;
    }
    
}
