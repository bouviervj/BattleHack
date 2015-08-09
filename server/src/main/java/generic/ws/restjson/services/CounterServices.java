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
import server.transaction.Reply;
import server.transaction.Device;

@Path("/v1/counter")
public class CounterServices {

	protected static final Logger LOGGER = Logger.getLogger(CounterServices.class.getName());
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/devices")
	public  HashMap<String,Vector<Device>> devicess( @Context SecurityContext sc ) {
		LOGGER.info("Retrieve devices ...");
		return Protocol._deviceMap;
	}

	
}
