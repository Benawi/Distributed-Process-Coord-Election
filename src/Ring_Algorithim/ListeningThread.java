package Ring_Algorithim;


import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ListeningThread extends Thread {
	ServerSocket listenSocket;
	private Node node;
	public ListeningThread(int port, Node node) {
		this.node = node;
		// TODO Auto-generated constructor stub
		try {
			listenSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		String msg="";
		while (true){
			Socket clientSocket;
			try {
			clientSocket = listenSocket.accept();
			    DataInputStream in = new DataInputStream ( clientSocket.getInputStream() );
			    msg = in.readUTF();
			    clientSocket.close();
			    
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		    System.out.println("  📲 Node: "+ node.id + " Received: "+msg);
	
		    node.onReceived(msg);
		}
	}
}
