package com.rplp.alfresco.repo.jscript;


import org.alfresco.repo.jscript.BaseScopableProcessorExtension;

import com.rplp.alfresco.repo.security.LdapChangeUserService;
import com.rplp.alfresco.repo.utils.RandomPasswordGenerator;


public class ChangePasswordJsHelper extends BaseScopableProcessorExtension {
  private LdapChangeUserService ldapChangeUserService;
  
  /*public String userExistsInLdap(String userId){
    boolean existsInLdap = ldapChangeUserService.existsInLdap(userId);
    if (existsInLdap){
      return "true";
    }else{
      return "false";
    }
  }*/
  
  public String changeLdapPassword(String userId){
    String newPassword = RandomPasswordGenerator.generatePswd(8, 12, 2, 2, 1);
    ldapChangeUserService.changePassword(userId, null, newPassword);
    
    return newPassword;
  }
  
  public void setLdapChangeUserService(LdapChangeUserService ldapChangeUserService) {
    this.ldapChangeUserService = ldapChangeUserService;
  }
}
