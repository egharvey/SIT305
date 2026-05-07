package com.example.learningbyloosing;

import android.content.Context;

import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class PaymentsUtil {
    private static JSONObject getBaseRequest() throws JSONException {
        return new JSONObject()
                .put("apiVersion", 2)
                .put("apiVersionMinor", 0);
    }

    public static PaymentsClient createPaymentsClient(Context context) {
        Wallet.WalletOptions walletOptions =
                new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build();
        return Wallet.getPaymentsClient(context, walletOptions);
    }

    private static JSONObject getGatewayTokenizationSpecification() throws JSONException {
        return new JSONObject()
                .put("type", "PAYMENT_GATEWAY")
                .put("parameters", new JSONObject()
                        .put("gateway", "example")
                        .put("gatewayMerchantId", "exampleGatewayMerchantId")
                );
    }

    private static JSONObject getDirectTokenizationSpecification()
            throws JSONException, RuntimeException {
        return new JSONObject()
                .put("type", "DIRECT")
                .put("parameters", new JSONObject(
                new HashMap<String, String>() {{
                    put("protocolVersion", "ECv2");
                    put("publicKey", "My key");
                }}));
    }

    private static JSONArray getAllowedCardNetworks() {
        return new JSONArray(Arrays.asList(
                "AMEX",
                "DISCOVER",
                "JCB",
                "MASTERCARD",
                "VISA"));
    }

    private static JSONArray getAllowedCardAuthMethods() {
        return new JSONArray(Arrays.asList(
                "PAN_ONLY",
                "CRYPTOGRAM_3DS"));
    }

    private static JSONObject getBaseCardPaymentMethod() throws JSONException {
        return new JSONObject()
                .put("type", "CARD")
                .put("parameters", new JSONObject()
                        .put("allowedAuthMethods", getAllowedCardAuthMethods())
                        .put("allowedCardNetworks", getAllowedCardNetworks())
                        .put("billingAddressRequired", true)
                        .put("billingAddressParameters", new JSONObject()
                                .put("format", "FULL")
                        )
                );
    }

    private static JSONObject getCardPaymentMethod() throws JSONException {
        return getBaseCardPaymentMethod()
                .put("tokenizationSpecification", getGatewayTokenizationSpecification());
    }

    public static JSONArray getAllowedPaymentMethods() throws JSONException {
        return new JSONArray().put(getCardPaymentMethod());
    }

    public static JSONObject getIsReadyToPayRequest() {
        try {
            return getBaseRequest()
                    .put("allowedPaymentMethods", new JSONArray().put(getBaseCardPaymentMethod()));
        } catch (JSONException e) {
            return null;
        }
    }

    private static JSONObject getTransactionInfo(String price) throws JSONException {
        return new JSONObject()
                .put("totalPrice", price)
                .put("totalPriceStatus", "FINAL")
                .put("countryCode", "AU")
                .put("currencyCode", "AUD")
                .put("checkoutOption", "COMPLETE_IMMEDIATE_PURCHASE");
    }

    private static JSONObject getMerchantInfo() throws JSONException {
        return new JSONObject().put("merchantName", "LearnByLoosing");
    }

    public static JSONObject getPaymentDataRequest(String priceLabel) {
        try {
            return PaymentsUtil.getBaseRequest()
                    .put("allowedPaymentMethods", getAllowedPaymentMethods())
                    .put("transactionInfo", getTransactionInfo(priceLabel))
                    .put("merchantInfo", getMerchantInfo())
                    .put("shippingAddressRequired", false)
                    .put("shippingAddressParameters", new JSONObject()
                            .put("phoneNumberRequired", false)
                    );

        } catch (JSONException e) {
            return null;
        }
    }
}

