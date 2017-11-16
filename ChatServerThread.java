import java.net.*;
import java.io.*;

public class ChatServerThread extends Thread
{  private Socket          socket   = null;// creating objects
   private ChatServer      server   = null;
   private int             ID       = -1;
   private DataInputStream streamIn =  null;
   private DataOutputStream streamOut = null;
   
   //Constructor to link client side server socket to ChatserverThread
   public ChatServerThread(ChatServer _server, Socket _socket)
   {  super();  server = _server;  socket = _socket;  ID = socket.getPort();
   }
   
   //method to send accepted input data out to users.
   public void send(String msg)
   {   try
       {  streamOut.writeUTF(msg);
          streamOut.flush();
       }
       catch(IOException ioe)
       {  System.out.println(ID + " ERROR sending: " + ioe.getMessage());
          server.remove(ID);
          stop();
       }
   }
   
   //Return id from ChatServerThread
   public int getID()
   {  return ID;
   }
   
   //during running call handle() on messages to be sent to connected users.
   public void run()
   {  System.out.println("Server Thread " + ID + " running.");
   while (true)
   {  try
      {  server.handle(ID, streamIn.readUTF());
      }
      catch(IOException ioe)
      {  System.out.println(ID + " ERROR reading: " + ioe.getMessage());
         server.remove(ID);
         stop();
      }
   }
   }
   
   //Creates datainput and output streams connected to server
   public void open() throws IOException
   {  streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
   streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
   }
   
   //Method to close stream connections
   public void close() throws IOException
   {  if (socket != null)    socket.close();
      if (streamIn != null)  streamIn.close();
      if (streamOut != null) streamOut.close();
   }
}
