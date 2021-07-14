# TerminalConsole

Simple and easy implementation of a terminal console based on JLine 3 and the SLF4J-to-JDK pipe.

Originally programmed to be used in the leven-proxy-runtime.

# Setup

The setup shows how to create an instance of `TerminalConsole` and which properties can be changed.

### ConsoleBuilder

````java
TerminalConsole console = ConsoleBuilder.builder()
       .setConsoleName("demo-console")
       .setConsolePrompt("&croot&7@&6demo-console&8: &r")
       .build();
````

### Options

* `setCommandThreads(int threads)`
    * Set total threads for command-line-interface
    * Default: `Runtime.getRuntime().availableProcessors()`
* `hideExternalLogs()`
    * Hide logs from external libraries (slf4j-jdk)
    * Default: `false`
* `hideCommandLogs()`
    * Hide logs from command-line-interface execute
    * Default: `false`
* `setConsoleName(String name)`
    * Set the name of the console-display
    * Default: `"console"`
* `initConsoleLevel(ConsoleLevel consoleLevel)`
    * Set the initial `ConsoleLevel` of console
    * Default: `ConsoleLevel.INFO`
* `setLogDirectory(String directory)`
    * Set the directory for log-files
    * Default: `"logs/"`
* `setConsolePrompt(String prompt)`
    * Set the prompt of the console
    * Default: `" &6>&r "`
* `setLogPrefix(String prefix)`
    * Set the log-format of the messages
    * Default: `"&8[&7%time% &8- &7%date%&8] &8[&b%name%&8] &8[&e%level%&8]"`
    * Placeholder: ``%time%``, ``%date%``, ``%name%``, ``%level%``
* `shutdown(Runnable runnable)`
    * Add a shutdown-hook to the stop command
    * Default: `null`
    
# ConsoleLevel

The ConsoleLevel is used to set different priorities of the messages.

### Level Order

Rows is the set `ConsoleLevel`. Columns indicate whether the `ConsoleLevel` is displayed or not.

| x = visible    |     |       |       |      |      |       |       |     |     |
|----------------|-----|-------|-------|------|------|-------|-------|-----|-----|
| `ConsoleLevel` | OFF | TRACE | DEBUG | INFO | WARN | ERROR | FATAL | ALL | EXT |
| OFF            |     |       |       |      |      |       |       |     |     |
| TRACE          |     | x     | x     | x    | x    | x     | x     | x   | x   |
| DEBUG          |     |       | x     | x    | x    | x     | x     | x   | x   |
| INFO           |     |       |       | x    | x    | x     | x     | x   | x   |
| WARN           |     |       |       |      | x    | x     | x     | x   | x   |
| ERROR          |     |       |       |      |      | x     | x     | x   | x   |
| FATAL          |     |       |       |      |      |       | x     | x   | x   |
| ALL            |     |       |       |      |      |       |       | x   | x   |
| EXT            |     |       |       |      |      |       |       |     | x   |

### External logging

Since some external libraries use logging frameworks like SLF4J, 
`TerminalConsole` uses a library from SLF4J to redirect the log messages 
directly to the JDK logger. There the message is processed according to 
the configuration and output, if allowed.

### API-Methods

Change the `ConsoleLevel` programmatically.

````java
ConsoleLevel consoleLevel = console.getLevel();
console.setLevel(ConsoleLevel consoleLevel);
````

### CLI-Command

Change the `ConsoleLevel` through the command-line-interface.

````bash
log <ConsoleLevel>
log
````

# Logging

### Base methods
````java
console.log(ConsoleLevel level, String message);
console.log(ConsoleLevel level, String message, Throwable error);
````
### Only `message`
````java
console.trace(String message);
console.debug(String message);
console.info(String message);
console.warn(String message);
console.error(String message);
console.fatal(String message);
console.all(String message);
````
### Combined `message` with `Throwable`
````java
console.trace(String message, Throwable error);
console.debug(String message, Throwable error);
console.info(String message, Throwable error);
console.warn(String message, Throwable error);
console.error(String message, Throwable error);
console.fatal(String message, Throwable error);
console.all(String message, Throwable error);
````

# Grep
TerminalConsole provides a function similar to Linux "grep" / Windows "find". 
This is called "grep" and is available both as a standard command and as an API method.

### Function
Grep filters all log messages according to the entered content and also displays only these.
**Attention: Grep also prevents logging of the messages that are not output.**

### API-Methods
Grep content programmatically.
````java
console.grep(String content);
console.ungrep();
````

### CLI-Command
Grep content through command-line-interface.
````bash
grep <content>
ungrep
````

# Stretch
Stretch solves an explicit problem, since there are messages which
have an indefinite length and thus the formatting cannot be output in a structured way.

### API-Methods
To stretch from the left-side
````java
String message = console.stretchLeft(String message, int length);
String message = console.stretchLeft(String message, int length, String stretchEnd);
````
To stretch from the right-side
````java
String message = console.stretchRight(String message, int length);
String message = console.stretchRight(String message, int length, String stretchEnd);
````

# Clear
If the `TerminalConsole` has too much output, the clear-function can be used to clear the console.
### API-Methods
Clear the console programmatically.
````java
console.clearConsole();
````

### CLI-Command
Clear the console through command-line-interface.
````bash
clear
````

# Command Line Interface

### Default Commands
To see the description of the commands, use the "help" command,
if not already explained in the documentation.
````bash
clear
grep
help
log
stop
ungrep
````

### Start
In order to use the command-line-interface, it must first be started. The method call is blocking.
````java
console.start();
````
To stop the command-line-interface execute
````bash
stop
````

### API
To create your own commands, you need to create a corresponding class for it.
Example `CommandClear`:
````java
import eu.koboo.terminal.command.Command;

public class CommandClear extends Command {

    @Override
    public String[] commands() {
        return new String[]{"cc", "clear"};
    }

    @Override
    public String description() {
        return "Clear the console";
    }

    @Override
    public void execute(String command, String[] args) {
        terminal.clearConsole();
        terminal.all("The console was cleared successfully!");
    }
}
````

### Register
After a command is created, it can be registered as follows.
````java
console.register(new CommandClear());
````
This call can also be executed during runtime.