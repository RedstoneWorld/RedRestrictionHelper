package de.redstoneworld.redrestrictionhelper.analyze;

import de.redstoneworld.redrestrictionhelper.RestrictionCheck;
import de.redstoneworld.redrestrictionhelper.enums.CheckMethods;
import de.redstoneworld.redrestrictionhelper.enums.RestrictionPlugins;
import de.redstoneworld.redrestrictionhelper.enums.AllowReasons;
import de.redstoneworld.redrestrictionhelper.analyze.queries.PlotSquared_V7;
import de.redstoneworld.redrestrictionhelper.analyze.queries.WorldGuard_V7;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Analyzer {
    
    private final Plugin bukkitPlugin;
    private final HashMap<RestrictionPlugins, Result> perPluginResultMap = new HashMap<>();
    
    
    public Analyzer(Plugin bukkitPlugin) {
        this.bukkitPlugin = bukkitPlugin;
    }
    
    public HashMap<RestrictionPlugins, Result> executeAnalyse(RestrictionCheck check) {
        
        if (check.getCheckMethod() == CheckMethods.EVENT_CALLING) {
            analyseByTestEvent(check);
            
        } else if (check.getCheckMethod() == CheckMethods.RESTRICTION_PLUGIN_API) {
            
            if (!hasRestrictionPlugins()) {
                bukkitPlugin.getServer().getLogger().warning("No restriction plugin found.");
                return null;
            }
            
            analyseByRestrictionPlugins(check);
        }
        
        return perPluginResultMap;
    }
    
    private void analyseByTestEvent(RestrictionCheck check) {
        
        Block block = check.getLocation().getBlock();
        Player player = check.getTargetPlayer();
        boolean passed = false;
        List<AllowReasons> reasons = new ArrayList<>();
        
        
        // Checking build permission by test-events. (The block is generally not placed via 'callEvent()' method.)
        
        switch (check.getActionType()) {
            case INTERACT -> {
                PlayerInteractEvent testInteractEvent = new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, new ItemStack(Material.AIR), 
                        block, BlockFace.UP);
                new TestEventExecuter(bukkitPlugin, check.getLocation(), testInteractEvent, false);
                
                // special event-result here:
                passed = (testInteractEvent.useInteractedBlock() == Event.Result.ALLOW);
                if (passed) reasons.add(AllowReasons.RRH_EVENT_INTERACT);
            }
            case PLACE_AND_BREAK -> {
                // Used 'BlockPlaceEvent', because e.g. 'EntityPlaceEvent' is not handled the same for every restriction system.
                BlockPlaceEvent testPlaceEvent = new BlockPlaceEvent(block, block.getState(), block, new ItemStack(Material.AIR), 
                        player, false);
                new TestEventExecuter(bukkitPlugin, check.getLocation(), testPlaceEvent, false);
                
                // Used 'BlockBreakEvent', because e.g. 'EntityBreak-Door-Event' is not handled the same for every restriction system.
                BlockBreakEvent testBreakEvent = new BlockBreakEvent(block, player);
                new TestEventExecuter(bukkitPlugin, check.getLocation(), testBreakEvent, false);
                
                passed = ((!testPlaceEvent.isCancelled()) && (!testBreakEvent.isCancelled()));
                if (passed) reasons.add(AllowReasons.RRH_EVENT_PLACE);
                if (passed) reasons.add(AllowReasons.RRH_EVENT_BREAK);
            }
            case PLACE -> {
                // Used 'BlockPlaceEvent', because e.g. 'EntityPlaceEvent' is not handled the same for every restriction system.
                BlockPlaceEvent testPlaceEvent = new BlockPlaceEvent(block, block.getState(), block, new ItemStack(Material.AIR), 
                        player, false);
                new TestEventExecuter(bukkitPlugin, check.getLocation(), testPlaceEvent, false);
                
                passed = !testPlaceEvent.isCancelled();
                if (passed) reasons.add(AllowReasons.RRH_EVENT_PLACE);
            }
            case BREAK -> {
                // Used 'BlockBreakEvent', because e.g. 'EntityBreak-Door-Event' is not handled the same for every restriction system.
                BlockBreakEvent testBreakEvent = new BlockBreakEvent(block, player);
                new TestEventExecuter(bukkitPlugin, check.getLocation(), testBreakEvent, false);
                
                passed = !testBreakEvent.isCancelled();
                if (passed) reasons.add(AllowReasons.RRH_EVENT_BREAK);
            }
        }
        
        perPluginResultMap.put(RestrictionPlugins.RED_RESTRICTION_HELPER, new Result(passed, reasons));
    }
    
    private void analyseByRestrictionPlugins(RestrictionCheck check) {
        
        if (bukkitPlugin.getServer().getPluginManager().isPluginEnabled(RestrictionPlugins.WORLD_GUARD.getName())) {
            Result wgResult = WorldGuard_V7.runCheck(check);
            perPluginResultMap.put(RestrictionPlugins.WORLD_GUARD, wgResult);
        }
        
        if (bukkitPlugin.getServer().getPluginManager().isPluginEnabled(RestrictionPlugins.PLOT_SQUARED.getName())) {
            Result psResult = PlotSquared_V7.runCheck(check);
            perPluginResultMap.put(RestrictionPlugins.PLOT_SQUARED, psResult);
        }
    }
    
    public boolean hasRestrictionPlugins() {
        
        AtomicBoolean foundPlugin = new AtomicBoolean(false);
        
        RestrictionPlugins.RESTRICTION_PLUGIN_NAMES.forEach(plugin -> {
            
            if (plugin.equalsIgnoreCase(RestrictionPlugins.RED_RESTRICTION_HELPER.getName())) return;
            
            if (bukkitPlugin.getServer().getPluginManager().isPluginEnabled(plugin)) {
                foundPlugin.set(true);
            }
        });
        
        return foundPlugin.get();
    }

}