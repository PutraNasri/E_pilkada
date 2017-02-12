package com.example.byonge.e_pilkada;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Tiga extends AppCompatActivity implements ListView.OnItemClickListener {
    String kec;
    String kel;
    String kode;

    private  TextView textview2;
    private  TextView textview3;

    private ListView listView;
    private String JSON_STRING;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiga);
        Intent intent = getIntent();
        kec = intent.getStringExtra("KECAMATAN");
        kel = intent.getStringExtra("KELURAHAN");
        kode = intent.getStringExtra("KODE");
        textview2=(TextView) findViewById(R.id.textView2) ;
        textview3=(TextView) findViewById(R.id.textView3) ;
        listView = (ListView) findViewById(R.id.listtps);
        textview2.setText("Kec. "+kec);
        textview3.setText("Kel. "+kel);
        listView.setOnItemClickListener(this);
        getJSON();
    }
    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Tiga.this,"Mengambil Data","Loading...",false,false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showEmployee();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam("http://rapindopjk3.com/getTPS.php?kode=",kode);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }
    private void showEmployee(){

        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(config.TAG_JSON_ARRAY);
            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(config.TAG_ID);
                String kecamatan =jo.getString(config.TAG_KECAMATAN);
                String gampong = jo.getString(config.TAG_GAMPONG);
                String tps = jo.getString(config.TAG_TPS);
                String kode = jo.getString(config.TAG_KODE);
                String totalPemilih = jo.getString(config.TAG_TOTALPEMILIH);
                String totalPemula =jo.getString(config.TAG_TOTALPEMULA);
                String password=jo.getString(config.TAG_PASSWORD);

                HashMap<String,String> employees = new HashMap<>();
                employees.put(config.TAG_ID,id);
                employees.put(config.TAG_KECAMATAN,kecamatan);
                employees.put(config.TAG_GAMPONG,gampong);
                employees.put(config.TAG_TPS,tps);
                employees.put(config.TAG_KODE,kode);
                employees.put(config.TAG_TOTALPEMILIH,totalPemilih);
                employees.put(config.TAG_TOTALPEMULA,totalPemula);
                employees.put(config.TAG_PASSWORD,password);


                list.add(employees);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                Tiga.this, list, R.layout.filter_tps,
                new String[]{config.TAG_TPS},
                new int[]{R.id.tps});
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, Empat.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String kel = map.get(config.TAG_GAMPONG).toString();
        String code = map.get(config.TAG_KODE).toString();
        String idd = map.get(config.TAG_ID).toString();
        String tps = map.get(config.TAG_TPS).toString();
        String pemilih = map.get(config.TAG_TOTALPEMILIH).toString();
        String pemula = map.get(config.TAG_TOTALPEMULA).toString();
        String pass = map.get(config.TAG_PASSWORD).toString();
        intent.putExtra("KECAMATAN",kec);
        intent.putExtra("KELURAHAN",kel);
        intent.putExtra("KODE",code);
        intent.putExtra("ID",idd);
        intent.putExtra("TPS",tps);
        intent.putExtra("PEMILIH",pemilih);
        intent.putExtra("PEMULA",pemula);
        intent.putExtra("PASSWORD",pass);
        startActivity(intent);
    }
}
