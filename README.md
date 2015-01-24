[![Build Status](https://api.travis-ci.org/stormpath/stormpath-spring-security-example.png?branch=master)](https://travis-ci.org/stormpath/stormpath-spring-security-example)

# Stormpath Spring Security Web Sample #

This sample application uses the [Stormpath Spring Security](https://github.com/stormpath/stormpath-spring-security) plugin to demonstrate how to integrate Stormpath and Spring Security.

Stormpath is a User Management API that reduces development time with instant-on, scalable user infrastructure. Stormpath's intuitive API and expert support make it easy for developers to authenticate, manage, and secure users and roles in any application.

The `stormpath-spring-security` plugin allows a [Spring Security](http://projects.spring.io/spring-security/)-enabled application to use the [Stormpath](http://www.stormpath.com) User Management & Authentication API for all authentication and access control needs.

## Documentation
Stormpath offers usage documentation and support for the Spring Security Plugin for Stormpath [in the wiki](https://github.com/stormpath/stormpath-spring-security-example/wiki). Please email support@stormpath.com with any errors or issues with the documentation.

## Links
Below are some resources you might find useful!
- [The Spring Security Plugin for Stormpath](http://projects.spring.io/spring-security/)
- Release changes are viewable in the [change log](changelog.md)

**Stormpath Java Support**
- [Stormpath API Docs for Java](https://docs.stormpath.com/java/apidocs/)
- [Stormpath Java Product Guide](https://docs.stormpath.com/java/product-guide/)
- [Stormpath Java SDK](https://github.com/stormpath/stormpath-sdk-java)

## Setup ##

### Sign Up For A Stormpath Account ###

1. Create a [Stormpath](http://www.stormpath.com/) developer account and [create your API Keys](http://docs.stormpath.com/console/product-guide/#manage-api-keys) downloading the "apiKey.properties" file into a ".stormpath" folder under your local home directory

2. Within [Stormpath's Admin Console](https://stormpath.com/docs/console/product-guide), create an application and a directory to store your users' accounts.

3. Create at least one Group and Account in the application. Assign the account to the group.

4. Through the Stormpath's Admin interface, note your [application's REST URL](http://www.stormpath.com/docs/libraries/application-rest-url).

### Configure the Sample Application ###

1. Clone stormpath-spring-security-example into your local machine:

	```shell
	git clone git@github.com:stormpath/stormpath-spring-security-example.git
	```

2. Edit `stormpath-spring-security-example/web/src/main/webapp/WEB-INF/spring-security.xml`:

	1. Replace the `applicationRestUrl` value with your Application's REST URL.
	2. Insert the [Stormpath Group HREF](http://docs.stormpath.com/console/product-guide/#groups) that will be allowed to access the secured content over `REPLACE_ME_WITH_YOUR_STORMPATH_GROUP_ALLOWED_TO_ACCESS_THIS_SECURED_RESOURCE`.


## Running the Sample Application ##

1. This project requires Maven 3 to build. Run the following from a command prompt:

	`mvn install`

2. Run it:

	`mvn tomcat:run`

## Using your domain-specific Role names ##

By default, the stormpath-spring-security plugin and this sample app uses Groups's HREFs as the role names for Spring Security. In this sample app, we have also included another `GroupGrantedAuthorityResolver`, the `GroupRoleGrantedAuthorityResolver` class which provides functionality to map Group's HREFs to any desired domain-specific role name. It is not enabled by default but it is present here as it may come in handy for you.

### Configuration ###

1. Add your own mappings in `stormpath-spring-security-example/web/src/main/webapp/WEB-INF/spring-security.xml`:

	```xml
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
	```

2. Set the Group Resolver to `StormpathAuthenticationProvider`:

	```xml
	<beans:bean id="groupGrantedAuthorityResolver" class="com.stormpath.spring.security.example.mapping.GroupRoleGrantedAuthorityResolver" >
		<beans:constructor-arg ref="groupRoleGrantedAuthoritiesMap" />
	</beans:bean>

	<beans:bean id="stormpathAuthenticationProvider" class="com.stormpath.spring.security.provider.StormpathAuthenticationProvider">
		<!-- etc... -->
		<beans:property name="groupGrantedAuthorityResolver" ref="groupGrantedAuthorityResolver" />
	</beans:bean>
	```

3. Specify your Spring Security access rules using the mapped roles names in your application. For example, in this case, the secured resource is available to `ROLE_A` (belonging to `ONE_STORMPATH_GROUP_HREF_HERE` in step 1):

	```xml
	<http auto-config='true' access-decision-manager-ref="accessDecisionManager" >
		<intercept-url pattern="/secured/*" access="ROLE_A" />
		<logout logout-url="/logout" logout-success-url="/logoutSuccess.jsp"/>
	</http>
	```

This way, your application code can stay agnostic of the Stormpath's role names. Now, in your code you can do this:

```java
@PreAuthorize("hasRole('ROLE_A')")
```
as opposed to:

```java
@PreAuthorize("hasRole('https://api.stormpath.com/v1/groups/l4aDkz0QPcf2z23j93l1T')")
```
## Contributing

Contributions, bug reports and issues are very welcome. Stormpath regularly maintains this repository, and are quick to review pull requests and accept changes!

You can make your own contributions by forking the develop branch of this
repository, making your changes, and issuing pull request on the develop branch.

## Copyright ##

Copyright &copy; 2013-2015 Stormpath, Inc. and contributors.

This project is open-source via the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0).
