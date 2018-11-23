package procorp.applikobo.Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Benji on 28/05/2018.
 */

public class MaBaseSQLite extends SQLiteOpenHelper {


    private static final String TABLE_PROFIL = "table_profil";
    private static final String COL_IDPROFIL = "idProfil";
    private static final String COL_MAIL = "mail";
    private static final String COL_PSEUDO = "pseudo";

    private static final String CREATE_TABLE_PROFIL = "CREATE TABLE " + TABLE_PROFIL + " ( "
            + COL_IDPROFIL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_MAIL + " TEXT NOT NULL, "
            + COL_PSEUDO + " TEXT );";



    private Context context;

    public MaBaseSQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PROFIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFIL + ";");
        onCreate(db);
    }
}
