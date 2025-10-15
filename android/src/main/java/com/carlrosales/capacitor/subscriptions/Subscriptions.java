package com.carlrosales.capacitor.subscriptions;

import com.getcapacitor.Logger;

public class Subscriptions {

    public String echo(String value) {
        Logger.info("Echo", value);
        return value;
    }
}
