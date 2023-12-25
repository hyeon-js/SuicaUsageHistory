package com.hyeonjs.suicahistory;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryAdapter extends BaseAdapter {

    protected ArrayList<History> list = new ArrayList<>();
    private int size = -1;

    public HistoryAdapter() {
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int index) {
        return list.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        Context ctx = parent.getContext();
        History item = list.get(pos);

        LinearLayout layout = new LinearLayout(ctx);
        layout.setOrientation(1);

        LinearLayout lay2 = new LinearLayout(ctx);
        lay2.setOrientation(0);
        lay2.setWeightSum(4);

        ImageView icon = new ImageView(ctx);
        icon.setImageDrawable(item.icon);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(ctx, 48), dip2px(ctx, 48), 1);
        params.setMargins(dip2px(ctx, 8), 0, dip2px(ctx, 16), 0);
        params.gravity = Gravity.START | Gravity.CENTER;
        icon.setLayoutParams(params);
        lay2.addView(icon);

        TextView title = new TextView(ctx);
        title.setText("잔액 : " + item.balance + "円");
        title.setTextSize(28);
        title.setLayoutParams(new LinearLayout.LayoutParams(-1, -1, 3));
        title.setGravity(Gravity.CENTER_VERTICAL);
        lay2.addView(title);

        layout.addView(lay2);
        TextView subtitle = new TextView(ctx);
        subtitle.setText("[" + item.date + "] " + item.action);
        subtitle.setTextSize(16);
        int mar = dip2px(ctx, 8);
        LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(-1, -2);
        margin.setMargins(mar, dip2px(ctx, 2), mar, mar);
        subtitle.setLayoutParams(margin);
        layout.addView(subtitle);

        int pad = dip2px(ctx, 16);
        layout.setPadding(pad, pad, pad, pad);
        margin = new LinearLayout.LayoutParams(-1, -2);
        margin.setMargins(mar, mar, mar, mar);
        layout.setLayoutParams(margin);
        layout.setBackgroundColor(Color.WHITE);
        layout.setElevation(dip2px(ctx, 3));

        LinearLayout layout0 = new LinearLayout(ctx);
        layout0.addView(layout);
        view = layout0;

        return view;
    }

    public void setIconSize(int size) {
        this.size = size;
    }

    public void setItems(ArrayList<History> list) {
        this.list.clear();
        this.list.addAll(list);
    }

    private int dip2px(Context ctx, int dips) {
        return (int) Math.ceil(dips * ctx.getResources().getDisplayMetrics().density);
    }
}