package ga.gasoft.smartpolice.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import ga.gasoft.smartpolice.AndroidGPSTrackingActivity;
import ga.gasoft.smartpolice.AtmActivity;
import ga.gasoft.smartpolice.BadCharacterActivity;
import ga.gasoft.smartpolice.BankActivity;
import ga.gasoft.smartpolice.BoloActivity;
import ga.gasoft.smartpolice.CheckPointActivity;
import ga.gasoft.smartpolice.LocatorActivity;
import ga.gasoft.smartpolice.MainActivity;
import ga.gasoft.smartpolice.adapters.CustomGridViewAdapter;
import ga.gasoft.smartpolice.utils.Item;
import ga.gasoft.smartpolice.MobActivity;
import ga.gasoft.smartpolice.PayingGuestActivity;
import ga.gasoft.smartpolice.PetrolbunkActivity;
import ga.gasoft.smartpolice.R;
import ga.gasoft.smartpolice.ReligiousActivity;
import ga.gasoft.smartpolice.SlumAreaVisitActivity;
import ga.gasoft.smartpolice.SosActivity;
import ga.gasoft.smartpolice.StolenActivity;
import ga.gasoft.smartpolice.WarrantNoticeActivity;
import ga.gasoft.smartpolice.WineShopBarActivity;

/**
 * @author Waleed Sarwar
 * @since March 30, 2016 12:34 PM
 */
public class SosDashboardFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener{
    GridView gridView;
    ArrayList<Item> gridArray = new ArrayList<Item>();
    CustomGridViewAdapter customGridAdapter;
    public static String[] gridViewStrings = {
            "ATM",
            "Bad Character",
            "Bank",
            "BOLO",
            "Religious Place",
            "Locator",
            "Mob",
            "Checkpoint",
            "Petrol Bunk",
            "Paying Guest",
            "Slum Area Visit",
            "Stolen Property",
            "Warrant and Notice",
            "Wine Shop and Bar",
            "SOS"
//            ,"Complaint"
    };
    public static int[] gridViewImages = {
            R.mipmap.atm_icon,
            R.mipmap.badcharector_icon,
            R.mipmap.bank_icon,
            R.mipmap.bolo_icon,
            R.mipmap.religiousplace_icon,
            R.mipmap.locator_icon,
            R.mipmap.mob_icon,
            R.mipmap.checkpoint_icon,
            R.mipmap.petrolbunk_icon,
            R.mipmap.payinguest_icon,
            R.mipmap.slumarea_icon,
            R.mipmap.stolen,
            R.mipmap.warrent_icon,
            R.mipmap.wineshopandbar_icon,
            R.mipmap.sos_icon,
//            R.mipmap.complaint_icon2,
    };
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;
    public SwipeRefreshLayout swipeRefreshLayout;

