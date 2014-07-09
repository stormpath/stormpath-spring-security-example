/*
 * Copyright 2014 Stormpath, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stormpath.spring.security.example.controller;

import com.stormpath.spring.security.example.authc.OauthLoginFactory;
import com.stormpath.spring.security.example.model.CustomDataBean;
import com.stormpath.spring.security.example.model.CustomDataFieldBean;
import com.stormpath.spring.security.provider.AccountCustomDataPermissionResolver;
import com.stormpath.spring.security.provider.StormpathAuthenticationProvider;
import com.stormpath.spring.security.provider.StormpathUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * Spring MVC Controller handling Custom Data-related requests
 *
 * @since 0.2.0
 */
@Controller
public class StormpathSpringSecurityExampleController {
    private static final Logger logger = LoggerFactory.getLogger(StormpathSpringSecurityExampleController.class);

    @Autowired
    private CustomDataManager customDataManager;

    @Autowired
    private StormpathAuthenticationProvider authenticationProvider;

    @Autowired
    private OauthLoginFactory oauthLoginFactory;

    private final String hrefBase = "https://api.stormpath.com/v1/";

    /**
     * Handles account's custom data retrieval. The actual retrieval operation is delegated to {@link CustomDataManager}.
     *
     * @param model placeholder to hold the information to be displayed on the view
     * @param customUser here we are using a new feature available in Spring Security 3.2 to get the current Authentication.getPrincipal()
     *                   automatically resolved for Spring MVC arguments
     * @return the web page to be displayed
     */
	@RequestMapping(value = "/account/customData", method = RequestMethod.GET)
    public String getCustomData(Model model, @AuthenticationPrincipal StormpathUserDetails customUser) {
        try {
            String accountHref = customUser.getProperties().get("href");
            CustomDataBean customDataBean = customDataManager.getCustomData(accountHref);
            model.addAttribute("customDataFields", customDataBean.getCustomDataFields());
            model.addAttribute("accountId", accountHref.substring(accountHref.lastIndexOf("/") + 1));
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
        }

        return "/account/customData";
    }

    /**
     * Handles account's custom data posts (i.e., addition of custom data fields). The actual insertion operation is delegated to
     * {@link CustomDataManager}
     *
     * @param accountId the account where the custom data field will be added
     * @param key the custom data field key
     * @param value the custom data field value
     * @return the updated status of the custom data field
     */
    @RequestMapping(value = "/account/customData/{accountId}", method = RequestMethod.POST)
    public ResponseEntity<String> addCustomData(@PathVariable(value = "accountId") String accountId,
                                @RequestParam(value = "key") String key,
                                @RequestParam(value = "value") String value) {
        try {
            CustomDataFieldBean field = customDataManager.addCustomDataField(getAccountHref(accountId), key, value, isPermissionKey(key));
            return new ResponseEntity<String>(field.getValue().toString(), HttpStatus.OK);
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
        }
        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles account's custom data deletion. The actual removal operation is delegated to
     * {@link CustomDataManager}
     *
     * @param accountId the account from where the custom data field will be removed
     * @param key the custom data field key
     * @return the status result of the operation
     */
    @RequestMapping(value = "/account/customData/{accountId}/{key}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCustomDataPage(@PathVariable(value = "accountId") String accountId,
                                                       @PathVariable(value = "key") String key) {
        try {
            customDataManager.deleteCustomDataField(getAccountHref(accountId), key, isPermissionKey(key));
            return new ResponseEntity<String>(HttpStatus.OK);
        } catch (Exception ex) {
            logger.warn(ex.getMessage());
        }
        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }

    /**
     * @since 0.3.0
     */
    @RequestMapping(value = "/googleOauthCallback", method = RequestMethod.GET)
    public ModelAndView googleOauthCallback(@RequestParam(value = "code", required = true) String code) {
        ModelAndView model = new ModelAndView();
        try {
            if(!oauthLoginFactory.createGoogleLogin().doLogin(code)) {
                model.addObject("error", "There was an error trying to login with the Google account.");
                model.setViewName("login");
                return model;
            }
        } catch (IOException ex) {
            logger.warn(ex.getMessage());
        }
        model.setViewName("home");
        return model;
    }

    /**
     * @since 0.3.0
     */
    @RequestMapping(value = "/facebookOauthCallback", method = RequestMethod.GET)
    public ModelAndView facebookOauthCallback(@RequestParam(value = "code", required = true) String code) {
        ModelAndView model = new ModelAndView();
        try {
            if(!oauthLoginFactory.createFacebookLogin().doLogin(code)) {
                model.addObject("error", "There was an error trying to login with the Facebook account.");
                model.setViewName("login");
                return model;
            }
        } catch (IOException ex) {
            logger.warn(ex.getMessage());
        }
        model.setViewName("home");
        return model;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout) {

        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", error);
        }

        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }
        model.setViewName("/login");

        return model;
    }

    /**
     * Checks whether the given key equals the "spring security permission string". See: <a href="https://github.com/stormpath/stormpath-spring-security/wiki#wiki-permissions"></a>
     * @param key the key to compare
     * @return 'true' if the given key equals the spring security permission string. 'False' otherwise.
     */
    private boolean isPermissionKey(String key) {
        return ((AccountCustomDataPermissionResolver)authenticationProvider.getAccountPermissionResolver()).getCustomDataFieldName().equals(key);
    }

    private String getAccountHref(String accountId) {
        return hrefBase + "accounts/" + accountId;
    }

}
