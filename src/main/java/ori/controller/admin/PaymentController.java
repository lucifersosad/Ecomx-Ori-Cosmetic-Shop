package ori.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller

@RequestMapping("payment")
public class PaymentController {
	@GetMapping("index")
	public String index() {
		return "web/payment/index";
	}
	
	@GetMapping("option")
	public String list(ModelMap model) {
		int amount = 20000;
		model.addAttribute("amount",amount);
		return "web/payment/vnpay_pay";
	}
//	@PostMapping(path ="create")
//	public ModelAndView createPayment() {
//		return ModelAndView();
//	}
}
