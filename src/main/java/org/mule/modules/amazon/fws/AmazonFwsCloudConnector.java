/**
 * Amazon FWS Cloud Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.modules.amazon.fws;


import org.joda.time.DateTime;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.modules.amazon.fws.inbound.AmazonFWSInbound_Client;
import org.mule.modules.amazon.fws.inbound.generated_classes.Address;
import org.mule.modules.amazon.fws.inbound.generated_classes.ItemCondition;
import org.mule.modules.amazon.fws.inbound.generated_classes.MerchantItem;
import org.mule.modules.amazon.fws.inbound.generated_classes.MerchantSKUQuantityItem;
import org.mule.tools.cloudconnect.annotations.Connector;
import org.mule.tools.cloudconnect.annotations.Operation;
import org.mule.tools.cloudconnect.annotations.Parameter;
import org.mule.tools.cloudconnect.annotations.Property;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * A cloud connector wrapper on {@link } api. Same exception handling
 * policies applies. Documentation is based in that of {@link /documentation/fws/}
 */
@Connector(namespacePrefix = "amazon-fws", namespaceUri = "http://www.mulesoft.org/schema/mule/amazon-fws")
public class AmazonFwsCloudConnector implements Initialisable {
    @Property
    private String aws_access_key_id;
    @Property
    private String aws_secret_access_key;
    //TODO: Implement 'configurable' property to determine if returnType is XML or OBJECT
    @Property
//    private Enum returnType {XML,OBJECT};

    //TODO: Important - Add WSDL to Maven config so that required aws objects are generated as a part of compilation process.
    //TODO: Investigate using JAXB from processing AWS responses (simple XML to Object processing)
    private AmazonFWSInbound_Client amazonFWSInboundClient;

    /*
     * getAwsAccessKeyId()
     */
    @Operation
    public String getAwsAccessKeyId() {
        return aws_access_key_id;
    }

    /*
     * setAwsAccessKeyId()
     */
    @Operation
    public void setAwsAccessKeyId(String keyId) {
        this.aws_access_key_id = keyId;
    }

    /*
     * getAwsSecretAccessKey()
     */
    @Operation
    public String getAwsSecretAccessKey() {
        return aws_secret_access_key;
    }

    /*
     * setAwsSecretAccessKey()
     */
    @Operation
    public void setAwsSecretAccessKey(String secretAccessKey) {
        this.aws_secret_access_key = secretAccessKey;
    }

    /*
     * getAmazonFWSInboundClient()
     */
    private AmazonFWSInbound_Client getAmazonFWSInboundClient() {
        return this.amazonFWSInboundClient;
    }

    /*
     * setAmazonFWSInboundClient()
     */
    private void setAmazonFWSInboundClient(AmazonFWSInbound_Client client) {
        this.amazonFWSInboundClient = client;
    }

    /*
     * initialise()
     */
    @Operation
    public void initialise() throws InitialisationException {
        if (amazonFWSInboundClient == null) {

            try {

                setAmazonFWSInboundClient(new AmazonFWSInbound_Client(getAwsAccessKeyId(), getAwsSecretAccessKey()));

            } catch (NoSuchAlgorithmException e) {

                System.out.println("Error occurred while calling initialise method unable to create AmazonFWSInboundClient: " + e.toString());

            } catch (KeyManagementException e) {

                System.out.println("Error occurred while calling initialise method unable to create AmazonFWSInboundClient: " + e.toString());
            }
        }

    }

    /*
     * deleteInboundShipmentItems()
     *
     * Example:
     *
     * {@code <amazon-fws:inbound-delete-shipment-items
     *                   shipmentId="#[map-payload:shipmentId]"
     *                  merchantSKU="#[map-payload:merchantSKU]"/>}
     *
     * @param String shipmentId
     * @param String merchantSKU
     * @return String xmlResponse
     * @throws java.security.SignatureException
     */
    @Operation
    public String deleteInboundShipmentItems(@Parameter String shipmentId,
                                             @Parameter String merchantSKU) {

        return amazonFWSInboundClient.deleteInboundShipmentItems(shipmentId, merchantSKU);
    }

