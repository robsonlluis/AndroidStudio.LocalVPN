package com.robson.localvpn;

import android.content.Intent;
import android.net.VpnService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView txtIpv4Address;
    private TextView txtIpv6Address;
    private TextView txtDns;
    private TextView txtRouteForIpv4;
    private TextView txtRouteForIpv6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtIpv4Address = (TextView) findViewById(R.id.txtIpv4Address);
        txtIpv6Address = (TextView) findViewById(R.id.txtIpv6Address);
        txtDns = (TextView) findViewById(R.id.txtDns);
        txtRouteForIpv4 = (TextView) findViewById(R.id.txtRouteForIpv4);
        txtRouteForIpv6 = (TextView) findViewById(R.id.txtRouteForIpv6);

        findViewById(R.id.btnCreateVpn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int idView = v.getId();
        switch (idView){
            case R.id.btnCreateVpn:
                Intent intent = VpnService.prepare(this);
                if (intent != null) {
                    startActivityForResult(intent, 0);
                } else {
                    onActivityResult(0, RESULT_OK, null);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        if (result == RESULT_OK) {
            String prefix = getPackageName();
            Intent intent = new Intent(this, MyLocalVpn.class)
                    .putExtra(prefix + ".txtIpv4Address", txtIpv4Address.getText().toString())
                    .putExtra(prefix + ".txtIpv6Address", txtIpv6Address.getText().toString())
                    .putExtra(prefix + ".txtDns", txtDns.getText().toString())
                    .putExtra(prefix + ".txtRouteForIpv4", txtRouteForIpv4.getText().toString())
                    .putExtra(prefix + ".txtRouteForIpv6", txtRouteForIpv6.getText().toString());
            startService(intent);
        }
    }
}
