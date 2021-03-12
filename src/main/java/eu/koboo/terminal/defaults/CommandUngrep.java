package eu.koboo.terminal.defaults;

import eu.koboo.terminal.command.Command;

public class CommandUngrep extends Command {

    @Override
    public String[] commands() {
        return new String[]{"ungrep"};
    }

    @Override
    public String description() {
        return "Deactivate console grep-mode";
    }

    @Override
    public void execute(String command, String[] args) {
        if(args.length == 0) {
            terminal.ungrep();
            terminal.all("Grep-Mode deactivated!");
        } else {
            printHelp();
        }
    }

    private void printHelp() {
        terminal.all("");
        terminal.all("Usage: ungrep");
        terminal.all("");
    }
}