    /*
     * getFulfillmentIdentifier()
     *
     * Example:
     *
     * {@code <amazon-fws:inbound-get-fulfillment-id
     *                   merchantItemASIN="#[map-payload:merchantItemASIN]"
     *                   merchantItemCondition="#[map-payload:merchantItemCondition]"
     *                   merchantItemMSKU="#[map-payload:merchantItemMSKU]"/>}
     *
     * @param String merchantItemASIN
     * @param String merchantItemCondition
     * @param String merchantItemMSKU
     * @return String xmlResponse
     * @throws java.security.SignatureException
     */
    @Operation
    public String getFulfillmentIdentifier(@Parameter String merchantItemASIN,
                                           @Parameter String merchantItemCondition,
                                           @Parameter String merchantItemMSKU) {

        MerchantItem merchantItem = new MerchantItem();
        merchantItem.setASIN(merchantItemASIN);
        merchantItem.setCondition(ItemCondition.valueOf(merchantItemCondition));
        merchantItem.setMerchantSKU(merchantItemMSKU);

        return amazonFWSInboundClient.getFulfillmentIdentifier(merchantItem);
    }

    /*
    * getFulfillmentIdentifierForMSKU()
    *
    * Example:
    *
    * {@code <amazon-fws:inbound-get-fulfillment-id-for-msku
    *               merchantSKU="#[map-payload:merchantSKU]"/>}
    *
    * @param String merchantSKU
    * @return String xmlResponse
    * @throws java.security.SignatureException
    */
    @Operation
    public String getFulfillmentIdentifierForMSKU(@Parameter String merchantSKU) {

        return amazonFWSInboundClient.getFulfillmentIdentifierForMSKU(merchantSKU);
    }

    /*
    * getFulfillmentItemByFNSKU()
    *
    * Example:
    *
    * {@code <amazon-fws:inbound-get-fulfillment-item-by-fnsku
    *                   fulfillmentNetworkSKU="#[map-payload:fulfillmentNetworkSKU]"/>}
    *
    * @return String xmlResponse
    * @throws java.security.SignatureException
    */
    @Operation
    public String getFulfillmentItemByFNSKU(@Parameter String fulfillmentNetworkSKU) {

        return amazonFWSInboundClient.getFulfillmentItemByFNSKU(fulfillmentNetworkSKU);
    }

    /*
    * getFulfillmentItemByMSKU()
    *
    * Example:
    *
    * {@code <amazon-fws:inbound-get-fulfillment-item-by-msku
    *                       merchantSKU="#[map-payload:merchantSKU]"/>}
    *
    * @param String merchantSKU
    * @return String xmlResponse
    * @throws java.security.SignatureException
    */
    @Operation
    public String getFulfillmentItemByMSKU(@Parameter String merchantSKU) {

        return amazonFWSInboundClient.getFulfillmentItemByMSKU(merchantSKU);
    }

    /*
    * getInboundShipmentData()
    *
    * Example:
    *
    * {@code <amazon-fws:inbound-get-shipment-data
    *                       shipmentId="#[map-payload:shipmentId]"/>}
    *
    * @param String shipmentId
    * @return String xmlResponse
    * @throws java.security.SignatureException
    */
    @Operation
    public String getInboundShipmentData(@Parameter String shipmentId) {

        return amazonFWSInboundClient.getInboundShipmentData(shipmentId);
    }

