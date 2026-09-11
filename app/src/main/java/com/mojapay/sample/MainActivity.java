package com.mojapay.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends Activity {
    private final int GREEN = Color.parseColor("#0B6E4F");
    private final int GREEN_DARK = Color.parseColor("#07563E");
    private final int LIGHT_GREEN = Color.parseColor("#E9F6F0");
    private final int TEXT = Color.parseColor("#273043");
    private final int MUTED = Color.parseColor("#6B7280");

    private int dp(int value) { return (int) (value * getResources().getDisplayMetrics().density + 0.5f); }

    private GradientDrawable rounded(int color, int radiusDp) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radiusDp));
        return d;
    }

    private TextView label(String text) {
        TextView v = new TextView(this);
        v.setText(text);
        v.setTextColor(TEXT);
        v.setTextSize(14);
        v.setTypeface(null, Typeface.BOLD);
        v.setPadding(0, dp(14), 0, dp(6));
        return v;
    }

    private void styleTab(Button button, boolean active) {
        button.setTextColor(active ? Color.WHITE : GREEN_DARK);
        button.setBackground(rounded(active ? GREEN : LIGHT_GREEN, 16));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(Color.parseColor("#F4F7FB"));

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(24), dp(20), dp(28));
        scroll.addView(root);

        TextView mark = new TextView(this);
        mark.setText("M");
        mark.setTextColor(Color.WHITE);
        mark.setTextSize(30);
        mark.setTypeface(null, Typeface.BOLD);
        mark.setGravity(Gravity.CENTER);
        GradientDrawable circle = new GradientDrawable();
        circle.setShape(GradientDrawable.OVAL);
        circle.setColor(GREEN);
        mark.setBackground(circle);
        LinearLayout.LayoutParams markParams = new LinearLayout.LayoutParams(dp(72), dp(72));
        markParams.gravity = Gravity.CENTER_HORIZONTAL;
        root.addView(mark, markParams);

        TextView logo = new TextView(this);
        logo.setText("MOJA PAY");
        logo.setTextColor(GREEN_DARK);
        logo.setTextSize(29);
        logo.setGravity(Gravity.CENTER);
        logo.setTypeface(null, Typeface.BOLD);
        logo.setPadding(0, dp(8), 0, 0);
        root.addView(logo);

        TextView tag = new TextView(this);
        tag.setText("Huduma moja • Malipo mengi • Sample Demo");
        tag.setTextColor(MUTED);
        tag.setTextSize(13);
        tag.setGravity(Gravity.CENTER);
        tag.setPadding(0, dp(3), 0, dp(16));
        root.addView(tag);

        TextView warning = new TextView(this);
        warning.setText("DEMO TU — haitumi pesa halisi, PIN wala OTP.");
        warning.setTextColor(Color.parseColor("#8A4B00"));
        warning.setBackground(rounded(Color.parseColor("#FFF3D6"), 12));
        warning.setPadding(dp(12), dp(11), dp(12), dp(11));
        warning.setGravity(Gravity.CENTER);
        root.addView(warning);

        root.addView(label("Chagua huduma"));

        LinearLayout tabs = new LinearLayout(this);
        tabs.setOrientation(LinearLayout.HORIZONTAL);
        Button sendTab = new Button(this);
        sendTab.setText("TUMA / LIPA");
        sendTab.setTextSize(14);
        sendTab.setTypeface(null, Typeface.BOLD);
        styleTab(sendTab, true);
        Button cashTab = new Button(this);
        cashTab.setText("TOA PESA");
        cashTab.setTextSize(14);
        cashTab.setTypeface(null, Typeface.BOLD);
        styleTab(cashTab, false);
        LinearLayout.LayoutParams tab1 = new LinearLayout.LayoutParams(0, dp(52), 1f);
        tab1.setMargins(0, 0, dp(5), 0);
        LinearLayout.LayoutParams tab2 = new LinearLayout.LayoutParams(0, dp(52), 1f);
        tab2.setMargins(dp(5), 0, 0, 0);
        tabs.addView(sendTab, tab1);
        tabs.addView(cashTab, tab2);
        root.addView(tabs);

        TextView fromLabel = label("Pesa inatoka wapi?");
        root.addView(fromLabel);
        Spinner from = new Spinner(this);
        String[] fromItems = {"Airtel Money", "M-Pesa", "Mixx by Yas / Tigo Pesa", "HaloPesa"};
        from.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, fromItems));
        root.addView(from, new LinearLayout.LayoutParams(-1, dp(52)));

        TextView toLabel = label("Inaenda wapi?");
        root.addView(toLabel);
        Spinner to = new Spinner(this);
        String[] toItems = {"Airtel Money", "M-Pesa", "Mixx by Yas / Tigo Pesa", "HaloPesa", "NMB Bank", "CRDB Bank", "NBC Bank", "LUKU / Bili"};
        to.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, toItems));
        root.addView(to, new LinearLayout.LayoutParams(-1, dp(52)));

        TextView recipientLabel = label("Namba ya mpokeaji / akaunti");
        root.addView(recipientLabel);
        EditText recipient = new EditText(this);
        recipient.setHint("Mfano: 07XXXXXXXX");
        recipient.setInputType(InputType.TYPE_CLASS_PHONE);
        root.addView(recipient, new LinearLayout.LayoutParams(-1, dp(56)));

        root.addView(label("Kiasi (TZS)"));
        EditText amount = new EditText(this);
        amount.setHint("Mfano: 10,000");
        amount.setInputType(InputType.TYPE_CLASS_NUMBER);
        root.addView(amount, new LinearLayout.LayoutParams(-1, dp(56)));

        TextView cashHint = new TextView(this);
        cashHint.setText("Kwa Toa Pesa: chagua mtandao wa mteja, weka namba na kiasi. Hii ni simulation ya wakala tu.");
        cashHint.setTextColor(MUTED);
        cashHint.setTextSize(12);
        cashHint.setPadding(0, dp(8), 0, 0);
        cashHint.setVisibility(View.GONE);
        root.addView(cashHint);

        Button confirm = new Button(this);
        confirm.setText("THIBITISHA MALIPO YA DEMO");
        confirm.setTextSize(15);
        confirm.setTypeface(null, Typeface.BOLD);
        confirm.setTextColor(Color.WHITE);
        confirm.setBackground(rounded(GREEN, 16));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(58));
        bp.setMargins(0, dp(22), 0, dp(12));
        root.addView(confirm, bp);

        TextView footer = new TextView(this);
        footer.setText("Moja Pay Sample v0.2 • Demo ya wazo tu");
        footer.setGravity(Gravity.CENTER);
        footer.setTextColor(Color.GRAY);
        footer.setPadding(0, dp(12), 0, 0);
        root.addView(footer);

        final boolean[] cashMode = {false};

        sendTab.setOnClickListener(v -> {
            cashMode[0] = false;
            styleTab(sendTab, true);
            styleTab(cashTab, false);
            fromLabel.setText("Pesa inatoka wapi?");
            toLabel.setVisibility(View.VISIBLE);
            to.setVisibility(View.VISIBLE);
            recipientLabel.setText("Namba ya mpokeaji / akaunti");
            cashHint.setVisibility(View.GONE);
            confirm.setText("THIBITISHA MALIPO YA DEMO");
        });

        cashTab.setOnClickListener(v -> {
            cashMode[0] = true;
            styleTab(sendTab, false);
            styleTab(cashTab, true);
            fromLabel.setText("Mtandao wa mteja");
            toLabel.setVisibility(View.GONE);
            to.setVisibility(View.GONE);
            recipientLabel.setText("Namba ya mteja anayetoa pesa");
            cashHint.setVisibility(View.VISIBLE);
            confirm.setText("THIBITISHA TOA PESA — DEMO");
        });

        confirm.setOnClickListener(v -> {
            String r = recipient.getText().toString().trim();
            String a = amount.getText().toString().replace(",", "").trim();
            if (r.length() < 4 || a.length() == 0) {
                Toast.makeText(MainActivity.this, "Jaza namba na kiasi kwanza.", Toast.LENGTH_SHORT).show();
                return;
            }

            long value;
            try { value = Long.parseLong(a); }
            catch (Exception e) {
                Toast.makeText(MainActivity.this, "Kiasi si sahihi.", Toast.LENGTH_SHORT).show();
                return;
            }

            String money = NumberFormat.getNumberInstance(Locale.US).format(value);
            String ref = "MJP-DEMO-" + String.valueOf(System.currentTimeMillis()).substring(7);
            String receipt;
            if (cashMode[0]) {
                receipt = "TOA PESA — DEMO IMEFANIKIWA\n\n" +
                        "Huduma: Toa Pesa\n" +
                        "Mtandao: " + from.getSelectedItem() + "\n" +
                        "Namba ya mteja: " + r + "\n" +
                        "Kiasi: TZS " + money + "\n" +
                        "Njia: Cash kupitia Wakala\n" +
                        "Reference: " + ref + "\n\n" +
                        "Hii ni receipt ya mfano tu. Hakuna pesa halisi iliyotolewa.";
            } else {
                receipt = "MUAMALA WA DEMO UMEFANIKIWA\n\n" +
                        "Huduma: Tuma / Lipa\n" +
                        "Kutoka: " + from.getSelectedItem() + "\n" +
                        "Kwenda: " + to.getSelectedItem() + "\n" +
                        "Mpokeaji: " + r + "\n" +
                        "Kiasi: TZS " + money + "\n" +
                        "Reference: " + ref + "\n\n" +
                        "Hii ni receipt ya mfano tu. Hakuna pesa halisi iliyohamishwa.";
            }

            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Moja Pay Receipt")
                    .setMessage(receipt)
                    .setPositiveButton("SAWA", null)
                    .show();
        });

        setContentView(scroll);
    }
}
