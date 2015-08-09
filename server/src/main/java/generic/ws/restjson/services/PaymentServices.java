package generic.ws.restjson.services;

import javax.ws.rs.GET;
import javax.ws.rs.POST;

import javax.ws.rs.Path;

import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;

import server.business.Payment;


@Path("/v1/pay")
public class PaymentServices {

	protected static final Logger LOGGER = Logger.getLogger(PaymentServices.class.getName());
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/creditcardpay")
	public String paybycreditcard( @Context SecurityContext sc,
                                  Payment paymentData) {
		LOGGER.info("Pay by credit card ...");
        return Payment.processPayment(paymentData);
	}
				
	
}
