package model;

import java.rmi.*;
import java.rmi.server.*;
import controller.Controller;
import common.*;
import java.awt.Color;
import java.awt.Font;
import java.io.File;

public class Model extends UnicastRemoteObject implements ClientInterface {

    Controller controller;

    // Constructor
    public Model(Controller controller) throws RemoteException {
        this.controller = controller;
    }

    @Override
    public void statusChanged(String userName, int status) throws RemoteException {
        controller.statusChangedController(userName, status);
    }

    @Override
    public void receiveRequest(Contact sender) throws RemoteException {
        controller.getNewFriendRequest(sender);
    }

    @Override
    public void stopService() throws RemoteException {
        controller.stopService();
    }

    @Override
    public void addToGroup(Group group) throws RemoteException {
//        controller.addToGroup(group);
    }

    @Override
    public void receiveMessage(String sender, String message, Font font, Color color) throws RemoteException {
        controller.DisplayMessage(sender, message, font, color);
    }

    @Override
    public void receiveMessage(String sender, Group group, String message, Font font, Color color) throws RemoteException {
        controller.DisplayMessage(sender, group, message, font, color);
    }

    @Override
    public void receiveFile(String sender, File file, byte[] fileContent) throws RemoteException {
        controller.receiveFile(sender, file, fileContent);
    }

    @Override
    public void receiveFile(String sender, int groupID, File file, byte[] fileContent) throws RemoteException {
    }

    @Override
    public void requestConfirmed(Contact sender) throws RemoteException {
        controller.requestConfirmed(sender);
    }

    @Override
    public void ping() throws RemoteException {
    }

    @Override
    public void receiveSystemMessage(String systemMessage) throws RemoteException {
        controller.receiveSystemMessage(systemMessage);
    }

    @Override
    public void receiveAdvertisement(int[] image, int width, int height) throws RemoteException {
        controller.receiveAdvertisement(image, width, height);
    }

}
