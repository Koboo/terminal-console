package eu.koboo.terminal.defaults;

import eu.koboo.terminal.command.Command;
import eu.koboo.terminal.TerminalConsole;

public class CommandClear extends Command {

    public CommandClear(TerminalConsole terminal) {
        super(terminal);
    }

    @Override
    public String[] commands() {
        return new String[]{"cc", "clearchat", "clear"};
    }

    @Override
    public String description() {
        return "Clear the console";
    }

    @Override
    public void execute(String command, String[] args) {
        terminal.clearConsole();
        terminal.cmd("The console was cleared successfully!");
    }
}