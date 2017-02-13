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
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Dua extends AppCompatActivity implements ListView.OnItemClickListener {
    String kec;

    private ListView listView;
    private String JSON_STRING;
    String id ;
    String empId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dua);
        Intent intent = getIntent();
        kec = intent.getStringExtra("kecamatan");
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        getJSON();
    }
    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Dua.this,"Mengambil Data","Loading...",false,false);
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
                String s = rh.sendGetRequestParam("http://rapindopjk3.com/getKelurahan.php?id=",kec);
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
                String gampong = jo.getString(config.TAG_GAMPONG);
                String tps = jo.getString(config.TAG_TPS);
                String kode =jo.getString(config.TAG_KODE);

                HashMap<String,String> employees = new HashMap<>();
                employees.put(config.TAG_ID,id);
                employees.put(config.TAG_GAMPONG,gampong);
                employees.put(config.TAG_TPS,tps);
                employees.put(config.TAG_KODE,kode);
                list.add(employees);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new SimpleAdapter(
                Dua.this, list, R.layout.filter_kelurahan,
                new String[]{config.TAG_GAMPONG,config.TAG_TPS},
                new int[]{R.id.kelurahan, R.id.tps});
        listView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, Tiga.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String kel = map.get(config.TAG_GAMPONG).toString();
        String code = map.get(config.TAG_KODE).toString();
        intent.putExtra("KECAMATAN",kec);
        intent.putExtra("KELURAHAN",kel);
        intent.putExtra("KODE",code);
        startActivity(intent);



    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler() {
            public void postDelayed(Runnable runnable, int i) {
            }
            @Override
            public void publish(LogRecord record) {
            }
            @Override
            public void flush() {
            }
            @Override
            public void close() throws SecurityException {
                System.exit(0);
            }
        }.postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
