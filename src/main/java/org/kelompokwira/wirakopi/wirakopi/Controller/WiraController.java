package org. kelompokwira.wirakopi.wirakopi.Controller;

import java.util.ArrayList;
import java.util.List;

import org.kelompokwira.wirakopi.wirakopi.Entity.Stuff;
import org.kelompokwira.wirakopi.wirakopi.Entity.User;
import org.kelompokwira.wirakopi.wirakopi.Entity.UserAuthorities;
import org.kelompokwira.wirakopi.wirakopi.Entity.UserSafe;
import org.kelompokwira.wirakopi.wirakopi.Entity.UserStuff;
import org.kelompokwira.wirakopi.wirakopi.HTMLJava.HTML;
import org.kelompokwira.wirakopi.wirakopi.JsonObject.JsonStuff;
import org.kelompokwira.wirakopi.wirakopi.JsonObject.StuffArray;
import org.kelompokwira.wirakopi.wirakopi.Repository.AuthRepo;
import org.kelompokwira.wirakopi.wirakopi.Repository.UserStuffRepo;
import org.kelompokwira.wirakopi.wirakopi.Repository.WiraRepo;
import org.kelompokwira.wirakopi.wirakopi.Service.UserService;
import org.kelompokwira.wirakopi.wirakopi.WirakopiApplication.Something;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    @Autowired
    private UserStuffRepo stuffRepo;

    public WiraController(UserService Services){
        this.service = Services;
    }

    @RequestMapping("/")
    public ModelAndView homePage() {
        return new ModelAndView("index");
    }

