package com.hyeonjs.suicahistory;

import com.darktornado.library.FeliCa;

public class Suica extends FeliCa {

    public static final int TYPE_SUBWAY = TYPE_METRO;
    public String date, action;

    private Suica() {
        super();
    }

    @Override
    public void init(byte[] res, int offset) {
        super.init(res, offset);
        if (type == TYPE_JR) {
            type = TYPE_SUBWAY;
        }
        this.date = year + ". " + month + ". " + super.date + ".";
        if (device != null) {
            this.action = super.action + " / " + device;
        } else {
            this.action = super.action;
        }
    }

    public static Suica parse(byte[] res, int offset) {
        Suica sc = new Suica();
        sc.init(res, offset);
        return sc;
    }
}
