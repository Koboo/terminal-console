package eu.koboo.terminal.defaults;

import eu.koboo.terminal.command.Command;
import eu.koboo.terminal.TerminalConsole;

public class CommandStop extends Command {

    public CommandStop(TerminalConsole terminal) {
        super(terminal);
    }

    @Override
    public String[] commands() {
        return new String[]{"stop", "end"};
    }

    @Override
    public void execute(String command, String[] args) {
        terminal.cmd("Stopping..");
        System.exit(0);
    }

    @Override
    public String description() {
        return "Stop the application";
    }

}
