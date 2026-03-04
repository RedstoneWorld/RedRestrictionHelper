package de.redstoneworld.redrestrictionhelper.enums;

public enum RestrictionPlugins {
    
    RED_RESTRICTION_HELPER("RedRestrictionHelper", 1), 
    WORLD_GUARD("WorldGuard", 7),
    PLOT_SQUARED("PlotSquared", 7);
    
    private final String restrictionPluginName;
    private final int supportedVersion;
    
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