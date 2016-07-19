package com.example.alquiler.alquilercom;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rene on 18/07/16.
 */

public class MyDialogFragment extends DialogFragment {
    List<String> im,serv;
    String lo;
    String no,tele,prec;


    public void setArgs(List<String> i, String l,String n,String tel_,String pre_,List<String> serv_){
        im=new ArrayList<String>(i);
        lo=l;
        no=n;
        tele=tel_;
        prec=pre_;
        serv=new ArrayList<>(serv_);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_main_slider, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.container, SimpleViewsFragment.instance(im,no,lo,tele,prec,serv))
                .addToBackStack(null)
                .commit();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
        return v;
    }
    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setOnDismissListener(null);
        super.onDestroyView();
    }


    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}