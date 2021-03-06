/*
*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.

  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/

package org.wso2.esb.samples.security;

import org.apache.axiom.om.OMElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.testng.annotations.Test;
import org.wso2.esb.integration.ESBIntegrationTestCase;
import org.wso2.esb.integration.axis2.SampleAxis2Server;
import org.wso2.esb.integration.axis2.StockQuoteClient;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static org.testng.Assert.assertTrue;

public class Sample200Test extends ESBIntegrationTestCase{

    protected Log log = LogFactory.getLog(this.getClass());

    private StockQuoteClient axis2Client;

    public void init() throws Exception {
        axis2Client = new StockQuoteClient();
        launchBackendAxis2Service(SampleAxis2Server.SIMPLE_STOCK_QUOTE_SERVICE);
    }

    @Test(groups = {"wso2.esb"}, description = "Sample 200: Using WS-Security with policy attachments for proxy services")
    public void testSecuredProxyService() throws Exception {

        loadSampleESBConfiguration(200);

        /**
         *     When running this test, the StockQuoteClient fails to fails to load files by their relative paths. Therefore we need to use
         *     absolute paths always. The policy_3.xml file specifies the patch to keystore files as relative values. Since this does not work,
         *     we make a copy of the policy_3.xml file as temp_policy_3.xml by changing the relative keystore paths to absolute paths and
         *     use the temp_policy_3.xml file instead of policy_3.xml
         */

        SAXBuilder builder = new SAXBuilder();

        String policyFileDir =  "repository" + File.separator + "samples" + File.separator +
                                 "resources" + File.separator + "policy";

        //Get the original policy file.
        File originalPolicy = new File(policyFileDir + File.separator + "policy_3.xml");

        //Build original policy file for XML parsing.
        Document document = builder.build(originalPolicy);

        Namespace namespace = Namespace.getNamespace("wsp", "http://schemas.xmlsoap.org/ws/2004/09/policy");

        Element rootNode = document.getRootElement();
        Element exactlyOne = rootNode.getChild("ExactlyOne", namespace);
        Element all = exactlyOne.getChild("All", namespace);

        Namespace rampartNamespace = Namespace.getNamespace("ramp", "http://ws.apache.org/rampart/policy");

        //Get the rampartConfig element
        Element rampartConfig = all.getChild("RampartConfig", rampartNamespace);

        Element signatureCrypto = rampartConfig.getChild("signatureCrypto", rampartNamespace);
        List<Element> properties = signatureCrypto.getChild("crypto", rampartNamespace).getChildren();

        //Find for the relevant property values to change.
        for(Element property : properties){
            if("org.apache.ws.security.crypto.merlin.file".equals(property.getAttribute("name").getValue())){

                 //When found, change the relative path to an absolute path
                 property.setText(new File(property.getValue().trim()).getAbsolutePath());
                 break;
            }
        }

        Element encryptionCypto = rampartConfig.getChild("encryptionCypto", rampartNamespace);
        properties = (encryptionCypto.getChild("crypto", rampartNamespace)).getChildren();

        //Find for the relevant property values to change.
        for(Element property : properties){
            if("org.apache.ws.security.crypto.merlin.file".equals(property.getAttribute("name").getValue())){

                //When found, change the relative path to an absolute path
                property.setText(new File(property.getValue().trim()).getAbsolutePath());
                break;
            }
        }

        String tempPolicyName = "temp_policy_3.xml";
        
        XMLOutputter xmlOut = new XMLOutputter();
        xmlOut.setFormat(Format.getRawFormat());

        //Write the modified XML to temp_policy_3.xml
        FileWriter writer =  new FileWriter(new File(policyFileDir + File.separator + tempPolicyName).getAbsolutePath());
        xmlOut.output(document, writer);
        writer.close();

        File tempPolicy = new File(policyFileDir + File.separator + tempPolicyName);

        //Send request and get response. Use the temp_policy_3.xml
        OMElement response = axis2Client.sendSecuredSimpleStockQuoteRequest(getProxyServiceURL("StockQuoteProxy", false),
                null, "WSO2", tempPolicy.getAbsolutePath());

        assertTrue(response.toString().contains("WSO2"));
        log.info("Response : " + response.toString());

        //Delete temp_policy_3.xml
        tempPolicy.delete();
    }

}
