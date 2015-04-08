/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.bbc.crealm;

import com.sun.appserv.security.AppservCertificateLoginModule;
import java.util.StringTokenizer;
import javax.security.auth.login.LoginException;

/**
 *
 * @author Ali Gholami <gholami@pdc.kth.se>
 */
public class CustomCertificateLoginModule extends AppservCertificateLoginModule {

  /*
   * taken from https://blogs.oracle.com/nasradu8/entry/extend_certificaterealm_with_loginmodule_glassfish
   */
  @Override
  protected void authenticateUser() throws LoginException {
    String dname = getX500Principal().getName();
    StringTokenizer st = new StringTokenizer(dname, " \t\n\r\f,");
    while (st.hasMoreTokens()) {
      String next = st.nextToken();
      if (next.startsWith("OU=")) {
        commitUserAuthentication(new String[]{getAppName() + ":" + next.
          substring(3)});
        return;
      }
    }
    throw new LoginException("No OU found.");
  }

}
