package ga.gasoft.smartpolice.fragments;

/**
 * Created by devendiran on 8/18/2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import ga.gasoft.smartpolice.MainActivity;
import ga.gasoft.smartpolice.utils.PreferenceHelper;
import ga.gasoft.smartpolice.R;

/**
 * Created by niccapdevila on 3/26/16.
 */
public class LanguageFragment extends Fragment implements View.OnClickListener{

    Button btn;
    private Button english,kannada;
    private PreferenceHelper languageFragmentHelper;
    Locale myLocale;
    public static String lang;
    String langu;
    private static final String tag = "SignUp Activity";
    SharedPreferences settings;
    private Activity mContext;
    //private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;
    private ImageView back;
    TextView hometitle;
    public  LanguageFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        LanguageFragment fragment = new LanguageFragment();

        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mPageNo = getArguments().getInt(ARG_PAGE);


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.language, container, false);
        /*hometitle=(TextView) getActivity().findViewById(R.id.toolbar_title1);
        hometitle.setText("Help");*/

        //  TextView textView = (TextView) view;
        // textView.setText("Fragment #" + mPageNo);

       /* back=(ImageView) view.findViewById(R.id.backbtn);
        back.setOnClickListener(this);*/

        english=(Button)view.findViewById(R.id.english);
        kannada=(Button)view.findViewById(R.id.kannada);
        english.setOnClickListener(this);
        kannada.setOnClickListener(this);
        languageFragmentHelper = new PreferenceHelper(getActivity());
        return view;
    }







    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        Intent intent1 = new Intent(getActivity(), MainActivity.class);
        startActivity(intent1);
    }

    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());
        // set dialog message
        alertDialogBuilder
                .setMessage(this.getString(R.string.areyou))
                .setCancelable(false)
                .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getActivity().finish();
                        System.exit(0);
                        dialog.cancel();
                    }
                })
                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
						/*Intent intent = new Intent(AndroidLocalize.this, AndroidLocalize.class);
						startActivity(intent);*/
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }



    @Override
    public void onClick(View v) {
        Fragment someFragment;
        if (v == english) {
            lang = "EN";
//            SharedPreferences settings = getActivity().getSharedPreferences("qibhr2", 0);
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putString("lang", lang);
            //editor.putString("lang1",lang);
//            editor.commit();
            /*someFragment = new SosDashboardFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame, someFragment ); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();*/

            Log.d(tag, "lang " + lang);
		/*SharedPreferences.Editor editor = settings.edit();
		editor.putString(lang, lang);*/

            //Toast.makeText(getApplicationContext(), R.string.succesfullyen, Toast.LENGTH_LONG).show();
            setLocale("en");
        }
        if(v == kannada){
            lang = "KAN";
//            SharedPreferences settings = getActivity().getSharedPreferences("qibhr2", 0);
//            SharedPreferences.Editor editor = settings.edit();
//            editor.putString("lang", lang);
//            editor.putString("lang1",lang);
//            editor.commit();



            //Toast.makeText(getApplicationContext(),R.string.succesfullyar, Toast.LENGTH_LONG).show();

            Log.d(tag, "lang " + lang);

            setLocale("kan");
        }
        languageFragmentHelper.saveValueToSharedPrefs("lang",lang);

    }
}