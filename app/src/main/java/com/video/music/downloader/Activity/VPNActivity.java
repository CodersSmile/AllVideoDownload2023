package com.video.music.downloader.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.DialogFragment;

import com.anchorfree.partner.api.data.Country;
import com.anchorfree.partner.api.response.AvailableCountries;
import com.anchorfree.reporting.TrackingConstants;
import com.anchorfree.sdk.SessionConfig;
import com.anchorfree.sdk.SessionInfo;
import com.anchorfree.sdk.UnifiedSDK;
import com.anchorfree.sdk.rules.TrafficRule;
import com.anchorfree.vpnsdk.callbacks.Callback;
import com.anchorfree.vpnsdk.callbacks.CompletableCallback;
import com.anchorfree.vpnsdk.compat.CredentialsCompat;
import com.anchorfree.vpnsdk.exceptions.VpnException;
import com.anchorfree.vpnsdk.transporthydra.HydraTransport;
import com.anchorfree.vpnsdk.vpnservice.VPNState;
import com.northghost.caketube.OpenVpnTransportConfig;
import com.video.music.downloader.R;
import com.video.music.downloader.vpn.Country_Code;
import com.video.music.downloader.vpn.RegionChooserDialog;
import com.video.music.downloader.vpn.SharePreferences;
import com.video.music.downloader.vpn.Utilss;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class VPNActivity extends AppCompatActivity implements RegionChooserDialog.RegionChooserInterface{

    LinearLayout select_country;
    ImageView connect_btn;
    TextView activate_btn;
    ProgressBar connection_progress;
    private String selected = "";
    List<Country> list;

    LinearLayoutCompat llVpnState;
    ArrayList<Country_Code> list_country;
    SharePreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpnactivity);
        connect_btn = findViewById(R.id.connect_btn);
        activate_btn = findViewById(R.id.activate_btn);
        select_country = findViewById(R.id.select_country);

        activate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectionCheck();
            }
        });
        connect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VPNActivity.this, "VPN is connecting", Toast.LENGTH_SHORT).show();
                getCountry();
            }
        });

        select_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegionChooserDialog regionChooserDialog = RegionChooserDialog.newInstance(VPNActivity.this);
                regionChooserDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
                regionChooserDialog.show(getSupportFragmentManager(), RegionChooserDialog.TAG);
            }
        });


    }


    public void getCountry() {
        try {
            UnifiedSDK.getInstance().getBackend().countries(new Callback<AvailableCountries>() {
                @Override
                public void success(@NonNull AvailableCountries availableCountries) {
                    try {
                        Utilss.list = new ArrayList<>();
                        list_country = new ArrayList<>();
                        list = availableCountries.getCountries();
                        Utilss.list = availableCountries.getCountries();
                        if (Utilss.list_Country.size() > 0) {
                            for (Country country : Utilss.list) {
                                for (Country_Code code : Utilss.list_Country) {

                                    if (country.getCountry().equalsIgnoreCase(code.getCode())) {
                                        Log.e("Country_Size", "Service_List " + code.getCode());
                                        Country_Code new_code = new Country_Code();
                                        new_code.setCode(code.getCode());
                                        list_country.add(new_code);
                                    }
                                }


                            }
                            if (!list_country.isEmpty()) {
                                selected = getRandomString(list_country);
                            } else {
                                selected = "";
                            }
                        } else {

                            if (!list.isEmpty()) {
                                selected = getRandomStringValue(list);
                            } else {
                                selected = "";
                            }
                        }

                        connectToVpn(selected);
                    } catch (Exception e) {
                        Log.e("test", "Error " + e.getMessage());
                        connectToVpn("");
                    }

                }


                @Override
                public void failure(@NonNull VpnException e) {
                    Log.e("test", "Vpn Error " + e.getMessage());
                    connectToVpn("");
                }
            });
        } catch (Exception e) {

        }

    }

    public void connectionCheck() {
        UnifiedSDK.getVpnState(new Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState vpnState) {
                if (vpnState == VPNState.CONNECTED) {
                    Intent intent = new Intent(VPNActivity.this, SplashActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(VPNActivity.this, "Please Connect your app with VPN", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void failure(@NonNull VpnException e) {
                Toast.makeText(VPNActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateUi() {


        UnifiedSDK.getVpnState(new Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState vpnState) {
                if (vpnState == VPNState.CONNECTED && sp.getVpnButtonStatus() == 1) {
                    llVpnState.setVisibility(View.VISIBLE);
                } else {
                    llVpnState.setVisibility(View.GONE);
                }
                if (sp.getVpnButtonStatus() == 1) {
                    connect_btn.setVisibility(vpnState == VPNState.CONNECTED ? View.VISIBLE : View.GONE);
                } else {
                    connect_btn.setVisibility(View.GONE);
                }

                switch (vpnState) {
                    case IDLE:
                        Log.e("Vpn_Status", " Disconnected");
                        break;
                    case CONNECTED:
                        Log.e("Vpn_Status", " Connected");
                        break;
                    case CONNECTING_VPN:
                    case CONNECTING_CREDENTIALS:
                    case CONNECTING_PERMISSIONS:
                        Log.e("Vpn_Status", " Connecting");
                        break;
                    case PAUSED:
                        Log.e("Vpn_Status", " PAUSE");
                        break;
                    case DISCONNECTING:
                        Log.e("Vpn_Status", " DISCONNECTING");
                }
            }
            @Override
            public void failure(@NonNull VpnException e) {
            }
        });
//        getCurrentServer(new Callback<String>() {
//            @Override
//            public void success(@NonNull String s) {
//                if (!s.equals("")) {
//                    CountryModal country = Utilss.getCountryFlagPath(selected, VPNActivity.this);
////                    if (country != null) {
////                        Glide.with(VPNActivity.this).load(country.getPathFlag()).into(image_icon);
////                        text_country.setText(country.getName());
////                    }
//                }
//            }
//            @Override
//            public void failure(@NonNull VpnException e) {
//            }
//        });
    }
    private void connectToVpn(String country) {
        isLoggedIn(new Callback<Boolean>() {
            @Override
            public void success(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    Log.e("Vpn_Error", "success: logged in");
                    List<String> fallbackOrder = new ArrayList<>();
                    fallbackOrder.add(HydraTransport.TRANSPORT_ID);
                    fallbackOrder.add(OpenVpnTransportConfig.tcp().getName());
                    fallbackOrder.add(OpenVpnTransportConfig.udp().getName());
                    List<String> bypassDomains = new LinkedList<>();
                    UnifiedSDK.getInstance().getVPN().start(new SessionConfig.Builder()
                            .withReason(TrackingConstants.GprReasons.M_UI)
                            .withTransportFallback(fallbackOrder)
                            .withTransport(HydraTransport.TRANSPORT_ID)
                            .withVirtualLocation(country)
                            .addDnsRule(TrafficRule.Builder.bypass().fromDomains(bypassDomains))
                            .build(), new CompletableCallback() {
                        @Override
                        public void complete() {
                            connection_progress.setVisibility(View.GONE);
                            updateUi();
                        }
                        @Override
                        public void error(@NonNull VpnException e) {
                            connection_progress.setVisibility(View.GONE);
                            Log.e("Check_Exception", e.getMessage());
//                            Toast.makeText(VPNActivity.this, "Error while connecting to vpn", Toast.LENGTH_SHORT).show();
                            updateUi();
                        }
                    });
                } else {
                    connection_progress.setVisibility(View.GONE);
                    updateUi();
                }
            }
            @Override
            public void failure(@NonNull VpnException e) {
                connection_progress.setVisibility(View.GONE);
                updateUi();
            }
        });

    }
    protected void isLoggedIn(Callback<Boolean> callback) {
        UnifiedSDK.getInstance().getBackend().isLoggedIn(callback);
    }
    private String getRandomString(ArrayList<Country_Code> list) {
        int min = 0;
        int max = list.size();
        return list.get(new Random().nextInt(((max - min) + 1) + min)).getCode();
    }
    private String getRandomStringValue(List<Country> list) {
        int min = 0;
        int max = list.size();
        return list.get(new Random().nextInt(((max - min) + 1) + min)).getCountry();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopVpn();
    }
    public void disconnectFromVpn() {
        UnifiedSDK.getVpnState(new Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState vpnState) {
                if (vpnState == VPNState.CONNECTED) {
                    UnifiedSDK.getInstance().getVPN().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
                        @Override
                        public void complete() {
                            connection_progress.setVisibility(View.GONE);
                            updateUi();
                            Log.e("Vpn_Error", "complete: vpn is disconnect successfully");
                        }
                        @Override
                        public void error(@NonNull VpnException e) {
                            Log.e("Vpn_Error", "error: ", e);
                        }
                    });
                }
            }
            @Override
            public void failure(@NonNull VpnException e) {
            }
        });
    }
    public static void stopVpn() {
        UnifiedSDK.getVpnState(new Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState vpnState) {
                if (vpnState == VPNState.CONNECTED) {
                    UnifiedSDK.getInstance().getVPN().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
                        @Override
                        public void complete() {
                        }
                        @Override
                        public void error(@NonNull VpnException e) {
                            Log.e("Vpn_Error", "error: ", e);
                        }
                    });
                }
            }
            @Override
            public void failure(@NonNull VpnException e) {
            }
        });
    }
    @Override
    public void onRegionSelected(Country item) {
        selected = item.getCountry();
//        CountryModal country = Utilss.getCountryFlagPath(selected, VPNActivity.this);
//        if (country != null) {
//            Glide.with(VPNActivity.this).load(country.getPathFlag()).into(image_icon);
//            text_country.setText(country.getName());
//        }
//        connectToVpn(selected);
    }
    protected void getCurrentServer(final Callback<String> callback) {
        UnifiedSDK.getVpnState(new Callback<VPNState>() {
            @Override
            public void success(@NonNull VPNState state) {
                if (state == VPNState.CONNECTED) {
                    UnifiedSDK.getStatus(new Callback<SessionInfo>() {
                        @Override
                        public void success(@NonNull SessionInfo sessionInfo) {
                            callback.success(CredentialsCompat.getServerCountry(sessionInfo.getCredentials()));
                        }
                        @Override
                        public void failure(@NonNull VpnException e) {
                            callback.success("");
                        }
                    });
                } else {
                    callback.success("");
                }
            }
            @Override
            public void failure(@NonNull VpnException e) {
                callback.failure(e);
            }
        });
    }
}

