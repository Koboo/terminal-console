package eu.koboo.terminal.defaults;

import eu.koboo.terminal.command.Command;

import java.util.List;

public class CommandHelp extends Command {

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

        terminal.all(" ====[  T E R M I N A L  ]====");
        terminal.all("");
        terminal.all("All available commands:");
        terminal.all("");
        for(Command cmd : commands) {
            StringBuilder builder = new StringBuilder();
            for(String alias : cmd.commands()) {
                builder.append(", ").append(alias);
            }
            String commandString = builder.toString().replaceFirst(", ", "");
            terminal.all("  * §c" + commandString + "§8 » §7" + cmd.description());
        }
        terminal.all("");
        terminal.all(" ====[  T E R M I N A L  ]====");
    }
}