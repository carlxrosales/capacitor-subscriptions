import { WebPlugin } from '@capacitor/core';

import type { SubscriptionsPlugin } from './definitions';

export class SubscriptionsWeb extends WebPlugin implements SubscriptionsPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
