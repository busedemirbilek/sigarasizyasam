package com.example.sigarapp2;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import vt.DB;
public class home extends Fragment {
    @Nullable
    DB db;
    int[] text_id={R.id.gecen_zaman1,R.id.gecen_zaman2,R.id.gecen_zaman3,R.id.kazanilan_para,R.id.kazandirilan_agac,R.id.icilmeyen_sigara};
    int[] textkazanilan_id={R.id.ka_hayat1,R.id.ka_hayat2,R.id.ka_hayat3,R.id.ka_hayat4};
    int[] textkaybedilen_id={R.id.ictigin_sigara,R.id.kaybedilen_para,R.id.kaybedilen_hayat};
    TextView[] text;
    TextView[] text_kazanilan;
    TextView[] text_kaybedilen;
    public static int gunadet;
    public static int psigarasay;
    public static double yilsay;
    public static double pfiyatbas;
    public static double pfiyatbirak;
    public static String pbirim;
    public static String birakilan_tarih="";//format yil/ay/gün
    View rootView;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.homescreen, container, false);
        init();
        db=new DB(getActivity());
        if(db.data_kontrol_icmebilgileri()) {//eğer veri varsa false dönecek yoksa true dönecek
            Intent frm = new Intent(getActivity(), icmebilgileri.class);
            startActivity(frm);
        }
        text_hesapla();
        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        text_hesapla();
    }
    public void init() {
        text_kazanilan = new TextView[textkazanilan_id.length];
        text = new TextView[text_id.length];
        text_kaybedilen=new TextView[textkaybedilen_id.length];
        for (int i = 0; i < text_id.length; i++) {
            text[i] = (TextView) rootView.findViewById(text_id[i]);
            if(i<text_kazanilan.length)
                text_kazanilan[i]=(TextView)rootView.findViewById(textkazanilan_id[i]);
            if(i<text_kaybedilen.length)
                text_kaybedilen[i]=(TextView)rootView.findViewById(textkaybedilen_id[i]);
        }
    }
    public int[] tarih_hesapla(String tarih2){//Hedef tarih
        Date hedef_tarih=new Date();
        SimpleDateFormat hedefformat=new SimpleDateFormat("yyyy-MM-dd-HH");
        String tarih1=hedefformat.format(hedef_tarih);
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH", Locale.US);
        try {
            cal.setTime(sdf.parse(tarih1));
            cal2.setTime(sdf.parse(tarih2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date d1 = new Date(cal.getTimeInMillis());
        Date d2 = new Date(cal2.getTimeInMillis());

        long diff = d1.getTime()- d2.getTime() ;
        long h=diff / (1000*60*60);
        long d=h/24;
        long m=d/30;
        d=d%30;
        h-=(d*24+((m*30)*24));
        return new int[]{(int)m,(int)d,(int)h};
    }
    public void text_hesapla()
    {
        if(birakilan_tarih!="")
        {
            int[] degerler=tarih_hesapla(birakilan_tarih);
            text[0].setText(" "+degerler[0]+"\n"+"AY");
            text[1].setText("  "+degerler[1]+"\n"+"GÜN");
            text[2].setText("   "+degerler[2]+"\n"+"SAAT");
            int biraktigi_gunsayisi=(degerler[0]*30)+degerler[1];
            double ortfiyat=(pfiyatbas+pfiyatbirak)/2;
            double bir_sigaraf=ortfiyat/psigarasay;
            double kazananilan_para=(bir_sigaraf)*gunadet*biraktigi_gunsayisi;
            int kazandirilan_agac=((gunadet*biraktigi_gunsayisi)/psigarasay);
            int icilmeyen_sigara=gunadet*biraktigi_gunsayisi;
            double ictigi_sigara=(yilsay*12*30)*gunadet;
            double para_harcadin=ictigi_sigara*bir_sigaraf;
            //bir tane sigara ömrü 11 dk kısaltıyor
            long kazanilan_dk=icilmeyen_sigara*11;
            long saat=(kazanilan_dk/60);
            int gun=(int)(saat/24);
            int ay=gun/30;
            int  yil=ay/12;
            gun=gun%30;
            saat-=(gun*24+((ay*30)*24));
            text_kazanilan[0].setText(yil+"\n"+"YIL");
            text_kazanilan[1].setText(ay+"\n"+"AY");
            text_kazanilan[2].setText(gun+"\n"+"GÜN");
            text_kazanilan[3].setText(saat+"\n"+"SAAT");

            text[3].setText(virgulkaydir(kazananilan_para)+"");
            text[4].setText(kazandirilan_agac+"");
            text[5].setText(icilmeyen_sigara+"");
            //Kaybedilen yil,ay,gun,saat
            long kaybedilen_dk= (long) (ictigi_sigara*11);
            long ksaat=(kaybedilen_dk/60);
            int kgun=(int)(ksaat/24);
            int kay=kgun/30;
            kgun=kgun%30;
            ksaat-=(kgun*24+((kay*30)*24));

            text_kaybedilen[0].setText(virgulkaydir(ictigi_sigara)+" Sigara İçtin");
            text_kaybedilen[1].setText(virgulkaydir(para_harcadin)+" "+pbirim+" Para Harcadın");
            text_kaybedilen[2].setText(kay+"A"+" "+kgun+"G"+" "+ksaat+"S"+"\n Hayatından Kaybettin");
        }
    }
    public double virgulkaydir(double sayi)
    {
        int virgulkaydir=(int)(sayi*100);
        sayi=virgulkaydir/100.0;
        return sayi;
    }
}

