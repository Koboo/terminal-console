package eu.koboo.terminal.defaults;

import eu.koboo.terminal.command.Command;
import eu.koboo.terminal.TerminalConsole;

import java.util.List;

public class CommandHelp extends Command {

    public CommandHelp(TerminalConsole terminal) {
        super(terminal);
    }

    @Override
    public String[] commands() {
        return new String[]{"help", "?"};
    }

    @Override
    public String description() {
        return "See what you are seeing right now. Do you see it?";
    }

    @Override
    public void execute(String command, String[] args) {
        List<Command> commands = terminal.getCommandRegistry();

        terminal.cmd(" ====[  T E R M I N A L  ]====");
        terminal.cmd("");
        terminal.cmd("All available commands:");
        terminal.cmd("");
        for(Command cmd : commands) {
            StringBuilder builder = new StringBuilder();
            for(String alias : cmd.commands()) {
                builder.append(", ").append(alias);
            }
            String cmds = builder.toString().replaceFirst(", ", "");
            terminal.cmd("  * §c" + cmds + "§8 » §7" + cmd.description());
        }
        terminal.cmd("");
        terminal.cmd(" ====[  T E R M I N A L  ]====");
    }
}
