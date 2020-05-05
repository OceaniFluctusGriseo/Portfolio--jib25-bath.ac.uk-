//Client and Server built upon Dr Michael Wright's echo server example
//no other accreditation

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.Thread;
import java.util.*;
import javax.swing.*;
import java.awt.*;

/**
 * ChatServer class provides the functionality of the server:
 * i.e. setting up clients and their new threads and echoing
 * messages to other clients
 */
public class ChatServer {
    //set up a ServerSocket object, do not instantiate yet as need to encase in try-catch
    private ServerSocket chatServerSocket = null;
    //set up a Socket that will become any new connecting client's socket
    private Socket clientSocket = null;
    //store port number of serverSocket, default 14001
    private static int port = 14001;
    //create a list of all current clients' sockets
    private ArrayList<Socket> clients = new ArrayList(){};
    //store the server-side GUI
    private ChatServerGUI chatServerGUI;

    //store a string to hold the admins inputs and a stream reader to read their inputs from command terminal
    private String adminInput = "";
    private BufferedReader adminInputReader = new BufferedReader(new InputStreamReader(System.in));

    public ChatServer(String[] args){
        sortCommandLineArgs(args);
        //attempt to instantiate ServerSocket object with port 14001 as default
        try{
            chatServerSocket = new ServerSocket(port);
        }
        //catch an IO exception caused by initiating the server
        catch(IOException e){
            e.printStackTrace();
            chatServerGUI.writeMessage("An error occurred while trying to set up a server socket");
        }
    }

    /**
     * run method provides main functionality of the server:
     * - creates a new connection thread to connect any new clients
     * - repeatedly takes server user input until they enter EXIT
     */
    private void run(){
        //instantiate server-side GUI
        chatServerGUI = new ChatServerGUI();

        //Continuously connect new clients until 'Exit' command initiated
        new ConnectionThread().start();

        //Welcome admin user to server control
        chatServerGUI.writeMessage("Welcome server admin! ServerSocket localPort: " + chatServerSocket.getLocalPort() +"\n Click 'EXIT' to close server");
        try {
            //until the admin enters 'EXIT'....
            while (adminInput.compareTo("EXIT") != 0) {
                //.... wait for admin input
                adminInput = adminInputReader.readLine();
            }
        }
        catch(IOException e){
            chatServerGUI.writeMessage("An error occurred while reading admin input");
        }
        //close down the server and clients upon closure
        finally{
            //tell all clients the server is cutting connection
            sendMessageToAll("Server is closing connection :(");
            //close all clients' socket connections
            for(Socket clientSocket : clients){
                try{clientSocket.close();}catch(IOException e){chatServerGUI.writeMessage("An error occurred while trying to close a client connection");}
            }
        }
    }

    /**
     * Method to repeat a message received from one client to all others and client itself
     */
    private synchronized void sendMessageToAll(String input){
        //for each socket (client) currently connected...
        for(Socket clientSocket : clients) {
            try {
                //...write to the socket the received message
                new PrintWriter(clientSocket.getOutputStream(), true).println(input);

            }
            catch(IOException e){
                chatServerGUI.writeMessage("An error occurred while sending a message");
            }
        }
    }

    //main method (what is run first when the program is executed)
    public static void main(String[] args) {
        ChatServer Server = new ChatServer(args);

        Server.run();
    }

    /**
     * method to change port address if command line argument entered
     */
    private void sortCommandLineArgs(String[] args){
        for(int arg = 0; arg < args.length; arg++){
            //if -cps argument detected
            if(args[arg].compareTo("-csp") != 0 && (arg+1)<args.length){
                try {
                    chatServerGUI.writeMessage("Using port: " + args[arg+1]);
                    //try to convert port into integer version of argument
                    port = Integer.parseInt(args[arg + 1]);
                }
                catch(Exception e){
                    //if cannot convert, resort to default port of 14001
                    chatServerGUI.writeMessage("Error using custom port, resorting to default");
                }
            }
        }
    }

    /**
     * ClientThread class which extends Thread is set up for each connected client and will constantly
     * read for new input from client and call the server to
     */
    private class ClientThread extends Thread{
        //this is the individual client's socket
        //needs to be protected because superclass may need to access this value
        protected Socket clientSocket = null;
        private String input;
        public BufferedReader readInFromClient;
        public PrintWriter writeToClient;


        //constructor assigns value of passed socket to this client's thread
        public ClientThread(Socket clientSocket){
            this.clientSocket = clientSocket;
        }

        /**
         * Run method called when the thread is executed
         */
        public void run(){
            try{
                //for each new client, create buffered reader for reading from server and print writer for writing to server
                readInFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writeToClient = new PrintWriter(clientSocket.getOutputStream(),true);
                while(true){
                    //if a message is received from client, repeat to all others and self with sendMessageToAll function
                    input = readInFromClient.readLine();
                    sendMessageToAll(input);
                    chatServerGUI.writeMessage(input);
                }
            }
            catch(Exception e){
                //client closes connection

            }
            finally {
                //write to server that a client has closed connection
                chatServerGUI.writeMessage("Client closing on port: " + clientSocket.getPort());
                //remove clients' socket from list of sockets
                clients.remove(clientSocket);

            }
        }
    }

    /**
     * ConnectionThread class is an extension of Thread and is always listening for new
     * socket connections from new clients and creates a new client thread for them
     */
    private class ConnectionThread extends Thread{
        public ConnectionThread(){}

        /**
         * Run method called when the thread is executed
         */
        public void run(){
            while(true){
                //try to connect an incoming client to a socket
                try {
                    clientSocket = chatServerSocket.accept();
                    chatServerGUI.writeMessage("Connecting client on port: " + clientSocket.getPort());
                    //create a new thread for any new client threads and run it
                    ClientThread ct = new ClientThread(clientSocket);
                    //add socket to list of connected sockets
                    clients.add(clientSocket);
                    ct.start();
                }
                //catch any IO exceptions that may happen during waiting for a client
                catch(IOException e){
                    chatServerGUI.writeMessage("An IO error occurred while trying to connect a client: " + e);
                }
            }
        }
    }

    /**
     * ChatServerGUI is a class that creates and operates a GUI for the server admin to interact with.
     */
    private class ChatServerGUI{
        //store components privately
        private JFrame window;
        private JTextArea messageArea;
        private JScrollPane messageAreaScrollPane;
        private JButton exitButton;

        public ChatServerGUI(){
            //instantiate components
            window = new JFrame("Chat Server");

            messageArea = new JTextArea(15,30);
            messageArea.setPreferredSize(new Dimension(400,300));
            messageAreaScrollPane = new JScrollPane(messageArea);
            messageArea.setEditable(false);

            //create exit button that closes server upon click
            exitButton = new JButton("EXIT");
            exitButton.setPreferredSize(new Dimension(100,50));
            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendMessageToAll("Server is closing connection :(");
                    System.exit(0);
                }
            });

            //set up layout and visibility of server-side GUI
            window.setLayout(new BorderLayout());
            window.add(messageAreaScrollPane,BorderLayout.PAGE_START); window.add(exitButton);
            window.setVisible(true);
            window.setSize(400,400);
        }

        //method the server class uses to send messages onto the message area
        protected void writeMessage(String input){
            messageArea.append(input + "\n");
        }
    }
}