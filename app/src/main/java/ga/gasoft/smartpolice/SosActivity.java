package ga.gasoft.smartpolice;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import ga.gasoft.smartpolice.Application.App;
import ga.gasoft.smartpolice.Network.CallBacks;
import ga.gasoft.smartpolice.Network.NetUtils;
import ga.gasoft.smartpolice.Network.UserNetworkManager;

public class SosActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView hometitle;
    private ImageView home,back;
    private RelativeLayout chaseLayout, attackLayout, gangLayout, mobLayout;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        hometitle=(TextView)findViewById(R.id.kkk);
        chaseLayout = (RelativeLayout)findViewById(R.id.chaseLayout);
        attackLayout = (RelativeLayout)findViewById(R.id.attackLayout);
        gangLayout = (RelativeLayout)findViewById(R.id.gangLayout);
        mobLayout = (RelativeLayout)findViewById(R.id.mobLayout);

        hometitle.setText(R.string.sos);

        back=(ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        chaseLayout.setOnClickListener(this);
        attackLayout.setOnClickListener(this);
        gangLayout.setOnClickListener(this);
        mobLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.chaseLayout:
                triggerSos(1);
                break;
            case R.id.attackLayout:
                triggerSos(2);
                break;
            case R.id.gangLayout:
                triggerSos(3);
                break;
            case R.id.mobLayout:
                triggerSos(4);
                break;
            case R.id.back:
                finish();
                break;
            default:
                break;
        }

    }

    private void triggerSos(int id){


        if(NetUtils.isOnline(SosActivity.this)){
            loading = new ProgressDialog(SosActivity.this);
            loading.setMessage(getResources().getString(R.string.sendReq));
            loading.setCancelable(false);
            loading.show();
            UserNetworkManager.postSOSDetails(new CallBacks.StringCallBackListener() {
                @Override
                public void onSuccess(String successMessage) {
                    Log.e("on", "success");
                    loading.dismiss();
                    Toast.makeText(SosActivity.this, getResources().getString(R.string.sosSuccess),Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(String errorMessage) {
                    Log.e("on", "error");
                    loading.dismiss();
                    Toast.makeText(SosActivity.this, getResources().getString(R.string.sosError),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStart() {

                }
            }, id);
        }else{
            Toast.makeText(SosActivity.this, getResources().getString(R.string.NO_NETWORK), Toast.LENGTH_SHORT).show();
        }

    }

}
