[![Build Status](https://api.travis-ci.org/stormpath/stormpath-spring-security-example.png?branch=master)](https://travis-ci.org/stormpath/stormpath-spring-security-example)

# Stormpath Spring Security Web Sample #

Copyright &copy; 2013 Stormpath, Inc. and contributors. This project is open-source via the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0).  

This sample application uses the [Stormpath Spring Security](https://github.com/stormpath/stormpath-spring-security) plugin to demonstrate how to achieve Stormpath and Spring Security integration.

The `stormpath-spring-security` plugin allows a [Spring Security](http://projects.spring.io/spring-security/)-enabled application to use the [Stormpath](http://www.stormpath.com) User Management & Authentication service for all authentication and access control needs.


## Setup ##

### Sign Up For A Stormpath Account ###

1. Create a [Stormpath](http://www.stormpath.com/) developer account and [create your API Keys](http://docs.stormpath.com/console/product-guide/#manage-api-keys) downloading the "apiKey.properties" file into a ".stormpath" folder under your local home directory

2. Within [Stormpath's Admin Console](https://stormpath.com/docs/console/product-guide), create an application and a directory to store your users' accounts.

3. Create at least one Group and Account in the application. Assign the account to the group.

4. Through the Stormpath's Admin interface, note your [application's REST URL](http://www.stormpath.com/docs/libraries/application-rest-url).

### Configure the Sample Application ###

1. Clone and install the [stormpath-spring-security](https://github.com/stormpath/stormpath-spring-security) plugin into your local machine:

		git clone git@github.com:stormpath/stormpath-spring-security.git
		cd stormpath-spring-security
		mvn clean install

2. Clone stormpath-spring-security-example into your local machine:

		git clone git@github.com:stormpath/stormpath-spring-security-example.git 

3. Edit `stormpath-spring-security-example/web/src/main/webapp/WEB-INF/spring-security.xml`:

	1. Replace the `applicationRestUrl` value with your Application's REST URL.
	2. Insert the [Stormpath Group HREF](http://docs.stormpath.com/console/product-guide/#groups) that will be allowed to access the secured content over `REPLACE_ME_WITH_YOUR_STORMPATH_GROUP_ALLOWED_TO_ACCESS_THIS_SECURED_RESOURCE`.


## Running the Sample Application ##

1. This project requires Maven 3 to build. Run the following from a command prompt:

		mvn install

2. Run it:

		mvn tomcat:run

### Usage Instructions ###

Once it is running, open the following url in your web browser: `http://localhost:8080`. It will automatically display a publicly visible web-page with the legend "This page is publicly accessible. No authentication is required to view it.".

Now, go to `http://localhost:8080/secured/mypage`. You will be prompted to enter credentials. Any Stormpath account belonging to the group that you configured in step 3.2 of "Configure the Sample Application" will be granted access to it. After authenticating, you should see a page with the legend "You are now authenticated and authorized to view this page." You can also enter the credentials of another account not being part of that group and, although you will be successfully authenticated, you will no be allowed to see the secured page.

You can always close your web session by going to `http://localhost:8080/logout`.

## Using your domain-specific Role names ##

By default, the stormpath-spring-security plugin uses Groups's HREFs as the role names for Spring Security. In this sample app, we have included the `GroupRoleGrantedAuthorityResolver` class which provides functionality to map Group's HREFs to any desired domain-specific role name.

### Configuration ###

1. Add mappings in `stormpath-spring-security-example/web/src/main/webapp/WEB-INF/spring-security.xml`:

	    <beans:bean id="groupRoleGrantedAuthoritiesMap" class="java.util.HashMap" scope="prototype" >
    	    <beans:constructor-arg>
        	    <beans:map key-type="java.lang.String" value-type="java.util.List">
            	    <beans:entry key="ONE_STORMPATH_GROUP_HREF_HERE">
                	    <beans:list>
                    	    <beans:value>ROLE_A</beans:value>
                        	<beans:value>ROLE_B</beans:value>
	                    </beans:list>
    	            </beans:entry>
        	        <beans:entry key="ANOTHER_STORMPATH_GROUP_HREF_HERE" value="ROLE_USER" />
            	    <beans:entry key="YET_ANOTHER_STORMPATH_GROUP_HREF_HERE" value="ROLE_ADMIN" />
	            </beans:map>
    	    </beans:constructor-arg>
	    </beans:bean>
	    
2. Set the Group Resolver to `StormpathAuthenticationProvider`

	    <beans:bean id="groupGrantedAuthorityResolver" class="com.stormpath.spring.security.example.mapping.GroupRoleGrantedAuthorityResolver" >
	        <beans:constructor-arg ref="groupRoleGrantedAuthoritiesMap" />
    	</beans:bean> 

		<beans:bean id="stormpathAuthenticationProvider" class="com.stormpath.spring.security.provider.StormpathAuthenticationProvider">
	        ...
	        <beans:property name="groupGrantedAuthorityResolver" ref="groupGrantedAuthorityResolver" />
	    </beans:bean>
	    
3. Specify your Spring Security access rules using the mapped roles names in your application. For example, in this case, the secured resource is available to `ROLE_A` (belonging to `ONE_STORMPATH_GROUP_HREF_HERE` in step 1):

	    <http auto-config='true' access-decision-manager-ref="accessDecisionManager" >
	        <intercept-url pattern="/secured/*" access="ROLE_A" />
	        <logout logout-url="/logout" logout-success-url="/logoutSuccess.jsp"/>
    	</http>
