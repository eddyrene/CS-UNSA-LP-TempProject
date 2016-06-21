package com.example.alquiler.alquilercom;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.alquiler.alquilercom.data.JsonHttpHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
    	/*
    Funcion para insertar un cuarto
     */
    //new CreateTask().execute(nameTV.getText().toString(), handleTV.getText().toString());
    /*
    Fin de la Funcion para insertar un cuarto
     */


    /*
    Clase para insertar un cuarto
     */
    private class CreateTask extends AsyncTask<String, Void, Boolean> {

        public CreateTask() {

        }
        @Override
        protected Boolean doInBackground(String... params) {
            JSONObject json = new JSONObject();
            JSONObject jsonr = null;
            try {

                json.accumulate("Distrito", params[0]);
                json.accumulate("Coord", params[1]);
                json.accumulate("Nombre", params[2]);
                json.accumulate("Servicios", params[3]);
                json.accumulate("Precio", params[4]);
                json.accumulate("Genero", params[5]);
                json.accumulate("Img", params[6]);

                jsonr=new JsonHttpHandler().postJSONfromUrl("http://myflaskapp-alquiler.rhcloud.com/new/cuarto", json);

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
            if (success) {
                finish();
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
}
