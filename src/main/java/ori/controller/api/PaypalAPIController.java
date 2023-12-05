package ori.controller.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.api.payments.Links;

import ori.config.VNPAYConfig;
import ori.entity.Order;
import ori.entity.User;
import ori.service.IOrderService;
import ori.service.IUserService;
import ori.service.PaypalService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
@Controller
public class PaypalAPIController {
	@Autowired
	PaypalService service;
	@Autowired
	IUserService userService;
	@Autowired
	IOrderService orderService;
	public static final String SUCCESS_URL = "pay/success";
	public static final String CANCEL_URL = "pay/cancel";
	public static final double unit = (double) 0.000041;
	@GetMapping("/pay")
	public String payment(@ModelAttribute("order") Order order) {
		try {
			Payment payment = service.createPayment((Double) unit * 20000, "USD", "PAYPAL", "SALE", "I buy it",
					"http://localhost:8888/" + CANCEL_URL, "http://localhost:8888/" + SUCCESS_URL);
			for (Links link : payment.getLinks()) {
				if (link.getRel().equals("approval_url")) {
					return "redirect:" + link.getHref();
				}
			}

		} catch (PayPalRESTException e) {

			e.printStackTrace();
		}
		return "redirect:/";
	}

	@GetMapping(value = CANCEL_URL)
	public String cancelPay() {
		return "web/paypal/cancel";
	}

	@GetMapping(value = SUCCESS_URL)
	public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
		try {
			Payment payment = service.executePayment(paymentId, payerId);			
			if (payment.getState().equals("approved")) {
				String jsonResult = payment.toJSON();
				JSONParser parser = new JSONParser();
				Order order = new Order();
		        try {
		            // Phân tích chuỗi JSON
		            JSONObject paymentObject = (JSONObject) parser.parse(jsonResult);

		            // Truy cập đến transactions
		            JSONArray transactions = (JSONArray) paymentObject.get("transactions");

		            // Lặp qua các giao dịch
		            for (Object transactionObj : transactions) {
		                JSONObject transaction = (JSONObject) transactionObj;

		                // Truy cập đến related_resources
		                JSONArray relatedResources = (JSONArray) transaction.get("related_resources");

		                // Lặp qua các related_resources
		                for (Object resourceObj : relatedResources) {
		                    JSONObject resource = (JSONObject) resourceObj;
		                    JSONObject sale = (JSONObject) resource.get("sale");
		                    JSONObject amount = (JSONObject) sale.get("amount");
		                    String amountTotal = (String) amount.get("total");
		                    order.setTotal(Double.parseDouble(amountTotal));
		                    order.setCurrency("USD");
		                }
		            }
		        } catch (ParseException e) {
		            e.printStackTrace();
		        }
				
				//Optional<User> optUser = userService.findById(Integer.parseInt(VNPAYConfig.userid));
		        Optional<User> optUser = userService.findById(1);
				User user = new User();
				if (optUser.isPresent()) {
					 user = optUser.get();
				}
				order.setUserId(user);
				order.setDate(payment.getCreateTime());
				order.setPayment_method("Paypal");
				order.setShipping_method(VNPAYConfig.shipping_method);
				order.setStatus(1);			
				orderService.save(order);
				return "web/paypal/success";
			}
		} catch (PayPalRESTException e) {
			System.out.println(e.getMessage());
		}
		return "redirect:/";
	}
}
