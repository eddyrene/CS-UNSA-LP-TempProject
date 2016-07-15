package com.example.alquiler.alquilercom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.alquiler.alquilercom.data.JsonHttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class register extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    ToggleButton btn_agua, btn_animales, btn_men, btn_toilet, btn_tv, btn_wifi, btn_woman;
    EditText nombre, precio, direc, fono;

    private  View scroll;
    private View spinner;
    //byte[] byteArray;
    String byteArray="";
    private Bitmap imagenSca;
    private String email;
    double plat=0,plon=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        plat=getIntent().getExtras().getDouble("lat");
        plon=getIntent().getExtras().getDouble("lon");
        email=getIntent().getExtras().getString("email");
        scroll=findViewById(R.id.scrollView2);
        spinner=findViewById(R.id.search_progress2);

        fono=(EditText) findViewById(R.id.editText2);
        precio=(EditText) findViewById(R.id.editText_precio);
        direc=(EditText) findViewById(R.id.editText_dirr);
        nombre=(EditText) findViewById(R.id.editText);
        btn_wifi=(ToggleButton)findViewById(R.id.toggleButton_wifi);
        btn_agua=(ToggleButton)findViewById(R.id.toggleButton_agua);
        btn_animales=(ToggleButton)findViewById(R.id.toggleButton_mascota);
        btn_toilet=(ToggleButton)findViewById(R.id.toggleButton_toilet);
        btn_tv=(ToggleButton)findViewById(R.id.toggleButton_tv);
        btn_men=(ToggleButton)findViewById(R.id.toggleButton_man);
        btn_woman=(ToggleButton)findViewById(R.id.toggleButton_woman);

        btn_men.setOnCheckedChangeListener(this);
        btn_woman.setOnCheckedChangeListener(this);

        btn_woman.setChecked(true);
        btn_men.setChecked(true);

        Button add = (Button) findViewById(R.id.button2);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ready()) {
                    String gen;
                    String[] serv={"0","0","0","0","0"};
                    if (btn_men.isChecked() && btn_woman.isChecked()) {
                        gen = "3";
                    } else if (btn_men.isChecked() && !btn_woman.isChecked()) {
                        gen = "1";
                    } else {
                        gen = "2";
                    }
                    if (btn_wifi.isChecked())
                        serv[3] = "1";
                    if (btn_animales.isChecked())
                        serv[4] = "1";
                    if (btn_tv.isChecked())
                        serv[1] = "1";
                    if (btn_agua.isChecked())
                        serv[2] = "1";
                    if (btn_toilet.isChecked())
                        serv[0] = "1";

                    showProgress(true);

                    MyTaskParams par=new MyTaskParams(nombre.getText().toString(),
                            direc.getText().toString(),
                            email,
                            fono.getText().toString(),
                            String.valueOf(plat),
                            String.valueOf(plon),
                            precio.getText().toString(),
                            gen,
                            serv[0],serv[1],serv[2],serv[3],serv[4],
                            //"aaaa");
                            byteArray);

                    new CreateTask().execute(par);
                }
                else
                    Toast.makeText(register.this, "Debe llenar todos los campos con información válida.", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton mapa=(ImageButton) findViewById(R.id.imageButton_mapa);
        mapa.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent a=new Intent(register.this,MapsRegisterActivity.class);
                  a.putExtra("lat",plat);
                  a.putExtra("lon",plon);
                  startActivityForResult(a,1);

                  //Toast.makeText(register.this,a.getData().toString(),Toast.LENGTH_SHORT);
              }
          });

        ImageButton imagen=(ImageButton) findViewById(R.id.imageButton_imagen);
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                //Intent intent = new Intent();
                //intent.setType("image/*");
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(Intent.createChooser(intent, "Seleccionar imagen"), 1);
                startActivityForResult(intent,2);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestcode,int resultcode,Intent data){

        if (data!=null && resultcode==RESULT_OK && requestcode==2){

            imagenSca=openBitmap(data.getData());
            if (imagenSca==null) {
                Toast.makeText(register.this, "No se pudo cargar la imagen.", Toast.LENGTH_SHORT).show();
            }
            else {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imagenSca.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                //byteArray = stream.toByteArray();
                this.byteArray = Base64.encodeToString(stream.toByteArray(),Base64.DEFAULT);
                Toast.makeText(register.this, "Imagen cargada con éxito", Toast.LENGTH_SHORT).show();
            }
        }
        else if (data!=null && resultcode==RESULT_OK && requestcode==1){
            plat=data.getExtras().getDouble("lat");
            plon=data.getExtras().getDouble("lon");
        }
        else {
            Toast.makeText(register.this,"La acción no se pudo realizar con éxito",Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap scaleBitmap(Bitmap bitmapToScale, float newWidth, float newHeight)
    {
        if (bitmapToScale == null)
            return null;
        // get the original width and height
        int width = bitmapToScale.getWidth();
        int height = bitmapToScale.getHeight();
        Log.v("BITMAP",String.valueOf(width)+" "+String.valueOf(height));
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        //float m=Math.max(newWidth-width,newHeight-height);
        if (width-newWidth>height-newHeight){
            newHeight=(newWidth/width)*height;
        }
        else
            newWidth=(newHeight/height)*width;
        // resize the bit map
        matrix.postScale(newWidth / width, newHeight / height);

        // recreate the new Bitmap and set it back
        return Bitmap.createBitmap(bitmapToScale, 0, 0, bitmapToScale.getWidth(),
                bitmapToScale.getHeight(), matrix, true);
    }

    public Bitmap openBitmap(Uri _path)//String path)
    {
        Bitmap bitmap = null;
        try
        {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(_path));
            int originalWidth = bitmap.getWidth();
            int originalHeight = bitmap.getHeight();
            Point size=new Point();
            Display display = getWindowManager().getDefaultDisplay();
            display.getSize(size);

            int width =(size.x*9)/10;
            int height = (size.y*7)/10;
            Log.v("IMAGEN",String.valueOf(width)+" "+String.valueOf(height));
            if (originalWidth > width || originalHeight > height)
            {
                // Scale it
                bitmap = scaleBitmap(bitmap, width, height);
                Log.v("IMAGEN2",String.valueOf(bitmap.getWidth())+" "+String.valueOf(bitmap.getHeight()));
            }
        } catch (Exception e)
        {
            Log.w("IMAGEN", "Coundn't load a file");
            e.printStackTrace();
        }

        return bitmap;
    }

    public Boolean ready(){
        if (nombre.getText().toString().trim().length()==0 || precio.getText().toString().trim().length()==0 ||
                direc.getText().toString().trim().length()==0 || fono.getText().toString().trim().length()==0 ||
                nombre.getText().toString().trim().length()<6 || precio.getText().toString().trim().length()==0 ||
                direc.getText().toString().trim().length()<6 || fono.getText().toString().trim().length()<6 || this.byteArray.equals("")
                )
            return false;
        return true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!buttonView.isChecked() && buttonView.getId()==btn_men.getId())
            btn_woman.setChecked(true);
        else if(!buttonView.isChecked() && buttonView.getId()==btn_woman.getId())
            btn_men.setChecked(true);
    }

    /*
    Clase para insertar un cuarto
     */
    private class CreateTask extends AsyncTask<MyTaskParams, Void, Boolean> {

        public CreateTask() {

        }
        @Override
        protected Boolean doInBackground(MyTaskParams... params) {
            JSONObject json = new JSONObject();
            JSONObject jsonr;
            try {
                json.accumulate("nombre", params[0].nombre);
                json.accumulate("direc", params[0].direc);
                json.accumulate("email", params[0].email);
                json.accumulate("fono", params[0].fono);
                json.accumulate("coord0", params[0].coord0);
                json.accumulate("coord1", params[0].coord1);
                json.accumulate("precio", params[0].precio);
                json.accumulate("genero", params[0].genero);
                json.accumulate("serv0", params[0].serv0);
                json.accumulate("serv1", params[0].serv1);
                json.accumulate("serv2", params[0].serv2);
                json.accumulate("serv3", params[0].serv3);
                json.accumulate("serv4", params[0].serv4);
                json.accumulate("imagen", params[0].imagen);

                //Log.v("IMAGENNNN",params[0].imagen);

                jsonr=new JsonHttpHandler().postJSONfromUrl("http://10.0.2.2:8100/new/cuarto", json);
                //jsonr=new JsonHttpHandler().postJSONfromUrl("http://127.0.0.1:8100//new/cuarto", json);
                //Log.d("WARNINNNNG", json.toString());

                if (jsonr == null)
                {
                    Log.v("ERRRORRRRRR","json null");
                    return false;}

                if ((jsonr.getString("status")).equals("error"))
                {
                    Log.v("ERRRORRRRRR","en el servidor");
                    return false;}
                else{
                    return true;}

            }  catch (JSONException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //super.onPostExecute(result);
            //set a null el objeto de esta clase
            //crear=null;
            showProgress(false);
            if (success) {
                //finish();
                Toast.makeText(register.this, "Habitación registrada correctamente", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(register.this, "La habitación no se pudo registrar. Inténtelo nuevamente", Toast.LENGTH_SHORT).show();
            }
        }

    }
    /*
    Fin de la clase para insertar un cuarto
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            scroll.setVisibility(show ? View.GONE : View.VISIBLE);
            scroll.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    scroll.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            spinner.setVisibility(show ? View.VISIBLE : View.GONE);
            spinner.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    spinner.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            spinner.setVisibility(show ? View.VISIBLE : View.GONE);
            scroll.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private static class MyTaskParams {
        String nombre;
        String direc;
        String email;
        String fono;
        String coord0;
        String coord1;
        String precio;
        String genero;
        String serv0;
        String serv1;
        String serv2;
        String serv3;
        String serv4;
        //byte[] imagen;
        String imagen;


        MyTaskParams(String nombre,
                String direc,
                String email,
                String fono,
                String coord0,
                String coord1,
                String precio,
                String genero,
                String serv0,
                String serv1,
                String serv2,
                String serv3,
                String serv4,
                //byte[] imagen
                String imagen) {
            this.nombre=nombre;
            this.direc=direc;
            this.email=email;
            this.fono=fono;
            this.coord0=coord0;
            this.coord1=coord1;
            this.precio=precio;
            this.genero=genero;
            this.serv0=serv0;
            this.serv1=serv1;
            this.serv2=serv2;
            this.serv3=serv3;
            this.serv4=serv4;
            //this.imagen=imagen.clone();
            this.imagen=imagen;
        }
    }
}
