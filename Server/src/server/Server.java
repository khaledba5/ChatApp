/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;


/**
 *
 * @author root
 */



public class Server extends JFrame{
    public static ArrayList<String> Messages = new ArrayList<String>();
    public static ArrayList<ClientThread> Clients = new ArrayList<ClientThread>();
    static ServerSocket server;
    /**
     * @param args the command line arguments
     */
    
    
    
   static class ClientThread extends Thread
{
    Socket Client;
    DataInputStream input;
    DataOutputStream output;
    public ClientThread(Socket Client) throws IOException
    {
        this.output = new DataOutputStream(Client.getOutputStream());
        this.input = new DataInputStream(Client.getInputStream());
        this.Client = Client;
    }
    
    
    @Override
    public void run()
    {
        try {
            while(true)
            {
                String message = input.readUTF();
                System.out.println(message);
                Messages.add(message);
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
    
    
    
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
    server = new ServerSocket(1234);
    while(true)
    {
        Socket c = server.accept();
        System.out.println("New Client Arrived");
        ClientThread T = new ClientThread(c);
        Clients.add(T);
        T.start();
        for(ClientThread client : Clients)
        {
            if(Messages.size()>0)
            {
                for(String msg : Messages)
                {
                    client.output.writeUTF(msg);
                }
            }
        }
    }
    }    
}
