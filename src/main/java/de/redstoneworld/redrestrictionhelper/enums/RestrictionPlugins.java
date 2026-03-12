package de.redstoneworld.redrestrictionhelper.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum RestrictionPlugins {
    
    RED_RESTRICTION_HELPER("RedRestrictionHelper", 1), 
    WORLD_GUARD("WorldGuard", 7),
    PLOT_SQUARED("PlotSquared", 7);
    
    private final String restrictionPluginName;
    private final int supportedVersion;
    
    public static final Set<String> RESTRICTION_PLUGIN_NAMES = Arrays.stream(RestrictionPlugins.values())
            .map(RestrictionPlugins::getName)
            .collect(Collectors.toSet());
    
    RestrictionPlugins(String pluginName, int supportedVersion) {
        this.restrictionPluginName = pluginName;
        this.supportedVersion = supportedVersion;
    }
    
    public String getName() {
        return restrictionPluginName;
    }

    public int getVersion() {
        return supportedVersion;
    }
    
}