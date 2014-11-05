/*
 * Copyright (c) 2014, Stormpath, Inc.
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
package com.stormpath.spring.security.example.config;

import com.stormpath.sdk.client.Client;
import com.stormpath.spring.security.authz.permission.evaluator.WildcardPermissionEvaluator;
import com.stormpath.spring.security.client.ClientFactory;
import com.stormpath.spring.security.example.controller.CustomDataManager;
import com.stormpath.spring.security.provider.StormpathAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Spring JavaConfig defining shared resources visible to all other web components.
 *
 * @since 0.3.0
 */
@Configuration
public class RootContextConfig {

    //Let's create the Stormpath client using the apiKey.properties file from the User's home folder.
    @Bean
    ClientFactory stormpathClient(CacheManager cacheManager) {
        ClientFactory clientFactory = new ClientFactory();
        clientFactory.setApiKeyFileLocation(System.getProperty("user.home") + File.separator + ".stormpath" + File.separator + "apiKey.properties");
        clientFactory.setCacheManager(cacheManager);
        return clientFactory;
    }

    //Let's instantiate the Stormpath Authentication Provider
    @Bean
    @Autowired
    public StormpathAuthenticationProvider stormpathAuthenticationProvider(Client client, String applicationRestUrl) throws Exception {
        StormpathAuthenticationProvider stormpathAuthenticationProvider = new StormpathAuthenticationProvider();
        stormpathAuthenticationProvider.setClient(client);
        stormpathAuthenticationProvider.setApplicationRestUrl(applicationRestUrl);
        return stormpathAuthenticationProvider;
    }

    //Bean for CustomData Management
    @Bean
    CustomDataManager customDataManager() {
        return new CustomDataManager();
    }

    @Bean
    WildcardPermissionEvaluator permissionEvaluator() {
        return new WildcardPermissionEvaluator();
    }

    @Bean
    MethodSecurityExpressionHandler methodExpressionHandler(WildcardPermissionEvaluator permissionEvaluator) {
        DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler = new DefaultMethodSecurityExpressionHandler();
        methodSecurityExpressionHandler.setPermissionEvaluator(permissionEvaluator);
        return methodSecurityExpressionHandler;
    }

    @Bean
    DefaultWebSecurityExpressionHandler webExpressionHandler(WildcardPermissionEvaluator permissionEvaluator) {
        DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        webSecurityExpressionHandler.setPermissionEvaluator(permissionEvaluator);
        return webSecurityExpressionHandler;
    }

    @Bean
    CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        Collection<Cache> caches = new ArrayList<Cache>();
        caches.add(applicationCache().getObject());
        caches.add(accountCache().getObject());
        caches.add(groupCache().getObject());
        caches.add(customDataCache().getObject());
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @Bean
    ConcurrentMapCacheFactoryBean applicationCache(){
        ConcurrentMapCacheFactoryBean cacheFactoryBean = new ConcurrentMapCacheFactoryBean();
        cacheFactoryBean.setName("com.stormpath.sdk.application.Application");
        return cacheFactoryBean;
    }

    @Bean
    ConcurrentMapCacheFactoryBean accountCache(){
        ConcurrentMapCacheFactoryBean cacheFactoryBean = new ConcurrentMapCacheFactoryBean();
        cacheFactoryBean.setName("com.stormpath.sdk.account.Account");
        return cacheFactoryBean;
    }

    @Bean
    ConcurrentMapCacheFactoryBean groupCache(){
        ConcurrentMapCacheFactoryBean cacheFactoryBean = new ConcurrentMapCacheFactoryBean();
        cacheFactoryBean.setName("com.stormpath.sdk.group.Group");
        return cacheFactoryBean;
    }

    @Bean
    ConcurrentMapCacheFactoryBean customDataCache(){
        ConcurrentMapCacheFactoryBean cacheFactoryBean = new ConcurrentMapCacheFactoryBean();
        cacheFactoryBean.setName("com.stormpath.sdk.directory.CustomData");
        return cacheFactoryBean;
    }

}
