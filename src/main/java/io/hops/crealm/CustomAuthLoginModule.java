package io.hops.crealm;

import com.sun.appserv.security.AppservPasswordLoginModule;
import java.util.Arrays;
import java.util.logging.Level;
import javax.security.auth.login.LoginException;

public class CustomAuthLoginModule extends AppservPasswordLoginModule {

  /**
   * Adapted from
   * http://grepcode.com/file/repo1.maven.org/maven2/org.glassfish.security/security/3.0.1/com/sun/enterprise/security/auth/realm/jdbc/JDBCRealm.java
   * @throws javax.security.auth.login.LoginException
   */
 
  @Override
  protected void authenticateUser() throws LoginException {
    if (!(_currentRealm instanceof CustomAuthRealm)) {
      String msg = sm.getString("CAuth bad realm");
      throw new LoginException(msg);
    }

    final CustomAuthRealm gauthRealm = (CustomAuthRealm) _currentRealm;

    if ((_username == null) || (_username.length() == 0)) {
      String msg = sm.getString("CAuth null user");
      throw new LoginException(msg);
    }

    String[] grpList = gauthRealm.authenticate(_username, String.valueOf(getPasswordChar()));

    if (grpList == null) {  // JAAS behavior
      String msg = sm.getString("CAaut login fail", _username);
      throw new LoginException(msg);
    }

    if (_logger.isLoggable(Level.FINEST)) {
      _logger.log(Level.FINEST, "CAuth login succeeded for: {0} groups:{1}",
              new Object[]{_username, Arrays.toString(grpList)});
    }

    final String[] groupListToForward = new String[grpList.length];
    System.arraycopy(grpList, 0, groupListToForward, 0, grpList.length);
    commitUserAuthentication(groupListToForward);

  }
  
  @Override
  public char[] getPasswordChar() {
    return _passwd;
  } 
}
