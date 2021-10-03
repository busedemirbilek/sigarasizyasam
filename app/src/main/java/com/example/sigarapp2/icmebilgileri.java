package com.example.sigarapp2;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import vt.DB;
public class icmebilgileri extends AppCompatActivity implements View.OnClickListener{
    String[] aylar={"Ocak","Şubat","Mart","Nisan","Mayıs","Haziran","Temmuz","Ağustos","Eylül","Ekim","Kasım","Aralık"};
    Date date;//Şuan ki tarihi almak için
    DatePickerDialog dialog;
    Button[] btn;
    int[] btn_id={R.id.dialog,R.id.kaydet};
    int ay,yil,gun;
    DB db;
    EditText[] et;
    Spinner sp;
    ArrayList<String> list;
    ArrayAdapter<String> adp;
    int[] et_id={R.id.sadet,R.id.psigarasay,R.id.psigarayil,R.id.pfiyatbas,R.id.pfiyatson};
    int[] secilen_tarih;
    String saat;
    SimpleDateFormat frm_saat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icmebilgileri);
        btn=new Button[btn_id.length];
        et=new EditText[5];
        for(int i=0;i<et.length;i++)
        {
            et[i]=(EditText)findViewById(et_id[i]);
            if(i<btn_id.length) {
                btn[i] = (Button) findViewById(btn_id[i]);
                btn[i].setOnClickListener(this);
            }
        }
        list=new ArrayList<String>();
        sp=(Spinner)findViewById(R.id.pbirim);
        adp=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,list);
        sp.setAdapter(adp);
        adp.add("Türk Lirası - ₺");
        adp.add("Dolar - $");
        adp.add("Euro - €");
        adp.add("Sterlin - £");
        date=new Date();//Ocak 1 den baslar
        SimpleDateFormat frm_yil=new SimpleDateFormat("yyyy");
        SimpleDateFormat frm_ay=new SimpleDateFormat("MM");
        SimpleDateFormat frm_gun=new SimpleDateFormat("dd");
        frm_saat=new SimpleDateFormat("HH");
        saat=frm_saat.format(date);
        yil=Integer.parseInt(frm_yil.format(date));
        ay=Integer.parseInt(frm_ay.format(date));
        gun=Integer.parseInt(frm_gun.format(date));
        secilen_tarih=new int[3];
        secilen_tarih[0]=yil;
        secilen_tarih[1]=ay;
        secilen_tarih[2]=gun;
        String frmt=gun+" "+aylar[ay-1]+" "+yil;
        btn[0].setText(frmt);
        db=new DB(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.dialog:dilaogcreate();break;
            case R.id.kaydet:kaydet();break;
        }
    }
    private void dilaogcreate() {
        //Datepicker' da ocak 0 dan baslar
        dialog= new DatePickerDialog(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                btn[0].setText(" "+dayOfMonth+" "+aylar[month]+" "+year);
                secilen_tarih[0]=year;
                secilen_tarih[1]=(month+1);//0 dan basladıgı için
                secilen_tarih[2]=dayOfMonth;
                saat=frm_saat.format(date);
            }
        },yil,(ay-1),gun);
        dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Tamam",dialog);
        dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE,"Iptal",dialog);
        dialog.setTitle("Tarih");
        dialog.setCancelable(false);
        dialog.show();
    }
    public boolean dogrula(EditText[] et1)
    {
        int sayac=0;
        for(int i=0;i<et1.length;i++)
            if(et1[i].getText().length()>0)
                sayac++;
        if(sayac==et1.length)
            return  true;
        else
            return false;
    }
    private void kaydet() {
        int secilen=sp.getSelectedItemPosition();
        if(dogrula(et)&&secilen>-1)
        {   try {
            int gunadet = Integer.parseInt(et[0].getText() + "");
            int psigarasay = Integer.parseInt(et[1].getText() + "");
            double yilsay = Double.parseDouble(et[2].getText() + "");
            double pfiyatbas = Double.parseDouble(et[3].getText() + "");
            double pfiyatbirak = Double.parseDouble(et[4].getText() + "");
            String pbirim = adp.getItem(secilen);

            if ((secilen_tarih[0] < yil)|| (secilen_tarih[0]==yil&&secilen_tarih[1] <= ay && secilen_tarih[2] <= gun))
            {
                String tarih=""+secilen_tarih[0];
                if(secilen_tarih[1]<10)
                    tarih+="-"+"0"+secilen_tarih[1];
                else
                    tarih+="-"+secilen_tarih[1];
                if(secilen_tarih[2]<10)
                    tarih+="-"+"0"+secilen_tarih[2];
                else
                    tarih+="-"+secilen_tarih[2];
                tarih+="-"+saat;
                db.ekle(gunadet, psigarasay, yilsay, pfiyatbas, pfiyatbirak, pbirim,tarih);
                for (int i=0;i<et.length;i++)
                    et[i].setText("");
                db.data_kontrol_icmebilgileri();
                finish();
            }
            else
                Toast.makeText(this, "İleri tarih Seçilemez!!!!", Toast.LENGTH_LONG).show();
        }
        catch (Exception ex)
        {
            Toast.makeText(this,"Alanları Doğru giriniz!",Toast.LENGTH_LONG).show();
        }
        }
        else
            Toast.makeText(this,"Alanlar boş geçilemez!",Toast.LENGTH_LONG).show();
    }
    @Override
    public void onBackPressed() {
        //geri tuşu override edildi hiç bir sey yapmıyor suan
    }
}
