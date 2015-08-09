package generic.ws.restjson.services;


import javax.ws.rs.GET;

import javax.ws.rs.Path;

import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;



@Path("/v1/pay")
public class PaymentServices {

	protected static final Logger LOGGER = Logger.getLogger(PaymentServices.class.getName());
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/creditcardpay")
	public String paybycreditcard( @Context SecurityContext sc ) {
		LOGGER.info("Pay by credit card ...");
		return "OK";
	}
				
	
}
