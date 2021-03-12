package eu.koboo.terminal;

import eu.koboo.terminal.defaults.*;
import eu.koboo.terminal.util.ConsoleColor;
import eu.koboo.terminal.util.ConsoleLevel;

public class ConsoleBuilder {

    int cliThreads;
    boolean showExecute;
    boolean showExternal;

    String appName;
    ConsoleLevel initialLevel;

    String logDirectory;

    String prompt;

    private ConsoleBuilder() {
        this.cliThreads = Runtime.getRuntime().availableProcessors();
        this.showExecute = true;
        this.showExternal = true;
        this.appName = "console";
        this.initialLevel = ConsoleLevel.INFO;
        this.logDirectory = "logs/";
        this.prompt = ConsoleColor.parseColor(" &6> &r");
    }

    public ConsoleBuilder setCLIThreads(int threads) {
        this.cliThreads = threads;
        return this;
    }

    public ConsoleBuilder hideExternal() {
        this.showExternal = false;
        return this;
    }

    public ConsoleBuilder hideExecute() {
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

    public TerminalConsole build() {
        TerminalConsole console = new TerminalConsole(this);
        console.register(new CommandClear());
        console.register(new CommandGrep());
        console.register(new CommandHelp());
        console.register(new CommandLog());
        console.register(new CommandStop());
        console.register(new CommandUngrep());
        return new TerminalConsole(this);
    }

    public int getCliThreads() {
        return cliThreads;
    }

    public boolean isShowExecute() {
        return showExecute;
    }

    public boolean isShowExternal() {
        return showExternal;
    }

    public String getAppName() {
        return appName;
    }

    public ConsoleLevel getInitialLevel() {
        return initialLevel;
    }

    public String getLogDirectory() {
        return logDirectory;
    }

    public String getPrompt() {
        return prompt;
    }

    public static ConsoleBuilder builder() {
        return new ConsoleBuilder();
    }
}
