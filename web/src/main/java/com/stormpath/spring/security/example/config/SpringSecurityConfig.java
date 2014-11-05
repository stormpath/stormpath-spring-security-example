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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.Arrays;


/**
 * Spring JavaConfig defining Spring Security settings.
 *
 * @since 0.3.0
 */
@Configuration
@EnableWebSecurity
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    //The HREF to the Stormpath Application
    final String applicationRestUrl = "REPLACE_ME_WITH_YOUR_STORMPATH_APP_REST_URL";

    //Let's specify some role here so we can later grant it access to restricted resources
    final String roleA = "REPLACE_ME_WITH_YOUR_STORMPATH_GROUP_ALLOWED_TO_ACCESS_THIS_SECURED_RESOURCE";

    @Autowired
    private AuthenticationProvider stormpathAuthenticationProvider;

    //The access control settings are defined here
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                    .and()
                .authorizeRequests()
                    .accessDecisionManager(accessDecisionManager())
                    .antMatchers("/account/*").hasAuthority(roleA)
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/index.jsp")
                    .and()
                .httpBasic()
                .and()
                .csrf().disable();
    }


    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return this.authenticationManagerBean();
    }

    //Let's add the StormpathAuthenticationProvider to the AuthenticationManager
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(stormpathAuthenticationProvider);
    }

    //Prevents the addition of the "ROLE_" prefix in authorities
    @Bean
    public WebExpressionVoter webExpressionVoter() {
        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        return webExpressionVoter;
    }

    @Bean
    public AffirmativeBased accessDecisionManager() {
        AffirmativeBased affirmativeBased = new AffirmativeBased(Arrays.asList((AccessDecisionVoter) webExpressionVoter()));
        affirmativeBased.setAllowIfAllAbstainDecisions(false);
        return affirmativeBased;
    }

    @Bean
    public String getApplicationRestUrl() {
        return this.applicationRestUrl;
    }

}
