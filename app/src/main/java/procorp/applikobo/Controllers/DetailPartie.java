package procorp.applikobo.Controllers;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import procorp.applikobo.Models.Manche;
import procorp.applikobo.Models.Score;
import procorp.applikobo.R;
import procorp.applikobo.Utils.OnGetDataListener;
import procorp.applikobo.Views.ScoreAdapter;


public class DetailPartie extends AppCompatActivity {

    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public final DatabaseReference parties = database.getReference().child("parties");
    public DatabaseReference manches;
    private String nomPartie;
    private ArrayList<Manche> listManches = new ArrayList<>();
    private ArrayList<Score> listScores = new ArrayList<>();
    private ArrayList<String> listJoueurs = new ArrayList<>();

    int mancheActuelle = 0;

    private ArrayList<String> listLabelManches = new ArrayList<>();

    TextView tvNomPartie, tvManche;
    LinearLayout btnJoueurs, frameJoueurs;
    RelativeLayout frameScore;
    Button btnCreerManche;
    ImageView imgBack;
    ListView listViewManche, listViewScore;

    ArrayAdapter<String> adapterManche;
    ScoreAdapter scoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_partie);

        tvNomPartie = findViewById(R.id.tvNomPartie);
        tvManche = findViewById(R.id.tvManche);

        btnJoueurs = findViewById(R.id.btnJoueurs);
        frameJoueurs = findViewById(R.id.frameJoueurs);

        frameScore = findViewById(R.id.frameScore);

        imgBack = findViewById(R.id.imgBack);

        btnCreerManche = findViewById(R.id.btnCreerManche);

        listViewManche = findViewById(R.id.listView);
        listViewScore = findViewById(R.id.listViewScore);

        nomPartie = getIntent().getExtras().getString("nomPartie");

        manches = database.getReference().child("parties").child(nomPartie).child("manches");

        tvNomPartie.setText(nomPartie);

        btnJoueurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (frameJoueurs.getVisibility() == View.VISIBLE) {
                    frameJoueurs.setVisibility(View.GONE);
                } else {
                    frameJoueurs.setVisibility(View.VISIBLE);
                }
            }
        });

        getPartie();

        adapterManche = new ArrayAdapter<String>(DetailPartie.this,android.R.layout.simple_list_item_1, listLabelManches);
        listViewManche.setAdapter(adapterManche);

        listViewManche.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                scoreAdapter = new ScoreAdapter(getApplicationContext(), listManches.get(position).getScores());
                listViewScore.setAdapter(scoreAdapter);
                mancheActuelle = listManches.get(position).getNumManche();
                tvManche.setText("Manche " + listManches.get(position).getNumManche());
                frameScore.setVisibility(View.VISIBLE);
                imgBack.setVisibility(View.VISIBLE);
                listViewManche.setVisibility(View.GONE);
            }
        });

        listViewScore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //dialog d'ajout de pts pour le joueur
                dialogAjoutPoints(listScores.get(position), Integer.toString(mancheActuelle));
                Toast.makeText(DetailPartie.this, "click sur le score de " + listScores.get(position).getPseudo(), Toast.LENGTH_SHORT).show();
            }
        });

        listViewScore.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //dialog de set des pts pour le joueur (reset a 50 ou erreur de saisie)
                Toast.makeText(DetailPartie.this, "long click sur le score de " + listScores.get(position).getPseudo(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgBack.setVisibility(View.GONE);
                frameScore.setVisibility(View.GONE);
                listViewManche.setVisibility(View.VISIBLE);
            }
        });

        btnCreerManche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetailPartie.this, "Création d'une manche bientot", Toast.LENGTH_SHORT).show();
            }
        });
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
        readData(parties, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                for (DataSnapshot parties : dataSnapshot.getChildren()) {
                    if (parties.getKey().equals(nomPartie)) {
                        for (DataSnapshot childPartie : parties.getChildren()) {
                            if (childPartie.getKey().equals("joueurs")) {
                                for (DataSnapshot joueurs : childPartie.getChildren()) {
                                    listJoueurs.add(joueurs.getValue().toString());
                                    Log.e(joueurs.getKey(), joueurs.getValue().toString());
                                }
                                for(int i = 0; i < listJoueurs.size(); i++){
                                    TextView tvJoueur = new TextView(getApplicationContext());
                                    tvJoueur.setText(listJoueurs.get(i));
                                    tvJoueur.setTextSize(19);
                                    tvJoueur.setTextColor(getResources().getColor(R.color.black));

                                    frameJoueurs.addView(tvJoueur);
                                    Log.e("listJoueurs", listJoueurs.get(i));
                                }
                            }
                            if (childPartie.getKey().equals("manches")) {
                                for (DataSnapshot manches : childPartie.getChildren()) {
                                    listScores.clear();
                                    listLabelManches.add("Manche " + manches.getKey());
                                    for(DataSnapshot scores : manches.getChildren()){
                                        listScores.add(new Score(scores.getKey(), Integer.parseInt(scores.getValue().toString())));
                                    }
                                    listManches.add(new Manche(Integer.parseInt(manches.getKey()), listScores));
                                    String txt = "";
                                    for(int i = 0; i < listManches.size(); i++){
                                        txt = listManches.get(i).getNumManche() + "";
                                        for(int j = 0; j < listManches.get(i).getScores().size(); j++){
                                            txt += "\n" + listManches.get(i).getScores().get(j);
                                        }
                                    }
                                    Log.e("manches", txt);
                                }
                                adapterManche.notifyDataSetChanged();
                            }
                        }
                    }

                }


            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void dialogAjoutPoints(final Score score, final String numManche){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailPartie.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.raw_dialog_score, null);
        final EditText editScoreSuppl = dialogView.findViewById(R.id.editDialog);
        TextView tvTitle = dialogView.findViewById(R.id.titleDialog);
        TextView tvMessage = dialogView.findViewById(R.id.messageDialog);
        Button btnAnnuler = dialogView.findViewById(R.id.btnAnnuler);
        Button btnModif = dialogView.findViewById(R.id.btnModif);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        tvTitle.setText("Score de " + score.getPseudo());
        tvMessage.setText("Indiquez le montant de points pris lors de cette manche");
        btnModif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalPts = score.getPoints() + Integer.parseInt(editScoreSuppl.getText().toString());
                manches.child(numManche).child(score.getPseudo()).setValue(Integer.toString(totalPts));
                Toast.makeText(DetailPartie.this, "Score de " + score.getPseudo() + " modifié", Toast.LENGTH_SHORT).show();
            }
        });
        btnAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getPartie();
            }
        });


        alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.design_dialog));

    }

}
