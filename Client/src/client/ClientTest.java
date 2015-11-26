/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;
import javax.swing.JFrame;

/**
 *
 * @author root
 */
public class ClientTest {
    public static void main(String [] args)
    {
        Client server = new Client("127.0.0.1");
        server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        server.startRunning();
    }
    
}
