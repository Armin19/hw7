package ir.aut.ac.ceit;

import java.io.*;
import javax.swing.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

public class Client extends JFrame {
    private JTextField enterField;
    private JTextArea displayArea;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String chatServer;
    private Socket client;

    public Client(String host) {
        super("Client");
        chatServer = host;
        Container container = getContentPane();
        enterField = new JTextField();
        enterField.setEnabled(false);
        enterField.addActionListener(
                new ActionListener() {
                    @Override
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

    public void runClient() {
        try {
            connectToServer();
            getStreams();
            processConnection();
            closeConnection();
        } catch (EOFException eofException) {
            System.out.println("Server terminated connection");
        } catch (IOException ioExeption) {
            ioExeption.printStackTrace();
        }
    }

    private void getStreams() throws IOException {
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();
        input = new ObjectInputStream(client.getInputStream());
        displayArea.append("\nGot I/O streams\n");
    }

    private void connectToServer() throws IOException {
        displayArea.setText("Attempting connection\n");
        client = new Socket(InetAddress.getByName(chatServer), 5000);
        displayArea.append("Connected to: " + client.getInetAddress().getHostName());
    }

    private void processConnection() throws IOException {
        enterField.setEnabled(true);
        do {
            try {
                message = (String) input.readObject();
                displayArea.append("\n" + message);
                displayArea.setCaretPosition(displayArea.getText().length());
            } catch (ClassNotFoundException classNotFoundException) {
                displayArea.append("\nUnknown object type received");
            }
        } while (!message.equals("SERVER>>> TERMINATE"));
    }

    private void closeConnection() throws IOException {
        displayArea.append("\nClosing connection");
        output.close();
        input.close();
        client.close();
    }

    private void sendData(String message) {
        try {
            output.writeObject("CLIENT>>> " + message);
            output.flush();
            displayArea.append("\nCLIENT" + message);
        } catch (IOException ioException) {
            displayArea.append("\nError writing object");
        }
    }

}
