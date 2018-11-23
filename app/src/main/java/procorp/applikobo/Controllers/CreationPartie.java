package procorp.applikobo.Controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import procorp.applikobo.R;
import procorp.applikobo.Utils.OnGetDataListener;
import procorp.applikobo.Utils.Utilitaire;

public class CreationPartie extends AppCompatActivity {

    public final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public FirebaseAuth auth = FirebaseAuth.getInstance();
    public ArrayList<User> listUser = new ArrayList<>();
    public ArrayList<CheckBox> listCheckbox = new ArrayList<>();

    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public final DatabaseReference users = database.getReference().child("users");
    public final DatabaseReference parties = database.getReference().child("parties");

    EditText editNom;
    public Button btnCreer;
    public TextView tvNbParticipants;
    public LinearLayout linearCheckbox;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_partie);

        editNom = findViewById(R.id.editNomPartie);
        btnCreer = findViewById(R.id.btnCreer);
        tvNbParticipants = findViewById(R.id.tvNbParticipants);
        linearCheckbox = findViewById(R.id.linearCheckbox);

        btnCreer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomPartie = editNom.getText().toString();
                if (TextUtils.isEmpty(nomPartie)) {
                    Toast.makeText(getApplicationContext(), "Le nom de la partie n'a pas été renseigné", Toast.LENGTH_SHORT).show();
                    return;
                }

                StringBuffer stringBuffer = new StringBuffer();
                for(int i = 0; i < listCheckbox.size(); i++){
                    stringBuffer.append(listCheckbox.get(i).isChecked() ? listCheckbox.get(i).getText().toString() + " ok\n" : listCheckbox.get(i).getText().toString() + " nop\n");
                }
                for(int i = 0; i < listUser.size(); i++){
                    Log.e("user=" + i, listUser.get(i).toString());
                    if(listUser.get(i).participe) {
                        parties.child(nomPartie).child("joueurs").child(listUser.get(i).mail).setValue(listUser.get(i).pseudo);
                        parties.child(nomPartie).child("manches").child("1").child(listUser.get(i).pseudo).setValue("0");
                    }
                }
                Intent intent = new Intent(CreationPartie.this, DetailPartie.class);
                intent.putExtra("nomPartie", nomPartie);
                startActivity(intent);
                Log.e("participants ", stringBuffer.toString());
            }
        });

        getSupportActionBar().setTitle(user.getEmail());

        readData(users, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    listUser.add(new User(dataSnapshot1.getKey(),dataSnapshot1.getValue(String.class)));
                }
                Log.e("listUser size", listUser.size() + "");
                for(int i = 0; i<listUser.size(); i++){
                    Log.e("user", listUser.get(i).toString());
                }
                creationCheckbox();
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });

        setupUI(findViewById(R.id.creationPartie));

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
        if (id == R.id.nav_parametres) {
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
                        startActivity(new Intent(CreationPartie.this, Connexion.class));
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

    public void creationCheckbox(){
        final int[] comptPart = {0};
        for(int i = 0; i < listUser.size(); i++){
            final CheckBox checkBox = new CheckBox(getApplicationContext());
            if(!listUser.get(i).mail.equals(user.getEmail().replace(".", "_"))){
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(20,10,0,0);
                checkBox.setLayoutParams(layoutParams);
                checkBox.setText(listUser.get(i).pseudo);
                checkBox.setTextColor(getResources().getColor(R.color.black));
                final int finalI = i;
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(checkBox.isChecked()){
                            comptPart[0]++;
                            User user = listUser.get(finalI);
                            Log.e("user checked", user.toString());
                            user.setParticipe(true);
                            listUser.set(finalI,user);
                        }else{
                            comptPart[0]--;
                            User user = listUser.get(finalI);
                            Log.e("user unchecked", user.toString());
                            user.setParticipe(false);
                            listUser.set(finalI,user);
                        }
                        tvNbParticipants.setText(Integer.toString(comptPart[0]));
                    }
                });
                Log.e("checkbox text=", checkBox.getText() + ".");
                listCheckbox.add(checkBox);
                linearCheckbox.addView(checkBox);
            }else{
                User user = listUser.get(i);
                Log.e("user checked", user.toString());
                user.setParticipe(true);
                listUser.set(i,user);
            }
        }
        tvNbParticipants.setText(Integer.toString(comptPart[0]));
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

    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utilitaire.hideSoftKeyboard(CreationPartie.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private class User {
        private String mail;
        private String pseudo;
        private boolean participe;

        public User(String mail, String pseudo) {
            this.mail = mail;
            this.pseudo = pseudo;
            this.participe = false;
        }

        public void setParticipe(boolean participe) {
            this.participe = participe;
        }

        @Override
        public String toString() {
            return "User{" +
                    "mail='" + mail + '\'' +
                    ", pseudo='" + pseudo + '\'' +
                    ", participe=" + participe +
                    '}';
        }
    }

}
