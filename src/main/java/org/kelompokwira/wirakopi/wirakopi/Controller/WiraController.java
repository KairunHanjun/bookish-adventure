package org.kelompokwira.wirakopi.wirakopi.Controller;

//import org.apache.commons.codec.digest.DigestUtils;
import org.kelompokwira.wirakopi.wirakopi.Entity.User;
import org.kelompokwira.wirakopi.wirakopi.Entity.UserAuthorities;
import org.kelompokwira.wirakopi.wirakopi.Entity.UserSafe;
import org.kelompokwira.wirakopi.wirakopi.Repository.AuthRepo;
import org.kelompokwira.wirakopi.wirakopi.Repository.WiraRepo;
import org.kelompokwira.wirakopi.wirakopi.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;






@EnableJdbcHttpSession
@RestController
public class WiraController {

    //============
    //OPEN METHOD
    //============

    // private void setTokenCSRF(Object request){
    //     HttpServletRequest httpReq = (HttpServletRequest) request;
    //     HttpServletResponse httpRes = (HttpServletResponse) response;
    //     String randomLong = ""+random.nextLong();
    //     Cookie cookie = new Cookie("csrfToken", randomLong);        
    //     httpRes.addCookie(cookie);
    //     next.doFilter(request, response);   
    // }

    //================
    //END OPEN METHOD
    //================
    private UserService service;
    @Autowired
    private WiraRepo userRepo;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AuthRepo authRepo;

    @Autowired
    public WiraController(UserService Services){
        this.service = Services;
    }

    // @InitBinder
    // void InitBinder(WebDataBinder peekyBinder){
    //     peekyBinder.setAllowedFields();
    // }

//===============================================================================================
//                              DISINI ADALAH SEBUAH KONTROL UNTUK PAGE
//===============================================================================================
    //HARUS ADA 
    @RequestMapping("/")
    public ModelAndView homePage() {
        return new ModelAndView("index");
    }
    @RequestMapping("/static/index")
    public ModelAndView homePageIndex() {
        return new ModelAndView("index");
    }
    @RequestMapping("/static/about.html")
    public ModelAndView about() {
        return new ModelAndView("about");
    }
    @RequestMapping("/static/TOS.html")
    public ModelAndView TOS() {
        return new ModelAndView("TOS");
    }
    @RequestMapping("/static/Privacy.html")
    public ModelAndView Privasi() {
        return new ModelAndView("Privacy");
    }
    @RequestMapping("/static/FAQ.html")
    public ModelAndView FAQ() {
        return new ModelAndView("FAQ");
    }

    // @GetMapping("/auth/login")
    // public ModelAndView login() {
    //     return new ModelAndView("SignIn_Page");
    // }
    // @PostMapping("/auth/login")
    // public ModelAndView loginPost() {
    //     return new ModelAndView("SignIn_Page");
    // }

    @GetMapping("/auth/login")
    public ModelAndView josAndView(@AuthenticationPrincipal User user) {
        if(user == null)
            return new ModelAndView("SignIn_Page").addObject("error", "Username atau password tidak valid");
        return new ModelAndView("redirect:/");
    }
    

    //MUlai dari sini adalah method untuk proses data
    @GetMapping("/auth/WiraSuruhFigma")
    public ModelAndView FigmaWoi(){
        return (new ModelAndView("Verify"));
    }

    @RequestMapping("/user/userProfile")
    public ModelAndView getMethodName() {
        return new ModelAndView("userProfile");
    }
    

    @PostMapping("/auth/trueVerify")
    public ModelAndView trueVerify(@RequestParam("usernameOTP") String username, @RequestParam("OTP") String OTP) {
        User user = null;
        if(userRepo.findByUsername(username).isEmpty()) return new ModelAndView("SignUp_Page").addObject("error", "Username untuk diverifikasi tidak ditemukan");
        user = userRepo.findByUsername(username).getFirst();
        if(!user.getOTP().toString().equals(OTP) || user.getOTP() == ((long) 0)) return (new ModelAndView("Verify").addObject("error", user.getOTP().toString().equals("0") ? "User telah diverifikasi" : "OTP tidak valid : " + user.getOTP()).addObject("user", username));
        user.setEnabled(true);
        user.setOTP((long) 0);
        service.updateUser(user);
        UserAuthorities auth = new UserAuthorities();
        auth.setAuthority("USER");
        auth.setUser(user);
        authRepo.save(auth);
        return new ModelAndView("SignIn_Page").addObject("info", "Register berhasil, Developer harap tidur");
    }
    

    @GetMapping("/auth/register")
    public ModelAndView regisGet(@Valid ModelAndView modelAndView){
        modelAndView.addObject("user", new UserSafe());
        modelAndView.setViewName("SignUp_Page");
        return modelAndView;
    }

    @PostMapping("/auth/register")
    public ModelAndView regisPost(@Valid @ModelAttribute("user") UserSafe userSafe, BindingResult result) {
        if(result.hasErrors()) return (new ModelAndView("redirect:/"));
        String randomShit =  String.valueOf(Math.round(Math.random() * (999999 - 111111)) + 111111);
        String WiraGantengSedunia = service.sendOTP(userSafe.getEmail(), randomShit);
        if(!WiraGantengSedunia.equals("Complete")) return (new ModelAndView("SignUp_Page").addObject("error", WiraGantengSedunia.equals("Complete") ? null : WiraGantengSedunia));
        User user = new User();
        user.setUsername(userSafe.getUsername());
        user.setPassword(userSafe.getPassword());
        user.setEmail(userSafe.getEmail());
        user.setPassword(encoder.encode(user.getPassword()));
        user.setOTP(Long.parseLong(randomShit));
        user.setEnabled(false);
        String WiraGanteng = service.register(user);
        return WiraGanteng.equals("Success") ? (new ModelAndView("Verify").addObject("user", userSafe.getUsername())) : (new ModelAndView("SignUp_Page").addObject("error", WiraGanteng.equals("Success") ? null : WiraGanteng));
    }
    //Verify/query?q="+user.Username

//===============================================================================================
//                                      AKHIR DARI KONTROL PAGE
//===============================================================================================
}
