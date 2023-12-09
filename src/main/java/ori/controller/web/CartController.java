package ori.controller.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ori.entity.Cart;
import ori.entity.Product;
import ori.model.CartModel;
import ori.model.ProductModel;
import ori.service.ICartService;
import ori.service.IProductService;
import ori.service.IUserService;

@Controller
@RequestMapping("/cart")
public class CartController
{
	@Autowired(required=true)
	IUserService userService;
	@Autowired(required=true)
	ICartService cartService;
	@Autowired(required=true)
	IProductService productService;
	@GetMapping("")
	public String viewCart(ModelMap model) 
	{
		
    	List<Cart> list= cartService.findByUserId(2);
		List<ProductModel> listp = new ArrayList<>();
		List<CartModel> listc = new ArrayList<>();
		List<Double> tong=new ArrayList<>();
		double sum =0;
		
		for (Cart cart : list) {
		    Product pro = cart.getProduct();
		    ProductModel productModel = new ProductModel();
		    CartModel cartModel = new CartModel();
		    productModel.setImage_link(pro.getImage_link());
		    productModel.setName(pro.getName());
		    productModel.setPrice(pro.getPrice());
		    productModel.setProId(pro.getProId());
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
	@GetMapping("/deleteItem/{proid}")
	public String deleteItem(ModelMap model, @PathVariable("proid") Integer proid) 
	{
		List<Cart> list =cartService.findByUserIdAndProid(2, proid);
		for (Cart cart : list) {
			cartService.delete(cart);
		}
		return viewCart(model);
	}
	@GetMapping(value = "deleteCart")
	public String deleteCart(ModelMap model) 
	{
		List<Cart> list =cartService.findByUserId(1);
		for (Cart cart : list) {
			cartService.delete(cart);
		}
	
		return "web/index";
	}
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
	}
}