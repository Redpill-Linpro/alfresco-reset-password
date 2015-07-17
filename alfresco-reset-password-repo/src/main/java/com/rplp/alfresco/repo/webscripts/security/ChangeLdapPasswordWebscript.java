package com.rplp.alfresco.repo.webscripts.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.alfresco.repo.security.authentication.AuthenticationException;
import org.alfresco.repo.web.scripts.person.ChangePasswordPost;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.cmr.security.AuthorityService;
import org.alfresco.service.cmr.security.MutableAuthenticationService;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.extensions.surf.util.Content;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.util.Assert;

import com.rplp.alfresco.repo.security.LdapChangeUserService;

/**
 * Changes password in remote ldap-server.
 * 
 * @author erik.billerby@redpill-linpro.com
 * 
 */
public class ChangeLdapPasswordWebscript extends ChangePasswordPost implements InitializingBean {
  private static final Logger logger = Logger.getLogger(ChangeLdapPasswordWebscript.class);
  private static final String PARAM_NEWPW = "newpw";
  private static final String PARAM_OLDPW = "oldpw";

  private LdapChangeUserService ldapChangeUserService;
  private AuthorityService authorityService;
  private AuthenticationService authenticationService;

  @Override
  protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
    if (logger.isTraceEnabled()) {
      logger.trace(ChangeLdapPasswordWebscript.class.getName() + ".executeImpl");
    }

    // Extract user name from the URL - cannot be null or webscript desc would
    // not match
    String userName = req.getExtensionPath();

    // Extract old and new password details from JSON POST
    Content content = req.getContent();
    if (content == null) {
      throw new WebScriptException(Status.STATUS_INTERNAL_SERVER_ERROR, "Missing POST body.");
    }
    JSONObject json;

    Set<String> authorityZones = authorityService.getAuthorityZones(userName);

    if (authorityZones.contains("AUTH.ALF")) {
      logger.debug("Request to change password is for internal alfresco user. Redirecting request to the alfresco change password webscript.");
      return super.executeImpl(req, status);
    }

    try {
      json = new JSONObject(content.getContent());

      String oldPassword = null;
      String newPassword;

      // admin users can change/set a password without knowing the old one
      boolean isAdmin = authorityService.hasAdminAuthority();
      if (!isAdmin || (userName.equalsIgnoreCase(authenticationService.getCurrentUserName()))) {
        if (!json.has(PARAM_OLDPW) || json.getString(PARAM_OLDPW).length() == 0) {
          throw new WebScriptException(Status.STATUS_BAD_REQUEST, "Old password 'oldpw' is a required POST parameter.");
        }
        oldPassword = json.getString(PARAM_OLDPW);
      }
      if (!json.has(PARAM_NEWPW) || json.getString(PARAM_NEWPW).length() == 0) {
        throw new WebScriptException(Status.STATUS_BAD_REQUEST, "New password 'newpw' is a required POST parameter.");
      }
      newPassword = json.getString(PARAM_NEWPW);

      ldapChangeUserService.changePassword(userName, oldPassword, newPassword);

    } catch (AuthenticationException err) {
      logger.error("Error when trying to change password", err);
      throw new WebScriptException(Status.STATUS_UNAUTHORIZED, "Do not have appropriate auth or wrong auth details provided.");
    } catch (JSONException jErr) {
      throw new WebScriptException(Status.STATUS_INTERNAL_SERVER_ERROR, "Unable to parse JSON POST body: " + jErr.getMessage());
    } catch (IOException ioErr) {
      throw new WebScriptException(Status.STATUS_INTERNAL_SERVER_ERROR, "Unable to retrieve POST body: " + ioErr.getMessage());
    }
    Map<String, Object> model = new HashMap<String, Object>(1, 1.0f);
    model.put("success", Boolean.TRUE);
    return model;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(ldapChangeUserService, "You have to provide an instance of LdapChangeUserService");
    Assert.notNull(authenticationService, "You have to provide an instance of AuthenticationService");
    Assert.notNull(authorityService, "You have to provide an instance of AuthorityService");

  }

  public void setLdapChangeUserService(LdapChangeUserService ldapChangeUserService) {
    this.ldapChangeUserService = ldapChangeUserService;
  }

  public void setAuthenticationService(MutableAuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
    super.setAuthenticationService(authenticationService);
  }

  public void setAuthorityService(AuthorityService authorityService) {
    this.authorityService = authorityService;
    super.setAuthorityService(authorityService);
  }

}
