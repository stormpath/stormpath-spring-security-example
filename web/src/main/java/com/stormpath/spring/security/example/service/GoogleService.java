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
package com.stormpath.spring.security.example.service;

import com.stormpath.sdk.provider.CreateProviderRequest;
import com.stormpath.sdk.provider.Providers;
import com.stormpath.spring.security.example.conf.Configuration;
import com.stormpath.spring.security.example.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Google-specific Singleton {@link ProviderService}.
 *
 * @since 0.3.0
 */
public class GoogleService extends ProviderService {

    private static final Logger logger = LoggerFactory.getLogger(GoogleService.class);

    public GoogleService() {
        super(Constants.GOOGLE);
    }

    /**
     * Returns a new Google-based directory creation request. See
     * <a href="http://docs.stormpath.com/java/product-guide/#creating-a-google-directory">Creating a google directory</a>
     *
     * @return a new Google-based directory creation request.
     */
    @Override
    protected CreateProviderRequest createProviderRequest() {
        //ClientID, ClientSecret and RedirectUri are retrieved from the configuration file.
        return Providers.GOOGLE.builder()
                .setClientId(Configuration.getGoogleClientId())
                .setClientSecret(Configuration.getGoogleClientSecret())
                .setRedirectUri(Configuration.getGoogleRedirectUri())
                .build();
    }

}
