package com.sideproject.caregiver_management.frontend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class MainController {

    @RequestMapping("/account/info")
    public String home(){
        log.info("home controller");
        return "pages/account_info";
    }
}
