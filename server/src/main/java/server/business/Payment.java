package server.business;

import java.math.BigDecimal;
import com.braintreegateway.*;

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
    
    
    
    public static String processPayment(Payment iPaymentData){
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
            return "OK";
            
        } else if (result.getTransaction() != null) {
            Transaction transaction = result.getTransaction();
            System.out.println("Error processing transaction:");
            System.out.println("  Status: " + transaction.getStatus());
            System.out.println("  Code: " + transaction.getProcessorResponseCode());
            System.out.println("  Text: " + transaction.getProcessorResponseText());
            return "KO";
            
        } else {
            for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
                System.out.println("Attribute: " + error.getAttribute());
                System.out.println("  Code: " + error.getCode());
                System.out.println("  Message: " + error.getMessage());
            }
            return "KO";
        }
    }
}
