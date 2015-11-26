/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
   private JTextField userText;
   private JTextArea chatWindow;
   private ObjectInputStream input;
   private ObjectOutputStream output;
   private ServerSocket server;
   private Socket connection;
   
   public Server()
   {
       super("Instant Messaging");
       userText = new JTextField();
       userText.setEditable(false);         //Prevent Typing Message Until Anyone Connect
       userText.addActionListener(new ActionListener() 
       {
           @Override
           //This Method is invoked When We Press Enter
           public void actionPerformed(ActionEvent e) 
           {
               sendMessage(e.getActionCommand());
               userText.setText("");        //Empty Text Field After Sending a Message
           }
       });
       add(userText,BorderLayout.NORTH);
       
       chatWindow = new JTextArea();
       add(new JScrollPane(chatWindow));
       setSize(300,150);
       setVisible(true);  
   }
   
   public void startRunning()
   {
       try{
        server = new ServerSocket(1234,100);
        while(true){
            try{
                waitForConnection();
                setupStreams();
                whileChatting();
            }catch(EOFException e){
                showMessage("\nThe Connection is Ended..");
            }finally{
                closeConnection();
            }
        }
       }catch(IOException e){
           e.printStackTrace();
       }   
   }
   
   private void waitForConnection() throws IOException
   {
       connection = server.accept();
       showMessage("\nNow Connected To: "+connection.getInetAddress().getHostName());
   }
   
   private void setupStreams() throws IOException
   {
       output = new ObjectOutputStream(connection.getOutputStream());
       output.flush();
       input = new ObjectInputStream(connection.getInputStream());
   }
   
   private void whileChatting() throws IOException
   {
       String message = " You Are Now Connected!! ";
       sendMessage(message);
       ableToType(true);
       do{
           try{
               message = (String)input.readObject();
               showMessage("\n"+message);
           }catch(ClassNotFoundException e){
               showMessage("\nCan't Recognize Data From The Other Side");
           }
       }while(!message.equals("CLIENT - END"));
   }
   
   private void closeConnection()
   {
       showMessage("\nClosing Connections..");
       ableToType(false);
       try{
           output.close();
           input.close();
           connection.close();
       }catch(IOException e){
           e.printStackTrace();
       }
   }
   
   private void sendMessage(String message)
   {
       try{
           output.writeObject("SERVER - "+message);
           output.flush();
           showMessage("\nSERVER - "+message);
       }catch(IOException e){
           chatWindow.append("\nError in Sending This Message...");
       }
   }
}
