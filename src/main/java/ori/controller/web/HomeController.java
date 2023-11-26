package ori.controller.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import ori.entity.Brand;
import ori.entity.Category;
import ori.entity.Product;
import ori.service.IBrandService;
import ori.service.ICategoryService;
import ori.service.IProductService;

@Controller
public class HomeController {
	@Autowired
	ICategoryService categoryService;
	@Autowired
	IBrandService brandService;
	@Autowired
	IProductService productService;

	@RequestMapping(value = {"/", "home"})
	public String trangchu(ModelMap model) {
		List<Product> products = productService.findProductsMostSaleByCategory(); 
		List<Brand> brands = brandService.findAll();
		List<Category> categories = categoryService.findTop10(); 
		model.addAttribute("brands", brands);
		model.addAttribute("categories", categories);
		model.addAttribute("products", products);
		return "web/index";
	}
}
