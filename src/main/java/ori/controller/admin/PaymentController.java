package ori.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ori.model.Response;

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
	@GetMapping("/return")
	public String transaction(
			ModelMap model,
			@Validated @RequestParam("vnp_Amount") long amount,
			@Validated @RequestParam("vnp_BankCode") String bankCode,
			@Validated @RequestParam("vnp_CardType") String cardType,
			@Validated @RequestParam("vnp_OrderInfo") String orderInfo,
			@Validated @RequestParam("vnp_PayDate") String payDate,
			@Validated @RequestParam("vnp_ResponseCode") String responseCode,
			@Validated @RequestParam("vnp_TmnCode") String tmnCode,
			@Validated @RequestParam("vnp_TransactionNo") String transactionNo,
			@Validated @RequestParam("vnp_TransactionStatus") String transactionStatus,
			@Validated @RequestParam("vnp_TxnRef") String txnRef,
			@Validated @RequestParam("vnp_SecureHash") String secureHash			
			){
		return "web/payment/vnpay_return";
	}
//	@PostMapping(path ="create")
//	public ModelAndView createPayment() {
//		return ModelAndView();
//	}
}
