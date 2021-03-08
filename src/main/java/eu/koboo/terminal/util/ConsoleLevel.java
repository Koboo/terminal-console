package eu.koboo.terminal.util;

/**
 * The ConsoleLevel-class to determine the log-level
 */
public enum ConsoleLevel {

    NONE(-1),
    TRACE(0),
    DEBUG(1),
    WARN(2),
    INFO(3),
    ALERT(4),
    ERROR(5),
    CMD(6),
    EXT(7);

    int level;

    ConsoleLevel(int level) {
        this.level = level;
    }

    /**
     * Get the level as Integer of the ConsoleLevel
     * @return The level
     */
    public int level() {
        return level;
    }
}
