package ori.controller.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
<<<<<<< HEAD

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

=======
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ori.config.scurity.AuthUser;
>>>>>>> dev
import ori.entity.Cart;
import ori.entity.CartKey;
import ori.entity.Product;
import ori.entity.User;
import ori.model.CartModel;
import ori.model.ProductModel;
import ori.service.ICartService;
import ori.service.IProductService;
import ori.service.IUserService;

@RequestMapping("/cart")
@Controller
<<<<<<< HEAD
@RequestMapping("/cart")
public class CartController
{
=======
public class CartController {
>>>>>>> dev
	@Autowired(required=true)
	IUserService userService;
	@Autowired(required=true)
	ICartService cartService;
	@Autowired(required=true)
	IProductService productService;
<<<<<<< HEAD
=======
	@Autowired(required = true)
	IProductService proService;
	
	@GetMapping(value = "backToHome")
	public String backToHome(ModelMap model) 
	{
		return "web/index";
	}
	
>>>>>>> dev
	@GetMapping("")
	public String viewCart(ModelMap model) 
	{
		User userLogged = userService.getUserLogged();
    	List<Cart> list= cartService.findByUserId(userLogged.getUserId());
		List<ProductModel> listp = new ArrayList<>();
		List<CartModel> listc = new ArrayList<>();
		List<Double> tong=new ArrayList<>();
		double sum =0;
		
		for (Cart cart : list) {
		    Product pro = cart.getProduct();
		    ProductModel productModel = new ProductModel();
		    CartModel cartModel = new CartModel();
		    productModel.setProId(pro.getProId());
		    productModel.setImage_link(pro.getImage_link());
		    productModel.setName(pro.getName());
<<<<<<< HEAD
		    productModel.setPrice(pro.getPrice());
		    productModel.setProId(pro.getProId());
=======
		    productModel.setPrice(Math.round(pro.getPrice() * (100 - pro.getSale()) / 100));
>>>>>>> dev
		    cartModel.setQuantity(cart.getQuantity());
		    double total= cartModel.getQuantity()*productModel.getPrice();
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
		model.addAttribute("list",CartList);
		model.addAttribute("total", sum);
		return "web/cart";
	}
<<<<<<< HEAD
	@GetMapping("/deleteItem/{proid}")
=======

	 
	@GetMapping("deleteItem/{proid}")
>>>>>>> dev
	public String deleteItem(ModelMap model, @PathVariable("proid") Integer proid) 
	{
		User userLogged = userService.getUserLogged();
		List<Cart> list =cartService.findByUserIdAndProid(userLogged.getUserId(), proid);
		for (Cart cart : list) {
			cartService.delete(cart);
		}
<<<<<<< HEAD
		return viewCart(model);
	}
=======
		return "redirect:/cart";
	} 
	
	
>>>>>>> dev
	@GetMapping(value = "deleteCart")
	public String deleteCart(ModelMap model) 
	{
		List<Cart> list =cartService.findByUserId(1);
		for (Cart cart : list) {
			cartService.delete(cart);
		}
	
		return "web/index";
	}
<<<<<<< HEAD
	@GetMapping("/updateQTT/{proid}/tang")
	public String updateCartTang(ModelMap model,@PathVariable("proid")Integer proid) 
	{

		List<Cart> list =cartService.findByUserIdAndProid(2, proid);
		for (Cart cart : list) {
			cart.setQuantity((cart.getQuantity()) + 1);
			cartService.save(cart);
		}
		return viewCart(model);

	}
	@GetMapping("/updateQTT/{proid}/giam")
	public String updateCartGiam(ModelMap model,@PathVariable("proid")Integer proid) 
	{

		List<Cart> list =cartService.findByUserIdAndProid(2, proid);
		String message="Loi";
		for (Cart cart : list) {
			int old = cart.getQuantity();
			if(cart.getQuantity()>1) 
			{
				cart.setQuantity(old - 1);
				cartService.save(cart);
			}
			else {
				model.addAttribute("message",message);
			}
		}
		return viewCart(model);
=======
	/*
	@DeleteMapping("/deleteItem/{proid}")
    @ResponseBody
    public ResponseEntity<String> deleteItem(@PathVariable("proid") Integer proid) {
        List<Cart> list = cartService.findByUserIdAndProid(2, proid);
        for (Cart cart : list) {
            cartService.delete(cart);
        }
        return ResponseEntity.ok("Item deleted successfully");
    }
    */
	
	@PostMapping(value = "updateQTT")
	@ResponseBody
	public String updateCart(@RequestBody Map<String, Integer> requestBody) {
		User userLogged = userService.getUserLogged();
		
	    Integer proid = requestBody.get("proid");
	    Integer qtt = requestBody.get("quantity");

	    List<Cart> list = cartService.findByUserIdAndProid(userLogged.getUserId(), proid);
	    for (Cart cart : list) {
	        cart.setQuantity(qtt);
	        cartService.save(cart);
	    }
	    return proid + " " + qtt;
>>>>>>> dev
	}
	
}