    public  SosDashboardFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        SosDashboardFragment fragment = new SosDashboardFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sos_dashboard, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);

        gridView = (GridView) view.findViewById(R.id.grid);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary

            ));
        }

        swipeRefreshLayout.setOnRefreshListener(this);
        //set grid view item
        Bitmap homeIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.atm_icon);
        Bitmap badIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.badcharector_icon);
        Bitmap bankIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.bank_icon);
        Bitmap boloIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.bolo_icon);
        Bitmap religiousIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.religiousplace_icon);
        Bitmap locatorIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.locator_icon);
        Bitmap mobIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.mob_icon);
        Bitmap checkIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.checkpoint_icon);
        Bitmap petrolIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.petrolbunk_icon);
        Bitmap payguestIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.payinguest_icon);
        Bitmap slumIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.slumarea_icon);
        Bitmap stolenIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.stolen);
        Bitmap warrentIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.warrent_icon);
        Bitmap wineshopIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.wineshopandbar_icon);
        Bitmap sosIcon = BitmapFactory.decodeResource(this.getResources(), R.mipmap.sos_icon);



        gridArray.clear();
        Log.e("array size", "count"+gridArray.size());
        gridArray.add(new Item(homeIcon,   getResources().getString(R.string.atm)));
        gridArray.add(new Item(badIcon,getResources().getString(R.string.badcharacter)));
        gridArray.add(new Item(bankIcon,getResources().getString(R.string.bank)));
        gridArray.add(new Item(boloIcon,getResources().getString(R.string.bolo)));
        gridArray.add(new Item(religiousIcon,getResources().getString(R.string.religiousplace)));
       gridArray.add(new Item(locatorIcon,getResources().getString(R.string.locator)));
        gridArray.add(new Item(mobIcon,getResources().getString(R.string.mob)));
        gridArray.add(new Item(checkIcon,getResources().getString(R.string.checkpoint)));
        gridArray.add(new Item(petrolIcon,getResources().getString(R.string.petrolbunk)));
        gridArray.add(new Item(payguestIcon,getResources().getString(R.string.payingguest)));
        gridArray.add(new Item(slumIcon,getResources().getString(R.string.slumareavisit)));
        gridArray.add(new Item(stolenIcon,getResources().getString(R.string.stolenproperty)));
        gridArray.add(new Item(warrentIcon,getResources().getString(R.string.warrantandnotice)));
        gridArray.add(new Item(wineshopIcon,getResources().getString(R.string.wineshopandbar)));
        gridArray.add(new Item(sosIcon,getResources().getString(R.string.sos)));

        gridView = (GridView) view.findViewById(R.id.grid);
        customGridAdapter = new CustomGridViewAdapter(getActivity(), R.layout.gridview_custom_layout, gridArray);
        gridView.setAdapter(customGridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                switch ( gridViewStrings[+i]) {
                    case "ATM":
                        Intent intent = new Intent(getActivity(), AtmActivity.class);
                        startActivity(intent);
                        break;
                    case "Bad Character":
                        Intent intent1 = new Intent(getActivity(), BadCharacterActivity.class);
                        startActivity(intent1);
                        break;
                    case "Bank":
                        Intent intent2 = new Intent(getActivity(), BankActivity.class);
                        startActivity(intent2);
                        break;
                    case "BOLO":
                        Intent intent3 = new Intent(getActivity(), BoloActivity.class);
                        startActivity(intent3);
                        break;

                    case "Religious Place":
                        Intent intent4 = new Intent(getActivity(), ReligiousActivity.class);
                        startActivity(intent4);
                        break;
                    case "Locator":
                        Intent intent5 = new Intent(getActivity(), LocatorActivity.class);
                        startActivity(intent5);
                        break;
                    case "Mob":
                        Intent intent6 = new Intent(getActivity(), MobActivity.class);
                        startActivity(intent6);
                        break;
                    case "Checkpoint":
                        Intent intent7 = new Intent(getActivity(), CheckPointActivity.class);
                        startActivity(intent7);
                        break;
                    case "Petrol Bunk":
                        Intent intent8 = new Intent(getActivity(), PetrolbunkActivity.class);
                        startActivity(intent8);
                        break;
                    case "Paying Guest":
                        Intent intent9 = new Intent(getActivity(), PayingGuestActivity.class);
                        startActivity(intent9);
                        break;
                    case "Slum Area Visit":
                        Intent intent10 = new Intent(getActivity(), SlumAreaVisitActivity.class);
                        startActivity(intent10);
                        break;
                    case "Stolen Property":
                        Intent intent11 = new Intent(getActivity(), StolenActivity.class);
                        startActivity(intent11);
                        break;
                    case "Warrant and Notice":
                        Intent intent12 = new Intent(getActivity(), WarrantNoticeActivity.class);
                        startActivity(intent12);
                        break;
                    case "Wine Shop and Bar":
                        Intent intent13 = new Intent(getActivity(), WineShopBarActivity.class);
                        startActivity(intent13);
                        break;
                    case "SOS":
                        Intent intent14 = new Intent(getActivity(), SosActivity.class);
                        startActivity(intent14);
                        break;
//
                    default:
                        break;
                }

            }
        });


        return view;
    }

    @Override
    public void onRefresh() {
        Log.e("onRefresh", "notification count");
        ((MainActivity)getActivity()).getNotificationCount(SosDashboardFragment.this);
    }
}
