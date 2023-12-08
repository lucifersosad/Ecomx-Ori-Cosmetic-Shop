package ori.controller.web;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ori.entity.Cart;
import ori.service.ICartService;
import ori.service.IProductService;
import ori.service.IUserService;

@Controller
@RequestMapping("cart")
public class CartController {
	@Autowired(required=true)
	IUserService userService;
	@Autowired(required=true)
	ICartService cartService;
	@Autowired(required=true)
	IProductService productService;
	
    @GetMapping("")
	public String viewCart(Model model) {
        //UserModel user = (UserModel) session.getAttribute("account");
        List<Cart> list= cartService.findByUserId(1);
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
        return "web/cart"; 
    }
	@GetMapping(value = "backToHome")
	public String backToHome(ModelMap model) {
		return "web/index";
	}
	
}