//===============================================================================================
//            DISINI ADALAH SEBUAH KONTROL UNTUK PAGE - HERE CONTROL FOR YOUR HEADACHE
//===============================================================================================
    //===================================================== 
    @RestController
    @RequestMapping("/static")    
    class StaticControl{
        @RequestMapping("/about.html")
        protected ModelAndView about() {
            return new ModelAndView("about");
        }
        @RequestMapping("/TOS.html")
        protected ModelAndView TOS() {
            return new ModelAndView("TOS");
        }
        @RequestMapping("/Privacy.html")
        protected ModelAndView Privasi() {
            return new ModelAndView("Privacy");
        }
        @RequestMapping("/FAQ.html")
        protected ModelAndView FAQ() {
            return new ModelAndView("FAQ");
        }
        @RequestMapping("/product")
        protected ModelAndView requestMethodName(@RequestParam(name = "product", required = false) String param, @AuthenticationPrincipal User user) {
            int ammount = 0;
            if(user != null)
                if(!stuffRepo.findByUser(user).isEmpty()){
                    UserStuff uStuff = stuffRepo.findByUser(user).getFirst();
                    JsonStuff jsonStuff = uStuff.getJsonContent();
                    for (StuffArray stuff : jsonStuff.getStuffArray()) {
                        ammount += stuff.getAmount();
                    }
                }
            
            String paramNull = param == null ? "" : param;
            return new ModelAndView("product").addObject("product", paramNull).addObject("ammount", ammount);
        }
        @RequestMapping("/menu")
        protected ModelAndView yeet(@AuthenticationPrincipal User user) {
            int ammount = 0;
            if(user != null)
                if(!stuffRepo.findByUser(user).isEmpty()){
                    UserStuff uStuff = stuffRepo.findByUser(user).getFirst();
                    JsonStuff jsonStuff = uStuff.getJsonContent();
                    for (StuffArray stuff : jsonStuff.getStuffArray()) {
                        ammount += stuff.getAmount();
                    }
                }
            return new ModelAndView("menu").addObject("ammount", ammount);
        }
    }
    //=====================================================

    //THIS AUTH THING GIVE ME A HEADACHE - KONTROL UNTUK AUTENTIKASI
    @RestController
    @RequestMapping("/auth")
    class Authentication{
        @GetMapping("/login")
        protected ModelAndView josAndView(@AuthenticationPrincipal User user) {
            if(user == null) return new ModelAndView("SignIn_Page");
            return new ModelAndView("redirect:/");
        }
        
        @GetMapping("/lupa")
        protected ModelAndView lupa(@AuthenticationPrincipal User user) {
            if(user != null) return new ModelAndView("redirect:/");
            return new ModelAndView("Forget_Password").addObject("user", new UserSafe());
        }

        @PostMapping("/lupa")
        protected ModelAndView ingat(@Valid @ModelAttribute("user") UserSafe userSafe) {
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
        
        @GetMapping("/WiraSuruhFigma")
        protected ModelAndView FigmaWoi(@AuthenticationPrincipal User user) {
            if(user != null) return new ModelAndView("redirect:/");
            return (new ModelAndView("Verify"));
        }

        @PostMapping("/trueVerify")
        protected ModelAndView trueVerify(@RequestParam("usernameOTP") String username, @RequestParam("OTP") String OTP, @RequestParam(name = "action", required = false) String getAction) {
            User user = null;
            if(userRepo.findByUsername(username).isEmpty()) return new ModelAndView("SignUp_Page").addObject("error", "Username untuk diverifikasi tidak ditemukan");
            user = userRepo.findByUsername(username).getFirst();
            if(!user.getOTP().toString().equals(OTP) || user.getOTP() == ((long) 0)) return (new ModelAndView("Verify").addObject("error", user.getOTP().toString().equals("0") ? "User telah diverifikasi" : "OTP tidak valid").addObject("user", username));
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
        

        @GetMapping("/register")
        protected ModelAndView regisGet(@Valid ModelAndView modelAndView, @AuthenticationPrincipal User user){
            if(user != null) return new ModelAndView("redirect:/");
            modelAndView.addObject("user", new UserSafe());
            modelAndView.setViewName("SignUp_Page");
            return modelAndView;
        }

        @PostMapping("/register")
        protected ModelAndView regisPost(@Valid @ModelAttribute("user") UserSafe userSafe, BindingResult result) {
            
            if(result.hasErrors()) return (new ModelAndView("index"));
            if(!userSafe.getPassword().equals(userSafe.getRePassword())) return new ModelAndView("SignUp_Page").addObject("error", "Password and Re-Password doesn't match");
            String randomShit =  String.valueOf(Math.round(Math.random() * (999999 - 111111)) + 111111);
            String WiraGantengSedunia = service.sendOTP(userSafe.getEmail(), randomShit);
            if(!WiraGantengSedunia.equals("Complete")) return (new ModelAndView("SignUp_Page").addObject("error", WiraGantengSedunia.equals("Complete") ? null : WiraGantengSedunia));
            User user = new User();
            user.setName(userSafe.getName());
            user.setNo_telp(userSafe.getTelp());
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
    }
    //THANK YOU VERY MUCH AUTH YOU DONE GAVE ME A SUFFER - AKHIR DARI KONTROL AUTENTIKASI
    
    //HERE LAYS ALL THE ERROR THING - SEKUMPULAN KONTROL BODOH DISINI
    @RestController
    @RestControllerAdvice
    @RequestMapping("/error")
    class ErrorControl implements ErrorController{
        @GetMapping
        @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
        @ExceptionHandler(value = Exception.class)
        protected ModelAndView errorGraveyard(Exception e) {
            return new ModelAndView("500").addObject("error", (e != null) ?  "Error Message: " + e.getMessage() + "\nError Cause: " + e.getCause(): "Nothing");  
        }

        @GetMapping("/404")
        @ExceptionHandler(value = NoHandlerFoundException.class)
        protected ModelAndView notFound(Exception e) {
            return new ModelAndView("404").addObject("error", (e != null) ? e.getMessage() : "Nothing");  
        }

        @GetMapping("/errorForYou")
        protected ModelAndView errorForYou(HttpServletRequest request) {
            HttpSession session = request.getSession(false);
            AuthenticationException ex = (session != null) 
            ? (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) : null;
            return new ModelAndView("SignIn_Page").addObject("error", (ex != null) ? (ex.getMessage().equals("Bad Credentials") ? "Username atau Password tidak valid" : ex.getMessage()) : null);
        }

        @GetMapping("/forbidYou")
        protected ModelAndView forbid(HttpServletRequest request, @AuthenticationPrincipal User user){
            HttpSession session = request.getSession(false);
            String errorMessage = null;
            String userRole = user != null ? user.getAuthorities().iterator().next().getAuthority() : "None";
            if (session != null) {
                AccessDeniedException ex = (AccessDeniedException) session
                        .getAttribute(WebAttributes.ACCESS_DENIED_403);
                errorMessage = ex != null ? ex.getMessage() : (userRole + ": " + request.isUserInRole("ROLE_"+userRole));
            }
            return new ModelAndView("forbidden").addObject("error", errorMessage);
        } 
    }
    //DONT UNDERESTIMATED MY POWER - TERIMA KASIH TELAH MEMBERIKAN SAYA PENGARAHAN

    //HERE WHAT THE USER CAN DO - KONTROL UNTUK PENGGUNA/USER DISINI  
    @RestController
    @RequestMapping("/user")
    class UserControl{
        @GetMapping("/jemBelanda")
        public ModelAndView jemBelanda(@RequestParam(name = "action", required = false) String param, @AuthenticationPrincipal User user) {
            String div = "";
            if(user == null) return new ModelAndView("SignIn_Page").addObject("error", "Please put on some pants");
            if(!stuffRepo.findByUser(user).isEmpty()){
                Long totalDrink = 0L;
                UserStuff uStuff = stuffRepo.findByUser(user).getFirst();
                JsonStuff jsonStuff = uStuff.getJsonContent();
                for (StuffArray stuff : jsonStuff.getStuffArray()) {
                    totalDrink += (stuff.getAmount() + Stuff.getDrinkNameEnum(stuff.getDrinkName()).getDrinkPrice()) * 1000;
                    div += HTML.divItemsBuilder(HTML.getImage(stuff.getDrinkName()), Stuff.getDrinkNameEnum(stuff.getDrinkName()), stuff.getAmount());
                }
                return new ModelAndView("checkout").addObject("div", div).addObject("totalDrink", totalDrink).addObject("total", totalDrink+10000L);
            }
            ModelAndView model = new ModelAndView("redirect:../static/menu");
            model.addObject("messageError", "error");
            model.addObject("message", "Please look on our magnificient coffee");
            return model;
        }
        
        // @PostMapping("/jemBelanda")
        // public ModelAndView jemBelanda(@RequestParam String param) {
            
        // }

        @GetMapping("/userProfile")
        public ModelAndView getMethodName() {
            return new ModelAndView("userProfile");
        }

        @PostMapping("/addcart")
        public ModelAndView postMethodName(@RequestParam(name = "product", required = false) String product, @AuthenticationPrincipal User user) {
            List<UserStuff> stuffOptional = stuffRepo.findByUser(user);
            UserStuff stuff = (!stuffOptional.isEmpty() ? stuffOptional.getFirst() : new UserStuff()) ;
            JsonStuff jStuff = new JsonStuff();
            StuffArray aStuff = new StuffArray(product);
            if(!stuffOptional.isEmpty()){
                jStuff = stuff.getJsonContent();
                List<StuffArray> stuffArrays = jStuff.getStuffArray();
                aStuff = stuffArrays.stream()
                     .filter(aStone -> aStone.getDrinkName().equals(product))
                     .findFirst()
                     .orElse(null);
                if(aStuff != null){
                    aStuff.setAmount(aStuff.getAmount()+1);
                    stuffArrays.removeIf(stuffArray -> (stuffArray.getDrinkName().equals(product)));
                    stuffArrays.add(aStuff);
                    jStuff.setStuffArray(stuffArrays);
                }else{
                    aStuff = new StuffArray(product);
                    stuffArrays.add(aStuff);
                    jStuff.setStuffArray(stuffArrays);
                }
            }else{
                List<StuffArray> stuffArrays = new ArrayList<>();
                stuffArrays.add(aStuff);
                jStuff.setStuffArray(stuffArrays);
            }
            stuff.setJsonContent(jStuff);
            stuff.setUser(user);
            stuffRepo.save(stuff);
            return new ModelAndView("redirect:../static/product").addObject("product", product);
        }
    }
    //USER CAN DO NOTHING BECAUSE IM LAZY AF - KONTROL UNTUK USER/PENGGUNA SELESAI

//===============================================================================================
//                   AKHIR DARI KONTROL PAGE - END OF THE HEADACHE THANK YOU
//===============================================================================================
}
