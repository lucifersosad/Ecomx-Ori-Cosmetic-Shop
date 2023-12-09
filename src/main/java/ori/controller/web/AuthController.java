package ori.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ori.common.enums.UserRole;
import ori.entity.Roles;
import ori.entity.User;
import ori.model.LoginRequest;
import ori.model.SignUpRequest;
import ori.repository.RoleRepository;
import ori.service.IUserService;
import ori.utils.AppUtil;

import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;


@Controller
@Slf4j
public class AuthController {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    IUserService userService;
    @Autowired
    AppUtil appUtil;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;


    @RequestMapping(path = "/auth/login",  method = RequestMethod.GET)
    public String login(Model model, Principal principal, @RequestParam(name = "message", required = false) String message, HttpServletRequest request) {
    	if (principal != null) {
        	return "redirect:/";
        }
    	String loginStatus = (String) request.getSession().getAttribute("loginStatus");
    	
    	if ("error".equals(message) && "failure".equals(loginStatus)) {
            model.addAttribute("errorMessage", "Tài khoản hoặc mật khẩu không đúng");
        }
        request.getSession().removeAttribute("loginStatus");
        return "web/auth/login";
    }
    @RequestMapping(path = "/auth/login1", method = RequestMethod.GET)
    public String user(Model model) {

        return "web/auth/security";
    }



    @RequestMapping(path = "/auth/sign-up", method = RequestMethod.GET)
    public String signUpForm(Model model, Principal principal, HttpServletRequest request) {
    	if (principal != null) {
    		return "redirect:/";
    	}
        SignUpRequest user = new SignUpRequest();
        System.out.println("[GET] signUpForm");
        model.addAttribute("user", user);
        String errorMessage = (String) request.getSession().getAttribute("errorMessage");
        if ("error".equals(errorMessage)) {
        	model.addAttribute("errorMessage", "Đăng kí thất bại");
        }
        request.getSession().removeAttribute("errorMessage");
        return "web/auth/signUp";
    }

    @PostMapping(path = "/auth/sign-up/save")
    public String signUpPostForm(@ModelAttribute("user") SignUpRequest userReq, BindingResult result, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        User user = userService.findByEmail(userReq.getEmail()).orElse(null);
        if(user != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        Set<Roles> role = new HashSet<>();
        Roles roleuser =roleRepository.findById(1).orElse(null);
        if(roleuser !=null){
            role.add(roleuser);
        }

        user = User.builder()
                .active(false)
                .username(".")
                .address(userReq.getAddress())
                .phone(userReq.getPhone())
                .email(userReq.getEmail())
                .fullName(userReq.getFullName())
                .username(userReq.getUsername())
                .roles(role)
                .build();
        if (result.hasErrors()) {
            model.addAttribute("user", user);
            request.getSession().setAttribute("errorMessage", "error");
            return "redirect:/auth/sign-up";
        }

        user.setPasswordHash(passwordEncoder.encode(userReq.getPasswordHash()));

        userService.save(user);
        return "redirect:/auth/login";
    }

    @GetMapping("/auth/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/auth/login?logout";
    }
}