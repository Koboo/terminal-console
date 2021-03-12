package eu.koboo.terminal.defaults;

import eu.koboo.terminal.command.Command;

public class CommandClear extends Command {

    @Override
    public String[] commands() {
        return new String[]{"cc", "clear"};
    }

    @Override
    public String description() {
        return "Clear the console";
    }

    @Override
    public void execute(String command, String[] args) {
        terminal.clearConsole();
        terminal.all("The console was cleared successfully!");
    }
}