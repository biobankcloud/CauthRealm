## Intro

This instructions describes step-by-step guidelines to deploy the custom two-factor authentication into a Glassfish instance.

### Build the Custom Realm:

 *  $git clone git@github.com:hopshadoop/CauthRealm.git
 *  $cd CauthRealm
 *  $mvn compile
 *  $mvn assembly:assembly
 *  Check the property file under: src/main/resource and enable/disable CauthRealm.
 *  After build a jar file called otp-auth-1.0-SNAPSHOT-jar-with-dependencies.jar will be produced. Put the produced jar file under the following glasfish directories:
 *  $cp target/otp-auth-1.0-SNAPSHOT-jar-with-dependencies.jar  [glassfish home installation]/glassfish/domains/domain1/lib/

### Install the DB schema

 * Create a db named "kthfs" in a MySQL server 
 * $mysql -u[username] -p[password] kthfs < src/main/resources/kthfs.sql

### Enable the config

* Add the following statement to [glassfish home installation]/glassfish/domains/domain1/config/login.conf

```
cauthRealm{
        io.hops.crealm.CustomAuthLoginModule required;
};

```

### Modify the domain.xml 

* domain.xml file is under [glassfish home installation]/glassfish/domains/domain1/config/domain.xml. The custom cauthRealm section should contain:


```
 <security-service default-realm="cauthRealm">
 	 	...
  
    <auth-realm name="cauthRealm" classname="io.hops.crealm.CustomAuthRealm">
          <property name="jaas-context" value="cauthRealm"></property>
          <property name="encoding" value="Hex"></property>
          <property name="password-column" value="password"></property>
          <property name="datasource-jndi" value="jdbc/kthfs"></property>
          <property name="group-table" value="hopsworks.users_groups"></property>
          <property name="user-table" value="hopsworks.users"></property>
          <property name="charset" value="UTF-8"></property>
          <property name="group-name-column" value="group_name"></property>
          <property name="user-name-column" value="email"></property>
          <property name="otp-secret-column" value="secret"></property>
          <property name="two-factor-column" value="two_factor"></property>
          <property name="user-status-column" value="status"></property>
          <property name="group-table-user-name-column" value="email"></property>
          <property name="yubikey-table" value="hopsworks.yubikey"></property>
          <property name="variables-table" value="hopsworks.variables"></property>
        
	</auth-realm>
	...

 </security-service>

Otherwise, you can use command line through the Glassfish asadmin console:

asadmin  create-auth-realm --classname io.hops.crealm.CustomAuthRealm --property "jaas-context=cauthRealm:encoding=Hex:password-column=password:datasource-jndi=jdbc/hopsworks:group-table=hopsworks.users_groups:user-table=hopsworks.users:charset=UTF-8:group-name-column=group_name:user-name-column=email:otp-secret-column=secret:two-factor-column=two_factor:user-status-column=status:group-table-user-name-column=email:yubikey-table=hopsworks.yubikey:variables-table=hopsworks.variables" cauthRealm
```


### Restart the Glassfish server

To enalbe the new configurations, the Glassfish server should be restarted either though command-line or the administrator GUI.
