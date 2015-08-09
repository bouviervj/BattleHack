package server.transaction;


import server.ServerThread;

public class Device {

	public String ID;
	public String OWNER;
	public String NAME;
	public String UNIT;
	public float UNITPRICE;
	public transient ServerThread _thread;
	
}
