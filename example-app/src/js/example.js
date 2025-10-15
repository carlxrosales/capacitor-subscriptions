import { Subscriptions } from 'capacitor-subscriptions';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    Subscriptions.echo({ value: inputValue })
}
