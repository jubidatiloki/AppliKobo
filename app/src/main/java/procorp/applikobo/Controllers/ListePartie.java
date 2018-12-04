package procorp.applikobo.Controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import procorp.applikobo.Models.Manche;
import procorp.applikobo.Models.Partie;
import procorp.applikobo.Models.Score;
import procorp.applikobo.R;
import procorp.applikobo.Utils.OnGetDataListener;
import procorp.applikobo.Views.PartieAdapter;
import procorp.applikobo.Views.ScoreAdapter;

public class ListePartie extends AppCompatActivity {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private ListView listView;
    private ImageView imgBack;

    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public final DatabaseReference parties = database.getReference().child("parties");
    private Partie partie;

    private ArrayList<Partie> listParties = new ArrayList<>();
    private PartieAdapter partieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_partie);

        listView = findViewById(R.id.listPartie);
        imgBack = findViewById(R.id.imgBack);

        getSupportActionBar().setTitle(user.getEmail());

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListePartie.this, MainActivity.class));
            }
        });

        partieAdapter = new PartieAdapter(getApplicationContext(), listParties);
        listView.setAdapter(partieAdapter);
        getPartie();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListePartie.this, DetailPartie.class);
                intent.putExtra("nomPartie", listParties.get(position).getNom());
                startActivity(intent);
            }
        });



    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.nav_nouvelle_partie){
            startActivity(new Intent(ListePartie.this, CreationPartie.class));
        }else if (id == R.id.nav_parametres) {
            Toast.makeText(this, "Aucun paramètre pour le moment", Toast.LENGTH_LONG).show();
            //getFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentProfilModifier()).commit();
        }else if(id == R.id.nav_deconnexion){
            deconnexion();

        }
        return super.onOptionsItemSelected(item);
    }

    private void deconnexion() {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.raw_dialog, null, false);
        TextView tvTitle = view.findViewById(R.id.titleDialog);
        tvTitle.setText("Déconnexion");
        TextView tvMsg = view.findViewById(R.id.messageDialog);
        tvMsg.setText("Voulez vous vous déconnecter ?");

        final AlertDialog alert = new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("Se déconnecter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        auth.signOut();
                        startActivity(new Intent(ListePartie.this, Connexion.class));
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        alert.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.green));
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.white));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.red));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.white));
                alert.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.design_dialog));

            }
        });
        alert.show();
    }

    public void readData(DatabaseReference ref, final OnGetDataListener listener){
        listener.onStart();
        ref.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getPartie(){
        final String[] nomPartie = {""};
        final int[] nbJoueurs = {0};
        final int[] nbManches = {0};
        readData(parties, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                for (DataSnapshot parties : dataSnapshot.getChildren()) {
                    nomPartie[0] = parties.getKey();
                    for (DataSnapshot childPartie : parties.getChildren()) {
                        if (childPartie.getKey().equals("joueurs")) {
                            for (DataSnapshot joueurs : childPartie.getChildren()) {
                                nbJoueurs[0]++;
                                Log.e(joueurs.getKey(), joueurs.getValue().toString());
                            }
                        }
                        if (childPartie.getKey().equals("manches")) {
                            for (DataSnapshot manches : childPartie.getChildren()) {
                                nbManches[0]++;
                            }
                        }
                    }
                    partie = new Partie(nomPartie[0], nbManches[0], nbJoueurs[0]);
                    nomPartie[0] = "";
                    nbJoueurs[0] = 0;
                    nbManches[0] = 0;;
                    Log.e("partie", partie.toString());
                    listParties.add(partie);
                }
                for(int i = 0; i < listParties.size(); i++){
                    Log.e("listParties", listParties.get(i).toString());
                }
                if(listParties.size() == 0)
                    findViewById(R.id.tvVide).setVisibility(View.VISIBLE);
                else
                    findViewById(R.id.tvVide).setVisibility(View.GONE);
                partieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });
    }
}
