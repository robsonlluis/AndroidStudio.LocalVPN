package com.robson.localvpn;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.VpnService;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Robson on 08/06/2016.
 */
public class MyLocalVpn extends VpnService implements Handler.Callback, Runnable {

    private String txtIpv4Address;
    private String txtIpv6Address;
    private String txtDns;
    private String txtRouteForIpv4;
    private String txtRouteForIpv6;
    private Builder builder;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String prefix = getPackageName();
        txtIpv4Address = intent.getStringExtra(prefix + ".txtIpv4Address");
        txtIpv6Address = intent.getStringExtra(prefix + ".txtIpv6Address");
        txtDns = intent.getStringExtra(prefix + ".txtDns");
        txtRouteForIpv4 = intent.getStringExtra(prefix + ".txtRouteForIpv4");
        txtRouteForIpv6 = intent.getStringExtra(prefix + ".txtRouteForIpv6");

        try{
            createVPN();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return 1;
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void run() {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void createVPN() throws Exception{
        builder = new Builder();
        builder.addAddress(txtIpv4Address, 32);
        builder.addAddress(txtIpv6Address, 128);
        builder.addDnsServer(txtDns);
        builder.addRoute(txtRouteForIpv4, 0);
        builder.addRoute(txtRouteForIpv6, 0);

        setAllowedApplications();

        builder.setBlocking(false);
        // Create a new interface using the builder and save the parameters.
        builder.setSession(getString(R.string.app_name)).establish();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setAllowedApplications() throws Exception{
        PackageManager packageManager = MyLocalVpn.this.getPackageManager();
        List<ApplicationInfo> listApplicationInfo = packageManager.getInstalledApplications(0);
        for (ApplicationInfo applicationInfo : listApplicationInfo){
            builder.addDisallowedApplication(applicationInfo.packageName);
        }
    }
}
