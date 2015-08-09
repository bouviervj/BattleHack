package server.business;

import java.util.HashMap;

public class Counters {

	public static HashMap<String,Integer> _counters = new  HashMap<String,Integer>();
	
	public static void removeTime(String iDeviceID, int iTime){
		
		int count = _counters.get(iDeviceID)-iTime;
		count = count<0? 0: count;
		_counters.put(iDeviceID, count);
		
	}
	
	public static void addTime(String iDeviceID, int iTime){
		
		int count = _counters.get(iDeviceID)+iTime;
		_counters.put(iDeviceID, count);
		
	}
	
	
}
