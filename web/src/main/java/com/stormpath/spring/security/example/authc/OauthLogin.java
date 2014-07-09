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

import com.stormpath.sdk.lang.Assert;
import com.stormpath.spring.security.authc.OauthAuthenticationToken;
import com.stormpath.spring.security.example.service.ProviderService;
import com.stormpath.spring.security.example.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A generic login class to be sub-classed by providers of Oauth authentication mechanisms like Google and Facebook.
 * <p/>
 * This abstract class provides most of the Oauth authentication logic and relies on Provider-specific
 * sub-classes (like {@link FacebookLogin} or {@link GoogleLogin})
 * to fulfill its task.
 *
 * @since 0.3.0
 */
public abstract class OauthLogin {

    private static final Logger logger = LoggerFactory.getLogger(OauthLogin.class);

    private final AuthenticationProvider authenticationProvider;
    private final ProviderService providerService;

    public OauthLogin(ProviderService providerService, AuthenticationProvider authenticationProvider) {
        Assert.notNull(providerService);
        Assert.notNull(authenticationProvider);
        this.providerService = providerService;
        this.authenticationProvider = authenticationProvider;
    }

    public boolean doLogin(String code) throws IOException {
        if(code != null && authenticate(code)) {
            return true;
        }

        //If we are here it is very likely that the Provider-based Account Store has not yet being created in
        //Stormpath. Let's make developer's life easier and let's try to create it automatically for him...
        if (!providerService.hasProviderBasedAccountStore()) {
            //Indeed, the application does not have a Provider-based Account Store.
            providerService.createProviderAccountStore();
            //Let's re-try to login
            if(authenticate(code)) {
                return true;
            }
        }

        //The Provider-based directory already existed in the Stormpath Application as an account store but the attempt to login
        //through it did not succeed. There is nothing else we can do, there is indeed an error when trying to login via
        //this Oauth provider.
        Map<String, String> failureMap = new HashMap<String, String>();
        failureMap.put(Constants.MESSAGE_FLAG, this.providerService.PROVIDER_ID + Constants.LOGIN_ERROR);
        return false;
    }

    /**
     * Tries to login using the given code. Delegates the creation of the {@link Authentication authentication token} to the
     * Provider-specific implementation.
     * <p/>
     * If the authentication attempt succeeds, the authenticated user is automatically applied to the current thread of execution. This
     * means that the user is indeed logged in to the application from this point on.
     *
     * @param code the actual token received from the Oauth provider.
     * @return true if the login succeeded; false otherwise.
     */
    protected boolean authenticate(String code) {
        try {
            Authentication token = authenticationProvider.authenticate(getOauthAuthenticatingToken(code));
            SecurityContextHolder.getContext().setAuthentication(token);
            return true;
        } catch (AuthenticationException e) {
            logger.debug("The Oauth user cannot access this application.");
        }
        return false;
    }

    /**
     * Creates a new Provider-specific {@link OauthAuthenticationToken} using the received authorization code from the provider.
     * This is the token that will be used to carry on the actual login attempt.
     *
     * @param code the authorization code received from the provider.
     * @return a new Provider-specific {@link OauthAuthenticationToken} using the received authorization accessToken.
     */
    protected abstract OauthAuthenticationToken getOauthAuthenticatingToken(String code);

}
