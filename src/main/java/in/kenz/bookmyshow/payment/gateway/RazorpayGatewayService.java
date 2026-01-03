package in.kenz.bookmyshow.payment.gateway;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import in.kenz.bookmyshow.payment.exception.PaymentGatewayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RazorpayGatewayService {

    private final RazorpayClient razorpayClient;

    public Order createOrder(Integer amount, String currency, String receipt) {

        try {
            JSONObject options = new JSONObject();
            options.put("amount", amount * 100); // paise
            options.put("currency", currency);
            options.put("receipt", receipt);

            return razorpayClient.orders.create(options);

        } catch (RazorpayException ex) {
            throw new PaymentGatewayException(
                    "Unable to create payment order with Razorpay",
                    ex
            );
        }
    }
}