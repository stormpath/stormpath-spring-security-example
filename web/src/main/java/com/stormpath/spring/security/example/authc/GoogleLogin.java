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
package com.stormpath.spring.security.example.authc;

import com.stormpath.spring.security.authc.GoogleAuthenticationToken;
import com.stormpath.spring.security.authc.OauthAuthenticationToken;
import com.stormpath.spring.security.example.service.GoogleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;


/**
 * Google-specific {@link OauthLogin}. The Google redirectUri must be configured in such a way that this class receives the actual
 * Google authentication attempt. For example,
 * in config.ini:
 * <pre>
 *     [Google]
 *     clientId = ***your_google_client_id***
 *     clientSecret = ***your_google_client_secret***
 *     redirectUri = http://localhost:8080/googleOauthCallback
 * </pre>
 * and in your controller:
 * <pre>
 *      <code>@RequestMapping(value = "/googleOauthCallback", method = RequestMethod.GET)</code>
 *      public ModelAndView googleOauthCallback(@RequestParam(value = "code", required = true) String code) {
 *          if(oauthLoginFactory.createGoogleLogin().doLogin(code)) { //the "doLogin(code)" part will be executed by this class
 *              //at this point the user will be effectively logged in
 *          } else {
 *              //your authentication attempt would have failed here
 *          }
 *          ....
 *      }
 * </pre>
 *
 * @since 0.3.0
 */
public class GoogleLogin extends OauthLogin {
    private static final Logger logger = LoggerFactory.getLogger(GoogleLogin.class);

    public GoogleLogin(GoogleService googleService, AuthenticationProvider authenticationProvider) {
        super(googleService, authenticationProvider);
    }

    /**
     * Creates a new {@link GoogleAuthenticationToken} using the received authorization code.
     * @param code the authorization code received from Google.
     * @return a new {@link GoogleAuthenticationToken} using the received authorization code.
     */
    @Override
    protected OauthAuthenticationToken getOauthAuthenticatingToken(String code) {
        return new GoogleAuthenticationToken(code);
    }


}
