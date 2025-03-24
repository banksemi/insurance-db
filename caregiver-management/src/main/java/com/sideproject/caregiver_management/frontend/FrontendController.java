package com.sideproject.caregiver_management.frontend;

import com.sideproject.caregiver_management.user.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class FrontendController {
    private final TenantService tenantService;
    @RequestMapping("/account/info")
    public String home(){
        return "page/account_info";
    }

    @RequestMapping("/login")
    public String index() {
        return "login";
    }

    @RequestMapping("/caregiver/register")
    public String caregiverRegister() {
        return "page/caregiver_register";
    }
}
