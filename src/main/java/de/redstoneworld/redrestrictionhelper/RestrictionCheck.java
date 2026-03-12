package de.redstoneworld.redrestrictionhelper;

import de.redstoneworld.redrestrictionhelper.analyze.Analyzer;
import de.redstoneworld.redrestrictionhelper.analyze.Result;
import de.redstoneworld.redrestrictionhelper.enums.ActionTypes;
import de.redstoneworld.redrestrictionhelper.enums.CheckMethods;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class RestrictionCheck {
    
    private static final CheckMethods DEFAULT_CHECK_METHOD = CheckMethods.EVENT_CALLING;
    
    private final Plugin bukkitPlugin;
    
    private final ActionTypes actionType;
    private final Location location;
    private final Player targetPlayer;
    private final CheckMethods checkMethod;
    
    private Result result;
    
    
    public RestrictionCheck(Plugin bukkitPlugin, ActionTypes actionType, Location location, Player targetPlayer) {
        this.bukkitPlugin = bukkitPlugin;
        this.actionType = actionType;
        this.location = location;
        this.targetPlayer = targetPlayer;
        this.checkMethod = DEFAULT_CHECK_METHOD;
        
        runCheck();
    }
    
    public RestrictionCheck(Plugin bukkitPlugin, ActionTypes actionType, Location location, Player targetPlayer, CheckMethods checkMethod) {
        this.bukkitPlugin = bukkitPlugin;
        this.actionType = actionType;
        this.location = location;
        this.targetPlayer = targetPlayer;
        this.checkMethod = checkMethod;
        
        runCheck();
    }
    
    private void runCheck() {
        
        Analyzer analyzer = new Analyzer(bukkitPlugin);
        result = analyzer.executeAnalyse(this);
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

    public static CheckMethods getDefaultCheckMethod() {
        return DEFAULT_CHECK_METHOD;
    }
    
    public CheckMethods getCheckMethod() {
        return checkMethod;
    }
    
    public Result getResult() {
        return result;
    }
    
}