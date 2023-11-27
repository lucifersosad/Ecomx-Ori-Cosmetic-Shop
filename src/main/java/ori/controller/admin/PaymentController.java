package ori.controller.admin;


import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import ori.config.Config;
import ori.entity.Payment;
import ori.service.IPaymentService;


@Controller
public class PaymentController {
	@Autowired
	IPaymentService paymentService;
	
	@GetMapping("/payment/index")
	public String index() {
		return "web/payment/index";
	}
	
	@GetMapping("/payment/option")
	public String list(ModelMap model) {
		int amount = 20000;
		model.addAttribute("amount",amount);
		return "web/payment/vnpay_pay";
	}
//	@GetMapping("/{pathVariable:.*ipn.*}")
//	public ModelAndView transaction(ModelMap model,
//			@RequestParam Map<String, String> queryParams	
//			){
//		if (queryParams.get("vnp_ResponseCode").equals("00")) {
//			Payment payment = new Payment();
//			payment.setAmount(Long.parseLong(queryParams.get("vnp_Amount")));
//			payment.setOrderInfo(queryParams.get("vnp_OrderInfo"));
//			payment.setPayDate(queryParams.get("vnp_PayDate"));
//			payment.setPayStatus(true);
//			paymentService.save(payment);
//			model.addAttribute("params", queryParams);			
//		}
//		return new ModelAndView("forward:/api/payment/return", model);
//    }
//	@GetMapping("/**")
//	public String transaction(HttpServletRequest request){
//		String requestURI = request.getRequestURI();
//	    if (requestURI.contains("ipn"))
//		return "web/payment/vnpay_pay";
//	    else return "web/payment/vnpay_pay";
//    }
}
