import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@available(iOS 15.0, *)
@objc(SubscriptionsPlugin)
public class SubscriptionsPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "SubscriptionsPlugin"
    public let jsName = "Subscriptions"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "getActiveSubscriptionsCount", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "getSubscriptions", returnType: CAPPluginReturnPromise),
        CAPPluginMethod(name: "isSubscriptionActive", returnType: CAPPluginReturnPromise)
    ]
    private let implementation = Subscriptions()

    @objc func getActiveSubscriptionsCount(_ call: CAPPluginCall) {
        do {
            let count = implementation.getActiveSubscriptionsCount()
            call.resolve([
                "count": count
            ])
        } catch {
            call.reject("Failed to get subscription count: \(error.localizedDescription)")
        }
    }
    
    @objc func getSubscriptions(_ call: CAPPluginCall) {
        do {
            let subscriptions = implementation.getSubscriptions()
            call.resolve([
                "subscriptions": subscriptions
            ])
        } catch {
            call.reject("Failed to get subscriptions: \(error.localizedDescription)")
        }
    }
    
    @objc func isSubscriptionActive(_ call: CAPPluginCall) {
        guard let productId = call.getString("productId"), !productId.isEmpty else {
            call.reject("productId is required and cannot be empty")
            return
        }
        
        do {
            let isActive = implementation.isSubscriptionActive(productId: productId)
            call.resolve([
                "isActive": isActive
            ])
        } catch {
            call.reject("Failed to check subscription status: \(error.localizedDescription)")
        }
    }
}
