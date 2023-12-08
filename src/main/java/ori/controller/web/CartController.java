package ori.controller.web;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import ori.entity.Cart;
import ori.entity.CartKey;
import ori.service.ICartService;
import ori.service.IProductService;
import ori.service.IUserService;

@Controller

public class CartController
{
	@Autowired(required=true)
	IUserService userService;
	@Autowired(required=true)
	ICartService cartService;
	@Autowired(required=true)
	IProductService productService;
	@GetMapping(value = "backToHome")
	public String backToHome(ModelMap model) 
	{
		return "web/index";
	}
	@GetMapping("/test")
	public String viewCart(ModelMap model) 
	{
		
    	List<Cart> list= cartService.findByUserId(2);
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
		
    	for (List<Object> list2 : obs) 
    	{
			System.out.println(list2.get(0));
			System.out.println(list2.get(1));
			System.out.println(list2.get(2));
			System.out.println(list2.get(3));
			System.out.println(list2.get(4));
		}
    	System.out.println(sum);
    	model.addAttribute("list", list);
    	model.addAttribute("total", sum);
        model.addAttribute("listp", obs);
        return "web/cart";
	}
	@GetMapping("deleteItem/{proid}")
	public String deleteItem(ModelMap model, @PathVariable("proid") Integer proid) 
	{
		List<Cart> list =cartService.findByUserIdAndProid(2, proid);
		for (Cart cart : list) {
			cartService.delete(cart);
		}
		return "web/cart";
	}
	@GetMapping(value = "deleteCart")
	public String deleteCart(ModelMap model) 
	{
		List<Cart> list =cartService.findByUserId(1);
		for (Cart cart : list) {
			cartService.delete(cart);
		}
		return "web/cart";
	}
	@GetMapping(value = "/updateQTT/{proid}")
	public String updateCart(ModelMap model,@PathVariable("proid")Integer proid, Integer qtt) 
	{
		qtt=69;
		List<Cart> list =cartService.findByUserIdAndProid(2, proid);
		for (Cart cart : list) {
			cart.setQuantity(qtt);
			cartService.save(cart);
		}
		return "web/cart";
	}
	
}