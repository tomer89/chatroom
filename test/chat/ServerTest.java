/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import static chat.Client.socket;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ZAKS
 */
public class ServerTest {
    
    public ServerTest() {
    }

    /**
     * Checks that the ClientHandler vectors pointer didn't change
     */
    @Test
    public void testMain() {
        System.out.println("main");
        Runner r = new Runner();
                    
        r.start();
        
        Vector<ClientHandler> before = Server.ar;
        try {
            // TODO review the generated test code and remove the default call to fail.
            socket = new Socket("127.0.0.1",1201);
        } catch (IOException ex) {
            Logger.getLogger(ServerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        int new_amount = Server.ar.size();
        assertEquals(Server.ar, before);
        //assertEquals(new_amount,before + 1);
        
    }
    public class Runner extends Thread{
        Runner(){
            
        }
        @Override
        public void run(){
            
            String[] args = null;
            Server.main(args);
        }
    }
}
