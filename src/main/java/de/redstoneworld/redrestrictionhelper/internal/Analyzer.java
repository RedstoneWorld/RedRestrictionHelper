package de.redstoneworld.redrestrictionhelper.internal;

import de.redstoneworld.redrestrictionhelper.RestrictionCheck;
import de.redstoneworld.redrestrictionhelper.enums.CheckMethods;
import de.redstoneworld.redrestrictionhelper.enums.RestrictionPlugins;
import de.redstoneworld.redrestrictionhelper.enums.ResultReasons;
import de.redstoneworld.redrestrictionhelper.restrictionplugins.PlotSquared_V7;
import de.redstoneworld.redrestrictionhelper.restrictionplugins.WorldGuard_V7;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Analyzer {
    
    private static Plugin bukkitPlugin;
    
    
    public static Plugin getBukkitPlugin() {
        return bukkitPlugin;
    }

    public static void setBukkitPlugin(Plugin bukkitPlugin) {
        Analyzer.bukkitPlugin = bukkitPlugin;
    }

    public void analyse(RestrictionCheck check) {
        
        if (check.getCheckMethod() == CheckMethods.EVENT_CALLING) {
            analyseByTestEvent(check);
        } else {
            analyseByRestrictionPlugins(check);
        }
    }

    private void analyseByTestEvent(RestrictionCheck check) {
        
        Block block = check.getLocation().getBlock();
        Player player = check.getTargetPlayer();
        boolean passed = false;
        List<ResultReasons> reasons = new ArrayList<>();
        
        
        // Checking build permission by test-events. (The block is generally not placed via 'callEvent()' method.)
        
        switch (check.getActionType()) {
            case INTERACT -> {
                PlayerInteractEvent testInteractEvent = new PlayerInteractEvent(player, Action.LEFT_CLICK_BLOCK, player.getItemInUse(), 
                        block, BlockFace.UP);
                callEvent(check.getLocation(), testInteractEvent);
                
                // special event-result here:
                passed = (testInteractEvent.useInteractedBlock() == Event.Result.ALLOW);
                reasons.add(ResultReasons.RRH_EVENT_INTERACT);
            }
            case PLACE_AND_BREAK -> {
                // Used 'BlockPlaceEvent', because e.g. 'EntityPlaceEvent' is not handled the same for every restriction system.
                BlockPlaceEvent testPlaceEvent = new BlockPlaceEvent(block, block.getState(), block, player.getItemOnCursor(), 
                        player, false);
                callEvent(check.getLocation(), testPlaceEvent);
                
                // Used 'BlockBreakEvent', because e.g. 'EntityBreak-Door-Event' is not handled the same for every restriction system.
                BlockBreakEvent testBreakEvent = new BlockBreakEvent(block, player);
                callEvent(check.getLocation(), testBreakEvent);
                
                passed = ((!testPlaceEvent.isCancelled()) && (!testBreakEvent.isCancelled()));
                reasons.add(ResultReasons.RRH_EVENT_PLACE);
                reasons.add(ResultReasons.RRH_EVENT_BREAK);
            }
            case PLACE -> {
                // Used 'BlockPlaceEvent', because e.g. 'EntityPlaceEvent' is not handled the same for every restriction system.
                BlockPlaceEvent testPlaceEvent = new BlockPlaceEvent(block, block.getState(), block, player.getItemOnCursor(), 
                        player, false);
                callEvent(check.getLocation(), testPlaceEvent);
                
                passed = !testPlaceEvent.isCancelled();
                reasons.add(ResultReasons.RRH_EVENT_PLACE);
            }
            case BREAK -> {
                // Used 'BlockBreakEvent', because e.g. 'EntityBreak-Door-Event' is not handled the same for every restriction system.
                BlockBreakEvent testBreakEvent = new BlockBreakEvent(block, player);
                callEvent(check.getLocation(), testBreakEvent);
                
                passed = !testBreakEvent.isCancelled();
                reasons.add(ResultReasons.RRH_EVENT_BREAK);
            }
        }
        
        check.setResult(passed, System.currentTimeMillis(), reasons);
        
    }
    
    private void analyseByRestrictionPlugins(RestrictionCheck check) {
        
        if (getBukkitPlugin().getServer().getPluginManager().isPluginEnabled(RestrictionPlugins.WORLD_GUARD.getName())) {
            new WorldGuard_V7(bukkitPlugin, check);
        }
        
        if (getBukkitPlugin().getServer().getPluginManager().isPluginEnabled(RestrictionPlugins.PLOT_SQUARED.getName())) {
            new PlotSquared_V7(bukkitPlugin, check);
        }
        
    }

    private void callEvent(Location location, Event testEvent) {
        
        if (isFoliaServer()) {
            
/*            
            World world = location.getWorld();
            world.getRegionScheduler().execute(location, () -> {
                Bukkit.getPluginManager().callEvent(testEvent);
            });
            */
            
            try {
                World world = location.getWorld();
    
                // world.getRegionScheduler()
                Method getRegionScheduler = world.getClass().getMethod("getRegionScheduler");
                Object regionScheduler = getRegionScheduler.invoke(world);
    
                // regionScheduler.execute(plugin, location, runnable)
                Method execute = regionScheduler.getClass().getMethod(
                        "execute",
                        Plugin.class,
                        Location.class,
                        Runnable.class
                );
                
                execute.invoke(regionScheduler, bukkitPlugin, location, (Runnable) () -> {
                    Bukkit.getPluginManager().callEvent(testEvent);
                });
            
            } catch (Exception e) {
                e.printStackTrace();
            }
        
        } else {
            getBukkitPlugin().getServer().getPluginManager().callEvent(testEvent);
            // (An event-cancel afterward would have no effects.)
        }
    }
    
    public static boolean hasRestrictionPlugins() {
        if ((getBukkitPlugin().getServer().getPluginManager().isPluginEnabled("WorldGuard")) 
                || (getBukkitPlugin().getServer().getPluginManager().isPluginEnabled("PlotSquared"))) return true;
        
        return false;
    }
    
    private static boolean isFoliaServer() {
        try {
            Class.forName("io.papermc.paper.threadedregions.scheduler.RegionScheduler");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}