   /*
    * getInboundShipmentPreview()
    *
    * Example:
    *
    * {@code <amazon-fws:inbound-get-shipment-preview
    *                       addressName="#[map-payload:addressName]"
    *                       addressLine1="#[map-payload:addressLine1]"
    *                       addressLine2="#[map-payload:addressLine2]"
    *                       addressCity="#[map-payload:addressCity]"
    *                       addressStateOrProvinceCode="#[map-payload:addressStateOrProvinceCode]"
    *                       addressCountryCode="#[map-payload:addressCountryCode]"
    *                       addressPostalCode="#[map-payload:addressPostalCode]"
    *                       merchantSKU="#[map-payload:merchantSKU]"
    *                       merchantQuantity="#[map-payload:merchantQuantity]"/>}
    *
    * @param String addressName
    * @param String addressLine1
    * @param String addressLine2
    * @param String addressCity
    * @param String addressStateOrProvinceCode
    * @param String addressCountryCode
    * @param String addressPostalCode
    * @param String merchantSKU
    * @param Integer merchantQuantity
    * @return String xmlResponse
    * @throws java.security.SignatureException
    */
    @Operation
    public String getInboundShipmentPreview(@Parameter String addressName,
                                            @Parameter String addressLine1,
                                            @Parameter String addressLine2,
                                            @Parameter String addressCity,
                                            @Parameter String addressStateOrProvinceCode,
                                            @Parameter String addressCountryCode,
                                            @Parameter String addressPostalCode,
                                            @Parameter String merchantSKU,
                                            @Parameter Integer merchantQuantity) {

        Address shipFromAddress = new Address();
        MerchantSKUQuantityItem[] merchantSKUQuantityItems;

        shipFromAddress.setName(addressName);
        shipFromAddress.setAddressLine1(addressLine1);
        shipFromAddress.setAddressLine2(addressLine2);
        shipFromAddress.setCity(addressCity);
        shipFromAddress.setStateOrProvinceCode(addressStateOrProvinceCode);
        shipFromAddress.setCountryCode(addressCountryCode);
        shipFromAddress.setPostalCode(addressPostalCode);

        //TODO: Verify multiplicity of MerchantSKUQuantityItems and iterate if required.
        merchantSKUQuantityItems = new MerchantSKUQuantityItem[1];
        merchantSKUQuantityItems[0] = new MerchantSKUQuantityItem();
        merchantSKUQuantityItems[0].setMerchantSKU(merchantSKU);
        merchantSKUQuantityItems[0].setQuantity(merchantQuantity);

        return amazonFWSInboundClient.getInboundShipmentPreview(shipFromAddress, merchantSKUQuantityItems);
    }

    /*
    * getInboundServiceStatus()
    *
    * Example:
    *
    * {@code <amazon-fws:inbound-get-service-status/>}
    *
    * @return Service Status
    * @throws java.security.SignatureException
    */
    @Operation
    public String getInboundServiceStatus() {

        return amazonFWSInboundClient.getServiceStatus();
    }

    /*
    * listAllFulfillmentItems()
    *
    * Example:
    *
    * {@code <amazon-fws:inbound-list-all-fulfillment-items
    *                       includeInactive="#[map-payload:includeInactive]"
    *                       maxCount="#[map-payload:maxCount]"/>}
    *
    * @param Boolean includeInactive
    * @param Integer maxCount
    * @return String xmlResponse
    * @throws java.security.SignatureException
    */
    @Operation
    public String listAllFulfillmentItems(@Parameter Boolean includeInactive,
                                          @Parameter Integer maxCount) {

        return amazonFWSInboundClient.listAllFulfillmentItems(includeInactive, maxCount);
    }

    /*
    * listAllFulfillmentItemsByNextToken()
    *
    * Example:
    *
    * {@code <amazon-fws:inbound-list-all-fulfillment-items-by-next-token
    *                       nextToken="#[map-payload:nextToken]"/>}
    *
    * @param String nextToken
    * @return String xmlResponse
    * @throws java.security.SignatureException
    */
    @Operation
    public String listAllFulfillmentItemsByNextToken(@Parameter String nextToken) {

        return amazonFWSInboundClient.listAllFulfillmentItemsByNextToken(nextToken);
    }

    /*
    * listInboundShipmentItems()
    *
    * Example:
    *
    * {@code <amazon-fws:inbound-list-shipment-items
    *                       shipmentId="#[map-payload:shipmentId]"
    *                       maxCount="#[map-payload:maxCount]"/>}
    *
    * @param String shipmentId
    * @param Integer maxCount
    * @return String xmlResponse
    * @throws java.security.SignatureException
    */
    @Operation
    public String listInboundShipmentItems(@Parameter String shipmentId,
                                            @Parameter Integer maxCount) {

        return amazonFWSInboundClient.listInboundShipmentItems(shipmentId, maxCount);
    }

    /*
    * listInboundShipmentItemsByNextToken()
    *
    * Example:
    *
    * {@code <amazon-fws:inbound-list-shipment-items-by-next-token
    *                       nextToken="#[map-payload:nextToken]"/>}
    *
    * @param String nextToken
    * @return String xmlResponse
    * @throws java.security.SignatureException
    */
    @Operation
    public String listInboundShipmentItemsByNextToken(@Parameter String nextToken) {

        return amazonFWSInboundClient.listInboundShipmentItemsByNextToken(nextToken);
    }

