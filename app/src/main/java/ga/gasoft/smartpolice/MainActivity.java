package ga.gasoft.smartpolice;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import ga.gasoft.smartpolice.Application.App;
import ga.gasoft.smartpolice.Network.CallBacks;
import ga.gasoft.smartpolice.Network.NetUtils;
import ga.gasoft.smartpolice.Network.UserNetworkManager;
import ga.gasoft.smartpolice.fragments.ComplaintFragment;
import ga.gasoft.smartpolice.fragments.HelpFragment;
import ga.gasoft.smartpolice.fragments.LanguageFragment;
import ga.gasoft.smartpolice.fragments.MessageFragment;
import ga.gasoft.smartpolice.fragments.ProfileFragment;
import ga.gasoft.smartpolice.fragments.SosDashboardFragment;

import static com.loopj.android.http.AsyncHttpClient.log;
import static ga.gasoft.smartpolice.LoginActivity.usrStr;


public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    TextView title, toolbar_title1;
    private Fragment fragment = null;
    Locale myLocale;
    public static String lang;
    private ImageView back;
    private static final String tag = "Main Activity";

    private int[] mTabsIcons = {
            R.drawable.profile_icon,
            R.drawable.complaint_icon,
            R.drawable.ic_home_white_24dp,
            R.drawable.help_icon,
            R.drawable.message_icon};
    private ViewPager viewPager;
    private int fromNotification;
    public TextView countTextView;
    private ProgressDialog pDialog;
    public int msgCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvitity_main);

//setTitle(R.string.madiwala);

        fromNotification = getIntent().getIntExtra("Notification", 0);
        Log.e("fromNotification", ""+fromNotification);
        if(fromNotification != 0) {
        Log.e("Intent", ""+getIntent().getExtras());
        }

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.mipmap.ic_launcher);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        // layoutParams.rightMargin = 40;
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary

            ));
        }



        getNotificationCount(null);

      /*  back=(ImageView) findViewById(R.id.backbtn);
        back.setOnClickListener(this);

        */


        // Setup the viewPager
       viewPager = (ViewPager) findViewById(R.id.view_pager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int pos) {
                Log.e("page selected",".."+pos);
                String[] mTabsTitle = getResources().getStringArray(R.array.my_books);
                getSupportActionBar().setTitle(mTabsTitle[pos]);
//                setTitle(Gravity.CENTER);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                Log.e("pos ", ""+pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                Log.e("pos ", ""+pos);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                Log.e("pos ", ""+pos);
            }
        });


        if (viewPager != null) {
            viewPager.setOffscreenPageLimit(0);
            viewPager.setAdapter(pagerAdapter);
        }


        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(viewPager);
            int i;
            for (i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                if (tab != null) {
                    tab.setCustomView(pagerAdapter.getTabView(i));
                }else{
                    Toast.makeText(MainActivity.this, "tab...."+tab, Toast.LENGTH_SHORT).show();
                }
            }

            mTabLayout.getTabAt(0).getCustomView()
                    .setSelected(true);
            if(fromNotification == 10){
                viewPager.setCurrentItem(4);
            }else{
                viewPager.setCurrentItem(2);
            }


        }
    }


    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                App.clear();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(this, getResources().getString(R.string.LogoutSuccess), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menglish:
                english();
                Toast.makeText(this,  getResources().getString(R.string.LanguageEnglish), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.mkannada:
                kannada();
                Toast.makeText(this,  getResources().getString(R.string.LanguageKannada), Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void english() {
        lang = "EN";
        SharedPreferences settings = getSharedPreferences("qibhr2", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("lang", lang);
        editor.commit();
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);

        Log.d(tag, "lang " + lang);
		editor.putString(lang, lang);
        setLocale("en");
    }

    private void kannada() {
        lang = "KAN";
        SharedPreferences settings = getSharedPreferences("qibhr2", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("lang", lang);
        editor.commit();

//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);

        Log.d(tag, "lang " + lang);

        setLocale("kan");
    }


    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public final int PAGE_COUNT = 5;
        Resources res = getResources();
        String[] mTabsTitle = res.getStringArray(R.array.my_books);
        Fragment currentFragment = null;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        public View getTabView(int position) {
            // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_tab, null);
            title = (TextView) view.findViewById(R.id.title);
            countTextView = (TextView)view.findViewById(R.id.messageCount);

            title.setText(mTabsTitle[position]);

            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            icon.setImageResource(mTabsIcons[position]);
           /* if(position == 4){
                countTextView.setVisibility(View.VISIBLE);
                countTextView.setText("");
            }*/
            return view;
        }


        @Override
        public android.support.v4.app.Fragment getItem(int pos) {
            Bundle args = new Bundle();

            switch (pos) {
                case 0:
                    currentFragment = new ProfileFragment();
                    return currentFragment;
                case 1:
                    currentFragment = new ComplaintFragment();
                    return currentFragment;
                case 2:
//                    getNotificationCount();
                    currentFragment = new SosDashboardFragment();
                    return currentFragment;
                case 3:
                    currentFragment = new HelpFragment();
                    return currentFragment;
                case 4:
                    currentFragment = new MessageFragment();

//                    countTextView.setText("");
                    return currentFragment;
                default:
                    return currentFragment;
            }

        }

        @Override
        public int getCount() {

            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mTabsTitle[position];
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle( getResources().getString(R.string.ExitMsg));


        alertDialog.setNegativeButton (getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });


        alertDialog.setPositiveButton( getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                   finish();
            }
        });

    }

    public void getNotificationCount(final SosDashboardFragment fragment){
        if(NetUtils.isOnline(MainActivity.this)) {
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(getResources().getString(R.string.pleasewait));
            pDialog.setCancelable(false);
            pDialog.show();

            UserNetworkManager.getNotificationCount(new CallBacks.ObjectCallBackListener() {
                @Override
                public void onSuccess(Object responseObject) {
                    Log.e("Notification response", responseObject+"");
                    pDialog.dismiss();
                    if(responseObject != null) {
                        JSONObject object = (JSONObject) responseObject;
                        try {
                            msgCount = Integer.parseInt(object.getString("count"));
                            log.e("count", ""+msgCount);
                            if(msgCount !=0) {
                                countTextView.setVisibility(View.VISIBLE);
                                countTextView.setText("" + msgCount);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(fragment != null){
                            fragment.swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }

                @Override
                public void onError(String errorMessage) {
                    Log.e("count error response", errorMessage+"");
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }

                @Override
                public void onErrorWithData(JSONObject errorjson) {
                    pDialog.dismiss();
                }

                @Override
                public void onStart() {

                }
            }, getApplicationContext(), null);



        }else{
            Toast.makeText(MainActivity.this,getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
        }
    }


}
