<#-- @overridden projects/slingshot/config/alfresco/site-webscripts/org/alfresco/components/profile/change-password.get.html.ftl -->

<@markup id="rplp-html" target="html" action="replace">
   <@uniqueIdDiv>
      <#assign el=args.htmlid?html>
      <div id="${el}-body" class="profile password">
         <form id="${el}-form" action="${url.context}/service/components/profile/change-ldap-password" method="post">
            <div class="header-bar">${msg("label.changepassword")}</div>
            <div class="row">
               <span class="label"><label for="${el}-oldpassword">${msg("label.oldpassword")}:</label></span>
               <span><input type="password" maxlength="255" size="30" id="${el}-oldpassword" /></span>
            </div>
            <div class="row">
               <span class="label"><label for="${el}-newpassword1">${msg("label.newpassword")}:</label></span>
               <span><input type="password" maxlength="255" size="30" id="${el}-newpassword1" /></span>
            </div>
            <div class="row">
               <span class="label"><label for="${el}-newpassword2">${msg("label.confirmpassword")}:</label></span>
               <span><input type="password" maxlength="255" size="30" id="${el}-newpassword2" /></span>
            </div>
            <hr/>
            <div class="buttons">
               <button id="${el}-button-ok" name="save">${msg("button.ok")}</button>
               <button id="${el}-button-cancel" name="cancel">${msg("button.cancel")}</button>
            </div>
         </form>
      </div>
   </@>
</@>

