package ori.controller.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ori.config.scurity.AuthUser;
import ori.entity.Cart;
import ori.entity.User;
import ori.service.ICartService;
import ori.service.IUserService;

@Controller
@RequestMapping("checkout")
public class CheckOutController {
	String PaymentMethod;
	String Note;
	@Autowired(required = true)
	IUserService userService;
	@Autowired(required = true)
	ICartService cartService;

	@GetMapping("")
	public String ThongtinKh(ModelMap model) {
		// khi gộp vào vào thì sẽ dùng đoạn này
		User user = userService.getUserLogged();
		String[] addressParts = user.getAddress().split(",");
		model.addAttribute("addressParts", addressParts);
		model.addAttribute("user", user);

        List<Cart> list= cartService.findByUserId(user.getUserId());
        List<List<Object>> obs = new ArrayList<>();
        float sum = 0;
    	for (Cart cart : list) 
    	{
    		 List<Object> o = new ArrayList<>();
             o.add(cart.getProduct().getImage_link());
             o.add(cart.getProduct().getName());
             o.add(cart.getProduct().getPrice());
             o.add(cart.getQuantity());
             o.add((cart.getQuantity())*(cart.getProduct().getPrice()));
             float total = (cart.getQuantity()*(cart.getProduct().getPrice()));
             sum = sum + total;
             obs.add(o);
		}
    	model.addAttribute("total", sum);
        model.addAttribute("listp", obs);
		return "web/checkout";
	}
//@RequestParam("total") String amount
	@GetMapping("PaymentMethod")
	public String PaymentMethod(RedirectAttributes redirectAttributes) {
		if ("PayPal".equals(PaymentMethod)) {
			
			return "redirect:/pay";
		} else if ("VNPAY".equals(PaymentMethod)) {
			redirectAttributes.addAttribute("amount", "180");
			return "redirect:/payment/option";

		} else {
			redirectAttributes.addAttribute("amount", "180");
			return "redirect:/payment/cod";
		}
	}

	@GetMapping("OrderNote")
	public String Note(ModelMap model) {
		model.addAttribute("note", Note);
		return "CheckOut/note";
	}

	@PostMapping("Post_OrderNote")
	public String Post_Note(@RequestParam("c_order_notes") String note) {
		Note = note;
		if (Note.equals(null)) {
			System.out.println("Rong");
		} else {
			System.out.println("Co kq");
		}
		return "redirect:/OrderNote";
	}

	@PostMapping("/Payment")
	public String processPayment(@RequestParam("paymentMethod") String paymentMethod) {
		PaymentMethod = paymentMethod;
		return "redirect:/CheckOut/PaymentMethod";
	}
}