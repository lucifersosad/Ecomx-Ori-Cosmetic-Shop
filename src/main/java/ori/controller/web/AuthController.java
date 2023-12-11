package ori.controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
import ori.model.VerifyCodeRequest;
import ori.repository.RoleRepository;
import ori.service.IUserService;
import ori.utils.AppUtil;
import ori.utils.Email;

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
    @Autowired
    private Email email;

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


    @GetMapping(path = "/auth/verify-code")
    public String showVerifyCodePage(Model model) {
        model.addAttribute("verifyCodeRequest", new VerifyCodeRequest());
        return "web/auth/verify-code"; // Create a new HTML page for code verification
    }

    @PostMapping(path = "/auth/verify-code")
    public String verifyCode(@ModelAttribute("verifyCodeRequest") VerifyCodeRequest verifyCodeRequest, BindingResult result, Model model) {
        // Find the user by email
        User user = userService.findByEmail(verifyCodeRequest.getEmail()).orElse(null);

        if (user == null) {
            result.rejectValue("email", null, "Invalid email");
            return "web/auth/verify-code";
        }

        // Check if the entered code matches the generated code
        if (verifyCodeRequest.getCode().equals(user.getCode())) {
            // Update user's isEnabled status
            user.setIsEnabled(true);
            userService.save(user);
            return "redirect:/auth/login"; // Redirect to the login page after successful verification
        } else {
            result.rejectValue("code", null, "Invalid code");
            return "web/auth/verify-code";
        }
    }





    @PostMapping(path = "/auth/sign-up/save")
    public String signUpPostForm(@ModelAttribute("user") @Valid SignUpRequest userReq, BindingResult result,
                                 Model model, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // Thực hiện kiểm tra validation errors
        if (result.hasErrors()) {
            model.addAttribute("user", userReq);
            request.getSession().setAttribute("errorMessage", "error");
            return "redirect:/auth/sign-up";
        }

        // Kiểm tra xem email đã được đăng ký trước đó chưa
        User user = userService.findByEmail(userReq.getEmail()).orElse(null);
        if(user != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
            return "redirect:/auth/sign-up";
        }

        // Xử lý lưu thông tin người dùng mới
        Set<Roles> role = new HashSet<>();
        Roles roleuser = roleRepository.findById(1).orElse(null);
        if(roleuser != null){
            role.add(roleuser);
        }

        user = User.builder()
                .isEnabled(false)
                .username(".")
                .address(userReq.getAddress())
                .phone(userReq.getPhone())
                .email(userReq.getEmail())
                .fullName(userReq.getFullName())
                .username(userReq.getUsername())
                .roles(role)
                .build();

        user.setPasswordHash(passwordEncoder.encode(userReq.getPasswordHash()));
        String randomCode = email.getRandom();
        user.setCode(randomCode);
        email.sendEmail(user);

        userService.save(user);
        VerifyCodeRequest verifyCodeRequest = new VerifyCodeRequest();
        verifyCodeRequest.setEmail(userReq.getEmail());

        model.addAttribute("verifyCodeRequest", verifyCodeRequest);
        return "redirect:/auth/verify-code?="
                ;
    }


    @GetMapping("/auth/logout")
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/auth/login";
    }
}
