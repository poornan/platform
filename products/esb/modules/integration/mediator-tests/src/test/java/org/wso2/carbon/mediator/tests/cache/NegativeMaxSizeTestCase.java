package org.wso2.carbon.mediator.tests.cache;
/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/

import org.apache.axiom.om.OMElement;
import org.testng.annotations.Test;
import org.wso2.esb.integration.ESBIntegrationTestCase;
import org.wso2.esb.integration.axis2.SampleAxis2Server;
import org.wso2.esb.integration.axis2.StockQuoteClient;

import javax.xml.namespace.QName;
import java.rmi.RemoteException;

import static org.testng.Assert.assertTrue;

/**
 * This class will test cache mediator which has negative value to max size property
 * in it's configuration
 */
public class NegativeMaxSizeTestCase extends ESBIntegrationTestCase {
    private StockQuoteClient axis2Client;

    public void init() throws Exception {
        axis2Client = new StockQuoteClient();
        launchBackendAxis2Service(SampleAxis2Server.SIMPLE_STOCK_QUOTE_SERVICE);
        String filePath = "/mediators/cache/negative_max_size.xml";
        loadESBConfigurationFromClasspath(filePath);
    }

    @Test(groups = {"wso2.esb"})
    public void testMessageSizeProperty() throws RemoteException, InterruptedException {

        OMElement response = axis2Client.sendSimpleStockQuoteRequest(
                null, getMainSequenceURL(), "MSFT");

        String tradingTime = response.getFirstElement().getFirstChildWithName(new QName
                ("http://services.samples/xsd", "lastTradeTimestamp")).getText();
        Thread.sleep(2000);

        response = axis2Client.sendSimpleStockQuoteRequest(
                null, getMainSequenceURL(), "MSFT");
        String newTradingTime = response.getFirstElement().getFirstChildWithName(new QName
                ("http://services.samples/xsd", "lastTradeTimestamp")).getText();

        Thread.sleep(2000);
        response = axis2Client.sendSimpleStockQuoteRequest(
                null, getMainSequenceURL(), "MSFT");
        String newTradingTime2 = response.getFirstElement().getFirstChildWithName(new QName
                ("http://services.samples/xsd", "lastTradeTimestamp")).getText();

        System.out.println("First message  :" + tradingTime);
        System.out.println("Second message :" + newTradingTime);
        System.out.println("Third message  :" + newTradingTime2);

        if(tradingTime.equals(newTradingTime)){
            assertTrue(true, "Response caching having error with negative max size value");
        }
        if(tradingTime.equals(newTradingTime2)){
            assertTrue(false,"Response caching having error with negative max size value");
        }
        if(newTradingTime.equals(newTradingTime2)){
            assertTrue(false,"Response caching having error with negative max size value");
        }

    }

    @Override
    protected void cleanup() {
        super.cleanup();
        axis2Client.destroy();
    }
}
