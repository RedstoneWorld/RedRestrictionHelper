package de.redstoneworld.redrestrictionhelper;

import de.redstoneworld.redrestrictionhelper.enums.ActionTypes;
import de.redstoneworld.redrestrictionhelper.enums.CheckMethods;
import de.redstoneworld.redrestrictionhelper.enums.ResultReasons;
import de.redstoneworld.redrestrictionhelper.internal.Analyzer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class RestrictionCheck {
    
    private static CheckMethods defaultCheckMethod = CheckMethods.EVENT_CALLING;
    
    private final ActionTypes actionType;
    private final Location location;
    private final Player targetPlayer;
    private final CheckMethods checkMethod;

    private long timeOfCheck;
    private boolean result;
    private List<ResultReasons> resultReason;


    public RestrictionCheck(ActionTypes actionType, Location location, Player targetPlayer) {
        this.actionType = actionType;
        this.location = location;
        this.targetPlayer = targetPlayer;
        this.checkMethod = defaultCheckMethod;
    }
    
    public RestrictionCheck(ActionTypes actionType, Location location, Player targetPlayer, CheckMethods checkMethod) {
        this.actionType = actionType;
        this.location = location;
        this.targetPlayer = targetPlayer;
        this.checkMethod = checkMethod;
    }

    public static void initial(Plugin bukkitPlugin) {
        Analyzer.setBukkitPlugin(bukkitPlugin);
        
        if (!Analyzer.hasRestrictionPlugins()) {
            bukkitPlugin.getServer().getLogger().warning("No restriction plugin found.");
        }
    }

    public static CheckMethods getDefaultCheckMethod() {
        return defaultCheckMethod;
    }

    public static void setDefaultCheckMethod(CheckMethods checkMethod) {
        RestrictionCheck.defaultCheckMethod = checkMethod;
    }

    public ActionTypes getActionType() {
        return actionType;
    }

    public Location getLocation() {
        return location;
    }

    public Player getTargetPlayer() {
        return targetPlayer;
    }

    public CheckMethods getCheckMethod() {
        return checkMethod;
    }

    public void setResult(boolean result, long timeOfCheck) {
        this.result = result;
        this.timeOfCheck = timeOfCheck;
    }

    public void setResult(boolean result, long timeOfCheck, List<ResultReasons> resultReason) {
        this.result = result;
        this.timeOfCheck = timeOfCheck;
        this.resultReason = resultReason;
    }

    public long getTimeOfCheck() {
        return timeOfCheck;
    }
    
    public boolean getResult() {
        return result;
    }

    public List<ResultReasons> getResultReason() {
        return resultReason;
    }
}
