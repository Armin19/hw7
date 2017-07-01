package ir.aut.ac.ceit;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends JFrame {
    private JTextField enterField;
    private JTextArea displayArea;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    private int counter = 1;

    public Server() {
        super("Server");
        Container container = getContentPane();
        enterField = new JTextField();
        enterField.setEnabled(false);
        enterField.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        sendData(event.getActionCommand());
                    }
                });
        container.add(enterField, BorderLayout.NORTH);
        displayArea = new JTextArea();
        container.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        setSize(300, 150);
        setVisible(true);
    }

    public void runServer() {
        try {
            server = new ServerSocket(5000, 100);
            while (true) {
                waitForConnection();
                getStreams();
                processConnection();
                closeConnection();
                ++counter;
            }
        } catch (EOFException eofException) {
            System.out.println("Client terminated connection");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    private void waitForConnection() throws IOException {
        displayArea.setText("Waiting for connection\n");
        connection = server.accept();
        displayArea.append("Connection" + counter + "received from: " + connection.getInetAddress().getHostName());
    }

    private void getStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        displayArea.append("\nGot I/O streams\n");
    }

    private void processConnection() throws IOException {
        String message = "SREVER>>> Connection successful";
        output.writeObject(message);
        output.flush();
        enterField.setEnabled(true);
        do {
            try {
                message = (String) input.readObject();
                displayArea.append("\n" + message);
                displayArea.setCaretPosition(displayArea.getText().length());
            } catch (ClassNotFoundException classNotFoundException) {
                displayArea.append("\nUnknown object type received");
            }
        } while (!message.equals("CLIENT>>> TERMINATE"));
    }

    private void closeConnection() throws IOException {
        displayArea.append("\nUser terminated connection");
        enterField.setEnabled(false);
        output.close();
        input.close();
        connection.close();
    }

    private void sendData(String message) {
        try {
            output.writeObject("SERVER>>> " + message);
            output.flush();
            displayArea.append("\nSERVER>>> " + message);
        } catch (IOException ioException) {
            displayArea.append("\nError writing object");

        }
    }

}
