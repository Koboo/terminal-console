package eu.koboo.terminal.defaults;

import eu.koboo.terminal.command.Command;
import eu.koboo.terminal.util.ConsoleLevel;
import eu.koboo.terminal.TerminalConsole;

import java.util.Arrays;

public class CommandLog extends Command {

    public CommandLog(TerminalConsole terminal) {
        super(terminal);
    }

    @Override
    public String[] commands() {
        return new String[]{"log", "console"};
    }

    @Override
    public String description() {
        return "Change the log level of your console";
    }

    @Override
    public void execute(String command, String[] args) {
        if (args.length == 1) {
            try {
                ConsoleLevel consoleLevel = ConsoleLevel.valueOf(args[0].toUpperCase());
                if(terminal.getLogLevel() != consoleLevel) {
                    terminal.setLevel(consoleLevel);
                    terminal.cmd("LogLevel changed to " + consoleLevel.name());
                } else {
                    terminal.cmd("LogLevel is already set to " + consoleLevel.name());
                }
            } catch (Exception e) {
                terminal.cmd("");
                terminal.cmd("Error while parsing '" + args[0].toUpperCase() + "' (" + e.getMessage() + ")");
                printHelp();
            }
        } else {
            printHelp();
        }
    }

    private void printHelp() {
        terminal.info("");
        StringBuilder builder = new StringBuilder();
        Arrays.stream(ConsoleLevel.values()).forEach(level -> builder.append(", ").append(level.name()));
        terminal.cmd("Level: " + builder.toString().replaceFirst(", ", ""));
        terminal.cmd("Current: " + terminal.getLogLevel().name());
        terminal.cmd("Usage: log <log-level>");
        terminal.cmd("");
    }
}
