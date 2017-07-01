package ir.aut.ac.ceit;


import javax.swing.*;

public class ServerThread extends Thread {
    public void run() {
        Server application = new Server();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.runServer();
    }
}
