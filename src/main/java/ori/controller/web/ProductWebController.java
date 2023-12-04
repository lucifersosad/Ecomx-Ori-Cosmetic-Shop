package ori.controller.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ori.entity.Brand;
import ori.entity.Category;
import ori.entity.Product;

import ori.model.ProductModel;
import ori.service.IProductService;

@Controller
@RequestMapping("web/product")
public class ProductWebController {
	@Autowired(required = true)
	IProductService proService;
	
	@GetMapping("detail/{proId}")
	public ModelAndView detailProduct(ModelMap model, @PathVariable("proId") Integer proId) {
	
		Optional<Product> optProduct = proService.findById(proId);
		
		ProductModel proModel = new ProductModel();
	
		Product entity = optProduct.get();

		BeanUtils.copyProperties(entity, proModel);

		proModel.setIsEdit(true);
		
		Category enityCate = entity.getCategory();
		
		Brand enityBrand = entity.getBrand();

		List<Product> listBrand = proService.findByBrand(enityBrand.getBrandId());
		List<Product> listCate = proService.findByCategory(enityCate.getCateId());
		for (Product product : listBrand) {
			System.out.println(product.getName());
		}
		model.addAttribute("listCate", listCate);
		model.addAttribute("detailPro", proModel);
		model.addAttribute("listBrand", listBrand);
		return new ModelAndView("web/product-detail", model);
		
	}
}