    /*
    * listInboundShipments()
    *
    * Example:
    *
    * {@code <amazon-fws:inbound-list-shipments
    *                     shipmentStatus="#[map-payload:shipmentStatus]"
    *                     createdBefore="#[map-payload:createdBefore]"
    *                     createdAfter="#[map-payload:createdAfter]"
    *                     maxCount="#[map-payload:maxCount]"/>}
    *
    * @param String shipmentStatus
    * @param DateTime createdBefore
    * @param DateTime createdAfter
    * @param Integer maxCount
    * @return String xmlResponse
    * @throws java.security.SignatureException
    */
    @Operation
    public String listInboundShipments(@Parameter (optional = true)String shipmentStatus,
                                       @Parameter (optional = true)DateTime createdBefore,
                                       @Parameter (optional = true)DateTime createdAfter,
                                       @Parameter Integer maxCount) {

        return amazonFWSInboundClient.listInboundShipments(shipmentStatus,createdBefore,createdAfter,maxCount);
    }

    /*
    * listInboundShipmentsByNextToken()
    *
    * Example:
    *
    * {@code <amazon-fws:inbound-list-shipments-by-next-token
    *                       nextToken="#[map-payload:nextToken]"/>}
    *
    * @param String nextToken
    * @return String xmlResponse
    * @throws java.security.SignatureException
    */
    @Operation
    public String listInboundShipmentsByNextToken(@Parameter String nextToken) {

        return amazonFWSInboundClient.listInboundShipmentsByNextToken(nextToken);
    }

    /*
    * putInboundShipment()
    *
    * Example:
    *
    * {@code <amazon-fws:inbound-put-shipment
    *                       shipmentId="#[map-payload:shipmentId]"
    *                       shipmentName="#[map-payload:shipmentName]"
    *                       destinationFulfillmentCenter="#[map-payload:destinationFulfillmentCenter]"
    *                       addressName="#[map-payload:addressName]"
    *                       addressLine1="#[map-payload:addressLine1]"
    *                       addressLine2="#[map-payload:addressLine2]"
    *                       addressCity="#[map-payload:addressCity]"
    *                       addressStateOrProvinceCode="#[map-payload:addressStateOrProvinceCode]"
    *                       addressCountryCode="#[map-payload:addressCountryCode]"
    *                       addressPostalCode="#[map-payload:addressPostalCode]"
    *                       merchantSKU="#[map-payload:merchantSKU]"
    *                       merchantQuantity="#[map-payload:merchantQuantity]"/>}
    *
    * @param String shipmentId
    * @param String shipmentName
    * @param String destinationFulfillmentCenter
    * @param String addressName
    * @param String addressLine1
    * @param String addressLine2
    * @param String addressCity
    * @param String addressStateOrProvinceCode
    * @param String addressCountryCode
    * @param String addressPostalCode
    * @param String merchantSKU
    * @param Integer merchantQuantity
    * @return String xmlResponse
    * @throws java.security.SignatureException
    */
    @Operation
    public String putInboundShipment(@Parameter String shipmentId,
                                     @Parameter String shipmentName,
                                     @Parameter String destinationFulfillmentCenter,
                                     @Parameter String addressName,
                                     @Parameter String addressLine1,
                                     @Parameter String addressLine2,
                                     @Parameter String addressCity,
                                     @Parameter String addressStateOrProvinceCode,
                                     @Parameter String addressCountryCode,
                                     @Parameter String addressPostalCode,
                                     @Parameter String merchantSKU,
                                     @Parameter Integer merchantQuantity) {

        Address shipmentAddress = new Address();
        MerchantSKUQuantityItem merchantSKUQuantityItem = new MerchantSKUQuantityItem();

        shipmentAddress.setName(addressName);
        shipmentAddress.setAddressLine1(addressLine1);
        shipmentAddress.setAddressLine2(addressLine2);
        shipmentAddress.setCity(addressCity);
        shipmentAddress.setStateOrProvinceCode(addressStateOrProvinceCode);
        shipmentAddress.setCountryCode(addressCountryCode);
        shipmentAddress.setPostalCode(addressPostalCode);

        merchantSKUQuantityItem.setMerchantSKU(merchantSKU);
        merchantSKUQuantityItem.setQuantity(merchantQuantity);

        return amazonFWSInboundClient.putInboundShipment(shipmentId,shipmentName,destinationFulfillmentCenter,shipmentAddress,merchantSKUQuantityItem);
    }

