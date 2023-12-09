package ori.controller.admin;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import ori.config.VNPAYConfig;
import ori.config.scurity.AuthUser;
import ori.entity.Cart;
import ori.entity.Order;
import ori.entity.OrderDetail;
import ori.entity.OrderDetailKey;
import ori.entity.Product;
import ori.entity.User;
import ori.model.Response;
import ori.service.ICartService;
import ori.service.IOrderDetailService;
import ori.service.IOrderService;
import ori.service.IProductService;
import ori.service.IUserService;

@Controller
@RequestMapping("/payment")
public class PaymentController {
	@Autowired
	IOrderService orderService;
	@Autowired
	IUserService userService;
	@Autowired
	IOrderDetailService orderDetailService;
	@Autowired
	ICartService cartService;
	@Autowired
	IProductService productService;

	@GetMapping("/option")
	public String list(ModelMap model, @Validated @RequestParam("amount") double amount) {
		//int amount = 20000;
		model.addAttribute("amount", amount);
		return "web/payment/vnpay_pay";
	}

	@GetMapping("/vnpay/ipn")
	public String transaction(@RequestParam Map<String, String> queryParams, RedirectAttributes redirectAttributes) {
		Map<String, String> fields = new HashMap<>();

		for (Map.Entry<String, String> entry : queryParams.entrySet()) {
			String fieldName = entry.getKey();
			String fieldValue = entry.getValue();

			try {
				fieldName = URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString());
				fieldValue = URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString());

				if (fieldValue != null && !fieldValue.isEmpty()) {
					fields.put(fieldName, fieldValue);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String vnp_SecureHash = queryParams.get("vnp_SecureHash");
		if (fields.containsKey("vnp_SecureHashType")) {
			fields.remove("vnp_SecureHashType");
		}
		if (fields.containsKey("vnp_SecureHash")) {
			fields.remove("vnp_SecureHash");
		}
		String signValue = VNPAYConfig.hashAllFields(fields);
		String payStatus = "", message = "";
		if (signValue.equals(vnp_SecureHash)) {
			boolean checkAmount = true; // Kiểm tra số tiền tài khoản so với tiền đơn hàng
			boolean checkOrderStatus = true; // Kiểm tra đơn hàng đã thanh toán hay chưa
			if (checkAmount) {
				if (checkOrderStatus) {
					if ("00".equals(queryParams.get("vnp_ResponseCode"))) {
						Object authen = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
						if (authen instanceof AuthUser) {
							String email = ((AuthUser) authen).getEmail();
							Optional<User> optUser = userService.findByEmail(email);
							if (optUser.isPresent()) {
								User user = optUser.get();
								Order order = new Order();
								order.setUserId(user);
								order.setDate(queryParams.get("vnp_PayDate"));
								order.setPayment_method("VNPAY");
								order.setStatus(1);
								order.setTotal(Double.parseDouble(queryParams.get("vnp_Amount")));
								order.setCurrency("VND");
								payStatus = "1";
								message = queryParams.get("vnp_Amount");
								orderService.save(order);
								List<Order> orders = orderService.findAll();
								Order lastOrder = orders.get(orders.size() - 1);
								List<Cart> carts = cartService.findByUserId(user.getUserId());
								for (Cart cart : carts) {
									OrderDetailKey orderDetailKey = new OrderDetailKey();
									orderDetailKey.setOrderId(lastOrder.getOrderId()); // Set the appropriate orderId
									orderDetailKey.setProId(cart.getProduct().getProId()); // Set the appropriate proId
									OrderDetail orderDetail = new OrderDetail();
									orderDetail.setId(orderDetailKey);
									orderDetail.setOrder(lastOrder);
									orderDetail.setProduct(cart.getProduct());
									orderDetail.setQuantity(cart.getQuantity());
									orderDetail.setDiscount(cart.getProduct().getSale());

									orderDetailService.save(orderDetail);
									Optional<Product> optPro = productService.findById(cart.getProduct().getProId());
									Product pro = new Product();
									if (optPro.isPresent()) {
										pro = optPro.get();
									}
									pro.setStock(pro.getStock() - cart.getQuantity());
									productService.save(pro);
									cartService.delete(cart);
								}
							}
						}

					} else {
						payStatus = "0";
						message = "Thanh toán không thành công";
					}
				} else {
					payStatus = "0";
					message = "Đơn hàng đã thanh toán";
				}
			} else {
				payStatus = "0";
				message = "Số tiền không hợp lệ";
			}

		} else {
			payStatus = "0";
			message = "Checksum không hợp lệ";
		}
		redirectAttributes.addAttribute("payStatus", payStatus);
		redirectAttributes.addAttribute("message", message);
		return "redirect:/payment/vnpay/return";
	}

	@GetMapping("/vnpay/return")
	public String info(@RequestParam("payStatus") String payStatus, @RequestParam("message") String message,
			Model model) {
		model.addAttribute("payStatus", payStatus);
		model.addAttribute("message", message);
		return "web/payment/vnpay_return";
	}

	@GetMapping("/paypal/create")
	public RedirectView createPayPalPayment() {
		User user = new User();
		Object authen = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (authen instanceof AuthUser) {
			String email = ((AuthUser) authen).getEmail();
			Optional<User> optUser = userService.findByEmail(email);
			if (optUser.isPresent()) {
				user = optUser.get();
			}
		}
		RestTemplate restTemplate = new RestTemplate();
		String apiUrl = "http://localhost:8888/api/payment/paypal/create?userId=" + user.getUserId(); // Thay đổi URL
																										// API cần gọi
		ResponseEntity<Response> response = restTemplate.getForEntity(apiUrl, Response.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			Object responseBody = response.getBody().getBody(); // Lấy body từ Response, nơi chứa URL
			if (responseBody instanceof String) {
				String redirectUrl = (String) responseBody; // Ép kiểu sang String nếu là URL
				return new RedirectView(redirectUrl); // Chuyển hướng trình duyệt tới URL từ API
			}
		}
		return new RedirectView("/"); // Chuyển hướng đến trang lỗi
	}

	@GetMapping("/paypal/success")
	public String successPayPalPayment(@RequestParam("paymentId") String paymentId,
			@RequestParam("PayerID") String payerId) {
		Object authen = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = "";
		if (authen instanceof AuthUser) {
			email = ((AuthUser) authen).getEmail();
		}
		RestTemplate restTemplate = new RestTemplate();
		String apiUrl = "http://localhost:8888/api/payment/paypal/success?paymentId=" + paymentId + "&PayerID="
				+ payerId + "&Email=" + email; // Thay đổi URL API cần gọi
		ResponseEntity<Response> response = restTemplate.getForEntity(apiUrl, Response.class);
		String redirectUrl = "";
		if (response.getStatusCode().is2xxSuccessful()) {
			Object responseBody = response.getBody().getBody(); // Lấy body từ Response, nơi chứa URL
			if (responseBody instanceof String) {
				redirectUrl = (String) responseBody; // Ép kiểu sang String nếu là URL
				// Chuyển hướng trình duyệt tới URL từ API
			}
		}
		return redirectUrl;
	}
	@GetMapping("/paypal/cancel")
	public String cancelPayPalPayment(@RequestParam("paymentId") String paymentId,
			@RequestParam("PayerID") String payerId) {

		RestTemplate restTemplate = new RestTemplate();
		String apiUrl = "http://localhost:8888/api/payment/paypal/cancel"; // Thay đổi URL API cần gọi
		ResponseEntity<Response> response = restTemplate.getForEntity(apiUrl, Response.class);
		String redirectUrl = "";
		if (response.getStatusCode().is2xxSuccessful()) {
			Object responseBody = response.getBody().getBody(); // Lấy body từ Response, nơi chứa URL
			if (responseBody instanceof String) {
				redirectUrl = (String) responseBody; // Ép kiểu sang String nếu là URL
				// Chuyển hướng trình duyệt tới URL từ API
			}
		}
		return redirectUrl;
	}
}
