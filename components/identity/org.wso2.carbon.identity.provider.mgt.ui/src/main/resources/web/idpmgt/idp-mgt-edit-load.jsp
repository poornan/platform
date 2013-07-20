<!--
~ Copyright (c) 2005-2013, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
~
~ WSO2 Inc. licenses this file to you under the Apache License,
~ Version 2.0 (the "License"); you may not use this file except
~ in compliance with the License.
~ You may obtain a copy of the License at
~
~    http://www.apache.org/licenses/LICENSE-2.0
~
~ Unless required by applicable law or agreed to in writing,
~ software distributed under the License is distributed on an
~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
~ KIND, either express or implied.  See the License for the
~ specific language governing permissions and limitations
~ under the License.
-->
<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.wso2.carbon.identity.provider.mgt.stub.dto.TrustedIdPDTO" %>
<%@ page import="org.wso2.carbon.identity.provider.mgt.ui.bean.TrustedIdPBean" %>
<%@ page import="org.wso2.carbon.identity.provider.mgt.ui.client.IdentityProviderMgtServiceClient" %>
<%@ page import="org.wso2.carbon.identity.provider.mgt.ui.util.IdentityProviderMgtUIUtil" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="java.text.MessageFormat" %>
<%@ page import="java.util.*" %>

<%
    String BUNDLE = "org.wso2.carbon.identity.provider.mgt.ui.i18n.Resources";
    ResourceBundle resourceBundle = ResourceBundle.getBundle(BUNDLE, request.getLocale());
    String issuer = request.getParameter("issuer");
    try {
        if(issuer != null && !issuer.equals("")){
            String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);
            String backendServerURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
            ConfigurationContext configContext =
                    (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
            IdentityProviderMgtServiceClient client = new IdentityProviderMgtServiceClient(cookie, backendServerURL, configContext);
            TrustedIdPDTO trustedIdPDTO = client.getTenantIdP(issuer);
            if(trustedIdPDTO != null){
                TrustedIdPBean trustedIdPBean = new TrustedIdPBean();
                trustedIdPBean.setIdPIssuerId(trustedIdPDTO.getIdPIssuerId());
                trustedIdPBean.setPrimary(trustedIdPDTO.getPrimary());
                trustedIdPBean.setIdPUrl(trustedIdPDTO.getIdPUrl());
                if(trustedIdPDTO.getPublicCert() != null){
                    trustedIdPBean.setCertData(IdentityProviderMgtUIUtil.getCertData(trustedIdPDTO.getPublicCert()));
                }
                if(trustedIdPDTO.getRoles() != null){
                    trustedIdPBean.setRoles(new ArrayList<String>(Arrays.asList(trustedIdPDTO.getRoles())));
                } else {
                    trustedIdPDTO.setRoles(new String[0]);
                    trustedIdPBean.setRoles(new ArrayList<String>());
                }
                if(trustedIdPDTO.getRoleMappings() != null){
                    trustedIdPBean.setRoleMappings(IdentityProviderMgtUIUtil.getRoleMappings(trustedIdPDTO.getRoleMappings()));
                } else {
                    trustedIdPDTO.setRoleMappings(new String[0]);
                    trustedIdPBean.setRoleMappings(new HashMap<String, String>());
                }
                session.setAttribute("trustedIdPDTO", trustedIdPDTO);
                session.setAttribute("trustedIdPBean", trustedIdPBean);
            }
        } else if(session.getAttribute("tenantIdPList") == null){
            response.sendRedirect("idp-mgt-list-load.jsp");
            return;
        }
    } catch (Exception e) {

        String message = MessageFormat.format(resourceBundle.getString("error.loading.idp"),
                new Object[]{e.getMessage()});
        CarbonUIMessage.sendCarbonUIMessage(message, CarbonUIMessage.ERROR, request);
        response.sendRedirect("idp-mgt-list.jsp");
        return;
    }
%>
<script type="text/javascript">
    location.href = "idp-mgt-edit.jsp?issuer=<%=issuer%>";
</script>


