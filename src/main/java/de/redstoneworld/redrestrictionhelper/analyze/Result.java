package de.redstoneworld.redrestrictionhelper.analyze;

import de.redstoneworld.redrestrictionhelper.enums.AllowReasons;

import java.util.ArrayList;
import java.util.List;

public class Result {
    
    private final boolean allowed;
    private final List<AllowReasons> allowReasons;
    private final long timeOfCheck;

    public Result(boolean allowed, List<AllowReasons> allowReasons, long timeOfCheck) {
        this.allowed = allowed;
        this.allowReasons = allowReasons;
        this.timeOfCheck = timeOfCheck;
    }
    
    public Result(boolean allowed, List<AllowReasons> allowReasons) {
        this.allowed = allowed;
        this.allowReasons = allowReasons;
        this.timeOfCheck = System.currentTimeMillis();
    }
    
    public Result(boolean allowed) {
        this.allowed = allowed;
        this.allowReasons = new ArrayList<>();
        this.timeOfCheck = System.currentTimeMillis();
    }
    
    public boolean isAllowed() {
        return allowed;
    }

    public List<AllowReasons> getAllowReasons() {
        return allowReasons;
    }

    public long getTimeOfCheck() {
        return timeOfCheck;
    }
}
