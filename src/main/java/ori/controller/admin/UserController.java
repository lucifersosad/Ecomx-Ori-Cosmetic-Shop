package ori.controller.admin;

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

import java.util.List;
import java.util.Optional;

import ori.entity.User;
import ori.model.UserModel;
import ori.service.IUserService;

@Controller
@RequestMapping("admin/users")
public class UserController {
	@Autowired(required=true)
	IUserService userService;
	@RequestMapping("")
	public String list(ModelMap model) {
		List<User> list = userService.findAll();
		model.addAttribute("user",list);
		return "admin/users/list";
	}
	@GetMapping("add")

	public String add(ModelMap model) {
		UserModel userModel = new UserModel();
		userModel.setIsEdit(false);
		model.addAttribute("user", userModel);
		return "admin/users/addOrEdit";
	}

	@GetMapping("edit/{userId}")

	public ModelAndView edit(ModelMap model, @PathVariable("userId") Integer userId) {
		Optional<User> optUser = userService.findById(userId);
		UserModel userModel = new UserModel();
		if (optUser.isPresent()) {
			User entity = optUser.get();
			BeanUtils.copyProperties(entity, userModel);
			userModel.setIsEdit(true);
			model.addAttribute("user", userModel);
			return new ModelAndView("admin/users/addOrEdit", model);
		}
		model.addAttribute("message", "User is not existed!!!!");
		return new ModelAndView("forward:/admin/users", model);

	}
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("user") UserModel userModel, BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("admin/users/error");
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
		return new ModelAndView("forward:/admin/users", model);

	}
	@GetMapping("delete/{userId}")
	public ModelAndView delet(ModelMap model, @PathVariable("userId") Integer userId) {
		userService.deleteById(userId);
		model.addAttribute("message", "User is deleted!!!!");
		return new ModelAndView("redirect:/admin/users", model);
	}
}
