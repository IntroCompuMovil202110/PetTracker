package com.example.pettracker.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pettracker.Model.Veterinaria;
import com.example.pettracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class BuscarVeterinariaActivity extends AppCompatActivity {
    private JSONArray jsonArray;
    private String[] nomVeterinarias;

    LinearLayout titulo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veterinarias);

        consumeRestVolley();


    }
    public void consumeRestVolley(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://serviciosnotificacionesbackend-default-rtdb.firebaseio.com/pet.json";
        StringRequest req = new StringRequest(Request.Method.GET, url,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        String data = (String)response;

                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            jsonArray = jsonObject.getJSONArray("veterinarias");
                            initArray();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }},
                    new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError error){
                            Log.i("TAG", "Error handling rest invocation"+ error.getCause());
                        }
                    }
	    );
	    queue.add(req);
    }
    public void initArray() throws JSONException{
       try{
           nomVeterinarias = new String[jsonArray.length()];
           for(int i = 0; i<jsonArray.length();i++){
               JSONObject explrObject = jsonArray.getJSONObject(i);
               String dato = explrObject.getString("nombre").toLowerCase();
               nomVeterinarias[i] = convert(dato);
           }
           ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, nomVeterinarias);
           ListView listView= (ListView) findViewById(R.id.listaVeterinarias);
           listView.setAdapter(adapter);
           listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   try {
                       JSONObject js = jsonArray.getJSONObject(position);
                       Veterinaria veterinaria = new Veterinaria(js.getDouble("calificacion"), js.getString("direccion"), js.getString("horas"), js.getDouble("latitud"), js.getDouble("longitud"), js.getString("nombre"), js.getString("telefono"));
                       Intent intent = new Intent(getBaseContext(), VeterinariaActivity.class );
                       intent.putExtra("veterinaria", (Serializable) veterinaria);
                       startActivity(intent);
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               }
           });
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
    }

    static String convert(String str)
    {

        // Create a char array of given String
        char ch[] = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {

            // If first character of a word is found
            if (i == 0 && ch[i] != ' ' ||
                    ch[i] != ' ' && ch[i - 1] == ' ') {

                // If it is in lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {

                    // Convert into Upper-case
                    ch[i] = (char)(ch[i] - 'a' + 'A');
                }
            }

            // If apart from first character
            // Any one is in Upper-case
            else if (ch[i] >= 'A' && ch[i] <= 'Z')

                // Convert into Lower-Case
                ch[i] = (char)(ch[i] + 'a' - 'A');
        }

        // Convert the char array to equivalent String
        String st = new String(ch);
        return st;
    }
}
