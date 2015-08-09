package generic.ws.restjson.services;

import java.util.HashMap;
import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;

import server.Protocol;
import server.business.Counters;
import server.transaction.Reply;
import server.transaction.Device;

@Path("/v1/counter")
public class CounterServices {

	protected static final Logger LOGGER = Logger.getLogger(CounterServices.class.getName());
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/listCounters")
	public HashMap<String,Integer> listCounters( @Context SecurityContext sc, String iHome ) {
		
		HashMap<String,Integer> result = new HashMap<String,Integer>();
		
		for (String aDevice:Counters._counters.keySet()){
			String aHash = Protocol.callDevices(aDevice, "counter", 0);
			Reply aReply = Protocol.waitForMessage(aHash);
			if (aReply!=null && aReply.actions.size()>0) {
			
				int val = aReply.actions.get(0).rest;
				if (val>=0) {
					result.put(aDevice, Counters._counters.get(aDevice)+val);
				} else {
					result.put(aDevice, Counters._counters.get(aDevice));
				}
				
			} else {
				result.put(aDevice, Counters._counters.get(aDevice));
			}
		}
		
		LOGGER.info("Retrieve devices ...");
		return result;
	}

	
}
