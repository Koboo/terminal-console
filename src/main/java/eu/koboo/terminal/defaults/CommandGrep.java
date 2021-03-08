package eu.koboo.terminal.defaults;

import eu.koboo.terminal.command.Command;
import eu.koboo.terminal.TerminalConsole;

public class CommandGrep extends Command {

    public CommandGrep(TerminalConsole terminal) {
        super(terminal);
    }

    @Override
    public String[] commands() {
        return new String[]{"grep"};
    }

    @Override
    public String description() {
        return "Grep only message, containing <message>";
    }

    @Override
    public void execute(String command, String[] args) {
        if (args.length >= 1) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                builder.append(args[i]).append(" ");
            }
            String message = builder.toString();
            message = message.substring(0, message.length() - 1);
            terminal.cmd("Grep-Mode active: '" + message + "'");
            terminal.grep(message);
        } else {
            printHelp();
        }
    }

    private void printHelp() {
        terminal.cmd("");
        terminal.cmd("Usage: grep <message> - Filter logs by message");
        terminal.cmd("");
    }
}
