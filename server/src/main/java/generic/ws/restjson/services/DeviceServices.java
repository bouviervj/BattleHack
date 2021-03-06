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

@Path("/v1/dev")
public class DeviceServices {

	protected static final Logger LOGGER = Logger.getLogger(DeviceServices.class.getName());
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/devices")
	public  HashMap<String,Vector<Device>> devicess( @Context SecurityContext sc ) {
		LOGGER.info("Retrieve devices ...");
		return Protocol._deviceMap;
	}
	
	public static String deviceActivate(String iDeviceID, int iTime){
		int time = Counters._counters.get(iDeviceID)-iTime;
		time = time < 0?Counters._counters.get(iDeviceID):iTime;
		Counters.removeTime(iDeviceID, time);
		Protocol.callDevices(iDeviceID, "activate", time);
		return "OK";
	}
			
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deviceActivate/{id}/{time}")
	public Object deviceActivate( @Context SecurityContext sc,
								  @PathParam("id") String iDeviceID,
								  @PathParam("time") int iTime) { 
		
		return deviceActivate(iDeviceID, iTime);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/deviceDeactivate/{id}")
	public String deviceDeactivate( @Context SecurityContext sc,
								  @PathParam("id") String iDeviceID) { 
		
		String iHash = Protocol.callDevices(iDeviceID, "deactivate", 0);
		Reply aReply = Protocol.waitForMessage(iHash);
		if (aReply.actions.size()>0) {
			
			// TODO Update counters
			Counters.addTime(iDeviceID, aReply.actions.get(0).rest);
			
			return ""+aReply.actions.get(0).rest;
		}
		return ""+-1;
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("/{state}/otherMIME/")
	public byte[] simpleOtherMIMEType( @Context SecurityContext sc, @PathParam("state") String iState) throws Exception {
		try {
			return "Stream".getBytes();
		} catch (Exception e) {
			LOGGER.info("Simple Error");
			throw new NotFoundException(e.getMessage());		
		}
		
	}
	
	
}
