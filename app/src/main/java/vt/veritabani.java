package vt;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class veritabani extends SQLiteOpenHelper {

    public static String dbname="sigaradb.db";
    public static int version=1;

    public veritabani(Context c)
    {
        super(c,dbname,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table bilgiler(id integer primary key autoincrement,gunadet integer not null,psigarasay integer not null,yilsay real not null,pfiyatbas real not null,pfiyatbirak real not null,pbirim text not null,tarih text not null)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="drop table if exists bilgiler";
        db.execSQL(sql);
        onCreate(db);
    }
}
