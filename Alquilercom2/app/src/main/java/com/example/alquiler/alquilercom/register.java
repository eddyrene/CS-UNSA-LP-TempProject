package com.example.alquiler.alquilercom;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.alquiler.alquilercom.data.JsonHttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class register extends AppCompatActivity {

    ToggleButton btn_agua, btn_animales, btn_men, btn_toilet, btn_tv, btn_wifi, btn_woman;
    EditText nombre, precio, direc, fono;
    Spinner meses;
    private  View scroll;
    private View spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        scroll=findViewById(R.id.scrollView2);
        spinner=findViewById(R.id.search_progress2);

        meses=(Spinner) findViewById(R.id.spinner2);
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

                    new CreateTask().execute(
                            nombre.getText().toString().trim(),
                            direc.getText().toString().trim(),
                            getIntent().getExtras().getString("email"),
                            fono.getText().toString().trim(),
                            String.valueOf(getIntent().getExtras().getDouble("lat")),
                            String.valueOf(getIntent().getExtras().getDouble("lon")),
                            precio.getText().toString().trim(),
                            gen,
                            serv[0],serv[1],serv[2],serv[3],serv[4]);
                }
                else
                    Toast.makeText(register.this, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Boolean ready(){
        if (nombre.getText().toString().trim().length()==0 || precio.getText().toString().trim().length()==0 ||
                direc.getText().toString().trim().length()==0 || fono.getText().toString().trim().length()==0)
            return false;
        return true;
    }


    /*
    Clase para insertar un cuarto
     */
    private class CreateTask extends AsyncTask<String, Void, Boolean> {

        public CreateTask() {

        }
        @Override
        protected Boolean doInBackground(String... params) {
            JSONObject json = new JSONObject();
            JSONObject jsonr;
            try {
                json.accumulate("nombre", params[0]);
                json.accumulate("direc", params[1]);
                json.accumulate("email", params[2]);
                json.accumulate("fono", params[3]);
                json.accumulate("coord0", params[4]);
                json.accumulate("coord1", params[5]);
                json.accumulate("precio", params[6]);
                json.accumulate("genero", params[7]);
                json.accumulate("serv0", params[8]);
                json.accumulate("serv1", params[9]);
                json.accumulate("serv2", params[10]);
                json.accumulate("serv3", params[11]);
                json.accumulate("serv4", params[12]);

                jsonr=new JsonHttpHandler().postJSONfromUrl("https://myflaskapp2-alquiler.rhcloud.com/new/cuarto", json);

                Log.d("WARNINNNNG", json.toString());

                if (jsonr == null){return false;}

                if ((jsonr.getString("status")).equals("error"))
                {
                    return false;}
                else{
                    return true;}

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (JSONException e) {
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
}
