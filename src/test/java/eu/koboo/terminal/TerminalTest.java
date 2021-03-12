package eu.koboo.terminal;


import eu.koboo.terminal.util.ConsoleLevel;

public class TerminalTest {

    public static void main(String[] args) {
        TerminalConsole console = ConsoleBuilder.builder()
                .setCLIThreads(6)
                .hideExternal()
                .hideExecute()
                .setConsoleName("test")
                .initConsoleLevel(ConsoleLevel.DEBUG)
                .setLogDirectory("logs/")
                .setConsolePrompt(" &2->&7|&5>:&r ")
                .build();
        String[] names = new String[]{
                "Test", "Emily", "Emma", "Isabella", "Jeff", "Tom", "Madeleine", "Michelle",
                "Walterschaaaaael", "LEvenProxyByPassMechanismusSucher", "Webportalknopfquellcode"
        };
        for (String name : names) {
            String left = console.stretchLeft(name, 20, "|");
            console.info(left);
        }
        for (String name : names) {
            String left = console.stretchRight(name, 20, "|");
            console.info(left);
        }
        console.start();
    }
}