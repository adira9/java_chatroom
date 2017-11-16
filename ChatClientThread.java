import java.net.*;
import java.io.*;


public class ChatClientThread extends Thread{
	private Socket           socket   = null;
	   private ChatClient       client   = null;
	   private DataInputStream  streamIn = null;
	  
	   //Constructor creates and runs the client and starts connection to server		
	   public ChatClientThread(ChatClient _client, Socket _socket)
	   {  client   = _client;
	      socket   = _socket;
	      open();  
	      start();
	   }
	
	   //Opens input stream from client side socket.
	   public void open()
	   {  try
	      {  streamIn  = new DataInputStream(socket.getInputStream());
	      }
	      catch(IOException ioe)
	      {  System.out.println("Error getting input stream: " + ioe);
	         client.stop();
	      }
	   }
	
	   //processes to close server connection
	   public void close()
	   {  try
	      {  if (streamIn != null) streamIn.close();
	      }
	      catch(IOException ioe)
	      {  System.out.println("Error closing input stream: " + ioe);
	      }
	   }
	
	   //run:- calls .handle() method on data written to console.
	   public void run()
	   {  while (true)
	      {  try
	         {  client.handle(streamIn.readUTF());
	         }
	         catch(IOException ioe)
	         {  System.out.println("Listening error: " + ioe.getMessage());
	            client.stop();
	         }
	      }
	   }
}
