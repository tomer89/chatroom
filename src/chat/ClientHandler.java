/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ZAKS
 */
class ClientHandler extends Thread  
{ 
    //DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd"); 
    //DateFormat fortime = new SimpleDateFormat("hh:mm:ss");
    //private int id;
    final DataInputStream dis; 
    final DataOutputStream dos; 
    Socket s; 
    boolean isloggedin;
    //int id;
    String name;
      
  
    /**
     * Constructor for a Client handler
     * @param s Socket to send/recieve data on
     * @param name Name of user
     * @param dis Data sent from Client
     * @param dos Data to send to Client
     */
    public ClientHandler(Socket s ,int name ,DataInputStream dis , DataOutputStream dos)  
    { 
        //this.id         = id;
        this.s          = s; 
        this.dis        = dis; 
        this.dos        = dos;
        this.isloggedin = true; 
    } 
  
    /**
     * Deals with any data sent to Server from Client.
     */
    @Override
    public void run()  
    { 
        String received; 
        intro();
        
        while (true)  
        {  
            try
            { 
                // receive the string 
                received = dis.readUTF(); 
                
                if(received.equals("logout")){ 
                    logout();
                    break;
                } 
                  
                // break the string into message and recipient part 
                StringTokenizer st = new StringTokenizer(received, "#"); 
                String recipient = st.nextToken();
                String msgToSend = st.nextToken();
                
                if(recipient.equals("unique name")){
                    if(!userName(msgToSend))
                        break;
                }
                else{
                    // search for the recipient in the connected devices list. 
                    // ar is the vector storing client of active users 
                    int user_count = 1;
                    for (ClientHandler mc : Server.ar)  
                    { 
                        // if the recipient is found, write on its 
                        // output stream 
                        if(recipient.toLowerCase().equals("all")){
                            mc.dos.writeUTF("\n" + this.name+" : "+msgToSend);
                        }
                        else{
                            if(recipient.toLowerCase().equals("unique allusers".toLowerCase())){
                                dos.writeUTF("\nUser number " + user_count + " is: " + mc.name+" ");
                                user_count++;
                            }
                            else{      
                                if (mc.name.equals(recipient) && mc.isloggedin==true)  
                                { 
                                    mc.dos.writeUTF("\n" + this.name+" : "+msgToSend); 
                                    break; 
                                }
                            }
                        }
                    } 
                }
            } catch (IOException e) { 
                e.printStackTrace(); 
            } 
        }
    } 
    
    /**
     * Intro to connectiong
     */
    private void intro(){
        try {
            //String toreturn;
            dos.writeUTF("\n You are connected!!!"
                    + "\nTo write to a certain person write the users name in the proper text Area"
                    + "\n then click on \"Send to\" button.\n"
                    + "After words write a message and click on \"send\" "
                    + "\n To broadcast do enter \"all\" as a recipient\n");
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    /**
     * sets username for client. If something goes wrong Client is disconnected
     * @param check_name
     * @return
     * @throws IOException 
     */
    private boolean userName(String check_name) throws IOException{
            boolean notUsed = true;
                    //this object was added to the ar there for if 1 is not intresting
            for (ClientHandler mc : Server.ar){
                if(mc != this){
                    if(mc.name.equals(check_name))
                        notUsed = false;
                }
            }
            if(notUsed){
                this.name = check_name;
                dos.writeUTF("\n Your Name is: " + name);
                return true;
            }
            else{
                dos.writeUTF("\n"+check_name +" already in use.\nPlease enter a new name"
                        + "\n disconnecting you from Server side"
                        + "\n Click on disconnect and connect again with a new name (IP is already saved in your client)");
                logout();
                return false;
            }
    }
    
    /**
     * logs out and closes all connections
     * @throws IOException 
     */
    private void logout() throws IOException{
                    this.isloggedin=false;
                    this.dos.writeUTF("\n\nYou are disconnecting");
                    this.dis.close();
                    this.dos.close();
                    this.s.close();
                    Server.ar.remove(this);
                    Server.count--;
    }
} 
