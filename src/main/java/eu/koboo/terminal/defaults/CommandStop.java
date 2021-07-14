package eu.koboo.terminal.defaults;

import eu.koboo.terminal.command.Command;

public class CommandStop extends Command {

    @Override
    public String[] commands() {
        return new String[]{"stop", "end", "shutdown"};
    }

    @Override
    public void execute(String command, String[] args) {
        terminal.all("Stopping..");
        if (terminal.getShutdownHook() != null) {
            terminal.getShutdownHook().run();
        }
        System.exit(0);
    }

    @Override
    public String description() {
        return "Stop the application";
    }

}
