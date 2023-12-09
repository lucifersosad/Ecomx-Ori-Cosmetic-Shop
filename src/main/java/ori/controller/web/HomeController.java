package ori.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import ori.entity.Brand;
import ori.entity.Category;
import ori.entity.Product;
import ori.service.IBrandService;
import ori.service.ICategoryService;
import ori.service.IProductService;

@RequestMapping(value = {"", "/", "home"})
@Controller
public class HomeController {
	@Autowired
	ICategoryService categoryService;
	@Autowired
	IBrandService brandService;
	@Autowired
	IProductService productService;

	@GetMapping()
	public String trangchu(ModelMap model) {
		List<Product> products = productService.findProductsMostSaleByCategory(); 
		List<Brand> brands = brandService.findAll();
		List<Category> categories = categoryService.findTop10(); 
		model.addAttribute("brands", brands);
		model.addAttribute("categories", categories);
		model.addAttribute("products", products);
		return "web/index";
	}
	
	@GetMapping("/checkout")
	public String checkout(ModelMap model) {
		return "web/checkout";
	}
}
