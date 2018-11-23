package procorp.applikobo.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ProfilManager {


    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "profil.db";

    private static final String TABLE_PROFIL = "table_profil";

    private static final String COL_IDPROFIL = "idProfil";
    private static final int NUM_COL_IDPROFIL = 0;

    private static final String COL_MAIL = "mail";
    private static final int NUM_COL_MAIL = 1;

    private static final String COL_PSEUDO = "pseudo";
    private static final int NUM_COL_PSEUDO = 2;

    private SQLiteDatabase bdd;

    private MaBaseSQLite maBaseSQLite;


    public ProfilManager(Context context){
        maBaseSQLite = new MaBaseSQLite(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open(){
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        bdd.close();
    }


    public long insertProfil(Profil profil){
        ContentValues values = new ContentValues();
        values.put(COL_MAIL, profil.getMail());
        values.put(COL_PSEUDO, profil.getPseudo());
        return bdd.insert(TABLE_PROFIL, null, values);
    }

    public int updateProfil(Profil profil){
        ContentValues values = new ContentValues();
        values.put(COL_MAIL, profil.getMail());
        values.put(COL_PSEUDO, profil.getPseudo());
        return bdd.update(TABLE_PROFIL, values, COL_IDPROFIL  + " = " + profil.getId(), null);
    }

    public void removeProfilById(int id){
        bdd.delete(TABLE_PROFIL, COL_IDPROFIL + " = " +id, null);
    }


    public Profil getProfilById(int id){
        //Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
        Cursor c = bdd.query(TABLE_PROFIL, null, COL_IDPROFIL + " = " + id, null, null, null, null);
        return cursorToProfil(c);
    }

    public Profil getProfilByMail(String mail){
        //Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
        Cursor c = bdd.query(TABLE_PROFIL, null, COL_MAIL + " LIKE \"" + mail + "\"", null, null, null, null);
        return cursorToProfil(c);
    }


    private Profil cursorToProfil(Cursor c){
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Profil profil = new Profil();
        profil.setId(c.getInt(NUM_COL_IDPROFIL));
        profil.setMail(c.getString(NUM_COL_MAIL));
        profil.setPseudo(c.getString(NUM_COL_PSEUDO));

        c.close();
        return profil;
    }

    public int nbreProfil(){
        Cursor curseur = bdd.query(TABLE_PROFIL, null, null, null, null, null, null);
        return curseur.getCount();
    }

    public Cursor getAllProfil(){
        Cursor c = bdd.query(TABLE_PROFIL, null, null, null, null, null, null);
        return c;
    }
}
