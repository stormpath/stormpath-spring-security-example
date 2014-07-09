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

import com.stormpath.sdk.client.Client;
import com.stormpath.spring.security.example.utils.Constants;
import com.stormpath.spring.security.provider.StormpathAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @since 0.3.0
 */
public abstract class AbstractService {

    @Autowired
    private StormpathAuthenticationProvider authenticationProvider;

    public static final String baseUrl = Constants.STORMPATH_BASE_URL;

    protected StormpathAuthenticationProvider getStormpathAuthenticationProvider() {
        return this.authenticationProvider;
    }

    protected Client getStormpathClient() {
        return this.authenticationProvider.getClient();
    }

}
