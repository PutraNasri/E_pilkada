package com.example.byonge.e_pilkada;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Empat extends AppCompatActivity {
    String kec;
    String kel;
    String kode;
    String idd;
    String tps;
    String pemilih;
    String pemula;
    String pass;

    private TextView textview2;
    private  TextView textview3;
    private  TextView textview4;
    private  TextView textview5;
    private  TextView textview6;
    private  TextView textsatu;
    private  TextView textdua;
    private  TextView texttiga;
    private  TextView textdatarusak;
    private  TextView textjumlahhadir;
    private  TextView textjumlahkertas;
    private  TextView textcode;
    private ListView listView;
    private String JSON_STRING;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empat);
        Intent intent = getIntent();
        kec = intent.getStringExtra("KECAMATAN");
        kel = intent.getStringExtra("KELURAHAN");
        kode = intent.getStringExtra("KODE");
        idd = intent.getStringExtra("ID");
        tps = intent.getStringExtra("TPS");
        pemilih = intent.getStringExtra("PEMILIH");
        pemula = intent.getStringExtra("PEMULA");
        pass = intent.getStringExtra("PASSWORD");

        textview2=(TextView) findViewById(R.id.textView10) ;
        textview3=(TextView) findViewById(R.id.text2) ;
        textview4=(TextView) findViewById(R.id.text3) ;
        textview5=(TextView) findViewById(R.id.textView9) ;
        textview6=(TextView) findViewById(R.id.textViewpemula) ;
        textcode=(TextView) findViewById(R.id.editTextcode) ;
        textsatu=(TextView) findViewById(R.id.editTextno1) ;
        textdua=(TextView) findViewById(R.id.editTextno2) ;
        texttiga=(TextView) findViewById(R.id.editTextno3) ;
        textdatarusak=(TextView) findViewById(R.id.editTextrusak) ;
        textjumlahhadir=(TextView) findViewById(R.id.editTexthadir) ;
        textjumlahkertas=(TextView) findViewById(R.id.editTextkertas) ;

        ivImage = (ImageView) findViewById(R.id.ivImage1);


        textview2.setText(" Kec. "+kec);
        textview3.setText("Kel. "+kel);
       // textview4.setText(kode);
        textview5.setText(pemilih);
        textview4.setText(tps);
        textview6.setText(pemula);
        ivImage.setImageResource(R.drawable.camera);
    }
    public void send(View v){

        if(TextUtils.isEmpty(textjumlahhadir.getText().toString())) {
            textjumlahhadir.setError("Tidak boleh kosong");
            return;
        }
        else if(TextUtils.isEmpty(textjumlahkertas.getText().toString())) {
            textjumlahkertas.setError("Tidak boleh kosong");
            return;
        }
        else if(TextUtils.isEmpty(textsatu.getText().toString())) {
            textsatu.setError("Tidak boleh kosong");
            return;
        }
        else if(TextUtils.isEmpty(textdua.getText().toString())) {
            textdua.setError("Tidak boleh kosong");
            return;
        }
        else if(TextUtils.isEmpty(texttiga.getText().toString())) {
            texttiga.setError("Tidak boleh kosong");
            return;
        }
        else if(TextUtils.isEmpty(textdatarusak.getText().toString())) {
            textdatarusak.setError("Tidak boleh kosong");
            return;
        }
        else {cek();
        }
    }

    public void cek(){
        if (textcode.getText().toString().equals(pass)) {
            addEmployee();
        }
        else{
            AlertDialog.Builder a_builder = new AlertDialog.Builder(Empat.this);
            a_builder.setMessage("kode verifikasi salah")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = a_builder.create();
            alert.setTitle("Info");
            alert.show();
        }
    }

    public void foto1(View v){
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo","Choose from Library",
                "Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Empat.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(Empat.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bm);
    }
    private void addEmployee() {
        //proses pengambilan string dari variabel
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) ivImage.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] fotoo = baos.toByteArray();


        final String id = idd.toString().trim();
        final String satu = textsatu.getText().toString().trim();
        final String dua = textdua.getText().toString().trim();
        final String tiga = texttiga.getText().toString().trim();
        final String dataRusak = textdatarusak.getText().toString().trim();
        final String jumlahHadir = textjumlahhadir.getText().toString().trim();
        final String jumlahKertas = textjumlahkertas.getText().toString().trim();
        final String foto = Base64.encodeToString(fotoo, Base64.DEFAULT);

        class AddEmployee extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Empat.this, "Mengirim Data ...", "HARAP TUNGGU...", false, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(Empat.this, s, Toast.LENGTH_LONG).show();

                                Intent hasilIntent = new Intent(Empat.this, Utama.class);
                                startActivity(hasilIntent);
                                finish();

            }
            @Override
            protected String doInBackground(Void... v) {

                HashMap<String, String> params = new HashMap<>();
                params.put(config.KEY_EMP_ID, id);
                params.put(config.KEY_EMP_SATU, satu);
                params.put(config.KEY_EMP_DUA, dua);
                params.put(config.KEY_EMP_TIGA, tiga);
                params.put(config.KEY_EMP_DATARUSAK, dataRusak);
                params.put(config.KEY_EMP_JUMLAHHADIR, jumlahHadir);
                params.put(config.KEY_EMP_JUMLAHKERTAS, jumlahKertas);
                params.put(config.KEY_EMP_FOTO, foto);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest("http://rapindopjk3.com/addsuara.php", params);
                return res;
            }
        }
        AddEmployee ae = new AddEmployee();
        ae.execute();


    }
}
