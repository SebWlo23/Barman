package com.example.post_yt;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{
    EditText ID, ID_zamowienia, ilosc, podaj_imie;
    TextView ID_butelki;
    Button SEND, Drink_Voda, Drink_Whiskey, dodaj_uzytkownika;
    TextView tw;
    RequestQueue requestQuene;

    int last_user_id, last_placed_order_id, last_order_id, wybrane_drink_id;

    String url = "https://sebwlo23.pythonanywhere.com/zlozone_zamowienia";
    String last_order_url = "https://sebwlo23.pythonanywhere.com/zamowienia/ostatnie";
    String last_user_url = "https://sebwlo23.pythonanywhere.com/uzytkownicy/ostatni";
    String last_placed_order_url = "https://sebwlo23.pythonanywhere.com/zlozone_zamowienia/ostatnie";
    String order_url = "https://sebwlo23.pythonanywhere.com/zamowienia";
    String users_url = "https://sebwlo23.pythonanywhere.com//uzytkownicy";



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SEND = findViewById(R.id.send_to_api);
        SEND.setOnClickListener(this);
        dodaj_uzytkownika = findViewById(R.id.dodaj_uzytkownika);
        dodaj_uzytkownika.setOnClickListener(this);
        Drink_Voda = findViewById(R.id.drink_voda);
        Drink_Voda.setOnClickListener(this);
        Drink_Whiskey = findViewById(R.id.drink_whiskey);
        Drink_Whiskey.setOnClickListener(this);
        ID_butelki = findViewById(R.id.ID_butelki);
        podaj_imie = findViewById(R.id.podaj_imie);
        ilosc = findViewById(R.id.ilosc);
        get_last_id();


    }

    public void showPopup(View view){
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.voda) {
            wybrane_drink_id = 1;
            Toast.makeText(this, "Wybrales wodke",Toast.LENGTH_SHORT).show();
            ID_butelki.setText("Wodka");

        } else if (item.getItemId()==R.id.whiskey) {
            wybrane_drink_id = 2;
            Toast.makeText(this,"Wybrales whiskey",Toast.LENGTH_SHORT).show();
            ID_butelki.setText("Whiskey");
        } else if (item.getItemId()==R.id.sok) {
            wybrane_drink_id = 3;
            Toast.makeText(this, "Wybrales sok", Toast.LENGTH_SHORT).show();
            ID_butelki.setText("Sok");
        } else if (item.getItemId()==R.id.cola) {
            wybrane_drink_id = 4;
            Toast.makeText(this, "Wybrales cole", Toast.LENGTH_SHORT).show();
            ID_butelki.setText("Cola");
        }

        return true;
    }

    private JSONObject get_data() {
        JSONObject data = new JSONObject();
        try {
            data.put("id", last_placed_order_id+1);
            data.put("butelka_id", wybrane_drink_id);
            data.put("zamowienie_id", last_order_id+1);
            data.put("ilosc", Float.parseFloat(ilosc.getText().toString()));
            data.put("czy_wykonane", 0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

   public void get_last_id(){
        JsonObjectRequest req1 = new JsonObjectRequest(Request.Method.GET, last_order_url, null,
                response -> {
                    try {
                            last_order_id = response.getInt("id");
                            Log.d(TAG, "Last_Order_ID: " + last_order_id);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, "Błąd podczas pobierania ID: " + error.getMessage()));
        requestQuene = Volley.newRequestQueue(MainActivity.this);
        requestQuene.add(req1);

       JsonObjectRequest req2 = new JsonObjectRequest(Request.Method.GET, last_placed_order_url, null,
               response -> {
                   try {
                       last_placed_order_id = response.getInt("id");
                       Log.d(TAG, "Last_Placed_Order_ID: " + last_placed_order_id);

                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               },
               error -> Log.e(TAG, "Błąd podczas pobierania ID: " + error.getMessage()));
       requestQuene = Volley.newRequestQueue(MainActivity.this);
       requestQuene.add(req2);

       JsonObjectRequest req3 = new JsonObjectRequest(Request.Method.GET, last_user_url, null,
               response -> {
                   try {
                       last_user_id = response.getInt("id");
                       Log.d(TAG, "Last_User_ID: " + last_user_id);

                   } catch (JSONException e) {
                       e.printStackTrace();
                   }
               },
               error -> Log.e(TAG, "Błąd podczas pobierania ID: " + error.getMessage()));
       requestQuene = Volley.newRequestQueue(MainActivity.this);
       requestQuene.add(req3);
    }

    private JSONObject drink_voda_voda() {
        JSONObject drink_voda_voda = new JSONObject();
        try {
            drink_voda_voda.put("id", last_placed_order_id+1);
            drink_voda_voda.put("butelka_id", 1);
            drink_voda_voda.put("zamowienie_id", last_order_id+1);
            drink_voda_voda.put("ilosc", 50);
            drink_voda_voda.put("czy_wykonane", 0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return drink_voda_voda;
    }

    private JSONObject drink_voda_sok() {
        JSONObject drink_voda_sok = new JSONObject();
        try {
            drink_voda_sok.put("id", last_placed_order_id+1);
            drink_voda_sok.put("butelka_id", 3);
            drink_voda_sok.put("zamowienie_id", last_order_id+1);
            drink_voda_sok.put("ilosc", 80);
            drink_voda_sok.put("czy_wykonane", 0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return drink_voda_sok;
    }

    private JSONObject drink_whiskey_whiskey() {
        JSONObject drink_whiskey_whiskey = new JSONObject();
        try {
            drink_whiskey_whiskey.put("id", last_placed_order_id+1);
            drink_whiskey_whiskey.put("butelka_id", 2);
            drink_whiskey_whiskey.put("zamowienie_id", last_order_id+1);
            drink_whiskey_whiskey.put("ilosc", 60);
            drink_whiskey_whiskey.put("czy_wykonane", 0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return drink_whiskey_whiskey;
    }

    private JSONObject drink_whiskey_cola() {
        JSONObject drink_whiskey_cola = new JSONObject();
        try {
            drink_whiskey_cola.put("id", last_placed_order_id+1);
            drink_whiskey_cola.put("butelka_id", 4);
            drink_whiskey_cola.put("zamowienie_id", last_order_id+1);
            drink_whiskey_cola.put("ilosc", 100);
            drink_whiskey_cola.put("czy_wykonane", 0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return drink_whiskey_cola;
    }
    private JSONObject new_user() {
        JSONObject nowy_uzytkownik = new JSONObject();
        try {
            nowy_uzytkownik.put("id", last_user_id+1);
            nowy_uzytkownik.put("imie", podaj_imie.getText().toString());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return nowy_uzytkownik;
    }

    private JSONObject new_order() {
        JSONObject new_order = new JSONObject();
        try {
            new_order.put("id", last_order_id+1);
            new_order.put("uzytkownicy_id", last_user_id);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return new_order;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send_to_api) {
            get_last_id();
            JsonObjectRequest request5 = new JsonObjectRequest(
                    Request.Method.POST,
                    order_url,
                    new_order(),
                    response -> Toast.makeText(MainActivity.this, "Succes", Toast.LENGTH_LONG).show(),
                    error -> Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show());

            requestQuene = Volley.newRequestQueue(MainActivity.this);
            requestQuene.add(request5);
            get_last_id();
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    get_data(),
                    response -> Toast.makeText(MainActivity.this, "Succes", Toast.LENGTH_LONG).show(),
                    error -> Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show());

            requestQuene = Volley.newRequestQueue(MainActivity.this);
            requestQuene.add(request);
            get_last_id();
        } else if (view.getId() == R.id.drink_voda) {
            get_last_id();
            JsonObjectRequest request6 = new JsonObjectRequest(
                    Request.Method.POST,
                    order_url,
                    new_order(),
                    response -> Toast.makeText(MainActivity.this, "Succes", Toast.LENGTH_LONG).show(),
                    error -> Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show());

            requestQuene = Volley.newRequestQueue(MainActivity.this);
            requestQuene.add(request6);
            get_last_id();
            JsonObjectRequest request1 = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    drink_voda_voda(),
                    response -> Toast.makeText(MainActivity.this, "Succes", Toast.LENGTH_LONG).show(),
                    error -> Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show());

            requestQuene = Volley.newRequestQueue(MainActivity.this);
            requestQuene.add(request1);
            get_last_id();
            JsonObjectRequest request2 = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    drink_voda_sok(),
                    response -> Toast.makeText(MainActivity.this, "Succes", Toast.LENGTH_LONG).show(),
                    error -> Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show());

            requestQuene.add(request2);
            get_last_id();
        } else if (view.getId()== R.id.dodaj_uzytkownika) {
            get_last_id();
            JsonObjectRequest request3 = new JsonObjectRequest(
                    Request.Method.POST,
                    users_url,
                    new_user(),
                    response -> Toast.makeText(MainActivity.this, "Succes", Toast.LENGTH_LONG).show(),
                    error -> Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show());

            requestQuene = Volley.newRequestQueue(MainActivity.this);
            requestQuene.add(request3);
            get_last_id();

        } else if (view.getId()==R.id.drink_whiskey) {
            get_last_id();
            JsonObjectRequest request10 = new JsonObjectRequest(
                    Request.Method.POST,
                    order_url,
                    new_order(),
                    response -> Toast.makeText(MainActivity.this, "Succes", Toast.LENGTH_LONG).show(),
                    error -> Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show());

            requestQuene = Volley.newRequestQueue(MainActivity.this);
            requestQuene.add(request10);
            get_last_id();
            JsonObjectRequest request11 = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    drink_whiskey_whiskey(),
                    response -> Toast.makeText(MainActivity.this, "Succes", Toast.LENGTH_LONG).show(),
                    error -> Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show());

            requestQuene = Volley.newRequestQueue(MainActivity.this);
            requestQuene.add(request11);
            get_last_id();
            JsonObjectRequest request12 = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    drink_whiskey_cola(),
                    response -> Toast.makeText(MainActivity.this, "Succes", Toast.LENGTH_LONG).show(),
                    error -> Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show());

            requestQuene.add(request12);
            get_last_id();
        }
    }
}



