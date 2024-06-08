package org. kelompokwira.wirakopi.wirakopi.Controller;

import java.util.List;
import java.util.Optional;

//import org.apache.commons.codec.digest.DigestUtils;
import org.kelompokwira.wirakopi.wirakopi.Entity.User;
import org.kelompokwira.wirakopi.wirakopi.Entity.UserAuthorities;
import org.kelompokwira.wirakopi.wirakopi.Entity.UserSafe;
import org.kelompokwira.wirakopi.wirakopi.Repository.AuthRepo;
import org.kelompokwira.wirakopi.wirakopi.Repository.WiraRepo;
import org.kelompokwira.wirakopi.wirakopi.Service.UserService;
import org.kelompokwira.wirakopi.wirakopi.WirakopiApplication.Something;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



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
    //===================================================== 
    @RequestMapping("/")
    public ModelAndView homePage() {
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
    //=====================================================

    //=====================================================
    @GetMapping("/auth/login")
    public ModelAndView josAndView(@AuthenticationPrincipal User user) {
        if(user == null) return new ModelAndView("SignIn_Page");
        return new ModelAndView("redirect:/");
    }
    
    @GetMapping("/auth/lupa")
    public ModelAndView lupa(@AuthenticationPrincipal User user) {
        //TODO: if redirect by /auth/trueVerify then do something
        if(user != null) return new ModelAndView("redirect:/");
        return new ModelAndView("Forget_Password").addObject("user", new UserSafe());
    }

    @PostMapping("/auth/lupa")
    public ModelAndView ingat(@Valid @ModelAttribute("user") UserSafe userSafe) {
        if(userSafe == null || userSafe.getUsername().isEmpty()) return new ModelAndView("Forget_Password").addObject("user", new UserSafe()).addObject("error", "Username masih kosong");
        if(!userSafe.getUsername().isEmpty() && userSafe.getEmail().isEmpty()) {
            //Mencari User dengan Username
            List<User> fakeUser = userRepo.findByUsername(userSafe.getUsername());
            if(fakeUser.isEmpty()) return new ModelAndView("Forget_Password").addObject("user", new UserSafe()).addObject("error", "Username not found");
            User user = fakeUser.getFirst();
            
            //Mulai kirim OTP 
            if(user.isEnabled()) {
                String randomShit =  String.valueOf(Math.round(Math.random() * (999999 - 111111)) + 111111);
                String WiraGantengSedunia = service.sendOTP(user.getEmail(), "-"+randomShit);
                if(!WiraGantengSedunia.equals("Complete")) return (new ModelAndView("Forget_Password").addObject("error", WiraGantengSedunia));
                user.setOTP(-Long.parseLong(randomShit));
                user.setEnabled(true);
                userRepo.save(user);
                //userSafe.setEmail(randomShit);
                return new ModelAndView("Forget_Password").addObject("user", userSafe).addObject("send", "true");
            }
            return new ModelAndView("Verify").addObject("user", userSafe.getUsername()).addObject("error", "Harap verifikasi untuk kode OTP yang lama");
        }
        else if(!userSafe.getUsername().isEmpty() && !userSafe.getEmail().isEmpty()){
           //Mencari User dengan Username
           List<User> fakeUser = userRepo.findByUsername(userSafe.getUsername());
           if(fakeUser.isEmpty()) return new ModelAndView("Forget_Password").addObject("user", new UserSafe()).addObject("error", "Username not found");
           User user = fakeUser.getFirst();

           //Mulai mengganti user password
           if(/*user.isChangePassword()*/ user.isEnabled() && userSafe.getEmail().equals(user.getOTP().toString()) ){
                if(!userSafe.getPassword().equals(userSafe.getRePassword())) return new ModelAndView("Forget_Password").addObject("user", new UserSafe()).addObject("send", "true").addObject("error", "Password dan Re-Password tidak sesuai");
                String hasil = service.changeSomething(Something.Password, userSafe.getRePassword(), user.getUsername());
                if(!hasil.equals("Success")) return new ModelAndView("Forget_Password").addObject("user", userSafe).addObject("send", "true").addObject("error", hasil);
            }else{
                return new ModelAndView(!user.isEnabled() ? "Verify" : "Forget_Password").addObject("user", new UserSafe()).addObject("error", "Your account isn't enabled or OTP not valid");
            }
            user.setOTP(0L);
            userRepo.save(user);
        }
        
        return new ModelAndView("Forget_Password").addObject("user", new UserSafe()).addObject("yeay", "Password berhasil Diubah");
    }
    

    //MUlai dari sini adalah method untuk proses data
    @GetMapping("/auth/WiraSuruhFigma")
    public ModelAndView FigmaWoi(@AuthenticationPrincipal User user) {
        if(user != null) return new ModelAndView("redirect:/");
        return (new ModelAndView("Verify"));
    }

    @GetMapping("/user/userProfile")
    public ModelAndView getMethodName() {
        return new ModelAndView("userProfile");
    }
    
    //HERE LAYS ALL ERROR BULLSHIT
    @GetMapping("/auth/error")
    public ModelAndView getMethodName(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        return new ModelAndView("SignIn_Page").addObject("error", errorMessage);
    }
    @GetMapping("/auth/forbidYou")
    public ModelAndView forbid(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AccessDeniedException ex = (AccessDeniedException) session
                    .getAttribute(WebAttributes.ACCESS_DENIED_403);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }
        return new ModelAndView("forbidden").addObject("error", errorMessage);
    }
    //DONT UNDERESTIMATED MY POWER

    @PostMapping("/auth/trueVerify")
    public ModelAndView trueVerify(@RequestParam("usernameOTP") String username, @RequestParam("OTP") String OTP, @RequestParam(name = "action", required = false) String getAction) {
        User user = null;
        if(userRepo.findByUsername(username).isEmpty()) return new ModelAndView("SignUp_Page").addObject("error", "Username untuk diverifikasi tidak ditemukan");
        user = userRepo.findByUsername(username).getFirst();
        if(!user.getOTP().toString().equals(OTP) || user.getOTP() == ((long) 0)) return (new ModelAndView("Verify").addObject("error", user.getOTP().toString().equals("0") ? "User telah diverifikasi" : "OTP tidak valid : " + user.getOTP()).addObject("user", username));
        //Check if resend OTP, if ANY
        if(getAction != null){
            if(getAction.equals("resendotp") && user.getOTP() != 0L){
            //Resend OTP Here
                String result = service.sendOTP(user.getEmail(), user.getOTP().toString());
                boolean result2 = result.equals("Complete");
                return new ModelAndView("Verify").addObject(result2 ? "info" : "error", result2 ? "OTP has been Resend" : result);
            }
        }
        if(user.getOTP() < 0L){
            UserSafe amanDah = new UserSafe();
            amanDah.setEmail(user.getOTP().toString());
            amanDah.setUsername(user.getUsername());
            return new ModelAndView("Forget_Password").addObject("send", "true").addObject("user", amanDah);
        }
        user.setEnabled(true);
        user.setOTP((long) 0);
        userRepo.save(user);
        UserAuthorities auth = new UserAuthorities();
        auth.setAuthority("USER");
        auth.setUser(user);
        authRepo.save(auth);
        return new ModelAndView("SignIn_Page").addObject("info", "Register berhasil, Developer harap tidur");
    }
    

    @GetMapping("/auth/register")
    public ModelAndView regisGet(@Valid ModelAndView modelAndView, @AuthenticationPrincipal User user){
        if(user != null) return new ModelAndView("redirect:/");
        modelAndView.addObject("user", new UserSafe());
        modelAndView.setViewName("SignUp_Page");
        return modelAndView;
    }

    @PostMapping("/auth/register")
    public ModelAndView regisPost(@Valid @ModelAttribute("user") UserSafe userSafe, BindingResult result) {
        if(result.hasErrors()) return (new ModelAndView("redirect:/"));
        // System.out.println("Password: " + userSafe.getPassword() +
        //                    "\nRePassword: " + userSafe.getRePassword());
        // System.out.println(!userSafe.getPassword().equals(userSafe.getRePassword()));
        if(!userSafe.getPassword().equals(userSafe.getRePassword())) return new ModelAndView("SignUp_Page").addObject("error", "Password and Re-Password doesn't match");
        String randomShit =  String.valueOf(Math.round(Math.random() * (999999 - 111111)) + 111111);
        String WiraGantengSedunia = service.sendOTP(userSafe.getEmail(), randomShit);
        if(!WiraGantengSedunia.equals("Complete")) return (new ModelAndView("SignUp_Page").addObject("error", WiraGantengSedunia.equals("Complete") ? null : WiraGantengSedunia));
        User user = new User();
        user.setId(Long.parseLong(randomShit));
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
