package com.carlrosales.capacitor.subscriptions;

import android.content.Context;
import com.android.billingclient.api.*;
import com.getcapacitor.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;

public class Subscriptions {
    
    private Context context;
    private BillingClient billingClient;
    private List<Purchase> activePurchases = new ArrayList<>();
    private CountDownLatch billingLatch;
    
    public Subscriptions(Context context) {
        this.context = context;
        initializeBillingClient();
    }
    
    public void cleanup() {
        if (billingClient != null && billingClient.isReady()) {
            billingClient.endConnection();
        }
    }

    private void initializeBillingClient() {
        billingClient = BillingClient.newBuilder(context)
            .setListener(new PurchasesUpdatedListener() {
                @Override
                public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                        for (Purchase purchase : purchases) {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                activePurchases.add(purchase);
                            }
                        }
                    }
                }
            })
            .enablePendingPurchases()
            .build();
    }

    public int getActiveSubscriptionsCount() {
        Logger.info("Subscriptions", "Getting active subscriptions count");
        queryActiveSubscriptions();
        return activePurchases.size();
    }

    public JSONArray getSubscriptions() {
        Logger.info("Subscriptions", "Getting subscriptions");
        queryActiveSubscriptions();
        
        JSONArray subscriptions = new JSONArray();
        
        for (Purchase purchase : activePurchases) {
            try {
                JSONObject subscription = new JSONObject();
                subscription.put("productId", purchase.getSkus().get(0)); // Get first SKU
                subscription.put("isActive", true);
                subscription.put("purchaseDate", purchase.getPurchaseTime());
                subscription.put("purchaseToken", purchase.getPurchaseToken());
                subscription.put("orderId", purchase.getOrderId());
                subscriptions.put(subscription);
            } catch (JSONException e) {
                Logger.error("Subscriptions", "Error creating subscription JSON: " + e.getMessage());
            }
        }
        
        return subscriptions;
    }

    public boolean isSubscriptionActive(String productId) {
        Logger.info("Subscriptions", "Checking if subscription is active for product: " + productId);
        queryActiveSubscriptions();
        
        for (Purchase purchase : activePurchases) {
            if (purchase.getSkus().contains(productId)) {
                return true;
            }
        }
        
        return false;
    }
    
    private void queryActiveSubscriptions() {
        if (!billingClient.isReady()) {
            Logger.warn("Subscriptions", "Billing client not ready, attempting to start connection");
            startBillingConnection();
            return;
        }
        
        billingLatch = new CountDownLatch(1);
        activePurchases.clear();
        
        billingClient.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),
            new PurchasesResponseListener() {
                @Override
                public void onQueryPurchasesResponse(BillingResult billingResult, List<Purchase> purchases) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (Purchase purchase : purchases) {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                                if (isPurchaseValid(purchase)) {
                                    activePurchases.add(purchase);
                                }
                            }
                        }
                        Logger.info("Subscriptions", "Found " + activePurchases.size() + " active subscriptions");
                    } else {
                        Logger.error("Subscriptions", "Failed to query purchases: " + billingResult.getDebugMessage());
                    }
                    billingLatch.countDown();
                }
            }
        );
        
        try {
            boolean completed = billingLatch.await(10, TimeUnit.SECONDS);
            if (!completed) {
                Logger.warn("Subscriptions", "Billing query timed out");
            }
        } catch (InterruptedException e) {
            Logger.error("Subscriptions", "Interrupted while waiting for billing query: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
    
    private void startBillingConnection() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Logger.info("Subscriptions", "Billing client connected successfully");
                    queryActiveSubscriptions();
                } else {
                    Logger.error("Subscriptions", "Billing setup failed: " + billingResult.getDebugMessage());
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Logger.warn("Subscriptions", "Billing service disconnected");
            }
        });
    }
    
    private boolean isPurchaseValid(Purchase purchase) {
        if (purchase == null || purchase.getSkus().isEmpty()) {
            return false;
        }
        
        if (purchase.getPurchaseToken() == null || purchase.getPurchaseToken().isEmpty()) {
            return false;
        }
        
        if (purchase.getOrderId() == null || purchase.getOrderId().isEmpty()) {
            return false;
        }
        
        return true;
    }
}
