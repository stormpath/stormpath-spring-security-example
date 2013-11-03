/*
 * Copyright 2013 Stormpath, Inc.
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

import com.stormpath.sdk.client.Client;
import com.stormpath.sdk.directory.CustomData;
import com.stormpath.spring.security.example.model.CustomDataBean;
import com.stormpath.spring.security.example.model.CustomDataFieldBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Singleton controller encapsulating Custom Data operations (i.e. retrieve, delete, insert) to be executed in
 * the <a href="http://www.stormpath.com">Stormpath</a> account by means of the
 * <a href="https://github.com/stormpath/stormpath-sdk-java">Stormpath Java SDK</a>
 */
public class CustomDataManager {
    private static final Logger logger = LoggerFactory.getLogger(CustomDataManager.class);

    @Autowired
    private Client stormpathClient;

    private static final String hrefBase = "https://api.stormpath.com/v1/";

    /**
     * Method that returns the custom data for the given account.
     *
     * @param accountId the accountId whose custom data is being requested
     * @return a {@link CustomDataBean} instance containing the custom data fields
     */
    public CustomDataBean getCustomData(String accountId) {
        if (accountId == null || accountId.length() == 0) {
            throw new IllegalArgumentException("accountId cannot be null or empty");
        }
        CustomData customData = stormpathClient.getResource(getCustomDataHref(accountId), CustomData.class);
        List<CustomDataFieldBean> customDataFieldBeanList = new ArrayList<CustomDataFieldBean>();

        for (Map.Entry<String, Object> entry : customData.entrySet()) {
            customDataFieldBeanList.add(new CustomDataFieldBean(entry.getKey(), entry.getValue()));
        }

        CustomDataBean customDataBean = new CustomDataBean();
        customDataBean.setCustomDataFields(customDataFieldBeanList);
        return customDataBean;
    }

    private static String getCustomDataHref(String accountId) {
        return getAccountHref(accountId) + "/customData";
    }

    private static String getAccountHref(String accountId) {
        return hrefBase + "accounts/" + accountId;
    }


}
