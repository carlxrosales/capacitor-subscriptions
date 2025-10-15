# capacitor-subscriptions

A lightweight capacitor plugin to access app store subscription for iOS and Android.

## Install

```bash
npm install @carlrosales/capacitor-subscriptions
npx cap sync
```

## Compatibility

- **iOS**: 15.0+ (uses StoreKit 2)
- **Android**: API level 23+ (uses Google Play Billing Library 6.1.0)
- **Web**: Not supported

## Usage

```typescript
import { Subscriptions } from '@carlrosales/capacitor-subscriptions';

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

## API

<docgen-index>

* [`getActiveSubscriptionsCount()`](#getactivesubscriptionscount)
* [`getSubscriptions()`](#getsubscriptions)
* [`isSubscriptionActive(...)`](#issubscriptionactive)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### getActiveSubscriptionsCount()

```typescript
getActiveSubscriptionsCount() => Promise<{ count: number; }>
```

Get the count of active subscriptions

**Returns:** <code>Promise&lt;{ count: number; }&gt;</code>

--------------------


### getSubscriptions()

```typescript
getSubscriptions() => Promise<{ subscriptions: SubscriptionInfo[]; }>
```

Get detailed information about all subscriptions

**Returns:** <code>Promise&lt;{ subscriptions: SubscriptionInfo[]; }&gt;</code>

--------------------


### isSubscriptionActive(...)

```typescript
isSubscriptionActive(options: { productId: string; }) => Promise<{ isActive: boolean; }>
```

Check if a specific product has an active subscription

| Param         | Type                                |
| ------------- | ----------------------------------- |
| **`options`** | <code>{ productId: string; }</code> |

**Returns:** <code>Promise&lt;{ isActive: boolean; }&gt;</code>

--------------------


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
