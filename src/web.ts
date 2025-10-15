import { WebPlugin } from '@capacitor/core';

import type { SubscriptionsPlugin, SubscriptionInfo } from './definitions';

export class SubscriptionsWeb extends WebPlugin implements SubscriptionsPlugin {
  async getActiveSubscriptionsCount(): Promise<{ count: number }> {
    throw new Error('Subscriptions plugin is not supported on web platform. Use iOS or Android.');
  }

  async getSubscriptions(): Promise<{ subscriptions: SubscriptionInfo[] }> {
    throw new Error('Subscriptions plugin is not supported on web platform. Use iOS or Android.');
  }

  async isSubscriptionActive(_options: { productId: string }): Promise<{ isActive: boolean }> {
    throw new Error('Subscriptions plugin is not supported on web platform. Use iOS or Android.');
  }
}
