package eu.koboo.terminal.command;

/**
 * The interface to implement for an TerminalCommand
 */
public interface ICommand {

    /**
     * The array of the commands to enter
     * to execute the specific TerminalCommand
     * @return The commands as String[]
     */
    String[] commands();

    /**
     * The execute method of the TerminalCommand
     * @param command The entered command
     * @param args The arguments of the command
     */
    void execute(String command, String[] args);

    /**
     * Get the description of the specific TerminalCommand
     * @return The description
     */
    default String description() {
        return "no description";
    }

}