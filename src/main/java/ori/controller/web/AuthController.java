package ori.controller.web;

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


    @RequestMapping(path = "/auth/login", method = RequestMethod.GET)
    public String login(Model model) {
//        System.out.println("[GET] login");
//        LoginRequest login = new LoginRequest();
//        model.addAttribute("login", login);
        return "web/auth/login";
    }

//    @PostMapping(path = "/auth/login-handler")
//    public String loginHandle(@ModelAttribute("login") LoginRequest loginRequest) {
//        User user = userService.findByEmail(loginRequest.getUsername()).orElse(null);
//        if(user != null) {
//            boolean isTrue = appUtil.checkPassword(loginRequest.getPassword(), user);
//            if(isTrue) {
//                return "redirect:/home";
//            }
//        }
//        return "web/auth/signUp";
//    }

    @RequestMapping(path = "/auth/sign-up", method = RequestMethod.GET)
    public String signUpForm(Model model) {
        SignUpRequest user = new SignUpRequest();
        System.out.println("[GET] signUpForm");
        model.addAttribute("user", user);
        return "web/auth/signUp";
    }

    @PostMapping(path = "/auth/sign-up/save")
    public String signUpPostForm(@ModelAttribute("user") SignUpRequest userReq, BindingResult result, Model model) {
        User user = userService.findByEmail(userReq.getEmail()).orElse(null);
        if(user != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        Set<Roles> role = new HashSet<>();
        Roles roleuser =roleRepository.findById(1).orElse(null);
        if(roleuser !=null){
            role.add(roleuser);
        }

//        role.add(Roles.builder()
//                .role(UserRole.ADMIN)
//                .build());

//               role.add(Roles.builder()
//                .role(UserRole.USER)
//                .build());

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
            return "web/auth/signUp";
        }

//        String pwSalt = appUtil.generateSalt();
//        user.setPasswordSalt(pwSalt);
//        String pwHash = appUtil.generatePasswordHash(userReq.getPasswordHash(), user);
//        user.setPasswordSalt(pwSalt);
        user.setPasswordHash(passwordEncoder.encode(userReq.getPasswordHash()));

        userService.save(user);
        return "redirect:/auth/sign-up?success";
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
