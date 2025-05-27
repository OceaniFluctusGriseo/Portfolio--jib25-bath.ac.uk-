import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;


/**
 * The ChatClient class encompasses the client side of the chat server, one object instantiated per client
 * Has various properties such as the users socket and buffers to read input as well as nested classes
 * which provide threads for reading and sending messages to and from the server
 */
public class ChatClient {

    //store basic properties of chat client:
    //1) The socket the client communicates from, where the program listens to for messages
    //2) Buffered readers for user input and server responses
    //3) A print-writer object to write messages to the server
    private Socket socket;
    private String input;
    private BufferedReader userInputBuffer = new BufferedReader(new InputStreamReader(System.in));
    private BufferedReader serverInputBuffer;
    private PrintWriter writeToServer;
    private ChatClientGUI chatClientGUI;

    //store default IP to use and port to connect to
    private String IPaddress = "localhost";
    private int port = 14001;

    //store a username for each user that will precede all of their messages
    private String username = "";

    public ChatClient(String[] args){
        chatClientGUI = new ChatClientGUI();
        handleCommandArgs(args);
        setUpProperties();
    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient(args);
        chatClient.run();
    }

    /**
     * method to handle command line arguments
     *
     * @param args The command line arguments entered at runtime of program
     */
    private void handleCommandArgs(String[] args){
        for(int arg = 0; arg < args.length; arg++){
            //if -ccp argument detected, to use custom port
            if(args[arg].compareTo("-ccp") == 0 && (arg+1)<args.length){
                try {
                    System.out.println("Using port: " + args[arg+1]);
                    //try to convert port into integer version of argument
                    port = Integer.parseInt(args[arg + 1]);
                }
                catch(Exception e){
                    //if cannot convert, resort to default port of 14001
                    System.out.println("Error using custom port, resorting to default");
                }
            }
            //if -cca argument detected, to use custom IP address
            if(args[arg].compareTo("-cca") == 0 && (arg+1)<args.length){
                System.out.println("Using IP address: " + args[arg+1]);
                IPaddress = args[arg + 1];
            }
        }
    }

    /**
     * Simple method that assigns the basic properties of the client, wrapped in a try-catch
     */
    private void setUpProperties(){
        try {
            socket = new Socket(IPaddress, port);
            serverInputBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writeToServer = new PrintWriter(socket.getOutputStream(),true);
        }
        catch(IOException e){
            System.out.println("An error occurred while setting up socket");
        }

    }


    /**
     * Method that initialises client functionality. Gets a user to enter name to appear under
     * then instantiates send and receive threads to send messages and read messages respectively.
     */
    private void run(){
        //get the user to enter a username
        //create threads for sending and receiving messages
        new SendThread().start();
        new ReceiveThread().start();

    }

    /**
     * SendThread is an extension of Thread and waits for user input and sends the input to server, until the
     * user enters 'QUIT'
     */
    private class SendThread extends Thread{

        /**
         * Overrides Thread's run method to perform required functionality:
         * Until the user enters 'QUIT' wait for user input and send to server
         */
        public void run(){
            try{

                //get input from the user
                input = userInputBuffer.readLine();
                //unless the user enters "QUIT", write the message to the server, and enter next line
                while(input.compareTo("QUIT") != 0){
                    writeToServer.println(username + ": " + input);
                    input = userInputBuffer.readLine();
                }
                writeToServer.println(username + " has left the chat server :(");

            }
            //catch any IO exceptions that may occur during sending/receiving messages
            catch(IOException e){
                chatClientGUI.writeMessage("An error occurred while sending message");
            }
            //catch any null pointers where objects have not been assigned
            catch(NullPointerException e){
                chatClientGUI.writeMessage("A null pointer exception occurred");
            }

            //when the user enters 'QUIT' safely close connection
            finally {
                try {
                    chatClientGUI.writeMessage("Closing connection to server");

                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    /**
     * ReceiveThread class is an extension of Thread and listens for and prints messages from server
     */
    private class ReceiveThread extends Thread{

        private String response = "";

        /**
         * Overrides Thread's run method to perform required functionality:
         * While the socket's connection is open, continuously checks for messages from server
         * and prints them to client's terminal
         */
        public void run(){
            //while the socket is connected to the server (i.e. end thread when no longer connected)
            while(!socket.isClosed() && response != null) {
                try {
                    //read the next message sent from the server and print
                    response = serverInputBuffer.readLine();
                    if(response != null){chatClientGUI.writeMessage(response);}
                } catch (IOException e) {
                    chatClientGUI.writeMessage("Message(s) cannot be received");
                }
            }
        }
    }

    /**
     * ChatClientGUI class creates and operates a GUI for the client to interact with.
     * They enter text in the text field and click the 'SEND' button to send that message to the server
     * The class is called to display a new message on the GUI when received from the server
     */
    private class ChatClientGUI{
        //store components privately before instantiation
        private JFrame window;
        private JTextField userInputTextField;
        private JButton sendButton;
        private JTextArea messageArea;
        private JLabel infoLabel;
        private JScrollPane messageAreaScrollPane;
        private JPanel messagesPanel;
        private JPanel textEnterPanel;

        public ChatClientGUI(){
            //instantiate components
            window = new JFrame("Chat Server");
            userInputTextField = new  JTextField("");
            sendButton = new JButton("SEND");
            userInputTextField.setPreferredSize(new Dimension(300,30));
            sendButton.setBounds(50,100,95,30);

            //create an action listener for 'send' button press, to send entered text to server
            sendButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(userInputTextField.getText().compareTo("QUIT") == 0){System.exit(0);}
                    writeToServer.println(username + ": " + userInputTextField.getText());
                    userInputTextField.setText("");
                }
            });

            //instantiate area where messages appear
            messageArea = new JTextArea(15,30);
            messageArea.setPreferredSize(new Dimension(400,300));

            //get a nickname for the user before window appears in an option dialogue prompt
            username = JOptionPane.showInputDialog(window, "Enter a nickname to use: ", null);

            //tell the user how to use the GUI
            infoLabel = new JLabel("Enter messages here, type 'QUIT' to exit");
            infoLabel.setHorizontalAlignment(JLabel.CENTER);

            //make the message area scrollable to view multiple messages and make it non-editable
            messageAreaScrollPane = new JScrollPane(messageArea);
            messageArea.setEditable(false);

            //set up the layout of the client GUI
            messagesPanel = new JPanel();
            textEnterPanel = new JPanel();
            textEnterPanel.setPreferredSize(new Dimension(400, 50));
            window.setLayout(new BorderLayout());
            window.add(messagesPanel,BorderLayout.PAGE_START); window.add(infoLabel); window.add(textEnterPanel,BorderLayout.PAGE_END);

            window.setVisible(true);
            window.setSize(400,400);

            messagesPanel.add(messageAreaScrollPane);

            textEnterPanel.add(userInputTextField);
            textEnterPanel.add(sendButton);


        }

        //method the client class uses to send messages onto the message area
        protected void writeMessage(String input){
            messageArea.append(input + "\n");
        }
    }
}
