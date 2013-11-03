package com.stormpath.spring.security.example.controller;

import com.stormpath.spring.security.example.model.CustomDataBean;
import com.stormpath.spring.security.provider.StormpathUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class StormpathSpringSecurityExampleController {
    private static final Logger logger = LoggerFactory.getLogger(StormpathSpringSecurityExampleController.class);

    @Autowired
    private CustomDataManager customDataManager;

	@RequestMapping("/")
	public String accessPublicPage(Model model) {
		model.addAttribute("message", "This page is publicly accessible. No authentication is required to view it.");

		return "public";
	}

    @RequestMapping("/secured/mypage")
	public String accessSecuredPage(Model model) {
		model.addAttribute("message", "You are now authenticated and authorized to view this page.");

		return "/secured/mypage";
	}

    @RequestMapping(value = "/secured/customData", method = RequestMethod.GET)
    public String editCustomDataPage(Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String accountHref = ((StormpathUserDetails)authentication.getPrincipal()).getProperties().get("href");
            String accountId = accountHref.substring(accountHref.lastIndexOf("/") + 1);
            CustomDataBean customDataBean = customDataManager.getCustomData(accountId);
            model.addAttribute("customDataFields", customDataBean.getCustomDataFields());
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
        }
        return "/secured/customData";
    }

}
