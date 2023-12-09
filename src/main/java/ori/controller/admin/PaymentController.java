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

import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.servlet.view.RedirectView;


import ori.config.scurity.AuthUser;

import ori.entity.User;
import ori.model.Response;

import ori.service.IUserService;

@Controller
@RequestMapping("/payment")
public class PaymentController {
	@Autowired
	IUserService userService;

	@GetMapping("/option")
	public String list(ModelMap model, @Validated @RequestParam("amount") double amount) {
		//int amount = 20000;
		model.addAttribute("amount", amount);
		return "web/payment/vnpay_pay";
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
	@GetMapping("/return")
	public String returnVNPAYPayment(ModelMap model) {
		RestTemplate restTemplate = new RestTemplate();
		String apiUrl = "http://localhost:8888/api/payment/vnpay/cancel"; // Thay đổi URL API cần gọi
		ResponseEntity<Response> response = restTemplate.getForEntity(apiUrl, Response.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			return "web/vnpay/success";
		}
		else {
			Object responseBody = response.getBody().getBody(); // Lấy body từ Response, nơi chứa URL
			if (responseBody instanceof String) {				
				model.addAttribute("message", (String) responseBody);				
			}
			return "web/vnpay/cancel";
		}
	}
}
