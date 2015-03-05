### Build the Custom Realm:

 *  $git clone https://github.com/gholamiali/CauthRealm.git
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
        se.kth.bbc.crealm.CustomAuthLoginModule required;
};

```

### Modify the domain.xml 

* domain.xml file is under [glassfish home installation]/glassfish/domains/domain1/config/domain.xml. The custom cauthRealm section should contain:


```
 <security-service default-realm="cauthRealm">
 	 	...
  
    <auth-realm name="cauthRealm" classname="se.kth.bbc.crealm.CustomAuthRealm">
          <property name="jaas-context" value="cauthRealm"></property>
          <property name="encoding" value="Hex"></property>
          <property name="password-column" value="password"></property>
          <property name="datasource-jndi" value="jdbc/name_of_resource]"></property>
          <property name="group-table" value="USERS_GROUPS"></property>
          <property name="user-table" value="USERS"></property>
          <property name="charset" value="UTF-8"></property>
          <property name="group-name-column" value="group_name"></property>
          <property name="user-name-column" value="email"></property>
          <property name="otp-secret-column" value="secret"></property>
          <property name="user-status-column" value="status"></property>
          <property name="group-table-user-name-column" value="email"></property>
	   <property name="yubikey-table" value="Yubikey"></property>
        
	</auth-realm>
	...

 </security-service>

```

### Restart the Glassfish server
