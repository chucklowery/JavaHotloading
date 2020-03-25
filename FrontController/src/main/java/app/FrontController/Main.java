package app.FrontController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

public class Main {
    private static Thread interfaceThread;
    private static Thread environmentThread;

    public static void main(String[] args) throws InterruptedException {
        Environment environment = new Environment();
        environment.loadEnvironment();

        environmentThread = new Thread(environment);
        environmentThread.start();

        CommandInterface commandInterface = new CommandInterface(System.in, System.out, environment);


        interfaceThread = new Thread(commandInterface);
        interfaceThread.start();

        interfaceThread.join();
        environmentThread.join();
    }

}
