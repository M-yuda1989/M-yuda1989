package com.mojapay.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
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

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private TextView label(String text) {
        TextView v = new TextView(this);
        v.setText(text);
        v.setTextColor(Color.parseColor("#273043"));
        v.setTextSize(14);
        v.setPadding(0, dp(14), 0, dp(6));
        return v;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(Color.parseColor("#F4F7FB"));

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(22), dp(28), dp(22), dp(28));
        scroll.addView(root);

        TextView logo = new TextView(this);
        logo.setText("MOJA PAY");
        logo.setTextColor(Color.parseColor("#0B6E4F"));
        logo.setTextSize(30);
        logo.setGravity(Gravity.CENTER);
        logo.setTypeface(null, 1);
        root.addView(logo);

        TextView tag = new TextView(this);
        tag.setText("Lipa kwa urahisi • Sample Demo");
        tag.setTextColor(Color.DKGRAY);
        tag.setTextSize(14);
        tag.setGravity(Gravity.CENTER);
        tag.setPadding(0, dp(4), 0, dp(18));
        root.addView(tag);

        TextView warning = new TextView(this);
        warning.setText("DEMO TU — haitumi pesa halisi, PIN wala OTP.");
        warning.setTextColor(Color.parseColor("#8A4B00"));
        warning.setBackgroundColor(Color.parseColor("#FFF3D6"));
        warning.setPadding(dp(12), dp(10), dp(12), dp(10));
        warning.setGravity(Gravity.CENTER);
        root.addView(warning);

        root.addView(label("Pesa inatoka wapi?"));
        Spinner from = new Spinner(this);
        String[] fromItems = {"Airtel Money", "M-Pesa", "Mixx by Yas / Tigo Pesa", "HaloPesa"};
        from.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, fromItems));
        root.addView(from, new LinearLayout.LayoutParams(-1, dp(52)));

        root.addView(label("Inaenda wapi?"));
        Spinner to = new Spinner(this);
        String[] toItems = {"Airtel Money", "M-Pesa", "Mixx by Yas / Tigo Pesa", "HaloPesa", "NMB Bank", "CRDB Bank", "NBC Bank", "LUKU / Bili"};
        to.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, toItems));
        root.addView(to, new LinearLayout.LayoutParams(-1, dp(52)));

        root.addView(label("Namba ya mpokeaji / akaunti"));
        EditText recipient = new EditText(this);
        recipient.setHint("Mfano: 07XXXXXXXX");
        recipient.setInputType(InputType.TYPE_CLASS_PHONE);
        root.addView(recipient, new LinearLayout.LayoutParams(-1, dp(56)));

        root.addView(label("Kiasi (TZS)"));
        EditText amount = new EditText(this);
        amount.setHint("Mfano: 10,000");
        amount.setInputType(InputType.TYPE_CLASS_NUMBER);
        root.addView(amount, new LinearLayout.LayoutParams(-1, dp(56)));

        Button confirm = new Button(this);
        confirm.setText("THIBITISHA MALIPO YA DEMO");
        confirm.setTextSize(16);
        confirm.setTextColor(Color.WHITE);
        confirm.setBackgroundColor(Color.parseColor("#0B6E4F"));
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, dp(58));
        bp.setMargins(0, dp(22), 0, dp(12));
        root.addView(confirm, bp);

        TextView footer = new TextView(this);
        footer.setText("Moja Pay Sample v0.1 • Kwa majaribio ya wazo tu");
        footer.setGravity(Gravity.CENTER);
        footer.setTextColor(Color.GRAY);
        footer.setPadding(0, dp(12), 0, 0);
        root.addView(footer);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String r = recipient.getText().toString().trim();
                String a = amount.getText().toString().replace(",", "").trim();
                if (r.length() < 4 || a.length() == 0) {
                    Toast.makeText(MainActivity.this, "Jaza namba na kiasi kwanza.", Toast.LENGTH_SHORT).show();
                    return;
                }

                long value;
                try {
                    value = Long.parseLong(a);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Kiasi si sahihi.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String money = NumberFormat.getNumberInstance(Locale.US).format(value);
                String ref = "MJP-DEMO-" + String.valueOf(System.currentTimeMillis()).substring(7);
                String receipt = "MUAMALA WA DEMO UMEFANIKIWA\n\n" +
                        "Kutoka: " + from.getSelectedItem() + "\n" +
                        "Kwenda: " + to.getSelectedItem() + "\n" +
                        "Mpokeaji: " + r + "\n" +
                        "Kiasi: TZS " + money + "\n" +
                        "Reference: " + ref + "\n\n" +
                        "Hii ni receipt ya mfano tu. Hakuna pesa halisi iliyohamishwa.";

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Moja Pay Receipt")
                        .setMessage(receipt)
                        .setPositiveButton("SAWA", null)
                        .show();
            }
        });

        setContentView(scroll);
    }
}
