package eu.koboo.terminal.command;

import eu.koboo.terminal.TerminalConsole;

public abstract class Command implements ICommand {

    protected final TerminalConsole terminal;

    public Command(TerminalConsole terminal) {
        this.terminal = terminal;
    }
}
