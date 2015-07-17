package com.rplp.alfresco.repo.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.LdapContext;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.repo.security.authentication.AuthenticationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.ldap.core.ContextExecutor;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.ldap.LdapUsernameToDnMapper;
import org.springframework.security.ldap.LdapUtils;
import org.springframework.util.Assert;

public class LdapChangeUserServiceImpl implements LdapChangeUserService, InitializingBean {
  private static final Log logger = LogFactory.getLog(LdapChangeUserServiceImpl.class);
  private LdapTemplate ldapTemplate;
  private ContextSource contextSource;
  private String passwordAttributeName;
  private LdapUsernameToDnMapper usernameMapper;

  public void changePassword(final String userId, final String oldPassword, final String newPassword) {

    logger.debug("Changing password for user " + userId);
    String hashedPassword = newPassword;
    try {
      hashedPassword = hashMD5Password(newPassword);
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e1) {
      logger.error(e1);
      throw new AlfrescoRuntimeException("Error hashing password", e1);
    }
    final DistinguishedName dn = usernameMapper.buildDn(userId);
    final ModificationItem[] passwordChange = new ModificationItem[] { new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(passwordAttributeName, hashedPassword)) };

    if (oldPassword == null) {
      try {
        ldapTemplate.modifyAttributes(dn, passwordChange);
      } catch (Exception e) {
        logger.error(e);
        throw e;
      }
      return;
    }

    ldapTemplate.executeReadWrite(new ContextExecutor() {

      public Object executeWithContext(DirContext dirCtx) throws NamingException {
        LdapContext ctx = (LdapContext) dirCtx;
        ctx.removeFromEnvironment("com.sun.jndi.ldap.connect.pool");
        ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, LdapUtils.getFullDn(dn, ctx).toString());
        ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, oldPassword);
        try {
          ctx.reconnect(null);
        } catch (javax.naming.AuthenticationException e) {
          logger.error(e);
          throw new AuthenticationException("Authentication for password change failed.");
        }

        ctx.modifyAttributes(dn, passwordChange);

        return null;
      }
    });
  }

  /**
   * Hash a password with md5 for LDAP
   * 
   * @param newPassword
   *          The password to hash
   * @return a md5 hashed password
   */
  private String hashMD5Password(final String newPassword) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    MessageDigest digest = MessageDigest.getInstance("MD5");
    digest.update(newPassword.getBytes("UTF8"));
    String md5Password = new String(Base64.encode(digest.digest()));
    return "{MD5}" + md5Password;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(contextSource, "You have to provide an instance of ContextSource");
    Assert.notNull(usernameMapper);
    Assert.notNull(passwordAttributeName);
    ldapTemplate = new LdapTemplate(contextSource);
  }

  public void setUsernameMapper(LdapUsernameToDnMapper usernameMapper) {
    this.usernameMapper = usernameMapper;
  }

  public void setContextSource(ContextSource contextSource) {
    this.contextSource = contextSource;
  }

  public void setLdapTemplate(LdapTemplate ldapTemplate) {
    this.ldapTemplate = ldapTemplate;
  }

  public void setPasswordAttributeName(String passwordAttributeName) {
    this.passwordAttributeName = passwordAttributeName;
  }

}
