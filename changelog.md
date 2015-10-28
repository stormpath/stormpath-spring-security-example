## Change Log

### x.x.x

- Everything in this repo has now been integrated into [stormpath-sdk-java](https://github.com/stormpath/stormpath-sdk-java) since 1.0.RC5

### 0.3.0

- Upgraded Stormpath SDK dependency to latest stable release of 1.0.RC2
- Upgraded Spring Security Stormpath plugin to latest stable release of 0.3.0
- [Issue 8](https://github.com/stormpath/stormpath-spring-security-example/issues/8): Removed dynamic permissions for CustomData changes. User must logout and re-login to get permission changes applied.

### 0.2.0

- Upgraded Stormpath SDK dependency to latest stable release of 0.9.2
- Upgraded Spring Security Stormpath plugin to latest stable release of 0.2.0
- Added page to view and edit account's CustomData
- Added Permissions example. It is now possible to use Spring Security Granted Authorities as permissions for Stormpath Accounts or Groups by leveraging Stormpath's newly released [CustomData](http://docs.stormpath.com/rest/product-guide/#custom-data) feature.
- Stormpath SDK now has a Spring cache configured: a simple JDK ConcurrentMap

### 0.1.0

- First release
