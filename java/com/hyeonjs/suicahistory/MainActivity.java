package com.hyeonjs.suicahistory;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private LinearLayout layout;
    PendingIntent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            layout = new LinearLayout(this);
            layout.setOrientation(1);

            TextView txt = new TextView(this);
            txt.setText(R.string.tag_card);
            txt.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
            txt.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
            layout.addView(txt);

            setContentView(layout);

            Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            this.intent = PendingIntent.getActivity(this, 0, intent, 0);

        } catch (Exception e) {
            toast(e.toString());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
        adapter.enableForegroundDispatch(this, intent, null, null); //onCreate에서는 못씀
    }

    @Override
    protected void onPause() {
        super.onPause();
        NfcAdapter.getDefaultAdapter(this).disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if (tag == null) return;

            byte[] id = tag.getId();
            if (id == null) return;

            NfcF fc = NfcF.get(tag);
            fc.connect();
            byte[] req = Suica.readWithoutEncryption(id, 10);
            byte[] res = fc.transceive(req);
            fc.close();

            parseHistory(res);
        } catch (Exception e) {
            toast(e.toString());
        }
    }


    private void parseHistory(byte[] res) {
        if (res[10] != 0x00) {
            toast("태그된 카드가 Suica 호환 교통카드가 아닌 것 같아요.");
            return;
        }
        int size = res[12];

        Drawable[] icons = new Drawable[4];
        icons[0] = getResources().getDrawable(R.drawable.charge);
        icons[Suica.TYPE_SUBWAY] = getResources().getDrawable(R.drawable.subway);
        icons[Suica.TYPE_BUS] = getResources().getDrawable(R.drawable.bus);
        icons[Suica.TYPE_GOODS] = getResources().getDrawable(R.drawable.shop);

        final ArrayList<History> items = new ArrayList<>();
        for (int n = 0; n < size; n++) {
            Suica sc = Suica.parse(res, 13 + n * 16);
            if (sc.index == 0) continue;
            Drawable icon = icons[sc.type];
            if (sc.action.contains("충전") || sc.action.contains("신규")) icon = icons[0];
            items.add(new History(sc, icon));
        }

        ListView list = new ListView(this);
        list.setDivider(null);
        HistoryAdapter adapter = new HistoryAdapter();
        adapter.setItems(items);
        list.setAdapter(adapter);
        int pad = dip2px(16);
        list.setPadding(pad, pad, pad, pad);

        layout.removeAllViews();
        layout.addView(list);

        toast("카드 정보를 불러왔어요.");
    }


    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private int dip2px(int dips) {
        return (int) Math.ceil(dips * getResources().getDisplayMetrics().density);
    }

}