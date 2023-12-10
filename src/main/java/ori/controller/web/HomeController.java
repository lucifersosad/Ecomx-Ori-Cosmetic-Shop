package ori.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ori.entity.Brand;
import ori.entity.Cart;
import ori.entity.Category;
import ori.entity.Product;
import ori.entity.User;
import ori.service.IBrandService;
import ori.service.ICartService;
import ori.service.ICategoryService;
import ori.service.IProductService;
import ori.service.IUserService;

@RequestMapping(value = {"", "/", "home"})
@Controller
public class HomeController {
	@Autowired
	ICategoryService categoryService;
	@Autowired
	IBrandService brandService;
	@Autowired
	IProductService productService;
	@Autowired
	IUserService userService;
	@Autowired
	ICartService cartService;

	@GetMapping()
	public String trangchu(ModelMap model) {
		List<Product> products = productService.findProductsMostSaleByCategory(); 
		List<Brand> brands = brandService.findAll();
		List<Category> categories = categoryService.findTop10(); 
		User userLogged = userService.getUserLogged();
		if (userLogged != null) {
			List<Cart> list = cartService.findByUserId(userLogged.getUserId());
			model.addAttribute("cartQty", list.size());
		}
		model.addAttribute("brands", brands);
		model.addAttribute("categories", categories);
		model.addAttribute("products", products);
		return "web/index";
	}
	
	@GetMapping("/cartQty")
	@ResponseBody
	public int getCartQuantity() {
	    User userLogged = userService.getUserLogged();
	    if (userLogged != null) {
	        List<Cart> list = cartService.findByUserId(userLogged.getUserId());
	        return list.size();
	    }
	    return 0;
	}
}
