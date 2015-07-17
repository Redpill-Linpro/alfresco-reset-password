package com.rplp.alfresco.repo.security;

public interface LdapChangeUserService {
  //public boolean existsInLdap(String userId);
  public void changePassword(String userId, String oldPassword, String newPassword);
}
