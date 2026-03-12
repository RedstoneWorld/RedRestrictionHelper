package de.redstoneworld.redrestrictionhelper.analyze.restrictionplugins;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.config.WorldConfiguration;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import de.redstoneworld.redrestrictionhelper.RestrictionCheck;
import de.redstoneworld.redrestrictionhelper.analyze.RestrictionPluginCheck;
import de.redstoneworld.redrestrictionhelper.analyze.Result;
import de.redstoneworld.redrestrictionhelper.enums.ResultReasons;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class WorldGuard_V7 extends RestrictionPluginCheck {
    
    public WorldGuard_V7(Plugin plugin) {
        super(plugin);
    }
    
    public Result runCheck(RestrictionCheck check) {
        
        Location wgLocation = BukkitAdapter.adapt(check.getLocation());
        Player player = check.getTargetPlayer();
        LocalPlayer wgPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        boolean passed = false;
        List<ResultReasons> reasons = new ArrayList<>();
        
        
        if (!isRestrictedWorld(wgPlayer.getWorld())) {
            passed = true;
            reasons.add(ResultReasons.WG_WORLD_DISABLED);
            return new Result(passed, reasons);
        } else if (hasWorldBypassPermission(wgPlayer)) {
            passed = true;
            reasons.add(ResultReasons.WG_BYPASS_PERMISSION);
            return new Result(passed, reasons);
        }
        
        // https://worldguard.enginehub.org/en/latest/developer/regions/protection-query/
        final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        final RegionQuery query = container.createQuery();
        
        switch (check.getActionType()) {
            case INTERACT -> {
                if (query.testState(wgLocation, wgPlayer, Flags.INTERACT)) {
                    passed = true;
                    reasons.add(ResultReasons.WG_FLAG_INTERACT);
                }
            }
            case PLACE_AND_BREAK -> {} // high-prio "BUILD" check is below
            case PLACE -> {
                if (query.testState(wgLocation, wgPlayer, Flags.BLOCK_PLACE)) {
                    passed = true;
                    reasons.add(ResultReasons.WG_FLAG_PLACE);
                }
            }
            case BREAK -> {
                if (query.testState(wgLocation, wgPlayer, Flags.BLOCK_BREAK)) {
                    passed = true;
                    reasons.add(ResultReasons.WG_FLAG_BREAK);
                }
            }
        }
        
        // Also check the high-prio BUILD flag to validate the region-membership tier (region owner / member):
        if (query.testState(wgLocation, wgPlayer, Flags.BUILD)) {
            passed = true;
            reasons.add(ResultReasons.WG_FLAG_BUILD);
        }
        
        return new Result(passed, reasons);
    }
    
    /**
     * This method returns whether the "regions.enable" option is activated for the world or not. 
     * The setting is taken from the WorldGuard main config (`config.yml`) and can be specified 
     * with the world config (`/worlds/%world-name%/config.yml`).
     * 
     * @param world (WorldGuard world) the target world
     * @return 'true' if the region-feature is activated in this world
     */
    public static boolean isRestrictedWorld(World world) {
        WorldConfiguration worldConfig = WorldGuardPlugin.inst().getConfigManager().get(world);
        
        return (worldConfig.useRegions);
    }

    /**
     * Check whether a player has bypass permission in the corresponding world of target location.
     * 
     * @param localPlayer (LocalPlayer) the target player
     * @return 'true' if the player has bypass permission in the corresponding world
     */
    public static boolean hasWorldBypassPermission(LocalPlayer localPlayer) {
        return WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, localPlayer.getWorld());
    }
    
}