   /*
    * putInboundShipmentData()
    *
    * Example:
    *
    * {@code <amazon-fws:inbound-put-shipment-data
    *                       shipmentId="#[map-payload:shipmentId]"
    *                       shipmentName="#[map-payload:shipmentName]"
    *                       destinationFulfillmentCenter="#[map-payload:destinationFulfillmentCenter]"
    *                       addressName="#[map-payload:addressName]"
    *                       addressLine1="#[map-payload:addressLine1]"
    *                       addressLine2="#[map-payload:addressLine2]"
    *                       addressCity="#[map-payload:addressCity]"
    *                       addressStateOrProvinceCode="#[map-payload:addressStateOrProvinceCode]"
    *                       addressCountryCode="#[map-payload:addressCountryCode]"
    *                       addressPostalCode="#[map-payload:addressPostalCode]"/>}
    *
    * @param String shipmentId
    * @param String shipmentName
    * @param String destinationFulfillmentCenter
    * @param String addressName
    * @param String addressLine1
    * @param String addressLine2
    * @param String addressCity
    * @param String addressStateOrProvinceCode
    * @param String addressCountryCode
    * @param String addressPostalCode
    * @return String xmlResponse
    * @throws java.security.SignatureException
    */
    @Operation
    public String putInboundShipmentData(@Parameter String shipmentId,
                                     @Parameter String shipmentName,
                                     @Parameter String destinationFulfillmentCenter,
                                     @Parameter String addressName,
                                     @Parameter String addressLine1,
                                     @Parameter String addressLine2,
                                     @Parameter String addressCity,
                                     @Parameter String addressStateOrProvinceCode,
                                     @Parameter String addressCountryCode,
                                     @Parameter String addressPostalCode) {

        Address shipmentAddress = new Address();

        shipmentAddress.setName(addressName);
        shipmentAddress.setAddressLine1(addressLine1);
        shipmentAddress.setAddressLine2(addressLine2);
        shipmentAddress.setCity(addressCity);
        shipmentAddress.setStateOrProvinceCode(addressStateOrProvinceCode);
        shipmentAddress.setCountryCode(addressCountryCode);
        shipmentAddress.setPostalCode(addressPostalCode);

        return amazonFWSInboundClient.putInboundShipmentData(shipmentId,shipmentName,destinationFulfillmentCenter,shipmentAddress);
    }

    /*
    * putInboundShipmentItems()
    *
    * Example:
    *
    * {@code <amazon-fws:inbound-put-shipment-items
    *                       shipmentId="#[map-payload:shipmentId]"
    *                       merchantSKU="#[map-payload:merchantSKU]"
    *                       merchantQuantity="#[map-payload:merchantQuantity]"/>}
    *
    * @param String shipmentId
    * @param String merchantSKU
    * @param Integer merchantQuantity
    * @return String xmlResponse
    * @throws java.security.SignatureException
    */
    @Operation
    public String putInboundShipmentItems(@Parameter String shipmentId,
                                          @Parameter String merchantSKU,
                                          @Parameter Integer merchantQuantity) {

        MerchantSKUQuantityItem merchantSKUQuantityItem = new MerchantSKUQuantityItem();
        merchantSKUQuantityItem.setMerchantSKU(merchantSKU);
        merchantSKUQuantityItem.setQuantity(merchantQuantity);

        return amazonFWSInboundClient.putInboundShipmentItems(shipmentId,merchantSKUQuantityItem);
    }

    /*
    * setInboundShipmentStatus()
    *
    * Example:
    *
    * {@code <amazon-fws:inbound-set-shipment-status
    *                       shipmentId="#[map-payload:shipmentId]"
    *                       shipmentStatus="#[map-payload:shipmentStatus]"/>}
    *
    * @param String shipmentId
    * @param String shipmentStatus
    * @return String xmlResponse
    * @throws java.security.SignatureException
    */
    @Operation
    public String setInboundShipmentStatus(@Parameter String shipmentId,
                                           @Parameter String shipmentStatus) {

        return amazonFWSInboundClient.setInboundShipmentStatus(shipmentId,shipmentStatus);
    }

}
