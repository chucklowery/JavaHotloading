package app.firstModule;

import app.interfaces.*;


@EventGateway
public class FirstGateway implements Executable {

    public String execute() {
        return "Changed the texts!";
    }
}
