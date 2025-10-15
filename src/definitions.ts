export interface SubscriptionsPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
