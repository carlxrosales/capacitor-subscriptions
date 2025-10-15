export interface SubscriptionInfo {
  productId: string;
  isActive: boolean;
  expirationDate?: string;
  purchaseDate?: string;
  isTrialPeriod?: boolean;
  isInIntroOfferPeriod?: boolean;
}

export interface SubscriptionsPlugin {
  /**
   * Get the count of active subscriptions
   */
  getActiveSubscriptionsCount(): Promise<{ count: number }>;

  /**
   * Get detailed information about all subscriptions
   */
  getSubscriptions(): Promise<{ subscriptions: SubscriptionInfo[] }>;

  /**
   * Check if a specific product has an active subscription
   */
  isSubscriptionActive(options: { productId: string }): Promise<{ isActive: boolean }>;
}
