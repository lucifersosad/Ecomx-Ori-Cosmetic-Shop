package ori.controller.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import ori.entity.Cart;
import ori.entity.Product;
import ori.entity.User;
import ori.model.CartModel;
import ori.model.ProductModel;
import ori.service.ICartService;
import ori.service.IUserService;

@Controller
@RequestMapping("CheckOut")
public class CheckOutController {
	String PaymentMethod;
	String Note;
	double sum = 0;
	@Autowired(required = true)
	IUserService userService;
	@Autowired(required = true)
	ICartService cartService;

	@GetMapping("")
	public String ThongtinKh(ModelMap model) {
		User user = userService.getUserLogged();
		String[] addressParts = user.getAddress().split(",");
		model.addAttribute("addressParts", addressParts);
		model.addAttribute("user", user);

		List<Cart> list = cartService.findByUserId(user.getUserId());
		List<ProductModel> listp = new ArrayList<>();
		List<CartModel> listc = new ArrayList<>();
		List<Double> tong = new ArrayList<>();
		

		for (Cart cart : list) {
			Product pro = cart.getProduct();
			ProductModel productModel = new ProductModel();
			CartModel cartModel = new CartModel();
			productModel.setProId(pro.getProId());
			productModel.setImage_link(pro.getImage_link());
			productModel.setName(pro.getName());
			productModel.setPrice(Math.round(pro.getPrice() * (100 - pro.getSale()) / 100));
			cartModel.setQuantity(cart.getQuantity());
			double total = cartModel.getQuantity() * productModel.getPrice();
			tong.add(total);
			sum = sum + total;
			listp.add(productModel);
			listc.add(cartModel);
		}
		List<Map<String, Object>> CartList = new ArrayList<>();
		for (int i = 0; i < listp.size(); i++) {
			Map<String, Object> item = new HashMap<>();
			item.put("product", listp.get(i));
			item.put("quantity", listc.get(i));
			item.put("tong", tong.get(i));
			CartList.add(item);
		}
		model.addAttribute("list", CartList);
		model.addAttribute("total", sum);
		return "web/checkout";
	}

	@GetMapping("PaymentMethod")
	public String PaymentMethod(RedirectAttributes redirectAttributes) {
		User user = userService.getUserLogged();
		List<Cart> carts = cartService.findByUserId(user.getUserId());
		int total = 0;
		for (Cart cart : carts) {		
			int sale = cart.getProduct().getSale();
			int price = (int) Math.ceil(cart.getProduct().getPrice() * (100 - sale) / 100);
		    int quantity = cart.getQuantity();
		    total += quantity * price;
		}
		if ("PayPal".equals(PaymentMethod)) {

			return "redirect:/pay";
		} else if ("VNPAY".equals(PaymentMethod)) {
			
			redirectAttributes.addAttribute("amount", String.valueOf(total));
			return "redirect:/payment/option";

		} else {
			redirectAttributes.addAttribute("amount", String.valueOf(total));
			return "redirect:/payment/cod";
		}
	}

	@GetMapping("OrderNote")
	public String Note(ModelMap model) {
		model.addAttribute("note", Note);
		return "CheckOut/note";
	}

	@PostMapping("/Payment")
	public String processPayment(@RequestParam("paymentMethod") String paymentMethod) {
		PaymentMethod = paymentMethod;
		return "redirect:/CheckOut/PaymentMethod";
	}
}