package org.mule.modules.amazon.fws.inbound;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.mule.modules.amazon.fws.inbound.generated_classes.Address;
import org.mule.modules.amazon.fws.inbound.generated_classes.MerchantItem;
import org.mule.modules.amazon.fws.inbound.generated_classes.MerchantSKUQuantityItem;
import org.mule.modules.amazon.fws.util.CustomHostNameVerifier;
import org.mule.modules.amazon.fws.util.CustomTrustManager;
import org.mule.modules.amazon.fws.util.Signature;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.MultivaluedMap;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * Created by IntelliJ IDEA.
 * User: davideason
 * Date: 6/8/11
 * Time: 4:03 PM
 * AmazonFWSInbound_Client invokes REST services associated with the Amazon Inbound Fulfillment Web Service (FWS).
 */


public class AmazonFWSInbound_Client {

    private WebResource webResource;
    private MultivaluedMap<String, String> queryParams;
    private String awsAccessKeyId;
    private String awsSecretAccessKey;
    private String responseString;
    private String serviceAction;
    private static final String BASE_URI = "https://fba-inbound.amazonaws.com/";

    public AmazonFWSInbound_Client(String awsAccessKeyId, String awsSecretAccessKey) throws NoSuchAlgorithmException, KeyManagementException {
        //Create a trust manager
        TrustManager[] myTrustManager = new TrustManager[]{new CustomTrustManager()};
        ClientConfig config = new DefaultClientConfig();
        SSLContext ctx;

        //Create hostNameVerifier
        HostnameVerifier hostnameVerifier = new CustomHostNameVerifier();


        try {

            ctx = SSLContext.getInstance("SSL");
            ctx.init(null, myTrustManager, null);

        } catch (NoSuchAlgorithmException nsae) {

            nsae.printStackTrace();
            throw nsae;

        } catch (KeyManagementException kme) {

            kme.printStackTrace();
            throw kme;
        }

        config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(hostnameVerifier, ctx));
        Client client = Client.create(config);
        client.addFilter(new LoggingFilter());
        webResource = client.resource(BASE_URI);
        this.awsAccessKeyId = awsAccessKeyId;
        this.awsSecretAccessKey = awsSecretAccessKey;
        queryParams = new MultivaluedMapImpl();
        responseString = "";
        serviceAction = "";

    }

    public String deleteInboundShipmentItems(@NotNull String shipmentId, @NotNull String merchantSKU) {
        serviceAction = "DeleteInboundShipmentItems";

        // Initialize invocation header
        setInvocationHeader(serviceAction);

        // Set request fields
        queryParams.add("ShipmentId", shipmentId);
        queryParams.add("MerchantSKU", merchantSKU);

        // Invoke service
        responseString = webResource.queryParams(queryParams).get(String.class);

        return responseString;
    }

    public String getFulfillmentIdentifier(@NotNull MerchantItem merchantItem) {
        serviceAction = "GetFulfillmentIdentifier";

        //Initialize invocation header
        setInvocationHeader(serviceAction);

        // Set request fields
        queryParams.add("MerchantItem.ASIN", merchantItem.getASIN());
        queryParams.add("MerchantItem.Condition", merchantItem.getCondition().value());
        queryParams.add("MerchantItem.MerchantSKU", merchantItem.getMerchantSKU());

        responseString = webResource.queryParams(queryParams).get(String.class);

        return responseString;
    }

    public String getFulfillmentIdentifierForMSKU(@NotNull String merchantSKU) {
        serviceAction = "GetFulfillmentIdentifierForMSKU";

        //Initialize invocation header
        setInvocationHeader(serviceAction);
        // Set request fields
        queryParams.add("MerchantSKU", merchantSKU);

        responseString = webResource.queryParams(queryParams).get(String.class);

        return responseString;
    }

    public String getFulfillmentItemByFNSKU(@NotNull String fulfillmentNetworkSKU) {
        serviceAction = "GetFulfillmentItemByFNSKU";

        //Initialize invocation header
        setInvocationHeader(serviceAction);

        // Set request fields
        queryParams.add("FulfillmentNetworkSKU", fulfillmentNetworkSKU);

        responseString = webResource.queryParams(queryParams).get(String.class);

        return responseString;
    }

    public String getFulfillmentItemByMSKU(@NotNull String merchantSKU) {
        serviceAction = "GetFulfillmentItemByMSKU";

        //Initialize invocation header
        setInvocationHeader(serviceAction);

        // Set query parameters
        queryParams.add("MerchantSKU", merchantSKU);

        responseString = webResource.queryParams(queryParams).get(String.class);

        return responseString;
    }

    public String getInboundShipmentData(@NotNull String shipmentId) {
        serviceAction = "GetInboundShipmentData";

        //Initialize invocation header
        setInvocationHeader(serviceAction);

        // Set request fields
        queryParams.add("ShipmentId", shipmentId);

        responseString = webResource.queryParams(queryParams).get(String.class);

        return responseString;
    }

    public String getInboundShipmentPreview(@NotNull Address address,
                                            @NotNull MerchantSKUQuantityItem[] mSKUQuantityItemList) {
        serviceAction = "GetInboundShipmentPreview";

        // Initialize invocation header
        setInvocationHeader(serviceAction);

        // Set Address fields
        queryParams.add("ShipFromAddress.Name", address.getName());
        queryParams.add("ShipFromAddress.AddressLine1", address.getAddressLine1());
        queryParams.add("ShipFromAddress.AddressLine2", address.getAddressLine2());
        queryParams.add("ShipFromAddress.City", address.getCity());
        queryParams.add("ShipFromAddress.StateOrProvinceCode", address.getStateOrProvinceCode());
        queryParams.add("ShipFromAddress.CountryCode", address.getCountryCode());
        queryParams.add("ShipFromAddress.PostalCode", address.getPostalCode());

        // Set Merchant SKU Quantity Item fields
        for (int i = 1; i < mSKUQuantityItemList.length + 1; i++) {
            queryParams.add("MerchantSKUQuantityItem." + i + ".MerchantSKU", mSKUQuantityItemList[i - 1].getMerchantSKU());
            queryParams.add("MerchantSKUQuantityItem." + i + ".Quantity", Integer.toString(mSKUQuantityItemList[i - 1].getQuantity()));
        }

        // Invoke service
        responseString = webResource.queryParams(queryParams).get(String.class);

        return responseString;
    }

    public String getServiceStatus() {
        serviceAction = "GetServiceStatus";

        //Initialize invocation header
        setInvocationHeader(serviceAction);

        responseString = webResource.queryParams(queryParams).get(String.class);

        return responseString;
    }

    public String listAllFulfillmentItems(@NotNull Boolean includeInactive,
                                          @NotNull Integer maxCount) {
        serviceAction = "ListAllFulfillmentItems";

        // Initialize invocation header
        setInvocationHeader(serviceAction);

        // Set request fields
        queryParams.add("IncludeInactive", Boolean.toString(includeInactive));
        queryParams.add("MaxCount", Integer.toString(maxCount));

        // Invoke service
        responseString = webResource.queryParams(queryParams).get(String.class);

        return responseString;
    }

    public String listAllFulfillmentItemsByNextToken(@NotNull String nextToken) {
        serviceAction = "ListAllFulfillmentItemsByNextToken";

        // Initialize invocation header
        setInvocationHeader(serviceAction);

        // Set request fields
        queryParams.add("NextToken", nextToken);

        // Invoke service
        responseString = webResource.queryParams(queryParams).get(String.class);

        return responseString;
    }

    public String listInboundShipmentItems(@NotNull String shipmentId,
                                           @NotNull Integer maxCount) {
        serviceAction = "ListInboundShipmentsItems";

        // Initialize invocation header
        setInvocationHeader(serviceAction);

        // Set request fields
        queryParams.add("ShipmentId", shipmentId);
        queryParams.add("MaxCount", Integer.toString(maxCount));

        // Invoke service
        responseString = webResource.queryParams(queryParams).get(String.class);

        return responseString;
    }

    public String listInboundShipmentItemsByNextToken(@NotNull String nextToken) {
        serviceAction = "ListInboundShipmentsItemsByNextToken";

        // Initialize invocation header
        setInvocationHeader(serviceAction);

        // Set request fields
        queryParams.add("NextToken", nextToken);

        // Invoke service
        responseString = webResource.queryParams(queryParams).get(String.class);


        return responseString;
    }


    public String listInboundShipments(String shipmentStatus,
                                       DateTime createdBefore,
                                       DateTime createdAfter,
                                       @NotNull Integer maxCount) {
        serviceAction = "ListInboundShipments";

        // Initialize invocation header
        setInvocationHeader(serviceAction);

        // Create new formatted DateTime
        DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-DDThh:mm:ss");

        // Set request fields
        queryParams.add("ShipmentStatus", shipmentStatus);
        queryParams.add("CreatedBefore", createdBefore.toString(formatter));
        queryParams.add("CreatedAfter", createdAfter.toString(formatter));
        queryParams.add("MaxCount", Integer.toString(maxCount));

        // Invoke service
        responseString = webResource.queryParams(queryParams).get(String.class);

        return responseString;
    }

    public String listInboundShipmentsByNextToken(@NotNull String nextToken) {
        serviceAction = "ListInboundShipmentsByNextToken";

        // Initialize invocation header
        setInvocationHeader(serviceAction);

        // Set request fields
        queryParams.add("NextToken", nextToken);

        // Invoke service
        responseString = webResource.queryParams(queryParams).get(String.class);

        return responseString;
    }

    public String putInboundShipment(@NotNull String shipmentId,
                                     @NotNull String shipmentName,
                                     @NotNull String destinationFulfillmentCenter,
                                     @NotNull Address shipFromAddress,
                                     @NotNull MerchantSKUQuantityItem mSKUQuantityItem) {
        serviceAction = "PutInboundShipment";

        // Initialize invocation header
        setInvocationHeader(serviceAction);

        // Set request fields
        queryParams.add("ShipmentId", shipmentId);
        queryParams.add("ShipmentName", shipmentName);
        queryParams.add("DestinationFulfillmentCenter", destinationFulfillmentCenter);
        queryParams.add("ShipFromAddress.Name", shipFromAddress.getName());
        queryParams.add("ShipFromAddress.AddressLine1", shipFromAddress.getAddressLine1());
        queryParams.add("ShipFromAddress.AddressLine2", shipFromAddress.getAddressLine2());
        queryParams.add("ShipFromAddress.City", shipFromAddress.getCity());
        queryParams.add("ShipFromAddress.StateOrProvinceCode", shipFromAddress.getStateOrProvinceCode());
        queryParams.add("ShipFromAddress.CountryCode", shipFromAddress.getCountryCode());
        queryParams.add("ShipFromAddress.PostalCode", shipFromAddress.getPostalCode());
        queryParams.add("MerchantSKUQuantityItem.MerchantSKU", mSKUQuantityItem.getMerchantSKU());
        queryParams.add("MerchantSKUQuantityItem.Quantity", Integer.toString(mSKUQuantityItem.getQuantity()));

        // Invoke service
        responseString = webResource.queryParams(queryParams).get(String.class);

        return responseString;
    }

    public String putInboundShipmentData(@NotNull String shipmentId,
                                         @NotNull String shipmentName,
                                         @NotNull String destinationFulfillmentCenter,
                                         @NotNull Address shipFromAddress) {
        serviceAction = "PutInboundShipmentData";

        // Initialize invocation header
        setInvocationHeader(serviceAction);

        // Set request fields
        queryParams.add("ShipmentId", shipmentId);
        queryParams.add("ShipmentName", shipmentName);
        queryParams.add("DestinationFulfillmentCenter", destinationFulfillmentCenter);
        queryParams.add("ShipFromAddress.Name", shipFromAddress.getName());
        queryParams.add("ShipFromAddress.AddressLine1", shipFromAddress.getAddressLine1());
        queryParams.add("ShipFromAddress.AddressLine2", shipFromAddress.getAddressLine2());
        queryParams.add("ShipFromAddress.City", shipFromAddress.getCity());
        queryParams.add("ShipFromAddress.StateOrProvinceCode", shipFromAddress.getStateOrProvinceCode());
        queryParams.add("ShipFromAddress.CountryCode", shipFromAddress.getCountryCode());
        queryParams.add("ShipFromAddress.PostalCode", shipFromAddress.getPostalCode());

        // Invoke service
        responseString = webResource.queryParams(queryParams).get(String.class);

        return responseString;
    }

    public String putInboundShipmentItems(@NotNull String shipmentId,
                                          @NotNull MerchantSKUQuantityItem mSKUQuantityItem) {
        serviceAction = "PutInboundShipmentItems";

        // Initialize invocation header
        setInvocationHeader(serviceAction);

        // Set request fields
        queryParams.add("ShipmentId", shipmentId);
        queryParams.add("MerchantSKUQuantityItem.MerchantSKU", mSKUQuantityItem.getMerchantSKU());
        queryParams.add("MerchantSKUQuantityItem.Quantity", Integer.toString(mSKUQuantityItem.getQuantity()));

        // Invoke service
        responseString = webResource.queryParams(queryParams).get(String.class);

        return responseString;
    }

    public String setInboundShipmentStatus(@NotNull String shipmentId,
                                           @NotNull String shipmentStatus) {
        serviceAction = "SetInboundShipmentStatus";

        // Initialize invocation header
        setInvocationHeader(serviceAction);

        // Set request fields
        queryParams.add("ShipmentId", shipmentId);
        queryParams.add("ShipmentStatus", shipmentStatus);

        // Invoke service
        responseString = webResource.queryParams(queryParams).get(String.class);

        return responseString;
    }

    private void setInvocationHeader(String action) {

        DateTime dateTime = new DateTime();
        String dateTimeAsString = dateTime.toString();
        String signatureData = action + dateTimeAsString;
        String signatureString;
        String serviceVersion = "2007-05-10";

        try {

            signatureString = Signature.calculateRFC2104HMAC(signatureData, awsSecretAccessKey);

        } catch (SignatureException se) {
            String msg = "Signature exception encountered when preparing to invoke: " + serviceAction + ". Ensure that aws_access_key_id and aws_secret_access_key have been correctly specified.";
            throw new org.mule.modules.amazon.fws.util.SignatureException(msg, se);

        }

        queryParams.clear();
        queryParams.add("Action", serviceAction);
        queryParams.add("Version", serviceVersion);
        queryParams.add("AWSAccessKeyId", awsAccessKeyId);
        queryParams.add("Signature", signatureString);
        queryParams.add("Timestamp", dateTimeAsString);
    }
}
