package com.mojapay.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {
    private final int PRIMARY = Color.rgb(79, 70, 229);
    private final int CYAN = Color.rgb(6, 182, 212);
    private final int PURPLE = Color.rgb(139, 92, 246);
    private final int BG = Color.rgb(247, 248, 252);
    private final int INK = Color.rgb(24, 31, 51);
    private final int MUTED = Color.rgb(113, 120, 139);

    private FrameLayout content;
    private LinearLayout bottomNav;
    private String currentTab = "Home";
    private final List<String> circles = new ArrayList<>(Arrays.asList("Family", "Friends", "Business", "Church"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(PRIMARY);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);
        root.addView(buildTopBar(), new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(70)));

        content = new FrameLayout(this);
        root.addView(content, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        bottomNav = buildBottomNav();
        root.addView(bottomNav, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(74)));
        setContentView(root);
        showHome();
    }

    private LinearLayout buildTopBar() {
        LinearLayout bar = new LinearLayout(this);
        bar.setGravity(Gravity.CENTER_VERTICAL);
        bar.setPadding(dp(18), dp(10), dp(16), dp(10));
        bar.setBackgroundColor(PRIMARY);

        TextView logo = new TextView(this);
        logo.setText("N");
        logo.setGravity(Gravity.CENTER);
        logo.setTextColor(PRIMARY);
        logo.setTextSize(22);
        logo.setTypeface(Typeface.DEFAULT_BOLD);
        logo.setBackground(round(Color.WHITE, 18));
        bar.addView(logo, new LinearLayout.LayoutParams(dp(42), dp(42)));

        LinearLayout titles = new LinearLayout(this);
        titles.setOrientation(LinearLayout.VERTICAL);
        titles.setPadding(dp(10), 0, 0, 0);
        titles.addView(text("NEXA", 20, Color.WHITE, true));
        titles.addView(text("Connect beyond chat", 11, Color.argb(210, 255, 255, 255), false));
        bar.addView(titles, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView badge = text("DEMO", 10, Color.WHITE, true);
        badge.setGravity(Gravity.CENTER);
        badge.setBackground(round(Color.argb(50, 255, 255, 255), 14));
        badge.setPadding(dp(10), dp(6), dp(10), dp(6));
        bar.addView(badge);
        return bar;
    }

    private LinearLayout buildBottomNav() {
        LinearLayout nav = new LinearLayout(this);
        nav.setOrientation(LinearLayout.HORIZONTAL);
        nav.setGravity(Gravity.CENTER);
        nav.setPadding(dp(6), dp(6), dp(6), dp(6));
        nav.setBackgroundColor(Color.WHITE);
        String[] names = {"Home", "Chats", "Circles", "Discover", "Profile"};
        String[] icons = {"⌂", "✉", "◎", "✦", "●"};
        for (int i = 0; i < names.length; i++) {
            final String name = names[i];
            LinearLayout item = new LinearLayout(this);
            item.setOrientation(LinearLayout.VERTICAL);
            item.setGravity(Gravity.CENTER);
            item.setTag(name);
            TextView icon = text(icons[i], 19, MUTED, true);
            TextView label = text(name, 10, MUTED, false);
            item.addView(icon);
            item.addView(label);
            item.setOnClickListener(v -> switchTab(name));
            nav.addView(item, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f));
        }
        return nav;
    }

    private void switchTab(String tab) {
        currentTab = tab;
        if (tab.equals("Home")) showHome();
        else if (tab.equals("Chats")) showChats();
        else if (tab.equals("Circles")) showCircles();
        else if (tab.equals("Discover")) showDiscover();
        else showProfile();
        updateNav();
    }

    private void updateNav() {
        for (int i = 0; i < bottomNav.getChildCount(); i++) {
            LinearLayout item = (LinearLayout) bottomNav.getChildAt(i);
            boolean selected = currentTab.equals(String.valueOf(item.getTag()));
            for (int j = 0; j < item.getChildCount(); j++) {
                View child = item.getChildAt(j);
                if (child instanceof TextView) ((TextView) child).setTextColor(selected ? PRIMARY : MUTED);
            }
            item.setBackground(selected ? round(Color.rgb(238, 237, 255), 16) : null);
        }
    }

    private LinearLayout page() {
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(16), dp(14), dp(16), dp(24));
        page.setBackgroundColor(BG);
        return page;
    }

    private void setPage(LinearLayout page) {
        content.removeAllViews();
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.addView(page, new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        content.addView(scroll, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        updateNav();
    }

    private void showHome() {
        currentTab = "Home";
        LinearLayout p = page();
        p.addView(text("Karibu NEXA", 26, INK, true));
        p.addView(text("Chat, moments, circles na community zako sehemu moja.", 13, MUTED, false));
        p.addView(space(14));

        LinearLayout hero = card();
        hero.setBackground(round(PRIMARY, 24));
        hero.addView(text("Your Circle. Your Voice.", 20, Color.WHITE, true));
        hero.addView(text("Wasiliana kwa @username bila kulazimika kushare namba yako.", 13, Color.argb(220,255,255,255), false));
        hero.addView(space(12));
        Button create = button("+ Create", Color.WHITE, PRIMARY);
        create.setOnClickListener(v -> showCreateMenu());
        hero.addView(create, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp(42)));
        p.addView(hero);

        p.addView(section("Moments"));
        HorizontalScrollView hsv = new HorizontalScrollView(this);
        hsv.setHorizontalScrollBarEnabled(false);
        LinearLayout moments = new LinearLayout(this);
        moments.setOrientation(LinearLayout.HORIZONTAL);
        moments.addView(moment("You", "+", PRIMARY));
        moments.addView(moment("Asha", "A", CYAN));
        moments.addView(moment("David", "D", PURPLE));
        moments.addView(moment("Neema", "N", Color.rgb(245, 158, 11)));
        moments.addView(moment("Juma", "J", Color.rgb(16, 185, 129)));
        hsv.addView(moments);
        p.addView(hsv);

        p.addView(section("Quick access"));
        LinearLayout quick = new LinearLayout(this);
        quick.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout c1 = miniCard("◎", "Circles", "Family, work & more", PURPLE);
        c1.setOnClickListener(v -> switchTab("Circles"));
        LinearLayout c2 = miniCard("◉", "Voice Room", "Join live discussion", CYAN);
        c2.setOnClickListener(v -> voiceRoomDialog());
        quick.addView(c1, new LinearLayout.LayoutParams(0, dp(126), 1f));
        Space gap = new Space(this); quick.addView(gap, new LinearLayout.LayoutParams(dp(10), 1));
        quick.addView(c2, new LinearLayout.LayoutParams(0, dp(126), 1f));
        p.addView(quick);

        p.addView(section("Recent chats"));
        p.addView(chatRow("Asha M.", "Nimeiona ile design, iko vizuri sana.", "2m", "A", CYAN));
        p.addView(chatRow("Business Circle", "Kelvin: Meeting ni saa 4:00.", "18m", "B", PURPLE));
        p.addView(chatRow("Family", "Mama: Tupo salama, asante.", "1h", "F", Color.rgb(16,185,129)));
        setPage(p);
    }

    private void showChats() {
        currentTab = "Chats";
        LinearLayout p = page();
        p.addView(text("Chats", 26, INK, true));
        p.addView(text("Private, fast and simple", 13, MUTED, false));
        p.addView(space(12));

        EditText search = new EditText(this);
        search.setHint("Search people, chats or @username");
        search.setTextSize(13);
        search.setSingleLine(true);
        search.setPadding(dp(14), 0, dp(14), 0);
        search.setBackground(round(Color.WHITE, 18));
        p.addView(search, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(50)));
        p.addView(space(12));

        p.addView(chatRow("Asha M.", "Nimeiona ile design, iko vizuri sana.", "2m", "A", CYAN));
        p.addView(chatRow("Kelvin B.", "Tuongee kwenye Voice Room baadaye.", "11m", "K", PURPLE));
        p.addView(chatRow("Business Circle", "Meeting ni saa 4:00.", "18m", "B", Color.rgb(245,158,11)));
        p.addView(chatRow("Family", "Tupo salama, asante.", "1h", "F", Color.rgb(16,185,129)));
        p.addView(chatRow("Neema", "Voice note · 0:18", "Yesterday", "N", PRIMARY));
        setPage(p);
    }

    private void showCircles() {
        currentTab = "Circles";
        LinearLayout p = page();
        p.addView(text("Circles", 26, INK, true));
        p.addView(text("Panga watu kwa maisha yako bila kuunda group kila mara.", 13, MUTED, false));
        p.addView(space(14));
        Button create = button("+ New Circle", PRIMARY, Color.WHITE);
        create.setOnClickListener(v -> createCircleDialog());
        p.addView(create, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(48)));
        p.addView(space(14));
        int[] colors = {PURPLE, CYAN, Color.rgb(245,158,11), Color.rgb(16,185,129), PRIMARY};
        for (int i = 0; i < circles.size(); i++) {
            String name = circles.get(i);
            LinearLayout c = circleCard(name, (i + 4) + " members", colors[i % colors.length]);
            final String selected = name;
            c.setOnClickListener(v -> Toast.makeText(this, selected + " Circle opened", Toast.LENGTH_SHORT).show());
            p.addView(c);
            p.addView(space(10));
        }
        setPage(p);
    }

    private void showDiscover() {
        currentTab = "Discover";
        LinearLayout p = page();
        p.addView(text("Discover", 26, INK, true));
        p.addView(text("Communities, creators, topics na voice rooms.", 13, MUTED, false));
        p.addView(space(14));

        LinearLayout voice = card();
        voice.setBackground(round(Color.rgb(239, 246, 255), 20));
        voice.addView(text("LIVE · Voice Room", 11, Color.rgb(220,38,38), true));
        voice.addView(text("East Africa Tech Talk", 18, INK, true));
        voice.addView(text("32 listening · 5 speakers", 12, MUTED, false));
        voice.addView(space(10));
        Button join = button("Join Room", CYAN, Color.WHITE);
        join.setOnClickListener(v -> voiceRoomDialog());
        voice.addView(join, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(44)));
        p.addView(voice);

        p.addView(section("Trending now"));
        p.addView(feedPost("@neema.design", "N", PURPLE, "Modern spaces should feel calm, simple and human.", "♡ 128    ◉ 24    ↗ Share"));
        p.addView(space(10));
        p.addView(feedPost("@tanzania.startups", "T", PRIMARY, "What should the next generation of East African apps solve first?", "♡ 246    ◉ 61    ↗ Share"));
        setPage(p);
    }

    private void showProfile() {
        currentTab = "Profile";
        LinearLayout p = page();
        LinearLayout profile = card();
        profile.setGravity(Gravity.CENTER_HORIZONTAL);
        profile.addView(avatar("YM", PRIMARY, 84));
        profile.addView(space(8));
        profile.addView(text("Yesse Madaha", 21, INK, true));
        profile.addView(text("@yesse", 13, PRIMARY, true));
        profile.addView(text("Building ideas. Connecting people.", 13, MUTED, false));
        profile.addView(space(12));
        LinearLayout stats = new LinearLayout(this);
        stats.setOrientation(LinearLayout.HORIZONTAL);
        stats.addView(stat("18", "Circles"), new LinearLayout.LayoutParams(0, dp(58), 1f));
        stats.addView(stat("264", "Followers"), new LinearLayout.LayoutParams(0, dp(58), 1f));
        stats.addView(stat("34", "Posts"), new LinearLayout.LayoutParams(0, dp(58), 1f));
        profile.addView(stats, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(58)));
        p.addView(profile);

        p.addView(section("Privacy & account"));
        p.addView(setting("Username privacy", "Phone number hidden by default", "ON"));
        p.addView(setting("Smart Status", "Available · resets in 2h", "›"));
        p.addView(setting("Hidden Chats", "Protected with PIN", "›"));
        p.addView(setting("Language", "Kiswahili / English", "›"));
        p.addView(setting("Demo version", "NEXA 0.1", ""));
        setPage(p);
    }

    private LinearLayout chatRow(String name, String message, String time, String initial, int color) {
        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(10), dp(10), dp(8), dp(10));
        row.setBackground(round(Color.WHITE, 18));
        row.addView(avatar(initial, color, 52));
        LinearLayout center = new LinearLayout(this);
        center.setOrientation(LinearLayout.VERTICAL);
        center.setPadding(dp(12), 0, dp(6), 0);
        center.addView(text(name, 15, INK, true));
        TextView msg = text(message, 12, MUTED, false);
        msg.setMaxLines(1);
        center.addView(msg);
        row.addView(center, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(text(time, 10, MUTED, false));
        row.setOnClickListener(v -> openChat(name, initial, color));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(72));
        lp.setMargins(0, 0, 0, dp(8));
        row.setLayoutParams(lp);
        return row;
    }

    private void openChat(String name, String initial, int color) {
        content.removeAllViews();
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG);

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        top.setPadding(dp(10), dp(8), dp(10), dp(8));
        top.setBackgroundColor(Color.WHITE);
        TextView back = text("‹", 34, PRIMARY, false);
        back.setGravity(Gravity.CENTER);
        back.setOnClickListener(v -> switchTab("Chats"));
        top.addView(back, new LinearLayout.LayoutParams(dp(42), dp(48)));
        top.addView(avatar(initial, color, 40));
        LinearLayout info = new LinearLayout(this);
        info.setOrientation(LinearLayout.VERTICAL);
        info.setPadding(dp(10), 0, 0, 0);
        info.addView(text(name, 15, INK, true));
        info.addView(text("Available · @" + name.toLowerCase().replace(" ", ""), 11, Color.rgb(16,185,129), false));
        top.addView(info, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        TextView call = text("☎", 22, PRIMARY, true);
        call.setGravity(Gravity.CENTER);
        call.setOnClickListener(v -> Toast.makeText(this, "Voice call demo", Toast.LENGTH_SHORT).show());
        top.addView(call, new LinearLayout.LayoutParams(dp(44), dp(44)));
        root.addView(top, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(64)));

        ScrollView messagesScroll = new ScrollView(this);
        LinearLayout messages = new LinearLayout(this);
        messages.setOrientation(LinearLayout.VERTICAL);
        messages.setPadding(dp(12), dp(16), dp(12), dp(16));
        addBubble(messages, "Hey! Karibu NEXA 👋", false);
        addBubble(messages, "Asante. Hii Circles feature nimeipenda.", true);
        addBubble(messages, "Unaweza pia kutumia username bila kushare namba yako.", false);
        messagesScroll.addView(messages);
        root.addView(messagesScroll, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        LinearLayout composer = new LinearLayout(this);
        composer.setGravity(Gravity.CENTER_VERTICAL);
        composer.setPadding(dp(8), dp(7), dp(8), dp(7));
        composer.setBackgroundColor(Color.WHITE);
        TextView mic = text("●", 18, CYAN, true);
        mic.setGravity(Gravity.CENTER);
        mic.setOnClickListener(v -> Toast.makeText(this, "Push-to-Talk demo", Toast.LENGTH_SHORT).show());
        composer.addView(mic, new LinearLayout.LayoutParams(dp(42), dp(42)));
        EditText input = new EditText(this);
        input.setHint("Message...");
        input.setTextSize(14);
        input.setMaxLines(3);
        input.setPadding(dp(14), 0, dp(14), 0);
        input.setBackground(round(Color.rgb(243,244,248), 20));
        composer.addView(input, new LinearLayout.LayoutParams(0, dp(46), 1f));
        Button send = button("➤", PRIMARY, Color.WHITE);
        LinearLayout.LayoutParams sendLp = new LinearLayout.LayoutParams(dp(48), dp(46));
        sendLp.setMargins(dp(7),0,0,0);
        composer.addView(send, sendLp);
        send.setOnClickListener(v -> {
            String msg = input.getText().toString().trim();
            if (!msg.isEmpty()) {
                addBubble(messages, msg, true);
                input.setText("");
                messagesScroll.post(() -> messagesScroll.fullScroll(View.FOCUS_DOWN));
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            }
        });
        root.addView(composer, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(60)));
        content.addView(root, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void addBubble(LinearLayout holder, String message, boolean mine) {
        LinearLayout line = new LinearLayout(this);
        line.setGravity(mine ? Gravity.END : Gravity.START);
        TextView bubble = text(message, 14, mine ? Color.WHITE : INK, false);
        bubble.setPadding(dp(14), dp(10), dp(14), dp(10));
        bubble.setBackground(round(mine ? PRIMARY : Color.WHITE, 18));
        line.addView(bubble);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, dp(9));
        holder.addView(line, lp);
    }

    private LinearLayout moment(String name, String initial, int color) {
        LinearLayout m = new LinearLayout(this);
        m.setOrientation(LinearLayout.VERTICAL);
        m.setGravity(Gravity.CENTER);
        m.setPadding(0, 0, dp(14), 0);
        m.addView(avatar(initial, color, 58));
        m.addView(text(name, 11, INK, false));
        return m;
    }

    private LinearLayout miniCard(String icon, String title, String subtitle, int color) {
        LinearLayout c = card();
        c.addView(text(icon, 25, color, true));
        c.addView(text(title, 15, INK, true));
        c.addView(text(subtitle, 11, MUTED, false));
        return c;
    }

    private LinearLayout circleCard(String title, String subtitle, int color) {
        LinearLayout row = card();
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.addView(avatar(title.substring(0,1), color, 52));
        LinearLayout t = new LinearLayout(this);
        t.setOrientation(LinearLayout.VERTICAL);
        t.setPadding(dp(12),0,0,0);
        t.addView(text(title, 16, INK, true));
        t.addView(text(subtitle + " · Private Circle", 12, MUTED, false));
        row.addView(t, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(text("›", 25, color, false));
        return row;
    }

    private LinearLayout feedPost(String username, String initial, int color, String body, String actions) {
        LinearLayout post = card();
        LinearLayout head = new LinearLayout(this);
        head.setGravity(Gravity.CENTER_VERTICAL);
        head.addView(avatar(initial, color, 42));
        TextView user = text(username, 14, INK, true);
        user.setPadding(dp(10),0,0,0);
        head.addView(user, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        Button follow = button("Follow", Color.rgb(238,237,255), PRIMARY);
        follow.setTextSize(11);
        follow.setOnClickListener(v -> follow.setText(follow.getText().toString().equals("Follow") ? "Following" : "Follow"));
        head.addView(follow, new LinearLayout.LayoutParams(dp(88), dp(36)));
        post.addView(head);
        post.addView(space(12));
        post.addView(text(body, 15, INK, false));
        post.addView(space(14));
        post.addView(text(actions, 12, MUTED, false));
        return post;
    }

    private LinearLayout setting(String title, String subtitle, String end) {
        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(14), dp(10), dp(14), dp(10));
        row.setBackground(round(Color.WHITE, 16));
        LinearLayout textWrap = new LinearLayout(this);
        textWrap.setOrientation(LinearLayout.VERTICAL);
        textWrap.addView(text(title, 14, INK, true));
        textWrap.addView(text(subtitle, 11, MUTED, false));
        row.addView(textWrap, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        row.addView(text(end, 14, PRIMARY, true));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(62));
        lp.setMargins(0,0,0,dp(8));
        row.setLayoutParams(lp);
        return row;
    }

    private LinearLayout stat(String number, String label) {
        LinearLayout s = new LinearLayout(this);
        s.setOrientation(LinearLayout.VERTICAL);
        s.setGravity(Gravity.CENTER);
        s.addView(text(number, 16, INK, true));
        s.addView(text(label, 10, MUTED, false));
        return s;
    }

    private LinearLayout card() {
        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(dp(14), dp(14), dp(14), dp(14));
        c.setBackground(round(Color.WHITE, 20));
        return c;
    }

    private TextView section(String label) {
        TextView t = text(label, 15, INK, true);
        t.setPadding(0, dp(18), 0, dp(9));
        return t;
    }

    private TextView avatar(String value, int color, int sizeDp) {
        TextView v = new TextView(this);
        v.setText(value);
        v.setTextColor(Color.WHITE);
        v.setTextSize(sizeDp >= 70 ? 22 : 16);
        v.setTypeface(Typeface.DEFAULT_BOLD);
        v.setGravity(Gravity.CENTER);
        GradientDrawable d = new GradientDrawable();
        d.setShape(GradientDrawable.OVAL);
        d.setColor(color);
        v.setBackground(d);
        v.setLayoutParams(new LinearLayout.LayoutParams(dp(sizeDp), dp(sizeDp)));
        return v;
    }

    private TextView text(String value, float size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT_BOLD);
        return t;
    }

    private Button button(String label, int bg, int fg) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextColor(fg);
        b.setTextSize(13);
        b.setAllCaps(false);
        b.setTypeface(Typeface.DEFAULT_BOLD);
        b.setPadding(dp(12), 0, dp(12), 0);
        b.setBackground(round(bg, 18));
        return b;
    }

    private Space space(int heightDp) {
        Space s = new Space(this);
        s.setLayoutParams(new LinearLayout.LayoutParams(1, dp(heightDp)));
        return s;
    }

    private GradientDrawable round(int color, float radiusDp) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radiusDp));
        return d;
    }

    private int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private void showCreateMenu() {
        String[] options = {"New Chat", "New Post", "New Circle", "Create Community", "Create Event"};
        new AlertDialog.Builder(this)
                .setTitle("Create on NEXA")
                .setItems(options, (dialog, which) -> {
                    if (which == 2) createCircleDialog();
                    else Toast.makeText(this, options[which] + " demo", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void createCircleDialog() {
        final EditText input = new EditText(this);
        input.setHint("e.g. Project Team");
        input.setPadding(dp(16), dp(8), dp(16), dp(8));
        new AlertDialog.Builder(this)
                .setTitle("Create a Circle")
                .setMessage("Circle posts can reach selected people without exposing everyone's phone number.")
                .setView(input)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Create", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (!name.isEmpty()) {
                        circles.add(name);
                        showCircles();
                        Toast.makeText(this, name + " created", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void voiceRoomDialog() {
        new AlertDialog.Builder(this)
                .setTitle("East Africa Tech Talk")
                .setMessage("LIVE Voice Room\n\n32 listening · 5 speakers\n\nIn the full version, hosts can invite listeners to speak in real time.")
                .setNegativeButton("Leave", null)
                .setPositiveButton("Join as Listener", (d, w) -> Toast.makeText(this, "Joined Voice Room (demo)", Toast.LENGTH_SHORT).show())
                .show();
    }
}
