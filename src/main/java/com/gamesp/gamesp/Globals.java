package com.gamesp.gamesp;

import android.app.Application;
import android.content.Context;

public class Globals extends Application {
    private String position="[0,0,S]";

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public Context currentContext;
}
