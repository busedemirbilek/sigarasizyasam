package vt;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import com.example.sigarapp2.home;
public class DB {
    veritabani vts;
    SQLiteDatabase conn;
    Context c;
    public DB(Context c)
    {
        this.c=c;
        vts=new veritabani(c);
    }
    private void baglan()
    {
        try {
            conn=vts.getWritableDatabase();
        }catch (Exception ex)
        {
            Toast.makeText(c,"Bağlantı Başarısız!!",Toast.LENGTH_LONG).show();
        }

    }
    private void kapat()
    {
        try {
            conn.close();
        }catch (Exception ex)
        {
            Toast.makeText(c,"Bağlantı Kapatılmadı!!",Toast.LENGTH_LONG).show();
        }

    }
    public void  ekle(int gunadet,int psigarasay,double yilsay,double pfiyatbas,double pfiyatbirak,String pbirim,String tarih)
    {
        try {
            baglan();
            ContentValues par=new ContentValues();
            par.put("gunadet",gunadet);
            par.put("psigarasay",psigarasay);
            par.put("yilsay",yilsay);
            par.put("pfiyatbas",pfiyatbas);
            par.put("pfiyatbirak",pfiyatbirak);
            par.put("pbirim",pbirim);
            par.put("tarih",tarih);
            conn.insertOrThrow("bilgiler",null,par);
            kapat();
        }catch (Exception ex)
        {
            kapat();
            Toast.makeText(c,"Ekleme İşlemi Başarısız!!",Toast.LENGTH_LONG).show();
        }

    }
    public void  sil()
    {
        try {
            conn=vts.getWritableDatabase();
        }catch (Exception ex)
        {
            Toast.makeText(c,"Silme İşlemi Başarısız!!",Toast.LENGTH_LONG).show();
        }

    }
    public void  guncelle()
    {
        try {
            conn=vts.getWritableDatabase();
        }catch (Exception ex)
        {
            Toast.makeText(c,"Güncelleme İşlemi Başarısız!!",Toast.LENGTH_LONG).show();
        }

    }
    public boolean data_kontrol_icmebilgileri()
    {
        try {
            baglan();
            Cursor c=conn.query("bilgiler",new String[]{"gunadet","psigarasay","yilsay","pfiyatbas","pfiyatbirak","pbirim","tarih"},null,null,null,null,null);
            c.moveToPosition(-1);
            if(c.moveToNext())
            {
                home.gunadet=c.getInt(0);
                home.psigarasay=c.getInt(1);
                home.yilsay=c.getDouble(2);
                home.pfiyatbas=c.getDouble(3);
                home.pfiyatbirak=c.getDouble(4);
                home.pbirim=c.getString(5);
                home.birakilan_tarih=c.getString(6);
                c.close();
                kapat();
                return  false;
            }
            c.close();
            kapat();
        }catch (Exception ex)
        {
            Toast.makeText(c,"Veri Okuma İşlemi Başarısız!!",Toast.LENGTH_LONG).show();
            return  false;
        }
        return  true;
    }
}
