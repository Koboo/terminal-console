package eu.koboo.terminal;

import eu.koboo.terminal.command.Command;
import eu.koboo.terminal.util.ConsoleColor;
import eu.koboo.terminal.util.ConsoleFormatter;
import eu.koboo.terminal.util.ConsoleLevel;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.*;

public class TerminalConsole {

    private static TerminalConsole INSTANCE;

    private final Logger logger = Logger.getLogger(TerminalConsole.class.getName());

    private final ConsoleBuilder consoleBuilder;
    private final ExecutorService commandExecutor;
    private final AtomicReference<Boolean> active;
    private final HashMap<String, Command> commandRegistry;

    private ConsoleLevel consoleLevel;
    private Terminal terminal;
    private LineReader lineReader;
    private String contentGrep;

    protected TerminalConsole(ConsoleBuilder consoleBuilder) {
        INSTANCE = this;

        this.consoleBuilder = consoleBuilder;
        this.commandExecutor = Executors.newFixedThreadPool(consoleBuilder.getCliThreads());
        this.consoleLevel = consoleBuilder.getInitialLevel();
        this.active = new AtomicReference<>(true);
        this.commandRegistry = new HashMap<>();
        this.contentGrep = null;

        File logDir = new File(consoleBuilder.getLogDirectory());
        if (!logDir.exists())
            logDir.mkdirs();
        int count = 1;
        String date = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
        File[] files = logDir.listFiles();
        if (files != null)
            for (File file : files)
                if (file.getName().startsWith(date))
                    count += 1;
        String logFile = logDir.getPath() + "/" + date + "-" + count + ".log";
        try {
            FileHandler fileHandler = new FileHandler(logFile);
            logger.setUseParentHandlers(false);
            fileHandler.setFormatter(new ConsoleFormatter());
            logger.addHandler(fileHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            this.terminal = TerminalBuilder
                    .builder()
                    .system(true)
                    .build();
            this.lineReader = LineReaderBuilder
                    .builder()
                    .terminal(this.terminal)
                    .variable(LineReader.SECONDARY_PROMPT_PATTERN, consoleBuilder.getPrompt())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setFormatter(new ConsoleFormatter());

        Handler handler = new Handler() {
            @Override
            public void publish(LogRecord record) {
                if (consoleBuilder.isShowExternal()) {
                    if (record.getThrown() != null)
                        log(ConsoleLevel.EXT, record.getMessage(), record.getThrown());
                    else
                        log(ConsoleLevel.EXT, record.getMessage());
                }
            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        };

        Logger root = Logger.getLogger("");
        root.setUseParentHandlers(false);
        Arrays.stream(root.getHandlers()).forEach(root::removeHandler);
        root.addHandler(handler);

    }

    public void register(Command command) {
        for (String cmd : command.commands()) {
            commandRegistry.put(cmd.toLowerCase(), command);
        }
    }

    public List<Command> getCommandRegistry() {
        Map<String, Command> commandMap = new HashMap<>();
        for (Command commands : commandRegistry.values()) {
            if (!commandMap.containsKey(commands.getClass().getName())) {
                commandMap.put(commands.getClass().getName(), commands);
            }
        }
        return new ArrayList<>(commandMap.values());
    }

    public void start() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> active.set(false)));
            while (active.get() && !Thread.currentThread().isInterrupted()) {
                if (lineReader != null) {
                    String line = lineReader.readLine(consoleBuilder.getPrompt());
                    line = line.trim();
                    if (!executeCLICommand(line)) {
                        all("No command registered: '" + line + "'! Type 'help' for help.");
                    }
                }
            }
        } catch (Exception e) {
            all("Error while console reading: ", e);
        }
    }

    private boolean executeCLICommand(String input) {
        try {
            String[] args = input.split(" ");
            String inputCommand = args[0].toLowerCase();
            args = Arrays.copyOfRange(args, 1, args.length);
            Command command = commandRegistry.getOrDefault(inputCommand, null);
            if (command != null) {
                String[] finalArgs = args;
                if (consoleBuilder.isShowExecute())
                    all("cli-execute <> '" + input + "'");
                commandExecutor.execute(() -> command.execute(inputCommand, finalArgs));
                return true;
            }
        } catch (Exception e) {
            all("Error while execute cmd '" + input + "'", e);
        }
        return false;
    }

    private boolean shouldPrint(ConsoleLevel consoleLevel, String message) {
        if (this.consoleLevel == ConsoleLevel.OFF)
            return false;
        if (consoleLevel == ConsoleLevel.ALL)
            return true;
        if (contentGrep != null)
            return message.contains(contentGrep);
        return this.consoleLevel.level() <= consoleLevel.level();
    }

    public void clearConsole() {
        for (int i = 0; i < 100; i++) {
            if (terminal == null) {
                System.out.println("");
                return;
            }
            if (lineReader == null) {
                terminal.writer().println("");
                terminal.flush();
                return;
            }
            lineReader.printAbove("");
        }
    }

    public void grep(String content) {
        this.contentGrep = content;
    }

    public void ungrep() {
        this.contentGrep = null;
    }

    public void log(ConsoleLevel consoleLevel, String message, Throwable t) {
        if (shouldPrint(consoleLevel, message)) {
            if (t != null) {
                StringWriter sw = new StringWriter();
                t.printStackTrace(new PrintWriter(sw));
                String errorString = sw.toString();
                message = message + ": " + errorString;
            }

            final long current = System.currentTimeMillis();
            String logTimeString = new SimpleDateFormat("HH:mm:ss").format(current);
            String logDateString = new SimpleDateFormat("yyyy-MM-dd").format(current);
            message = "&8[&7" + logDateString + " &8- &7" + logTimeString + "&8] " +
                    "&8[&b" + this.consoleBuilder.getAppName() + "&8] " +
                    "[&6" + consoleLevel.name().toLowerCase() + "&8] &r" +
                    message + "&r" + System.lineSeparator();
            logger.info(ConsoleColor.removeColor(message));
            message = ConsoleColor.parseColor(message);
            if (terminal == null) {
                System.out.println(message);
                return;
            }
            if (lineReader == null) {
                terminal.writer().println(message);
                terminal.flush();
                return;
            }
            lineReader.printAbove(message);
        }
    }

    public void log(ConsoleLevel consoleLevel, String message) {
        log(consoleLevel, message, null);
    }

    public void trace(String message) {
        log(ConsoleLevel.TRACE, message);
    }

    public void debug(String message) {
        log(ConsoleLevel.DEBUG, message);
    }

    public void info(String message) {
        log(ConsoleLevel.INFO, message);
    }

    public void warn(String message) {
        log(ConsoleLevel.WARN, message);
    }

    public void error(String message) {
        log(ConsoleLevel.ERROR, message);
    }

    public void fatal(String message) {
        log(ConsoleLevel.FATAL, message);
    }

    public void all(String message) {
        log(ConsoleLevel.ALL, message);
    }


    public void trace(String message, Throwable e) {
        log(ConsoleLevel.TRACE, message, e);
    }

    public void debug(String message, Throwable e) {
        log(ConsoleLevel.DEBUG, message, e);
    }

    public void info(String message, Throwable e) {
        log(ConsoleLevel.INFO, message, e);
    }

    public void warn(String message, Throwable e) {
        log(ConsoleLevel.WARN, message, e);
    }

    public void error(String message, Throwable e) {
        log(ConsoleLevel.ERROR, message, e);
    }

    public void fatal(String message, Throwable e) {
        log(ConsoleLevel.FATAL, message, e);
    }

    public void all(String message, Throwable e) {
        log(ConsoleLevel.ALL, message, e);
    }


    public ConsoleLevel getLevel() {
        return consoleLevel;
    }

    public void setLevel(ConsoleLevel consoleLevel) {
        this.consoleLevel = consoleLevel;
    }

    public String stretchLeft(String message, int length, String stretchEnd) {
        if (message.length() < length)
            for (int i = message.length(); i < length; i++) {
                message = message + " ";
            }

        else
            for (int i = message.length(); i > length; i--) {
                message = message.substring(0, message.length() - 1);
            }
        return message + (stretchEnd != null ? stretchEnd : "");
    }

    public String stretchLeft(String message, int length) {
        return stretchLeft(message, length, null);
    }

    public String stretchRight(String message, int length, String stretchEnd) {
        if (message.length() < length)
            for (int i = message.length(); i < length; i++) {
                message = " " + message;
            }
        else
            for (int i = message.length(); i > length; i--) {
                message = message.substring(1);
            }
        return message + (stretchEnd != null ? stretchEnd : "");
    }

    public String stretchRight(String message, int length) {
        return stretchRight(message, length, null);
    }

    public static TerminalConsole getConsole() {
        return INSTANCE;
    }

}
