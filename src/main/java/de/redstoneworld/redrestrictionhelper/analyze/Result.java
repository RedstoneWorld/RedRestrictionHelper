package de.redstoneworld.redrestrictionhelper.analyze;

import de.redstoneworld.redrestrictionhelper.enums.ResultReasons;

import java.util.ArrayList;
import java.util.List;

public class Result {
    
    private final boolean allowed;
    private final List<ResultReasons> resultReason;
    private final long timeOfCheck;

    public Result(boolean allowed, List<ResultReasons> resultReason, long timeOfCheck) {
        this.allowed = allowed;
        this.resultReason = resultReason;
        this.timeOfCheck = timeOfCheck;
    }
    
    public Result(boolean allowed, List<ResultReasons> resultReason) {
        this.allowed = allowed;
        this.resultReason = resultReason;
        this.timeOfCheck = System.currentTimeMillis();
    }
    
    public Result(boolean allowed) {
        this.allowed = allowed;
        this.resultReason = new ArrayList<>();
        this.timeOfCheck = System.currentTimeMillis();
    }
    
    public boolean isAllowed() {
        return allowed;
    }

    public List<ResultReasons> getResultReason() {
        return resultReason;
    }

    public long getTimeOfCheck() {
        return timeOfCheck;
    }
}
