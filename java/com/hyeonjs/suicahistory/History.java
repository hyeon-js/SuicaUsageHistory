package com.hyeonjs.suicahistory;

import android.graphics.drawable.Drawable;

public class History {

    final int index, balance, type;
    final String date, action;
    final Drawable icon;

    public History(Suica sc, Drawable icon) {
        index = sc.index;
        balance = sc.balance;
        type = sc.type;
        date = sc.date;
        action = sc.action;
        this.icon = icon;
    }

}
