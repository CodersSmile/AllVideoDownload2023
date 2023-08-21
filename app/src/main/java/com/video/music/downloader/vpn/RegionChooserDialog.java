package com.video.music.downloader.vpn;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anchorfree.partner.api.data.Country;
import com.anchorfree.partner.api.response.AvailableCountries;
import com.anchorfree.sdk.UnifiedSDK;
import com.anchorfree.vpnsdk.callbacks.Callback;
import com.anchorfree.vpnsdk.exceptions.VpnException;
import com.video.music.downloader.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RegionChooserDialog extends DialogFragment implements CountryAdapter.selecTionCountry, View.OnClickListener {
    public static final String TAG = RegionChooserDialog.class.getSimpleName();
    Context context;
    TextView tvOops;
    RecyclerView rvCountryList;
    ProgressBar regionsProgress;
    ArrayList<Country_Code> list;
    List<Country> list_vpn;
    ImageView ivBAck;
    View view;
    boolean isServerLoad = false;
    RegionChooserInterface regionChooserInterface;
    View mainView;
    CountryAdapter countryAdapter;

    public RegionChooserDialog() {
    }

    public RegionChooserDialog(Context context) {
        this.context = context;
    }

    public static RegionChooserDialog newInstance(Context context) {
        RegionChooserDialog frag = new RegionChooserDialog(context);
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.layout_server_fragmnet, container);
        Window window = getActivity().getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.black));
        window.setNavigationBarColor(getResources().getColor(R.color.black));

        init(mainView);

        this.view = mainView;
        return mainView;
    }


    private void init(View view) {

        rvCountryList = view.findViewById(R.id.countrylist);
        regionsProgress = view.findViewById(R.id.regions_progress);
        ivBAck = view.findViewById(R.id.as_back);
        tvOops = view.findViewById(R.id.tv_oops);


        ivBAck.setOnClickListener(this);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadServer();

    }

    private void loadServer() {
        ShowProgress();
        UnifiedSDK.getInstance().getBackend().countries(new Callback<AvailableCountries>() {
            @Override
            public void success(@NonNull final AvailableCountries countries) {
                isServerLoad = true;
                hideProress();

                list = new ArrayList<>();
                list = new ArrayList<>();
                list_vpn = countries.getCountries();
                Utilss.list = countries.getCountries();
                for (Country country : Utilss.list) {
                    for (Country_Code counteyModal : Utilss.list_Country) {
                        if (country.getCountry().equalsIgnoreCase(counteyModal.getCode())) {
                            Country_Code newModal = new Country_Code();
                            newModal.setCode(counteyModal.getCode());
                            newModal.setCountry(country);
                            list.add(newModal);
                        }
                    }
                }
                String jsonObject = loadJSONFromAsset();
                ArrayList<sModal> list1 = new ArrayList<>();
                try {
                    JSONObject mObject = new JSONObject(jsonObject);
                    JSONArray mArray = mObject.getJSONArray("country_flag");

                    for (int i = 0; i < mArray.length(); i++) {
                        JSONObject object = mArray.getJSONObject(i);
                        String path = object.getString("url");
                        String name = object.getString("Name");
                        String code = object.getString("Code");
                        list1.add(new sModal(path, name, code));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                for (Country_Code aa : list) {
                    for (sModal sModal : list1) {
                        if (aa.getCode().equalsIgnoreCase(sModal.code)) {
                            aa.setPathFlag(sModal.path);
                            aa.setName(sModal.name);
                        }
                    }

                }


                countryAdapter = new CountryAdapter(context, list, 1);
                rvCountryList.setAdapter(countryAdapter);
                countryAdapter.setCallBack(RegionChooserDialog.this);

                RecyclerView.LayoutManager manager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                rvCountryList.setLayoutManager(manager);


            }


            @Override
            public void failure(VpnException e) {
                hideProress();
                tvOops.setVisibility(View.VISIBLE);
            }
        });
    }

    private void ShowProgress() {
        regionsProgress.setVisibility(View.VISIBLE);
        rvCountryList.setVisibility(View.INVISIBLE);
        tvOops.setVisibility(View.GONE);
    }

    private void hideProress() {


        regionsProgress.setVisibility(View.GONE);
        rvCountryList.setVisibility(View.VISIBLE);


    }


    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        if (ctx instanceof RegionChooserInterface) {
            regionChooserInterface = (RegionChooserInterface) ctx;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        regionChooserInterface = null;
    }

    @Override
    public void onSelectionCountry(Country countryName) {
        regionChooserInterface.onRegionSelected(countryName);
        dismiss();
    }

    @Override
    public void onClick(View v) {


        if (v == ivBAck) {
            dismiss();
        }

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("country_flag.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public interface RegionChooserInterface {
        void onRegionSelected(Country item);
    }

    class sModal {
        public String name;
        public String path;
        public String code;

        public sModal(String path, String name, String code) {
            this.path = path;
            this.name = name;
            this.code = code;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}