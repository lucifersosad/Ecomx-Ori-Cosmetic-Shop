package ori.controller.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;
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
import ori.service.ICategoryService;
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
	@Autowired(required = true)
	ICategoryService categoryService;
	
	@GetMapping("/{cateID}/page/{pageNo}")
	public String viewProduct(
			ModelMap model,
			@PathVariable("cateID") Integer cateID,
            @PathVariable("pageNo") Integer pageNo) {		
		List<Category> listCate = categoryService.findAll();
		model.addAttribute("listAllCategory", listCate);	
		
		if (cateID == 0) {
			int pageSize = 27;
			int totalProducts = proService.findAll().size(); // Số lượng sản phẩm tổng cộng trong cơ sở dữ liệu
			int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
			
			int startPage, endPage;
		    if (totalPages <= 5) {
		        startPage = 1;
		        if (totalPages > 0)
		        	endPage = totalPages;
		        else endPage = 1;
		    } else {
		        if (pageNo <= 3) {
		            startPage = 1;
		            endPage = 5;
		        } else if (pageNo + 1 >= totalPages) {
		            startPage = totalPages - 4;
		            endPage = totalPages;
		        } else {
		            startPage = pageNo - 2;
		            endPage = pageNo + 2;
		        }
		    }
		    
		    model.addAttribute("startPage", startPage);
		    model.addAttribute("endPage", endPage);
			if (pageNo > totalPages) {
			    pageNo = totalPages; // Đặt pageNo bằng totalPages nếu vượt quá số trang thực tế
			}
			Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
			Page<Product> listPro = proService.findAll(pageable);
			model.addAttribute("cateID", cateID);
			model.addAttribute("countPro", totalProducts);
			model.addAttribute("listAllProduct", listPro);
			model.addAttribute("currentPage", pageNo);

			double minPrice = listPro.stream().mapToDouble(Product::getPrice).min().orElse(0);
			double maxPrice = listPro.stream().mapToDouble(Product::getPrice).max().orElse(0);
			model.addAttribute("min_price", minPrice);
			model.addAttribute("max_price", maxPrice);
		}
		else {		
			Optional<Category> optCate = categoryService.findById(cateID);
			if (optCate.isPresent()) {
				int pageSize = 27;
				int totalProducts = proService.findByCategory(optCate.get()).size(); // Số lượng sản phẩm tổng cộng trong cơ sở dữ liệu
				int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
				
				int startPage, endPage;
			    if (totalPages <= 5) {
			        startPage = 1;
			        if (totalPages > 0)
			        	endPage = totalPages;
			        else endPage = 1;
			    } else {
			        if (pageNo <= 3) {
			            startPage = 1;
			            endPage = 5;
			        } else if (pageNo + 1 >= totalPages) {
			            startPage = totalPages - 4;
			            endPage = totalPages;
			        } else {
			            startPage = pageNo - 2;
			            endPage = pageNo + 2;
			        }
			    }
			    
			    model.addAttribute("startPage", startPage);
			    model.addAttribute("endPage", endPage);
				if (pageNo > totalPages) {
				    pageNo = totalPages; // Đặt pageNo bằng totalPages nếu vượt quá số trang thực tế
				}
				Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
				Page<Product> listPro = proService.findByCategory(optCate.get(),pageable);
				model.addAttribute("cateID", cateID);
				model.addAttribute("countPro", totalProducts);
				model.addAttribute("listAllProduct", listPro);
				model.addAttribute("currentPage", pageNo);
				double minPrice = listPro.stream().mapToDouble(Product::getPrice).min().orElse(0);
				double maxPrice = listPro.stream().mapToDouble(Product::getPrice).max().orElse(0);
				model.addAttribute("min_price", minPrice);
				model.addAttribute("max_price", maxPrice);
			}	
		}
		return "web/product";
	}
	
	
	
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
		
		String input = proModel.getDescription();
		String description = input;
		int index = input.indexOf("Thành phần sản phẩm");

	    if (index != -1) {
	        String thanhPhanSanPham = input.substring(index + "Thành phần sản phẩm".length()).trim();
	        description = thanhPhanSanPham;
	    }
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
	            List<Cart> entityCart = cartService.findByUserId(userId);
	            int quantityCart = 0;
	            for (Cart cart : entityCart) {
					if(cart.getProduct().getProId()-proId ==0) {
						quantityCart = cart.getQuantity();	
					}
				}
	            System.out.println(quantityCart);
	            model.addAttribute("quantityCart", quantityCart);
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
		
		
		model.addAttribute("description", description);
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
	
	@GetMapping(value = "addToCart/{proId}&&{qty}")
    public ResponseEntity<String> addToCart(@PathVariable("proId") Integer proId, @PathVariable("qty") Integer qty) {
        try {
        	String successMessage = "Product added to cart successfully.";
        	
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
    				return new ResponseEntity<>(successMessage, HttpStatus.OK);
    			}
    		}
    		entityCart.setProduct(entity);
    		entityCart.setUser(userLogged);
    		entityCart.setId(cartkey);
    		entityCart.setQuantity(qty);
    		
    		cartService.addtocart(entityCart.getProduct().getProId(), entityCart.getUser().getUserId(), entityCart.getQuantity());
            
            return new ResponseEntity<>(successMessage, HttpStatus.OK);
            
        } catch (Exception e) {
            String errorMessage = "Error adding product to cart: " + e.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
