package com.example.alquiler.alquilercom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cleveroad.splittransformation.SquareViewPagerIndicator;
import com.cleveroad.splittransformation.TransformationAdapterWrapper;
import com.example.alquiler.alquilercom.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Fragment with inner view pager and implementation of pager adapter with views.
 */
public class SimpleViewsFragment extends Fragment {


    private ViewPager viewPager;
    private SquareViewPagerIndicator indicator;
    List<String> imgs;
    String nom;
    String loc;
    String tele,prec;
    List<String> serv;

    public static SimpleViewsFragment instance(List<String> img, String nom, String loc,String t, String p,List<String> s) {
        //Log.v("IMAGENNNNNNNNNNNN",img.get(0));
        SimpleViewsFragment nuevo=new SimpleViewsFragment();
        nuevo.setArgs(img,nom,loc,t,p,s);
        return  nuevo;
    }

    public void setArgs(List<String> img,String nom_,String loc_,String t,String p,List<String> s){
        imgs = new ArrayList<String>(img);
        nom=nom_;
        loc=loc_;
        tele=t;
        prec=p;
        serv=new ArrayList<>(s);
    }

    @Override
    public void onSaveInstanceState(Bundle b){
        b.putStringArrayList("img",new ArrayList<String>(imgs));
        b.putString("log",loc);
        b.putString("nog",nom);
        b.putString("teleg",tele);
        b.putString("precg",prec);
        b.putStringArrayList("servg",new ArrayList<String>(serv));
        super.onSaveInstanceState(b);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if( savedInstanceState != null ) {
            imgs=savedInstanceState.getStringArrayList("img");
            nom=savedInstanceState.getString("nog");
            loc=savedInstanceState.getString("log");
            tele=savedInstanceState.getString("teleg");
            prec=savedInstanceState.getString("precg");
            serv=savedInstanceState.getStringArrayList("servg");
        }
        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);
        Button ok=(Button)view.findViewById(R.id.botonok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(RegisterActivity.this,a.getData().toString(),Toast.LENGTH_SHORT);
            }
        });
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        indicator = (SquareViewPagerIndicator) view.findViewById(R.id.indicator);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SimplePagerAdapter adapter = new SimplePagerAdapter(getContext(),imgs,loc,nom,tele,prec,serv);
        TransformationAdapterWrapper wrapper = TransformationAdapterWrapper
                .wrap(getContext(), adapter)
                .rows(10)
                .columns(7)
                .marginTop(getResources().getDimensionPixelSize(R.dimen.margin_top))
                .bitmapScale(1f)
                .build();
        viewPager.setAdapter(wrapper);
        viewPager.setPageTransformer(false, wrapper);

        indicator.initializeWith(viewPager);
    }

    @Override
    public void onDestroyView() {
        indicator.reset();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.simple_views);
    }

    private static class SimplePagerAdapter extends PagerAdapter {

        /*private final int[] drawables = new int[] {
                R.drawable.administrator,
                R.drawable.cashier,
                R.drawable.cook,
                R.drawable.administrator,
                R.drawable.cashier,
                R.drawable.cook,
                R.drawable.administrator,
                R.drawable.cashier,
                R.drawable.cook,

        };*/

        private List<Bitmap> drawables=new ArrayList<Bitmap>();
        private String loc,nom,tel,precio;
        List<String> servicios;//=new ArrayList<>();

        private final Context context;
        private final LayoutInflater inflater;

        public SimplePagerAdapter(Context context,List<String> img,String loc_,String nom_,String tel_,String prec_,List<String> serv_) {
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.loc=loc_;
            this.nom=nom_;
            this.tel=tel_;
            this.precio=prec_;
            this.servicios=new ArrayList<>(serv_);
            //this.drawables =new ArrayList<String>(img);
            for (int i=0;i<img.size();++i){
                byte[] decodedBytes = Base64.decode(img.get(i),Base64.DEFAULT);
                drawables.add(BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length));
            }


        }

        @Override
        public int getCount() {
            return drawables.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = inflater.inflate(R.layout.pager_item_et, container, false);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);

            ImageView ima=(ImageView) view.findViewById(R.id.iman);
            ImageView iwo=(ImageView) view.findViewById(R.id.iwo);
            ImageView itv=(ImageView) view.findViewById(R.id.itv);
            ImageView iwi=(ImageView) view.findViewById(R.id.iwi);
            ImageView idu=(ImageView) view.findViewById(R.id.idu);
            ImageView imas=(ImageView) view.findViewById(R.id.imas);
            ImageView iba=(ImageView) view.findViewById(R.id.iba);


            TextView loc=(TextView) view.findViewById(R.id.textu);
            TextView due=(TextView) view.findViewById(R.id.textd);
            TextView ttel=(TextView) view.findViewById(R.id.textt);
            TextView tpre=(TextView) view.findViewById(R.id.textp);
            loc.setText(this.loc);
            due.setText(this.nom);
            ttel.setText(this.tel);
            String prec=precio+" soles";
            tpre.setText(prec);

            imageView.setImageBitmap(drawables.get(position));
            if (servicios.get(0).equals("3")){
                ima.setImageResource(R.mipmap.verde_standingmanfilled);
                iwo.setImageResource(R.mipmap.verde_standingwomanfilled);
            } else if (servicios.get(0).equals("2"))
                iwo.setImageResource(R.mipmap.verde_standingwomanfilled);
            else
                ima.setImageResource(R.mipmap.verde_standingmanfilled);

            for (int i=1;i<servicios.size();++i){

                if (servicios.get(i).equals("1")) {
                    Log.v("SERVICIOOOOOOOOOOOOOOOS",servicios.get(i));
                        if(i==1){
                            Log.v("11111111111111","1111111111");
                            itv.setImageResource(R.mipmap.verde_retrotvfilled);
                        }
                        else if (i==2) {
                            Log.v("2222222222222", "22222222");
                            iwi.setImageResource(R.mipmap.verde_wififilled);
                        }
                        else if (i==3) {
                            Log.v("33333333", "1111111111");
                            idu.setImageResource(R.mipmap.verde_showerandtubfilled);
                        }
                        else if (i==4)
                            imas.setImageResource(R.mipmap.verde_dogfilled);
                       else
                            iba.setImageResource(R.mipmap.verde_toiletbowlfilled);
                }
            }
            Log.v("Settinggggggggggggg","colocando imagen");
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
