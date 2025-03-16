package com.sideproject.caregiver_management.frontend;

import com.sideproject.caregiver_management.user.service.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final TenantService tenantService;
    @RequestMapping("/account/info")
    public String home(){
        log.info("home controller");
        return "pages/account_info";
    }

    @RequestMapping("/login")
    public String index() {
        return "login";
    }
}
