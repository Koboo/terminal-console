package eu.koboo.terminal;

import eu.koboo.terminal.defaults.*;
import eu.koboo.terminal.util.ConsoleColor;
import eu.koboo.terminal.util.ConsoleLevel;

public class ConsoleBuilder {

    private int commandThreads;
    private boolean showExecute;
    private boolean showExternal;
    private String appName;
    private ConsoleLevel initialLevel;
    private String logDirectory;
    private String prompt;
    private String logPrefix;
    private Runnable shutdownHook;

    private ConsoleBuilder() {
        this.commandThreads = Runtime.getRuntime().availableProcessors();
        this.showExecute = true;
        this.showExternal = true;
        this.appName = "console";
        this.initialLevel = ConsoleLevel.INFO;
        this.logDirectory = "logs/";
        this.logPrefix = "&8[&7%time% &8- &7%date%&8] &8[&b%name%&8] &8[&e%level%&8]";
        this.prompt = ConsoleColor.parseColor(" &6> &r");
    }

    public ConsoleBuilder setCommandThreads(int threads) {
        this.commandThreads = threads;
        return this;
    }

    public ConsoleBuilder hideExternalLogs() {
        this.showExternal = false;
        return this;
    }

    public ConsoleBuilder hideCommandLog() {
        this.showExecute = false;
        return this;
    }

    public ConsoleBuilder setConsoleName(String name) {
        this.appName = name;
        return this;
    }

    public ConsoleBuilder initConsoleLevel(ConsoleLevel consoleLevel) {
        this.initialLevel = consoleLevel;
        return this;
    }

    public ConsoleBuilder setLogDirectory(String logDirectory) {
        this.logDirectory = logDirectory;
        return this;
    }

    public ConsoleBuilder setConsolePrompt(String prompt) {
        this.prompt = ConsoleColor.parseColor(prompt);
        return this;
    }

    public ConsoleBuilder setLogPrefix(String logPrefix) {
        this.logPrefix = logPrefix;
        return this;
    }

    public ConsoleBuilder shutdown(Runnable runnable) {
        shutdownHook = runnable;
        return this;
    }

    public TerminalConsole build() {
        TerminalConsole console = new TerminalConsole(this);
        console.register(new CommandClear());
        console.register(new CommandGrep());
        console.register(new CommandHelp());
        console.register(new CommandLog());
        console.register(new CommandStop());
        console.register(new CommandUngrep());
        return console;
    }

    protected int getCommandThreads() {
        return commandThreads;
    }

    protected boolean isShowingCommandLogs() {
        return showExecute;
    }

    protected boolean isShowingExternalLogs() {
        return showExternal;
    }

    protected String getConsoleName() {
        return appName;
    }

    protected ConsoleLevel getInitialConsoleLevel() {
        return initialLevel;
    }

    protected String getLogDirectory() {
        return logDirectory;
    }

    protected String getConsolePrompt() {
        return prompt;
    }

    protected String getLogPrefix() {
        return logPrefix;
    }

    protected Runnable getShutdownHook() {
        return shutdownHook;
    }

    public static ConsoleBuilder builder() {
        return new ConsoleBuilder();
    }
}
