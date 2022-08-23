package eu.koboo.terminal.util;

/**
 * The ConsoleLevel-class to determine the log-level
 */
public enum ConsoleLevel {

    OFF(-1),
    TRACE(0),
    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4),
    FATAL(5),
    ALL(6),
    EXT(7);

    private final int level;

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