package ori.controller.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.api.payments.Links;
import ori.config.VNPAYConfig;
import ori.entity.Cart;
import ori.entity.Order;
import ori.entity.OrderDetail;
import ori.entity.OrderDetailKey;
import ori.entity.Product;
import ori.entity.User;
import ori.service.ICartService;
import ori.service.IOrderDetailService;
import ori.service.IOrderService;
import ori.service.IProductService;
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
	@Autowired
	IOrderDetailService orderDetailService;
	@Autowired
	ICartService cartService;
	@Autowired
	IProductService productService;
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

				order.setStatus(1);			
				orderService.save(order);
				List<Order> orders = orderService.findAll();
				Order lastOrder = orders.get(orders.size()-1);
				List<Cart> carts = cartService.findByUserId(1);
				for (Cart cart : carts) {
					OrderDetailKey orderDetailKey = new OrderDetailKey();
					orderDetailKey.setOrderId(lastOrder.getOrderId()); // Set the appropriate orderId
					orderDetailKey.setProId(cart.getProduct().getProId()); // Set the appropriate proId

					OrderDetail orderDetail = new OrderDetail();
					orderDetail.setId(orderDetailKey);
					orderDetail.setOrder(lastOrder);
					orderDetail.setProduct(cart.getProduct());
					orderDetail.setQuantity(cart.getQuantity());
					orderDetailService.save(orderDetail);
					Optional<Product> optPro = productService.findById(cart.getProduct().getProId());
					Product pro = new Product();
					if (optPro.isPresent()) {
						pro = optPro.get();
					}
					pro.setStock(pro.getStock()-cart.getQuantity());
					productService.save(pro);
					
					cartService.delete(cart);
				}
				return "web/paypal/success";
			}
		} catch (PayPalRESTException e) {
			System.out.println(e.getMessage());
		}
		return "redirect:/";
	}
}