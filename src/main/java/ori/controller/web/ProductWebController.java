package ori.controller.web;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ori.config.scurity.AuthUser;
import ori.entity.Brand;
import ori.entity.Cart;
import ori.entity.CartKey;
import ori.entity.Category;
import ori.entity.Product;
import ori.entity.ShoppingSession;
import ori.entity.ShoppingSessionKey;
import ori.entity.User;
import ori.model.ProductModel;
import ori.model.UserModel;
import ori.service.ICartService;
import ori.service.IProductService;
import ori.service.IShoppingSessionService;
import ori.service.IUserService;
import ori.service.ShoppingSessionServiceImpl;

@Controller
@RequestMapping("web/product")
public class ProductWebController {
	@Autowired(required = true)
	IProductService proService;
	@Autowired(required = true)
	IShoppingSessionService ssService;
	@Autowired(required=true)
	IUserService userService;
	@Autowired(required=true)
	ICartService cartService;
	
	@GetMapping("detail/{proId}")
	public ModelAndView detailProduct(ModelMap model, @PathVariable("proId") Integer proId) {
	
		Optional<Product> optProduct = proService.findById(proId);
		
		ProductModel proModel = new ProductModel();
	
		Product entity = optProduct.get();

		BeanUtils.copyProperties(entity, proModel);

		proModel.setIsEdit(true);
		
		Category enityCate = entity.getCategory();
		
		Brand enityBrand = entity.getBrand();

		List<Product> listBrand = proService.findByBrand(enityBrand.getBrandId(), proModel.getPrice());
		List<Product> listCate = proService.findByCategory(enityCate.getCateId(), proModel.getPrice());
		
		
		LocalDate localDate = LocalDate.now();

        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		
        int userId = -1;
        ShoppingSession entitySS = new ShoppingSession();
        ShoppingSessionKey ssKey = new ShoppingSessionKey();
        Object authen = SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
        if (authen instanceof AuthUser) {
	        String email = ((AuthUser)authen) .getEmail();
	        Optional<User> optUser = userService.findByEmail(email);
	        if (optUser.isPresent()) {
	        	UserModel userModel = new UserModel();
	        	User user = optUser.get();
	        	BeanUtils.copyProperties(user, userModel);
	        	userModel.setIsEdit(true);
	        	userId = userModel.getUserId();
	        	entitySS.setUser(user);
	        	entitySS.setId(ssKey);
	            entitySS.setProduct(entity);
	            entitySS.setDate(formattedDate);
	        }
        }
        int flagSave = 1;
        if(userId == -1) {
        	flagSave = 0;
        }
        //System.out.println(proId);
        List<ShoppingSession> listSS = ssService.findByUser(userId);
        List<ShoppingSession> listSSAll = ssService.findAll();
        for (ShoppingSession shoppingSession : listSSAll) {
        	Product pro = shoppingSession.getProduct();
        	System.out.println(pro.getProId());
			if(pro.getProId() - proId == 0) {
				flagSave = 0;
				System.out.println("yes");
				break;
			}
				
		}
        if(flagSave > 0) {
        	ssService.save(entitySS);
        }
		List<Product> listProSeen = new ArrayList<Product>();
		for (ShoppingSession shoppingSession : listSS) {
			Product proSeen = shoppingSession.getProduct();
			listProSeen.add(proSeen);
		}
           
		proModel.setOldPrice(proModel.getPrice());
		proModel.setPrice(Math.round(proModel.getPrice() * (100 - proModel.getSale()) / 100));
		String brandName = enityBrand.getName();
		String cateName = enityCate.getName();
		
		model.addAttribute("isHasUser", userId);
		model.addAttribute("cateName",cateName);
		model.addAttribute("brandName", brandName);
		model.addAttribute("listProSeen", listProSeen);
		model.addAttribute("listCate", listCate);
		model.addAttribute("detailPro", proModel);
		model.addAttribute("listBrand", listBrand);
		return new ModelAndView("web/product-detail", model);
		
	}
	@GetMapping(value = "add-to-cart/{proId}&&{qty}")
	public String addToCart(ModelMap model, @PathVariable("proId") Integer proId, @PathVariable("qty") Integer qty) {
		Optional<Product> optProduct = proService.findById(proId);
		
		ProductModel proModel = new ProductModel();
	
		Product entity = optProduct.get();

		BeanUtils.copyProperties(entity, proModel);

		proModel.setIsEdit(true);
		User userLogged = userService.getUserLogged();
		
		CartKey cartkey = new CartKey();
		Cart entityCart = new Cart();

		List<Cart> list = cartService.findByUserId(userLogged.getUserId());
		int quatity = qty;
		for (Cart cart : list) {
			System.out.println(cart.getProduct().getProId());
			if(cart.getProduct().getProId() - proId == 0) {
				quatity = cart.getQuantity()+qty;
				cart.setQuantity(quatity);
				cartService.save(cart);
				return "redirect:/cart";
			}
		}
		entityCart.setProduct(entity);
		entityCart.setUser(userLogged);
		entityCart.setId(cartkey);
		entityCart.setQuantity(qty);
		
		cartService.addtocart(entityCart.getProduct().getProId(), entityCart.getUser().getUserId(), entityCart.getQuantity());
		return "redirect:/cart";
	}
}
