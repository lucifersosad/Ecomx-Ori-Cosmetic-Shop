package ori.controller.admin;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import ori.entity.Brand;
import ori.entity.Category;
import ori.entity.Product;
import ori.model.ProductModel;
import ori.service.IBrandService;
import ori.service.ICategoryService;
import ori.service.IProductService;

@Controller

@RequestMapping("admin/products")

public class ProductController {
	@Autowired(required = true)
	IProductService productService;
	@Autowired(required = true)
	ICategoryService categoryService;
	@Autowired(required = true)
	IBrandService brandService;
	
	@RequestMapping("")
	public String list(ModelMap model) {
		List<Product> list = productService.findAll();
		model.addAttribute("products", list);
		return "admin/products/list";
	}
	
	@GetMapping("add")
	public String add(ModelMap model) {
		ProductModel proModel = new ProductModel();
		List<Category> categories = categoryService.findAll();
		List<Brand> brands = brandService.findAll();
		proModel.setIsEdit(false);
		
		model.addAttribute("product", proModel);
		model.addAttribute("categories", categories);
		model.addAttribute("brands", brands);
		return "admin/products/addOrEdit";

	}
	
	@GetMapping("edit/{proId}")

	public ModelAndView edit(ModelMap model, @PathVariable("proId") Integer proId) {

		Optional<Product> optProduct = productService.findById(proId);

		ProductModel proModel = new ProductModel();

		if (optProduct.isPresent()) {

			Product entity = optProduct.get();

			BeanUtils.copyProperties(entity, proModel);
			
			List<Category> categories = categoryService.findAll();
			List<Brand> brands = brandService.findAll();
			
			proModel.setIsEdit(true);

			model.addAttribute("product", proModel);
			model.addAttribute("categories", categories);
			model.addAttribute("brands", brands);

			return new ModelAndView("admin/products/addOrEdit", model);

		}

		model.addAttribute("message", "Product is not existed!!!!");

		return new ModelAndView("forward:/admin/products", model);

	}

	@PostMapping("saveOrUpdate")

	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("product") ProductModel proModel, BindingResult result) {

		if (result.hasErrors()) {

			return new ModelAndView("admin/products/addOrEdit");

		}
		Product entity = new Product();

		BeanUtils.copyProperties(proModel, entity);
		entity.setCategory(categoryService.findById(proModel.getCateId()).get());
		entity.setBrand(brandService.findById(proModel.getBrandId()).get());

		productService.save(entity);

		String message = "";

		if (proModel.getIsEdit() == true) {

			message = "Product is Edited!!!!!!!!";

		} else {

			message = "Product is saved!!!!!!!!";

		}

		model.addAttribute("message", message);

		return new ModelAndView("forward:/admin/products", model);

	}

	@GetMapping("delete/{proId}")

	public ModelAndView delete(ModelMap model, @PathVariable("proId") Integer productId) {

		productService.deleteById(productId);

		model.addAttribute("message", "Product is deleted!!!!");

		return new ModelAndView("redirect:/admin/products", model);

	}
}
