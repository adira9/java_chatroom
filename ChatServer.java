import java.net.*;
import java.io.*;

public class ChatServer implements Runnable
{
	//List of connected users threads
	private ChatServerThread clients[] = new ChatServerThread[50];
	private ServerSocket     server = null;
   private Thread           thread = null;
   //private ChatServerThread client = null;
   private int clientCount = 0;

   //Constructor attempts to create serverSocket to recieve data from client Sockets
   public ChatServer(int port)
   {  try
      {
	   System.out.println("");System.out.println("");
	   System.out.println("...------------  JAVA CHATROOM ----------");
	   System.out.println("Binding to port " + port + ", please wait  ...");
         server = new ServerSocket(port);  
         System.out.println("Server started: " + server);
         System.out.println("--------------------------------------------------");  
         System.out.println("");System.out.println("");
         start();
      }
      catch(IOException ioe)
      {
    	  System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
    	  //System.out.println(ioe); 
    	  }
   }
   
   //While running accept requests for connection and call addThread()
   public void run()
   {  while (thread != null)
      {  try
         {  System.out.println("Waiting for a client ..."); 
            addThread(server.accept());
         }
         catch(IOException ie)
         {  System.out.println("Acceptance Error: " + ie); stop(); }
      }
   }
   
   //Find client in list of connected clients given ID
   private int findClient(int ID)
   {  for (int i = 0; i < clientCount; i++)
         if (clients[i].getID() == ID)
            return i;
      return -1;
   }
   
   //Synchronized method to handle multiple input threads to the server.
   public synchronized void handle(int ID, String input)
   {  if (input.equals(".bye"))
      {  clients[findClient(ID)].send(".bye");
         remove(ID); }
      else
         for (int i = 0; i < clientCount; i++)
         {
        	 	if (clients[i].getID() != ID)
        	 	{
            clients[i].send( ID + ": " +input+"\n");
            }	
         }
   }
   
   //Synchronized method to remove client threads upin exiting connection
   public synchronized void remove(int ID)
   {  int pos = findClient(ID);
      if (pos >= 0)
      {  ChatServerThread toTerminate = clients[pos];
         System.out.println("Removing client thread " + ID + " at " + pos);
         if (pos < clientCount-1)
            for (int i = pos+1; i < clientCount; i++)
               clients[i-1] = clients[i];
         clientCount--;
         try
         {  toTerminate.close(); }
         catch(IOException ioe)
         {  System.out.println("Error closing thread: " + ioe); }
         toTerminate.stop(); }
   }
   
   //add Clients to server through ChatServerThreads to the client
   public void addThread(Socket socket)
   {  
	   if (clientCount < clients.length)
	      {  System.out.println("Client accepted: " + socket);
	         clients[clientCount] = new ChatServerThread(this, socket);
	         try
	         {  clients[clientCount].open(); 
	            clients[clientCount].start();  
	            clientCount++; }
	         catch(IOException ioe)
	         {  System.out.println("Error opening thread: " + ioe); } }
	      else
	         System.out.println("Client refused: maximum " + clients.length + " reached.");
   }
   
   //Create server thread
   public void start()
      {  if (thread == null)
         {  thread = new Thread(this); 
            thread.start();
         }
      }
      
      //Closing server connections
      public void stop()
      {  if (thread != null)
         {  thread.stop(); 
            thread = null;
         }
   }
   public static void main(String args[])
         {  ChatServer server = null;
            if (args.length != 1)
               System.out.println("Usage: java ChatServer port");
            else
               server = new ChatServer(Integer.parseInt(args[0]));
   }
}
