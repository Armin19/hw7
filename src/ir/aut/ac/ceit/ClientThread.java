package ir.aut.ac.ceit;


import javax.swing.*;

public class ClientThread extends Thread {
    public void run() {
        Client application;
        if (args.length == 0) application = new Client("127.0.0.1");
        else application = new Client(args[0]);
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.runClient();
    }

}
