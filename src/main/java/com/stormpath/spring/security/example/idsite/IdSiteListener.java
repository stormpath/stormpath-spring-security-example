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
package com.stormpath.spring.security.example.idsite;

import com.stormpath.sdk.idsite.AccountResult;
import com.stormpath.sdk.idsite.IdSiteResultListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This listener will be notified about successful ID Site activities such as registration, authentication and logout.
 * <p/>
 * In order for this listener to get notifications, it must be exposed as a bean and must be called <code>idSiteResultListener</code>.
 * <p/>
 * See <code>webapp/WEB-INF/spring/root-context.xml</code>
 *
 * @since 0.3.0
 */
public class IdSiteListener implements IdSiteResultListener{
    private static final Logger logger = LoggerFactory.getLogger(IdSiteListener.class);

    @Override
    public void onRegistered(AccountResult accountResult) {
        logger.debug("Successful Id Site registration for account: " + accountResult.getAccount().getEmail());
    }

    @Override
    public void onAuthenticated(AccountResult accountResult) {
        logger.debug("Successful Id Site authentication for account: " + accountResult.getAccount().getEmail());
    }

    @Override
    public void onLogout(AccountResult accountResult) {
        logger.debug("Successful Id Site logout for account: " + accountResult.getAccount().getEmail());
    }

}
