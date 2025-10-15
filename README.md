# capacitor-subscriptions

A lightweight capacitor plugin to access app store subscription for iOS and Android.

## Install

```bash
npm install capacitor-subscriptions
npx cap sync
```

## Usage

```typescript
import { Subscriptions } from 'capacitor-subscriptions';

// Get the count of active subscriptions
const { count } = await Subscriptions.getActiveSubscriptionsCount();
console.log(`Active subscriptions: ${count}`);

// Get detailed subscription information
const { subscriptions } = await Subscriptions.getSubscriptions();
console.log('All subscriptions:', subscriptions);

// Check if a specific product has an active subscription
const { isActive } = await Subscriptions.isSubscriptionActive({
  productId: 'your_subscription_product_id',
});
console.log(`Subscription active: ${isActive}`);
```

## Example Implementation

Here's a complete example of how to use the plugin in your Ionic app:

```typescript
import { Component, OnInit } from '@angular/core';
import { Subscriptions, SubscriptionInfo } from 'capacitor-subscriptions';

@Component({
  selector: 'app-subscription-check',
  template: `
    <ion-content>
      <ion-card>
        <ion-card-header>
          <ion-card-title>Subscription Status</ion-card-title>
        </ion-card-header>
        <ion-card-content>
          <p>Active Subscriptions: {{ activeCount }}</p>
          <ion-button (click)="checkSubscriptions()" [disabled]="loading">
            {{ loading ? 'Checking...' : 'Check Subscriptions' }}
          </ion-button>
        </ion-card-content>
      </ion-card>

      <ion-card *ngIf="subscriptions.length > 0">
        <ion-card-header>
          <ion-card-title>Your Subscriptions</ion-card-title>
        </ion-card-header>
        <ion-card-content>
          <ion-list>
            <ion-item *ngFor="let sub of subscriptions">
              <ion-label>
                <h3>{{ sub.productId }}</h3>
                <p>Active: {{ sub.isActive ? 'Yes' : 'No' }}</p>
                <p *ngIf="sub.expirationDate">Expires: {{ sub.expirationDate }}</p>
              </ion-label>
            </ion-item>
          </ion-list>
        </ion-card-content>
      </ion-card>
    </ion-content>
  `,
})
export class SubscriptionCheckComponent implements OnInit {
  activeCount = 0;
  subscriptions: SubscriptionInfo[] = [];
  loading = false;

  async ngOnInit() {
    await this.checkSubscriptions();
  }

  async checkSubscriptions() {
    this.loading = true;

    try {
      // Get active subscription count
      const countResult = await Subscriptions.getActiveSubscriptionsCount();
      this.activeCount = countResult.count;

      // Get detailed subscription information
      const subscriptionsResult = await Subscriptions.getSubscriptions();
      this.subscriptions = subscriptionsResult.subscriptions;

      // Check specific product (example)
      const specificCheck = await Subscriptions.isSubscriptionActive({
        productId: 'your_premium_subscription_id',
      });

      console.log('Premium subscription active:', specificCheck.isActive);
    } catch (error) {
      console.error('Error checking subscriptions:', error);
    } finally {
      this.loading = false;
    }
  }
}
```

## API

<docgen-index>

- [`getActiveSubscriptionsCount()`](#getactivesubscriptionscount)
- [`getSubscriptions()`](#getsubscriptions)
- [`isSubscriptionActive(...)`](#issubscriptionactive)
- [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### getActiveSubscriptionsCount()

```typescript
getActiveSubscriptionsCount() => Promise<{ count: number; }>
```

Get the count of active subscriptions

**Returns:** <code>Promise&lt;{ count: number; }&gt;</code>

---

### getSubscriptions()

```typescript
getSubscriptions() => Promise<{ subscriptions: SubscriptionInfo[]; }>
```

Get detailed information about all subscriptions

**Returns:** <code>Promise&lt;{ subscriptions: SubscriptionInfo[]; }&gt;</code>

---

### isSubscriptionActive(...)

```typescript
isSubscriptionActive(options: { productId: string; }) => Promise<{ isActive: boolean; }>
```

Check if a specific product has an active subscription

| Param         | Type                                |
| ------------- | ----------------------------------- |
| **`options`** | <code>{ productId: string; }</code> |

**Returns:** <code>Promise&lt;{ isActive: boolean; }&gt;</code>

---

### Interfaces

#### SubscriptionInfo

| Prop                       | Type                 |
| -------------------------- | -------------------- |
| **`productId`**            | <code>string</code>  |
| **`isActive`**             | <code>boolean</code> |
| **`expirationDate`**       | <code>string</code>  |
| **`purchaseDate`**         | <code>string</code>  |
| **`isTrialPeriod`**        | <code>boolean</code> |
| **`isInIntroOfferPeriod`** | <code>boolean</code> |

</docgen-api>
