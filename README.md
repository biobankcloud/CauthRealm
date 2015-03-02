* Checkout the Custom Realm (crealm) and build it:

 1.  $git clone https://github.com/gholamiali/CauthRealm.git
 2.  $cd CauthRealm
 3.  $mvn compile
 4.  $mvn assembly:assembly
  
* Check the property file under: src/main/resource to enable/disable CauthRealm.

* Add the custom realm tables to your database
  $mysql -u[username] -p[password] kthfs < src/main/resources/kthfs.sql

* After build a jar file called otp-auth-1.0-SNAPSHOT-jar-with-dependencies.jar will be produced. Put the produced jar file under the following glasfish directories:

1. $cp crealm/target/otp-auth-1.0-SNAPSHOT-jar-with-dependencies.jar  [glassfish home installation]/glassfish/domains/domain1/lib/

* Add the following statement to [glassfish home installation]/glassfish/domains/domain1/config/login.conf

cauthRealm{
        se.kth.bbc.crealm.CustomAuthLoginModule Sufficient;
        se.kth.bbc.crealm.CustomCertificateLoginModule Sufficient;
};

* Modify the domin.xml file under [glassfish home installation]/glassfish/domains/domain1/config/domain.xml so the custom cauthRealm section looks like below:

  <security-service default-realm="cauthRealm">
  	...
  
      <auth-realm name="cauthRealm" classname="se.kth.bbc.crealm.CustomAuthRealm">
          <property name="jaas-context" value="cauthRealm"></property>
          <property name="encoding" value="Hex"></property>
          <property name="password-column" value="password"></property>
          <property name="datasource-jndi" value="jdbc/lims"></property>
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

* Restart the Glassfish server
