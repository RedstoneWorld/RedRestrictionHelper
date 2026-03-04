package de.redstoneworld.redrestrictionhelper.enums;

public enum ResultReasons {
    
    RRH_EVENT_INTERACT(RestrictionPlugins.RED_RESTRICTION_HELPER.getName(), "The interact event was not cancelled from the server."),
    RRH_EVENT_PLACE(RestrictionPlugins.RED_RESTRICTION_HELPER.getName(), "The block-place event was not cancelled from the server."),
    RRH_EVENT_BREAK(RestrictionPlugins.RED_RESTRICTION_HELPER.getName(), "The block-break event was not cancelled from the server."),
    
    WG_WORLD_DISABLED(RestrictionPlugins.WORLD_GUARD.getName(), "The 'regions.enable' option is deactivated for the target world."),
    WG_BYPASS_PERMISSION(RestrictionPlugins.WORLD_GUARD.getName(), "The player has bypass-permission for the target world."), 
    WG_FLAG_INTERACT(RestrictionPlugins.WORLD_GUARD.getName(), "Location is inside of an region with activated 'INTERACT' flag or the user is a owner / member of this region."),
    WG_FLAG_BUILD(RestrictionPlugins.WORLD_GUARD.getName(), "Location is inside of an region with activated 'BUILD' flag or the user is a owner / member of this region."),
    WG_FLAG_PLACE(RestrictionPlugins.WORLD_GUARD.getName(), "Location is inside of an region with activated 'BLOCK_PLACE' flag or the user is a owner / member of this region."),
    WG_FLAG_BREAK(RestrictionPlugins.WORLD_GUARD.getName(), "Location is inside of an region with activated 'BLOCK_BREAK' flag or the user is a owner / member of this region."),
    
    PS_OWNER_OF_PLOT(RestrictionPlugins.PLOT_SQUARED.getName(), "Location is inside in this own plot (= plot-owner)."),
    PS_MEMBER_OF_PLOT(RestrictionPlugins.PLOT_SQUARED.getName(), "Location is inside of an plot the player has been 'added' or 'trusted'."),
    
    PS_BYPASS_PERMISSION_INTERACT_OTHER(RestrictionPlugins.PLOT_SQUARED.getName(), "Location is inside of an occupied plot and the player has the bypass-permission for 'INTERACT'."),
    PS_BYPASS_PERMISSION_PLACE_OTHER(RestrictionPlugins.PLOT_SQUARED.getName(), "Location is inside of an occupied plot and the player has the bypass-permission for 'DESTROY'."),
    PS_BYPASS_PERMISSION_BREAK_OTHER(RestrictionPlugins.PLOT_SQUARED.getName(), "Location is inside of an occupied plot and the player has the bypass-permission for 'BUILD'."),
    
    PS_BYPASS_PERMISSION_INTERACT_UNOWNED(RestrictionPlugins.PLOT_SQUARED.getName(), "Location is inside of an unowned plot and the player has the bypass-permission for 'INTERACT'."),
    PS_BYPASS_PERMISSION_PLACE_UNOWNED(RestrictionPlugins.PLOT_SQUARED.getName(), "Location is inside of an unowned plot and the player has the bypass-permission for 'DESTROY'."),
    PS_BYPASS_PERMISSION_BREAK_UNOWNED(RestrictionPlugins.PLOT_SQUARED.getName(), "Location is inside of an unowned plot and the player has the bypass-permission for 'BUILD'."),
    
    PS_BYPASS_PERMISSION_INTERACT_ROAD(RestrictionPlugins.PLOT_SQUARED.getName(), "Location is inside of the plot-road and the player has the bypass-permission for 'INTERACT'."),
    PS_BYPASS_PERMISSION_PLACE_ROAD(RestrictionPlugins.PLOT_SQUARED.getName(), "Location is inside of the plot-road and the player has the bypass-permission for 'DESTROY'."),
    PS_BYPASS_PERMISSION_BREAK_ROAD(RestrictionPlugins.PLOT_SQUARED.getName(), "Location is inside of the plot-road and the player has the bypass-permission for 'BUILD'.");

    private final String restrictionPluginName;
    private final String reasonDescription;
    
    ResultReasons(String pluginName, String description) {
        this.restrictionPluginName = pluginName;
        this.reasonDescription = description;
    }

    public String getRestrictionPluginName() {
        return restrictionPluginName;
    }

    public String getReasonDescription() {
        return reasonDescription;
    }
}