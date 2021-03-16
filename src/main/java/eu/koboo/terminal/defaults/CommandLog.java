package eu.koboo.terminal.defaults;

import eu.koboo.terminal.command.Command;
import eu.koboo.terminal.util.ConsoleLevel;

import java.util.Arrays;

public class CommandLog extends Command {

    @Override
    public String[] commands() {
        return new String[]{"log", "console"};
    }

    @Override
    public String description() {
        return "Change the log level of console";
    }

    @Override
    public void execute(String command, String[] args) {
        if (args.length == 1) {
            try {
                ConsoleLevel consoleLevel = ConsoleLevel.valueOf(args[0].toUpperCase());
                if(terminal.getLevel() != consoleLevel) {
                    terminal.setLevel(consoleLevel);
                    terminal.all("LogLevel changed to " + consoleLevel.name());
                } else {
                    terminal.all("LogLevel is already set to " + consoleLevel.name());
                }
            } catch (Exception e) {
                terminal.all("");
                terminal.all("Error while parsing '" + args[0].toUpperCase() + "' (" + e.getMessage() + ")");
                printHelp();
            }
        } else {
            printHelp();
        }
    }

    private void printHelp() {
        terminal.all("");
        StringBuilder builder = new StringBuilder();
        Arrays.stream(ConsoleLevel.values()).forEach(level -> builder.append(", ").append(level.name()));
        terminal.all("Level: " + builder.toString().replaceFirst(", ", ""));
        terminal.all("Current: " + terminal.getLevel().name());
        terminal.all("Usage: log <log-level>");
        terminal.all("");
    }
}
