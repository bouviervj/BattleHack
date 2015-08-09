package server.business;

import generic.ws.restjson.services.DeviceServices;

import java.math.BigDecimal;
import com.braintreegateway.*;
import server.business.Counters;

import java.util.*;
import com.twilio.sdk.*;
import com.twilio.sdk.resource.factory.*;
import com.twilio.sdk.resource.instance.*;
import com.twilio.sdk.resource.list.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


public class Payment {

	public String creditCardNumber;
    public String creditCardExpiration;
    public String amountToPay;
    public int timeCredit;
	public String DeviceID;
    
    //Payment Data
    private static BraintreeGateway gateway = new BraintreeGateway(
                                                                   Environment.SANDBOX,
                                                                   "n3hkt3yf79ry3y2d",
                                                                   "kw6ddj6r96tkp2fr",
                                                                   "56bbd8ec8b0ce28b1e183010ab1b1aa8"
                                                                   );
    
    public static final String ACCOUNT_SID = "AC29676e1d4e128beb97eddea17bfc2f03";
    public static final String AUTH_TOKEN = "4d0464319866dd9625f57431b8045f0e";
    
    public static String processPayment(Payment iPaymentData){
        
        String aTextToSend = "Device doesn't exist";
        String aReturnValue = "KO";
        
        if(Counters._counters.containsKey(iPaymentData.DeviceID)){
            
            TransactionRequest request = new TransactionRequest().
                amount(new BigDecimal(iPaymentData.amountToPay)).
                creditCard().
                    number(iPaymentData.creditCardNumber).
                    expirationDate(iPaymentData.creditCardExpiration).
                    done();
        
            Result<Transaction> result = gateway.transaction().sale(request);
        
            if (result.isSuccess()) {
                Transaction transaction = result.getTarget();
                System.out.println("Success!: " + transaction.getId());
            
                Counters.addTime(iPaymentData.DeviceID, iPaymentData.timeCredit);
            
                DeviceServices.deviceActivate(iPaymentData.DeviceID, iPaymentData.timeCredit);
            
                aReturnValue = "OK";
                
                aTextToSend = "Payment of " + iPaymentData.amountToPay + " is successful";
            }
            else
                aTextToSend = "Payment of " + iPaymentData.amountToPay + " failed";
        
        }
        //--------- Send SMS --------------
        
        TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
        
        // Build the parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("To", "8572428463"));
        params.add(new BasicNameValuePair("From", "+17812187345"));
        params.add(new BasicNameValuePair("Body", aTextToSend));
        
        MessageFactory messageFactory = client.getAccount().getMessageFactory();
        try {
            com.twilio.sdk.resource.instance.Message message = messageFactory.create(params);
            System.out.println(message.getSid());
        }
        catch (com.twilio.sdk.TwilioRestException e) {
            //Do nothing
        }
        
        return aReturnValue;
    }
}
