package server.transaction;


import server.ServerThread;

public class Device {

	public String ID;
	public String OWNER;
	public String NAME;
	public String TYPE;
	public transient ServerThread _thread;
	
}
