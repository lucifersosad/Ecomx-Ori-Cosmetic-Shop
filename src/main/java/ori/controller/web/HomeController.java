package ori.controller.web;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ori.config.scurity.AuthUser;
import ori.entity.Brand;
import ori.entity.Category;
import ori.entity.Product;
import ori.entity.User;
import ori.model.UserModel;
import ori.service.IBrandService;
import ori.service.ICategoryService;
import ori.service.IProductService;
import ori.service.IUserService;

@Controller
@RequestMapping(value = {"/", "home"})
public class HomeController {
	@Autowired(required=true)
	IUserService userService;
	
	@GetMapping("")
	public String trangchu(ModelMap model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof AuthUser) {
			String email = ((AuthUser)principal).getEmail();
			Optional<User> optUser = userService.findByEmail(email);
			if (optUser.isPresent()) {
				User user = optUser.get();
				model.addAttribute("user", user);
			}	
		} 
		return "web/index";
	}
	
	@GetMapping("my-account")
	public String myAccount(ModelMap model) {
		return "web/profile";
	}
}
