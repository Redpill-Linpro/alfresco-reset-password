package com.rplp.alfresco.repo.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tradeshift.test.remote.Remote;
import com.tradeshift.test.remote.RemoteTestRunner;

//@RunWith(RemoteTestRunner.class)
//@Remote(runnerClass=SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:alfresco/application-context.xml")
public class LdapChangeUserServiceTest {
  private static final String ADMIN_USER_NAME = "admin";
  static Logger log = Logger.getLogger(LdapChangeUserServiceTest.class);
    
  //@Autowired
  //@Qualifier("webscript.org.alfresco.repository.person.changepassword.post")
  //protected LdapChangeUserServiceImpl ldapChangeUserServiceImpl;
  
  //@Autowired
  //@Qualifier("NodeService")
  //protected NodeService nodeService;
  
  //@Test
  public void testWiring() {
   //   assertNotNull(nodeService);
  }
  
  //@Test
  public void testChangePassword() {
    //assertNotNull(ldapChangeUserServiceImpl);
    //ldapChangeUserServiceImpl.changePassword("ole", "ole", "bole");
  }

}
