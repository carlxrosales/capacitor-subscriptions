package com.carlrosales.capacitor.subscriptions;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import org.json.JSONArray;

@CapacitorPlugin(name = "Subscriptions")
public class SubscriptionsPlugin extends Plugin {

    private Subscriptions implementation;
    
    @Override
    public void load() {
        implementation = new Subscriptions(getContext());
    }

    @PluginMethod
    public void getActiveSubscriptionsCount(PluginCall call) {
        try {
            int count = implementation.getActiveSubscriptionsCount();
            
            JSObject ret = new JSObject();
            ret.put("count", count);
            call.resolve(ret);
        } catch (Exception e) {
            call.reject("Failed to get subscription count: " + e.getMessage());
        }
    }

    @PluginMethod
    public void getSubscriptions(PluginCall call) {
        try {
            JSONArray subscriptions = implementation.getSubscriptions();
            
            JSObject ret = new JSObject();
            ret.put("subscriptions", subscriptions);
            call.resolve(ret);
        } catch (Exception e) {
            call.reject("Failed to get subscriptions: " + e.getMessage());
        }
    }

    @PluginMethod
    public void isSubscriptionActive(PluginCall call) {
        String productId = call.getString("productId");
        
        if (productId == null || productId.trim().isEmpty()) {
            call.reject("productId is required and cannot be empty");
            return;
        }
        
        try {
            boolean isActive = implementation.isSubscriptionActive(productId);
            
            JSObject ret = new JSObject();
            ret.put("isActive", isActive);
            call.resolve(ret);
        } catch (Exception e) {
            call.reject("Failed to check subscription status: " + e.getMessage());
        }
    }
}
