package de.redstoneworld.redrestrictionhelper.analyze.restrictionplugins;

import com.plotsquared.bukkit.util.BukkitUtil;
import com.plotsquared.core.PlotSquared;
import com.plotsquared.core.location.Location;
import com.plotsquared.core.permissions.Permission;
import com.plotsquared.core.plot.Plot;
import com.plotsquared.core.plot.PlotArea;
import de.redstoneworld.redrestrictionhelper.RestrictionCheck;
import de.redstoneworld.redrestrictionhelper.analyze.RestrictionPluginCheck;
import de.redstoneworld.redrestrictionhelper.analyze.Result;
import de.redstoneworld.redrestrictionhelper.enums.ResultReasons;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class PlotSquared_V7 extends RestrictionPluginCheck {
    
    public PlotSquared_V7(Plugin plugin) {
        super(plugin);
    }
    
    public Result runCheck(RestrictionCheck check) {
        
        /*
        * Note: Only the Plot membership tiers and the bypass-permission are checked here.
        * 
        * See: https://intellectualsites.gitbook.io/plotsquared/features/plot-membership-tiers
        * See: https://intellectualsites.gitbook.io/plotsquared/permissions/bypass-permissions
        * See: https://intellectualsites.gitbook.io/plotsquared/api/api-documentation#maven-plotsquared-core-and-bukkit
         */

        Location psLocation = BukkitUtil.adapt(check.getLocation());
        Player player = check.getTargetPlayer();
        boolean passed = false;
        List<ResultReasons> reasons = new ArrayList<>();
        
        
        if (!isPlotWorld(psLocation)) {
            passed = true;
            reasons.add(ResultReasons.PS_NOT_A_PLOT_WORLD);
            return new Result(passed);
        }


        if (!psLocation.isPlotRoad()) {
            
            final Plot plot = psLocation.getPlot();
            assert plot != null;
            
            if (!psLocation.isUnownedPlotArea()) {

                // Normal plot-membership-tier check of player:
                if (plot.isAdded(player.getUniqueId())) {
                    passed = true;

                    if (plot.isOwner(player.getUniqueId())) {
                        reasons.add(ResultReasons.PS_OWNER_OF_PLOT);
                    } else {
                        reasons.add(ResultReasons.PS_MEMBER_OF_PLOT);
                    }
                }

                // Bypass-permission check for occupied plots:
                switch (check.getActionType()) {
                    case INTERACT -> {
                        if (player.hasPermission(Permission.PERMISSION_ADMIN_INTERACT_OTHER.toString())) {
                            passed = true;
                            reasons.add(ResultReasons.PS_BYPASS_PERMISSION_INTERACT_OTHER);
                        }
                    }
                    case PLACE_AND_BREAK -> {
                        if ((player.hasPermission(Permission.PERMISSION_ADMIN_BUILD_OTHER.toString()))
                                && (player.hasPermission(Permission.PERMISSION_ADMIN_DESTROY_OTHER.toString()))) {
                            passed = true;
                            reasons.add(ResultReasons.PS_BYPASS_PERMISSION_PLACE_OTHER);
                            reasons.add(ResultReasons.PS_BYPASS_PERMISSION_BREAK_OTHER);
                        }
                    }
                    case PLACE -> {
                        if (player.hasPermission(Permission.PERMISSION_ADMIN_BUILD_OTHER.toString())) {
                            passed = true;
                            reasons.add(ResultReasons.PS_BYPASS_PERMISSION_PLACE_OTHER);
                        }
                    }
                    case BREAK -> {
                        if (player.hasPermission(Permission.PERMISSION_ADMIN_DESTROY_OTHER.toString())) {
                            passed = true;
                            reasons.add(ResultReasons.PS_BYPASS_PERMISSION_BREAK_OTHER);
                        }
                    }
                }

            } else {
                
                // Bypass-permission check for unowned plots:
                switch (check.getActionType()) {
                    case INTERACT -> {
                        if (player.hasPermission(Permission.PERMISSION_ADMIN_INTERACT_UNOWNED.toString())) {
                            passed = true;
                            reasons.add(ResultReasons.PS_BYPASS_PERMISSION_INTERACT_UNOWNED);
                        }
                    }
                    case PLACE_AND_BREAK -> {
                        if ((player.hasPermission(Permission.PERMISSION_ADMIN_BUILD_UNOWNED.toString())) 
                                && (player.hasPermission(Permission.PERMISSION_ADMIN_DESTROY_UNOWNED.toString()))) {
                            passed = true;
                            reasons.add(ResultReasons.PS_BYPASS_PERMISSION_PLACE_UNOWNED);
                            reasons.add(ResultReasons.PS_BYPASS_PERMISSION_BREAK_UNOWNED);
                        }
                    }
                    case PLACE -> {
                        if (player.hasPermission(Permission.PERMISSION_ADMIN_BUILD_UNOWNED.toString())) {
                            passed = true;
                            reasons.add(ResultReasons.PS_BYPASS_PERMISSION_PLACE_UNOWNED);
                        }
                    }
                    case BREAK -> {
                        if (player.hasPermission(Permission.PERMISSION_ADMIN_DESTROY_UNOWNED.toString())) {
                            passed = true;
                            reasons.add(ResultReasons.PS_BYPASS_PERMISSION_BREAK_UNOWNED);
                        }
                    }
                }
            }
            
        } else {
            
            // Bypass-permission check for plot-roads:
            switch (check.getActionType()) {
                case INTERACT -> {
                    if (player.hasPermission(Permission.PERMISSION_ADMIN_INTERACT_ROAD.toString())) {
                        passed = true;
                        reasons.add(ResultReasons.PS_BYPASS_PERMISSION_INTERACT_ROAD);
                    }
                }
                case PLACE_AND_BREAK -> {
                    if ((player.hasPermission(Permission.PERMISSION_ADMIN_BUILD_ROAD.toString())) 
                            && (player.hasPermission(Permission.PERMISSION_ADMIN_DESTROY_ROAD.toString()))) {
                        passed = true;
                        reasons.add(ResultReasons.PS_BYPASS_PERMISSION_PLACE_ROAD);
                        reasons.add(ResultReasons.PS_BYPASS_PERMISSION_BREAK_ROAD);
                    }
                }
                case PLACE -> {
                    if (player.hasPermission(Permission.PERMISSION_ADMIN_BUILD_ROAD.toString())) {
                        passed = true;
                        reasons.add(ResultReasons.PS_BYPASS_PERMISSION_PLACE_ROAD);
                    }
                }
                case BREAK -> {
                    if (player.hasPermission(Permission.PERMISSION_ADMIN_DESTROY_ROAD.toString())) {
                        passed = true;
                        reasons.add(ResultReasons.PS_BYPASS_PERMISSION_BREAK_ROAD);
                    }
                }
            }
            
        }
        
        return new Result(passed, reasons);
    }
    
    public boolean isPlotWorld(Location location) {
        PlotArea area = PlotSquared.get().getPlotAreaManager().getPlotArea(location);
        return area != null;
    }
    
}
