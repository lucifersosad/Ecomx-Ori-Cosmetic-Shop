package ori.controller.web;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import ori.entity.User;
import ori.model.UserModel;
import ori.service.IUserService;

@Controller
@RequestMapping("web/users")
public class WebUserController {
	@Autowired(required=true)
	IUserService userService;
	
	@GetMapping("edit/{userId}")
	public ModelAndView edit(ModelMap model, @PathVariable("userId") Integer userId) {
		Optional<User> optUser = userService.findById(userId);
		UserModel userModel = new UserModel();
		if (optUser.isPresent()) {
			User entity = optUser.get();
			BeanUtils.copyProperties(entity, userModel);
			userModel.setIsEdit(true);
			model.addAttribute("user", userModel);
			return new ModelAndView("web/users/addOrEdit", model);
		}
		model.addAttribute("message", "User is not existed!!!!");
		return new ModelAndView("forward:/web/users/", model);

	}
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("user") UserModel userModel, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("web/users/error");
		}
		User entity = new User();

		BeanUtils.copyProperties(userModel, entity);
		userService.updateUser(entity);

		String message = "";
		if (userModel.getIsEdit() == true) {
			message = "User is Edited!!!!!!!!";
		} else {
			message = "User is saved!!!!!!!!";
		}
		model.addAttribute("message", message);
//redirect về URL controller
		return new ModelAndView("/web/users/infor", model);

	}
	@GetMapping("/infor/{email}")
	public ModelAndView infor(ModelMap model, @PathVariable("email") String email) {
	    Optional<User> optUser = userService.findByEmail(email);

	    if (optUser.isPresent()) {
	        User user = optUser.get();
        	UserModel userModel = new UserModel();
        	BeanUtils.copyProperties(user, userModel);
			userModel.setIsEdit(true);
			String add = user.getAddress();
			String[] tmp = add.split(",");
            if(tmp.length == 4) {
            	 model.addAttribute("subAdd",tmp[0]);
            	 model.addAttribute("city",tmp[3]);
                 model.addAttribute("district",tmp[2]);
                 model.addAttribute("town",tmp[1]);
            }else {

            }
			model.addAttribute("user", userModel);
            return new ModelAndView("web/users/infor", model);
	       
	    }
	    model.addAttribute("message", "User is not existed!!!!");
	    return new ModelAndView("forward:/web/users/", model);
	}
	@PostMapping("/updateAddress/{email}")
	public ModelAndView updateAddress(ModelMap model,
	                                   @PathVariable("email") String email,
	                                   @RequestParam("city") String city,
	                                   @RequestParam("district") String district,
	                                   @RequestParam("town") String town) {
	    Optional<User> optUser = userService.findByEmail(email);

	    if (optUser.isPresent()) {
	        User user = optUser.get();
	        UserModel userModel = new UserModel();
        	BeanUtils.copyProperties(user, userModel);
        	userModel.setIsEdit(true);
	        String address = city + ", " + district + ", " + town;
	        user.setAddress(address);
	        userService.updateUser(user);
	        model.addAttribute("message", "Address is updated!!!");
	        model.addAttribute("user", userModel);
	        return new ModelAndView("forward:web/users/infor", model);
	    }
	    return new ModelAndView("forward:/web/users/", model);
	}

	
}