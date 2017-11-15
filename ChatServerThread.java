import java.net.*;
import java.io.*;

public class ChatServerThread extends Thread
{  private Socket          socket   = null;// creating objects
   private ChatServer      server   = null;
   private int             ID       = -1;
   private DataInputStream streamIn =  null;
   private DataOutputStream streamOut = null;
   //private PrintStream Output = null; //change
   public ChatServerThread(ChatServer _server, Socket _socket)
   {  super();  server = _server;  socket = _socket;  ID = socket.getPort();
   }
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
   public int getID()
   {  return ID;
   }
   public void run()
   {  System.out.println("Server Thread " + ID + " running.");
      /*while (true)
      {  try
         {  System.out.println(streamIn.readUTF());
         	//Output = new PrintStream(socket.getOutputStream()); //changed 
         }
         catch(IOException ioe) {  }
      }*/
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
   public void open() throws IOException
   {  streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
   streamOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
   }
   public void close() throws IOException
   {  if (socket != null)    socket.close();
      if (streamIn != null)  streamIn.close();
      if (streamOut != null) streamOut.close();
   }
}