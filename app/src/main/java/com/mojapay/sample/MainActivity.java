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
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends Activity {
    private final int GREEN = Color.parseColor("#0B6E4F");
    private final int GREEN_DARK = Color.parseColor("#07563E");
    private final int LIGHT_GREEN = Color.parseColor("#E8F5EF");
    private final int TEXT = Color.parseColor("#263238");
    private final int MUTED = Color.parseColor("#6B7280");
    private final int BG = Color.parseColor("#F4F7FB");

    private Spinner networkSpinner;
    private TextView networkStatus;
    private TextView txCountView;
    private TextView txValueView;
    private TextView commissionView;
    private int txCount = 0;
    private long txValue = 0;
    private int refCounter = 1;
    private final ArrayList<String> history = new ArrayList<>();

    private int dp(int v) { return (int)(v * getResources().getDisplayMetrics().density + 0.5f); }

    private GradientDrawable rounded(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        return d;
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(null, Typeface.BOLD);
        return t;
    }

    private TextView label(String value) {
        TextView t = text(value, 13, TEXT, true);
        t.setPadding(0, dp(10), 0, dp(5));
        return t;
    }

    private Button actionButton(String title) {
        Button b = new Button(this);
        b.setText(title);
        b.setTextSize(13);
        b.setTypeface(null, Typeface.BOLD);
        b.setTextColor(GREEN_DARK);
        b.setBackground(rounded(LIGHT_GREEN, 16));
        return b;
    }

    private EditText input(String hint, boolean number) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setTextSize(15);
        e.setInputType(number ? InputType.TYPE_CLASS_NUMBER : InputType.TYPE_CLASS_PHONE);
        return e;
    }

    private String selectedNetwork() {
        return String.valueOf(networkSpinner.getSelectedItem());
    }

    private String ref() {
        return String.format(Locale.US, "MP-260911-%06d", refCounter++);
    }

    private String money(long value) {
        return "TSh " + NumberFormat.getNumberInstance(Locale.US).format(value);
    }

    private long parseAmount(EditText amount) {
        try { return Long.parseLong(amount.getText().toString().replace(",", "").trim()); }
        catch (Exception e) { return -1; }
    }

    private void addHistory(String service, String destination, long amount, String reference, String status) {
        txCount++;
        txValue += amount;
        history.add(0, service + " • " + selectedNetwork() + "\n" + destination + " • " + money(amount) + "\n" + reference + " • " + status);
        updateDashboard();
    }

    private void updateDashboard() {
        txCountView.setText(String.valueOf(txCount));
        txValueView.setText(money(txValue));
        long est = Math.round(txValue * 0.005);
        commissionView.setText(money(est) + " (estimate)");
    }

    private LinearLayout formRoot() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(20), dp(8), dp(20), 0);
        return box;
    }

    private void showReceipt(String service, String destination, long amount, String reference, String extra) {
        String receipt = "CUSTOMER COPY\n" +
                "MojaPay DEMO\n\n" +
                "Network: " + selectedNetwork() + "\n" +
                "Service: " + service + "\n" +
                "Destination: " + destination + "\n" +
                "Amount: " + money(amount) + "\n" +
                (extra.length() > 0 ? extra + "\n" : "") +
                "MojaPay Ref: " + reference + "\n" +
                "Status: SUCCESSFUL\n\n" +
                "AGENT COPY pia imehifadhiwa kwenye demo history.\n" +
                "Huu ni mfano tu — hakuna pesa halisi iliyohamishwa.";
        new AlertDialog.Builder(this)
                .setTitle("Moja Pay Receipt")
                .setMessage(receipt)
                .setPositiveButton("SAWA", null)
                .show();
    }

    private void showSimpleForm(final String service, String phoneLabel) {
        LinearLayout box = formRoot();
        box.addView(label("Network"));
        TextView net = text(selectedNetwork() + " • SIM AVAILABLE (DEMO)", 15, GREEN_DARK, true);
        box.addView(net);
        box.addView(label(phoneLabel));
        EditText phone = input("07XXXXXXXX", false);
        box.addView(phone);
        box.addView(label("Kiasi (TZS)"));
        EditText amount = input("50000", true);
        box.addView(amount);

        new AlertDialog.Builder(this)
                .setTitle(service + " — DEMO")
                .setView(box)
                .setNegativeButton("GHAIRI", null)
                .setPositiveButton("HAKIKI", null)
                .create().setOnShowListener(dialog -> {
                    AlertDialog d = (AlertDialog) dialog;
                    d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                        String p = phone.getText().toString().trim();
                        long a = parseAmount(amount);
                        if (p.length() < 4 || a <= 0) {
                            Toast.makeText(this, "Jaza namba na kiasi sahihi.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        d.dismiss();
                        confirmGeneric(service, p, a, "");
                    });
                });
        AlertDialog dlg = new AlertDialog.Builder(this).create();
        // dialog is created above through builder; this dummy line is intentionally unused.
    }

    private void confirmGeneric(String service, String destination, long amount, String extra) {
        String msg = "HII NI NAMBA / DESTINATION SAHIHI?\n\n" +
                "Network: " + selectedNetwork() + "\n" +
                "Service: " + service + "\n" +
                "Destination: " + destination + "\n" +
                "Amount: " + money(amount) + "\n\n" +
                "Demo ita-simulate provider authorization tu.";
        new AlertDialog.Builder(this)
                .setTitle("Hakiki Muamala")
                .setMessage(msg)
                .setNegativeButton("EDIT", null)
                .setPositiveButton("YES — ENDELEA", (d, w) -> {
                    String r = ref();
                    addHistory(service, destination, amount, r, "SUCCESSFUL");
                    showReceipt(service, destination, amount, r, extra);
                })
                .show();
    }

    private void showCashOut() {
        LinearLayout box = formRoot();
        box.addView(label("Network"));
        box.addView(text(selectedNetwork() + " • SIM AVAILABLE (DEMO)", 15, GREEN_DARK, true));
        box.addView(label("Namba ya mteja anayetoa pesa"));
        EditText phone = input("07XXXXXXXX", false);
        box.addView(phone);
        box.addView(label("Kiasi (TZS)"));
        EditText amount = input("50000", true);
        box.addView(amount);

        AlertDialog entry = new AlertDialog.Builder(this)
                .setTitle("TOA PESA — DEMO")
                .setView(box)
                .setNegativeButton("GHAIRI", null)
                .setPositiveButton("REQUEST WITHDRAWAL", null)
                .create();
        entry.setOnShowListener(x -> entry.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String p = phone.getText().toString().trim();
            long a = parseAmount(amount);
            if (p.length() < 4 || a <= 0) {
                Toast.makeText(this, "Jaza namba na kiasi sahihi.", Toast.LENGTH_SHORT).show();
                return;
            }
            entry.dismiss();
            new AlertDialog.Builder(this)
                    .setTitle("AWAITING CUSTOMER CONFIRMATION")
                    .setMessage("SUBIRI — Mteja anathibitisha muamala kwenye simu yake.\n\nUSIMPE MTEJA CASH BADO.\n\nDEMO: bonyeza hapa ku-simulate customer/provider confirmation.")
                    .setNegativeButton("CANCEL", null)
                    .setPositiveButton("SIMULATE CONFIRMED", (a1, b1) -> showCashSuccess(p, a))
                    .show();
        }));
        entry.show();
    }

    private void showCashSuccess(String phone, long amount) {
        String r = ref();
        AlertDialog success = new AlertDialog.Builder(this)
                .setTitle("MUAMALA UMEFANIKIWA")
                .setMessage("PROVIDER SUCCESS — DEMO\n\nMPE MTEJA " + money(amount) + " CASH.\n\nBaada ya kumpa pesa kimwili, bonyeza CASH GIVEN.")
                .setCancelable(false)
                .setPositiveButton("CASH GIVEN", null)
                .create();
        success.setOnShowListener(x -> success.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            success.dismiss();
            addHistory("TOA PESA", phone, amount, r, "CASH GIVEN");
            showReceipt("TOA PESA", phone, amount, r, "Cash handover: CASH GIVEN");
        }));
        success.show();
    }

    private void showBankTransfer() {
        LinearLayout box = formRoot();
        box.addView(label("Mobile Money source"));
        box.addView(text(selectedNetwork(), 15, GREEN_DARK, true));
        box.addView(label("Chagua Bank"));
        Spinner bank = new Spinner(this);
        String[] banks = {"NMB", "CRDB", "NBC", "Equity", "Absa", "Stanbic", "Exim"};
        bank.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, banks));
        box.addView(bank);
        box.addView(label("Account Number"));
        EditText account = input("Account number", true);
        box.addView(account);
        box.addView(label("Kiasi (TZS)"));
        EditText amount = input("100000", true);
        box.addView(amount);
        TextView note = text("Account-name verification haipatikani kwenye demo hii.", 12, MUTED, false);
        note.setPadding(0, dp(8), 0, 0);
        box.addView(note);

        AlertDialog d = new AlertDialog.Builder(this)
                .setTitle("TUMA BENKI — DEMO")
                .setView(box)
                .setNegativeButton("GHAIRI", null)
                .setPositiveButton("HAKIKI", null)
                .create();
        d.setOnShowListener(x -> d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String acc = account.getText().toString().trim();
            long a = parseAmount(amount);
            if (acc.length() < 4 || a <= 0) {
                Toast.makeText(this, "Jaza account na kiasi sahihi.", Toast.LENGTH_SHORT).show();
                return;
            }
            d.dismiss();
            String dest = bank.getSelectedItem() + " A/C " + acc;
            confirmGeneric("TUMA BENKI", dest, a, "Bank: " + bank.getSelectedItem());
        }));
        d.show();
    }

    private void showBillPay() {
        LinearLayout box = formRoot();
        box.addView(label("Network"));
        box.addView(text(selectedNetwork(), 15, GREEN_DARK, true));
        box.addView(label("Biller"));
        Spinner biller = new Spinner(this);
        String[] billers = {"LUKU / Umeme", "Maji", "DStv", "Azam TV", "StarTimes", "Internet", "Government / Control No.", "School Fees", "Merchant Payment"};
        biller.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, billers));
        box.addView(biller);
        box.addView(label("Meter / Control / Account Number"));
        EditText number = input("Weka namba", true);
        box.addView(number);
        box.addView(label("Kiasi (TZS)"));
        EditText amount = input("20000", true);
        box.addView(amount);

        AlertDialog d = new AlertDialog.Builder(this)
                .setTitle("LIPA BILI — DEMO")
                .setView(box)
                .setNegativeButton("GHAIRI", null)
                .setPositiveButton("HAKIKI", null)
                .create();
        d.setOnShowListener(x -> d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String n = number.getText().toString().trim();
            long a = parseAmount(amount);
            if (n.length() < 3 || a <= 0) {
                Toast.makeText(this, "Jaza namba na kiasi sahihi.", Toast.LENGTH_SHORT).show();
                return;
            }
            d.dismiss();
            String b = String.valueOf(biller.getSelectedItem());
            String extra = b.startsWith("LUKU") ? "Demo Token: 1234 5678 9012 3456 7890" : "Biller: " + b;
            confirmGeneric("LIPA BILI", b + " • " + n, a, extra);
        }));
        d.show();
    }

    private void showBalance() {
        new AlertDialog.Builder(this)
                .setTitle("SALIO / FLOAT — SAMPLE DATA")
                .setMessage("Airtel Float: TSh 1,200,000\nM-Pesa Float: TSh 950,000\nMixx Float: TSh 780,000\nHaloPesa Float: TSh 420,000\nAgent Cash: TSh 850,000\n\nHizi ni SAMPLE DATA za demo tu.")
                .setPositiveButton("SAWA", null)
                .show();
    }

    private void showHistory() {
        StringBuilder s = new StringBuilder();
        if (history.isEmpty()) s.append("Bado hakuna demo transactions.");
        else {
            for (String h : history) s.append(h).append("\n\n");
        }
        new AlertDialog.Builder(this)
                .setTitle("Transaction History — DEMO")
                .setMessage(s.toString())
                .setPositiveButton("SAWA", null)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BG);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(18), dp(22), dp(18), dp(28));
        scroll.addView(root);

        TextView mark = text("M", 30, Color.WHITE, true);
        mark.setGravity(Gravity.CENTER);
        GradientDrawable circle = new GradientDrawable();
        circle.setShape(GradientDrawable.OVAL);
        circle.setColor(GREEN);
        mark.setBackground(circle);
        LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(dp(68), dp(68));
        mp.gravity = Gravity.CENTER_HORIZONTAL;
        root.addView(mark, mp);

        TextView logo = text("MOJA PAY", 28, GREEN_DARK, true);
        logo.setGravity(Gravity.CENTER);
        logo.setPadding(0, dp(7), 0, 0);
        root.addView(logo);
        TextView tagline = text("One App. All Payments. • MASTER PROMPT DEMO", 12, MUTED, false);
        tagline.setGravity(Gravity.CENTER);
        tagline.setPadding(0, dp(2), 0, dp(14));
        root.addView(tagline);

        TextView warning = text("DEMO / SIMULATION TU — haitumii pesa halisi, PIN wala OTP.", 12, Color.parseColor("#8A4B00"), true);
        warning.setGravity(Gravity.CENTER);
        warning.setPadding(dp(10), dp(10), dp(10), dp(10));
        warning.setBackground(rounded(Color.parseColor("#FFF3D6"), 12));
        root.addView(warning);

        root.addView(label("CHAGUA MTANDAO"));
        networkSpinner = new Spinner(this);
        String[] networks = {"Airtel Money", "M-Pesa", "Mixx by Yas", "HaloPesa"};
        networkSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, networks));
        root.addView(networkSpinner, new LinearLayout.LayoutParams(-1, dp(52)));
        networkStatus = text("SIM AVAILABLE • ACTIVE (DEMO)", 12, GREEN_DARK, true);
        networkStatus.setPadding(0, 0, 0, dp(10));
        root.addView(networkStatus);

        LinearLayout dash = new LinearLayout(this);
        dash.setOrientation(LinearLayout.VERTICAL);
        dash.setPadding(dp(14), dp(12), dp(14), dp(12));
        dash.setBackground(rounded(Color.WHITE, 16));
        TextView dashTitle = text("DASHBOARD YA LEO — DEMO", 14, TEXT, true);
        dash.addView(dashTitle);
        LinearLayout r1 = new LinearLayout(this);
        r1.setOrientation(LinearLayout.HORIZONTAL);
        TextView c1 = text("Miamala\n", 12, MUTED, false); txCountView = text("0", 17, GREEN_DARK, true); c1.append(txCountView.getText());
        TextView c2 = text("Thamani\nTSh 0", 12, MUTED, false);
        txValueView = c2;
        r1.addView(c1, new LinearLayout.LayoutParams(0, -2, 1f));
        r1.addView(c2, new LinearLayout.LayoutParams(0, -2, 1f));
        dash.addView(r1);
        commissionView = text("TSh 0 (estimate)", 13, GREEN_DARK, true);
        TextView commissionLabel = text("Commission estimate: ", 12, MUTED, false);
        LinearLayout cr = new LinearLayout(this);
        cr.setOrientation(LinearLayout.HORIZONTAL);
        cr.addView(commissionLabel);
        cr.addView(commissionView);
        cr.setPadding(0, dp(8), 0, 0);
        dash.addView(cr);
        TextView floatSample = text("Cash sample: TSh 850,000 • Float summary: DEMO", 12, MUTED, false);
        floatSample.setPadding(0, dp(6), 0, 0);
        dash.addView(floatSample);
        LinearLayout.LayoutParams dpms = new LinearLayout.LayoutParams(-1, -2);
        dpms.setMargins(0, 0, 0, dp(14));
        root.addView(dash, dpms);

        root.addView(label("HUDUMA"));
        LinearLayout row1 = new LinearLayout(this); row1.setOrientation(LinearLayout.HORIZONTAL);
        Button weka = actionButton("WEKA PESA");
        Button toa = actionButton("TOA PESA");
        row1.addView(weka, new LinearLayout.LayoutParams(0, dp(58), 1f));
        row1.addView(toa, new LinearLayout.LayoutParams(0, dp(58), 1f));
        root.addView(row1);

        LinearLayout row2 = new LinearLayout(this); row2.setOrientation(LinearLayout.HORIZONTAL);
        Button tuma = actionButton("TUMA PESA");
        Button bank = actionButton("TUMA BENKI");
        row2.addView(tuma, new LinearLayout.LayoutParams(0, dp(58), 1f));
        row2.addView(bank, new LinearLayout.LayoutParams(0, dp(58), 1f));
        root.addView(row2);

        LinearLayout row3 = new LinearLayout(this); row3.setOrientation(LinearLayout.HORIZONTAL);
        Button bill = actionButton("LIPA BILI");
        Button balance = actionButton("ANGALIA SALIO");
        row3.addView(bill, new LinearLayout.LayoutParams(0, dp(58), 1f));
        row3.addView(balance, new LinearLayout.LayoutParams(0, dp(58), 1f));
        root.addView(row3);

        Button hist = new Button(this);
        hist.setText("TRANSACTION HISTORY / RECEIPTS");
        hist.setTextColor(Color.WHITE);
        hist.setTypeface(null, Typeface.BOLD);
        hist.setBackground(rounded(GREEN, 16));
        LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(-1, dp(56));
        hp.setMargins(0, dp(12), 0, 0);
        root.addView(hist, hp);

        TextView footer = text("Moja Pay v0.3 Master Prompt Demo • No real-money connection", 11, Color.GRAY, false);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(16), 0, 0);
        root.addView(footer);

        weka.setOnClickListener(v -> showSimpleForm("WEKA PESA", "Namba ya mteja"));
        toa.setOnClickListener(v -> showCashOut());
        tuma.setOnClickListener(v -> showSimpleForm("TUMA PESA", "Namba ya mpokeaji"));
        bank.setOnClickListener(v -> showBankTransfer());
        bill.setOnClickListener(v -> showBillPay());
        balance.setOnClickListener(v -> showBalance());
        hist.setOnClickListener(v -> showHistory());

        setContentView(scroll);
    }
}
