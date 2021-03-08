package eu.koboo.terminal.defaults;

import eu.koboo.terminal.command.Command;
import eu.koboo.terminal.TerminalConsole;

public class CommandUngrep extends Command {

    public CommandUngrep(TerminalConsole terminal) {
        super(terminal);
    }

    @Override
    public String[] commands() {
        return new String[]{"ungrep"};
    }

    @Override
    public String description() {
        return "Deactivate ConsoleGrep";
    }

    @Override
    public void execute(String command, String[] args) {
        if(args.length == 0) {
            terminal.ungrep();
            terminal.cmd("Grep-Mode deactivated!");
        } else {
            printHelp();
        }
    }

    private void printHelp() {
        terminal.cmd("");
        terminal.cmd("Usage: ungrep - Remove ConsoleGrep");
        terminal.cmd("");
    }
}
