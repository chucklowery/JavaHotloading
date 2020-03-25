package app.FrontController;

import app.interfaces.Executable;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

public class CommandInterface implements Runnable {

    private final BufferedReader in;
    private final PrintStream out;
    private final Environment environment;

    public CommandInterface(InputStream in, PrintStream out, Environment environment) {
        this.in = new BufferedReader(new InputStreamReader(in));
        this.out = out;

        this.environment = environment;
    }

    @Override
    public void run() {
        for(;;) {
            try {
                String line = in.readLine();
                if(line.equals("exit")) {
                    environment.stop();
                    break;
                }

                if(line.equals("call")) {
                    callIt();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void callIt() {
        try {
            Executable exec =  environment.create("app.firstModule.FirstGateway");

            out.println(exec.execute());
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Yikes, something went wrong!");
        }
    }
}
