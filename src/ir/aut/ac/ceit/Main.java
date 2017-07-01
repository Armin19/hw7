package ir.aut.ac.ceit;


public class Main {
    ClientThread clientThread = new ClientThread();
    ServerThread serverThread = new ServerThread();
    clientThread.start();
    serverThread.start();
}
