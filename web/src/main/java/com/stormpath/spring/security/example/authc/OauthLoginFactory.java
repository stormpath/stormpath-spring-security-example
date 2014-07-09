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

import com.stormpath.spring.security.example.service.FacebookService;
import com.stormpath.spring.security.example.service.GoogleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;

/**
 * Factory class that constructs Provider-specific oauth login objects.
 *
 * @see {@link com.stormpath.spring.security.example.authc.GoogleLogin}
 * @see {@link com.stormpath.spring.security.example.authc.FacebookLogin}
 * @since 0.3.0
 */
public class OauthLoginFactory  {
    private static final Logger logger = LoggerFactory.getLogger(OauthLoginFactory.class);

    private static OauthLoginFactory factory = new OauthLoginFactory();

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private GoogleService googleService;

    @Autowired
    private FacebookService facebookService;

    //Static factory method
    public static OauthLoginFactory createLoginService() {
        return factory;
    }

    //Instance factory methods
    public GoogleLogin createGoogleLogin(){
        return new GoogleLogin(googleService, authenticationProvider);
    }

    public FacebookLogin createFacebookLogin(){
        return new FacebookLogin(facebookService, authenticationProvider);
    }

}
