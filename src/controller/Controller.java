package controller;

import common.*;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.*;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import model.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import view.*;
import signup.*;

public class Controller {

    private static final String SERVER_IP;
    private static final int SERVER_PORT;
    private ServerInterface server;
    private ClientInterface model;

    static {
        Document document = null;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse("config.xml");
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            String title = "Configuration error";
            String message = "An error occurred while loading the configuration file";
            JOptionPane.showMessageDialog(null, message);
            System.exit(0);
        }
        SERVER_IP = document.getElementsByTagName("IP").item(0).getFirstChild().getTextContent();
        SERVER_PORT = Integer.parseInt(document.getElementsByTagName("Port").item(0).getFirstChild().getTextContent());
    }

    private HashMap<String, ChatFrame> singleChatRooms = new HashMap<>();
    private Vector<ChatFrame> groupChatRooms = new Vector<>();

    // session and the key will be username in case 
    // of single chat or groupid in case of group chat
    private Contact contact;
    private Vector<Contact> friendList;
    private Vector<Group> groupList;
    private SignInFrame signIn;
    private AddContactDialog add_friend_request;
    private Vector<Contact> friendRequestList;

    private Notification status_notification;
    private MessengerFrame mainview;
    private ContactDialog signup_dialog;

    // Constructor
    public Controller() {
        try {
            signIn = new SignInFrame(this);
            signIn.setVisible(true);
            model = new Model(this);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    // Sign Up
    public void signup() {
        Contact new_contact; // contain data that user has just entered
        boolean signup_finished_successfully = false; // true if server submit sign up request successfully
        boolean signup_has_error = false; // if an error occurs in sign in dialog (i.e missed field )
        boolean ok = true;
        signup_dialog = new ContactDialog();
        while (!signup_finished_successfully && ok) {
            ok = signup_dialog.showDialog(signup_has_error);
            if (ok) {
                new_contact = signup_dialog.getContact();
                try {
                    if (createConnection()) {
                        signup_finished_successfully = server.signUp(model, new_contact);
                        if (!signup_finished_successfully) {
                            signup_has_error = true;
                        } else {
                            contact = new_contact;
                            startChatApp();
                        }
                    }
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    // Login
    public boolean login(String username, String password) {
        try {
            if (!createConnection()) {
                return true;
            }
            contact = server.signIn(model, username, password);
            if (contact != null) {
                startChatApp();
                return true;
            }
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(mainview, "Server can't be reached");
        }
        return false;
    }

    // Create Chat Frame (Single)
    public void createChatFrame(String key) {
        if (!singleChatRooms.containsKey(key)) {
            for (Contact contact : friendList) {
                if (contact.getUserName().equals(key)) {
                    singleChatRooms.put(key, new ChatFrame(this, contact));
                    break;
                }
            }
        }
        singleChatRooms.get(key).setVisible(true);
    }

    // Create Chat Frame (Group)
    public void createChatFrame(Group group) {
        if (group.getContacts().size() == 2) {
            Contact contact = group.getContacts().elementAt(0);
            if (this.contact.equals(contact)) {
                contact = group.getContacts().elementAt(1);
            }
            createChatFrame(contact.getUserName());
            return;
        }
        boolean exists = false;
        for (ChatFrame chatFrame : groupChatRooms) {
            if (chatFrame.getGroup().equals(group)) {
                chatFrame.setVisible(true);
                exists = true;
                break;
            }
        }
        if (!exists) {
            ChatFrame chatFrame = new ChatFrame(this, group);
            groupChatRooms.add(chatFrame);
            chatFrame.setVisible(true);
        }
    }

    // Display Message (Signle)
    public void DisplayMessage(String sender, String message, Font font, Color color) {
        if (sender.equals(contact.getUserName())) {
            return;
        }
        createChatFrame(sender);
        singleChatRooms.get(sender).receiveMessage(sender, message, font, color);
    }

    // Display Message (Group)
    public void DisplayMessage(String sender, Group group, String message, Font font, Color color) {
        if (sender.equals(contact.getUserName())) {
            return;
        }
        createChatFrame(group);
        for (ChatFrame chatFrame : groupChatRooms) {
            if (chatFrame.getGroup().equals(group)) {
                chatFrame.receiveMessage(sender, message, font, color);
            }
        }

    }

    // Send File
    public void sendFile(String receiver, File file, byte[] fileContent) {
        new Thread() {
            @Override
            public void run() {
                try {
                    server.sendFile(contact.getUserName(), receiver, file, fileContent);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    // Receive File
    public void receiveFile(String sender, File file, byte[] fileContent) {
        new Thread() {
            @Override
            public void run() {
                String title = "File Transfer";
                String message = sender + " is sending you a file \"" + file.getName() + "\". \n";
                message += "Do you want to save this file ?";
                int response = JOptionPane.showConfirmDialog(mainview, message, title, JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setSelectedFile(file);
                    if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                        try {
                            Files.write(Paths.get(fileChooser.getSelectedFile().getPath()), fileContent);
                        } catch (IOException ex) {
//                    ex.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    // Send Message
    public void sendMessage(String reciever, String message, Font font, Color color) {
        try {
            server.sendMessage(contact.getUserName(), reciever, message, font, color);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public void sendMessage(Group group, String message, Font font, Color color) {
        try {
            server.sendMessage(contact.getUserName(), group, message, font, color);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    // Upload File
    public void uploadFile(String reciever, File file, byte[] file_data) {
        try {
            server.sendFile(contact.getUserName(), reciever, file, file_data);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    // Status Changed
    public void statusChangedController(String userName, int status) throws RemoteException {
        for (Contact temp : friendList) {
//            System.out.println(temp.getUserName());
            if (temp.getUserName().equals(userName)) {
                temp.setStatus(Contact.Status.values()[status]);
//                contact.printData(false);
                mainview.updatelist(friendList);
                break;
            }
        }
    }

    // Receive Advertisement
    public void receiveAdvertisement(int[] image, int width, int height) throws RemoteException {
        mainview.receiveAdvertisement(image, width, height);
    }

    // Stop Service
    public void stopService() {
        for (String userName : singleChatRooms.keySet()) {
            singleChatRooms.get(userName).dispose();
        }
        for (ChatFrame chatFrame : groupChatRooms) {
            chatFrame.dispose();
        }
        mainview.dispose();
        signIn = new SignInFrame(this);
        signIn.stopService();
    }

    // Create Connection
    public boolean createConnection() throws RemoteException {
        Registry reg;
        try {
            reg = LocateRegistry.getRegistry(SERVER_IP, SERVER_PORT);
            server = (ServerInterface) reg.lookup("Chat Service");
            return true;
        } catch (RemoteException | NotBoundException ex) {
            JOptionPane.showMessageDialog(mainview, "Server can't be reached");
            return false;
        }
    }

    // Start Chat App
    public void startChatApp() {
        friendList = contact.getFriends();
        groupList = contact.getGroups();
        friendRequestList = contact.getReceivedRequests();
        mainview = new MessengerFrame(this);
        mainview.updatelist(friendList);
        mainview.updateRequestPanel(friendRequestList);
        signIn.setVisible(false);
        mainview.setVisible(true);

    }

    // Display Add Contact
    public void displayAddContact() {
        add_friend_request = new AddContactDialog(this);
    }

    // Send Friend Request
    public void sendFriendRequest(String reciever) {
        int response = 1;
        try {
            response = server.sendRequest(contact.getUserName(), reciever);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
        switch (response) {
            case ServerInterface.REQUEST_SENT_SUCCESSFULLY:
                JOptionPane.showMessageDialog(mainview, "Request was send successfully");
//                add_friend_request.setVisible(false);
                break;
            case ServerInterface.CONTACT_ALREADY_ADDED:
                JOptionPane.showMessageDialog(mainview, "You are already friend with " + reciever);
                break;
            case ServerInterface.CONTACT_DOES_NOT_EXIST:
                JOptionPane.showMessageDialog(mainview, "Contact doesn't exist");
                break;
            case ServerInterface.REQUEST_ALREADY_SENT:
                JOptionPane.showMessageDialog(mainview, "Request already sent");
                break;
        }
    }

    // Accept Friend Request
    public void acceptFriendRequest(String reciever) {
        try {
            Contact newFriend = server.confirmRequest(contact.getUserName(), reciever);
            updateLists(newFriend);// remove request sender from request list and add him in friend list

        } catch (RemoteException ex) {
            ex.printStackTrace();
        }

    }

    // Decline Friend Request
    public void declineFriendRequest(String reciever) {
        try {
            server.cancelRequest(contact.getUserName(), reciever);
            Contact temp = null; // this reference will hold a temporary object to be added to friedlist and removed from friend request list
            for (Contact friend : friendRequestList) {
                if (friend.getUserName().equals(reciever)) {
                    temp = friend;
                }
                friendRequestList.remove(temp);
                mainview.updateRequestPanel(friendRequestList);
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    // Update Lists
    private void updateLists(Contact newFriend) {
        friendList.add(newFriend);
        friendRequestList.remove(newFriend);
        mainview.updateRequestPanel(friendRequestList);
        mainview.updatelist(friendList);
    }

    // Set Status
    public void setStatus(int status) {
        try {
            server.setStatus(contact.getUserName(), status);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // Request Confirmed
    public void requestConfirmed(Contact sender) {
        friendList.add(sender);
        mainview.updatelist(friendList);
        displayPopupMessage(sender, 1); // indicate friend request was accepted
    }

    // Display Popup Message
    public void displayPopupMessage(Contact con, int message_type) {
        String message_type_1 = " you are now friend with ";
        String message_type_2 = " you have a friend request from ";

        switch (message_type) {
            case 1:
                status_notification = new Notification(mainview, con, message_type_1);
                break;
            case 2:
                status_notification = new Notification(mainview, con, message_type_2);
                break;

        }
        status_notification.setVisible(true);
    }

    // Get New Friend Request
    public void getNewFriendRequest(Contact sender) {
        friendRequestList.add(sender);
        mainview.updateRequestPanel(friendRequestList);
        displayPopupMessage(sender, 2);
    }

    // Receive System Message
    public void receiveSystemMessage(String message) {
        JOptionPane.showMessageDialog(mainview, message);
    }

    // Sign Out
    public void signout() {
        try {
            server.signOut(contact.getUserName());
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    // Get Contact
    public Contact getContact() {
        return contact;
    }

    // Main
    public static void main(String[] args) {
        new Controller();
    }

}
