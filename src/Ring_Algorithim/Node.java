package Ring_Algorithim;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class Node {
	private static final char NON_PARTICIPANT = 1;
	private static final char PARTICIPANT = 2;
	private static final String ELECTION = "election"+"🖐".toString();
	private static final String ELECTED = "elected"+"👑".toString();
	private static final String SEPERATOR = " ";
	private static final int PORT_BASE = 3000;
	protected static final String HOST = "localhost";
	
	
	private Node nextNode;
	int id;
	private char status;
	private ListeningThread thread;
	private Logger logger;
	protected static int totalMSgSent = 0;
	public void init(int id, Node nextNode) {
		// TODO Auto-generated method stub
		this.id = id;
		this.nextNode = nextNode;
		
		this.status = NON_PARTICIPANT;
		
		thread = new ListeningThread(getPort(),this);
		thread.start();
		
		logger = Logger.getLogger(Integer.toString(id));
		logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
		logger.setLevel(Level.OFF);
	}

	public void beginElection() {
		// send election msg with own id
		String msg = String.format("%s%s%s", ELECTION, SEPERATOR, id);

		status = PARTICIPANT;
		sendMsg(msg);
	}
	public int getPort(){
		return PORT_BASE + id;
	}

	synchronized private void notifyElectionCompleted() {
		System.out.println("📳 Notification: Election Completed");
                System.out.println("✉ Total msg Sent : "+ totalMSgSent);
		notify();
	}
	public void onReceived(String msg){
		String[] s = msg.split(SEPERATOR);
		int receivedId = Integer.parseInt(s[1]);

		if (s[0].equals(ELECTION)){
			// ELECTION
			if (receivedId > id){
				// forward msg
				logger.info(receivedId+">"+id);
				sendMsg(msg);
				
			} else if (receivedId < id){
				
				logger.info(receivedId+"<"+id);
				if (status == PARTICIPANT) return;

				beginElection();
			} else if (receivedId == id){

				logger.info(receivedId+"=="+id);
				status = NON_PARTICIPANT;
				// sent elected msg
				String m = String.format("%s%s%s", ELECTED, SEPERATOR, id);
				sendMsg(m);
			}
		}else{
			// ELECTED
			
			// forward msg if participant or if received id <> id
			if (status == PARTICIPANT || receivedId != id){
				// forward msg
				sendMsg(msg);
			} else {

				notifyElectionCompleted();
			}
			status = NON_PARTICIPANT;
		}
	}
	private void sendMsg(String msg){
		new Thread(
			new Runnable() {
				public void run() {
				    try{
					  int serverPort = nextNode.getPort();
					  Socket s = new Socket (HOST, serverPort);
					  DataOutputStream out = new DataOutputStream(s.getOutputStream());
					  out.writeUTF (msg);
					  totalMSgSent  ++;
					  System.out.println("  📤 Node: "+ id + " Sent: " + msg);
					  s.close();
				    }catch (UnknownHostException e){
					  System.out.println("UnknownHostException:"+ e.getMessage());
				    }catch (IOException e){ System.out.println("IOException:"+ e.getMessage());} 
				}
			}
		).start();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("  💻 Node: id: %s, port:%s, next: %s", id, getPort(), nextNode.getPort());
	}
}
