package eu.koboo.terminal.command;

import eu.koboo.terminal.TerminalConsole;

public abstract class Command {

    protected final TerminalConsole terminal;

    public Command() {
        this.terminal = TerminalConsole.getConsole();
    }

    /**
     * The array of the commands to enter
     * to execute the specific TerminalCommand
     * @return The commands as String[]
     */
    public abstract String[] commands();

    /**
     * The execute method of the TerminalCommand
     * @param command The entered command
     * @param args The arguments of the command
     */
    public abstract void execute(String command, String[] args);

    /**
     * Get the description of the specific TerminalCommand
     * @return The description
     */
    public abstract String description();
}
