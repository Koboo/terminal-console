package eu.koboo.terminal.util;

public enum ConsoleColor {

    RESET("\u001B[0m", "r"),
    BLACK("\u001B[30m", "0"),
    DARK_BLUE("\u001B[34m", "1"),
    DARK_GREEN("\u001B[32m", "2"),
    DARK_CYAN("\u001B[36m", "3"),
    DARK_RED("\u001B[31m", "4"),
    DARK_PURPLE("\u001B[35m", "5"),
    DARK_YELLOW("\u001B[33m", "6"),
    LIGHT_GRAY("\u001B[97m", "7"),
    DARK_GRAY("\u001B[90m", "8"),
    LIGHT_BLUE("\u001B[94m", "9"),
    LIGHT_GREEN("\u001B[92m", "a"),
    LIGHT_CYAN("\u001B[96m", "b"),
    LIGHT_RED("\u001B[91m", "c"),
    LIGHT_PURPLE("\u001B[95m", "d"),
    LIGHT_YELLOW("\u001B[93m", "e"),
    WHITE("\u001B[37m", "f");

    String ansiColor;
    String code;

    ConsoleColor(String ansiColor, String code) {
        this.ansiColor = ansiColor;
        this.code = code;
    }

    public String getAnsiColor() {
        return ansiColor;
    }

    public String getAmpCode() {
        return "&" + code;
    }

    public String getParagraphCode() {
        return "§" + code;
    }

    private static String replaceColor(ConsoleColor color, String message, String replacement) {
        message = message.contains(color.getAmpCode()) ? message.replaceAll(color.getAmpCode(), replacement) : message;
        return message.contains(color.getParagraphCode()) ? message.replaceAll(color.getParagraphCode(), replacement) : message;
    }

    public static String removeColor(ConsoleColor color, String message) {
        return replaceColor(color, message, "");
    }

    public static String removeColor(String message) {
        for(ConsoleColor consoleColor : values()) {
            message = removeColor(consoleColor, message);
        }
        return message;
    }

    public static String parseColor(String message) {
        for(ConsoleColor consoleColor : values()) {
            message = replaceColor(consoleColor, message, consoleColor.getAnsiColor());
        }
        return message;
    }

}
