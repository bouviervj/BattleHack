package server;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;


import server.transaction.Action;
import server.transaction.Query;
import server.transaction.Reply;
import server.transaction.Device;


public class Protocol {

	private static final int WAITING = 0;
	private static final int SENTKNOCKKNOCK = 1;

	private int state = WAITING;

	public static HashMap<String,Vector<Device>> _deviceMap = new HashMap<String,Vector<Device>>();
	public static HashMap<String,Reply> _repliesMap = new HashMap<String,Reply>();
	
	

	public Message processInput(ServerThread thread, Message theInput) {
		Message theOutput = null;

		if (state == WAITING) {
			theOutput = new Message(Message.TYPE_QUERY,new Query());
			state = SENTKNOCKKNOCK;
		} else if (state == SENTKNOCKKNOCK) {

			if (theInput._type==Message.TYPE_REPLY) {

				Reply reply = (Reply) theInput._object;
				if (reply.devices!=null) {

					thread.clientname = reply.from;
					for (Device srv:reply.devices) {
						srv._thread = thread;
					}
					publishDevices(reply);
				} 
			}

		} 

		return theOutput;
	}

	public void publishDevices(Reply reply){

		_deviceMap.put(reply.from, reply.devices);

	}

	public static String timeHash(){

		String aDate = (new Date()).toString();

		MessageDigest md;
		try {
			byte[] bytesOfMessage = aDate.getBytes("UTF-8");
			
			md = MessageDigest.getInstance("MD5");

			byte[] thedigest = md.digest(bytesOfMessage);

			StringBuffer hexString = new StringBuffer();
			for (int i=0;i<thedigest.length;i++) {
				String hex=Integer.toHexString(0xff & thedigest[i]);
				if(hex.length()==1) hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return "failure";

	}

	public static Device findDevice(String iID){
		for (Vector<Device> services:_deviceMap.values()){
			for (Device srv:services) {
				if (srv.ID.equals(iID)) {
					return srv;
				}
			}
		}
		return null;
	}
	
	public static Reply waitForMessage(String iHash){
		
		boolean found = false;
		while (!found) {
			if (_repliesMap.containsKey(iHash)) {
				found = true;
			} else {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return _repliesMap.get(iHash);
		
	}
	
	public static String callDevices(String iID, String iActionCode){

		String hash = timeHash();
		
		for (Vector<Device> services:_deviceMap.values()){

			for (Device srv:services) {
				if (srv.ID.equals(iID)) {

					Action act = new Action();
					act.id = iID;
					act.actioncode = iActionCode;

					Query aQuery = new Query();
					aQuery.from = hash;
					aQuery.to = "device";

					aQuery.actions = new Vector<Action>();
					aQuery.actions.add(act);

					Message msg = new Message(Message.TYPE_QUERY, aQuery);
					srv._thread.sendMessage( msg );

				}
			}
			
		}
		
		return hash;

	}
	


}
