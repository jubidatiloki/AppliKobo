package procorp.applikobo.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import procorp.applikobo.Models.Profil;
import procorp.applikobo.Models.ProfilManager;
import procorp.applikobo.R;
import procorp.applikobo.Utils.Utilitaire;


public class Inscription extends AppCompatActivity {


    private EditText inputEmail, inputPassword, inputPseudo;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    private ProfilManager profilManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        getSupportActionBar().setTitle("AppliKobo");

        profilManager = new ProfilManager(this);
        profilManager.open();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = findViewById(R.id.sign_in_button);
        btnSignUp = findViewById(R.id.sign_up_button);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputPseudo = findViewById(R.id.pseudo);
        progressBar = findViewById(R.id.progressBar);
        btnResetPassword = findViewById(R.id.btn_reset_password);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "RESET", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Inscription.this, ResetPasswordActivity.class));

            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Inscription.this, Connexion.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                final String pseudo = inputPseudo.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.auth_email), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(pseudo)) {
                    Toast.makeText(getApplicationContext(), "Entrez un pseudo", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.auth_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), getString(R.string.minimum_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Inscription.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    if(task.getException().toString().equals(getString(R.string.exception_auth_already_exist))){
                                        Toast.makeText(getApplicationContext(), getString(R.string.auth_already_exist), Toast.LENGTH_SHORT).show();
                                    }else if(task.getException().toString().equals(getString(R.string.exception_auth_internet))){
                                        Toast.makeText(getApplicationContext(), getString(R.string.auth_internet), Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Inscription réussie !!", Toast.LENGTH_SHORT).show();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    final DatabaseReference users = database.getReference().child("users");
                                    final String mail = email.replace(".", "_");
                                    users.child(mail).setValue(pseudo);
                                    if(profilManager.getProfilByMail(email) == null)
                                        profilManager.insertProfil(new Profil(email, pseudo));
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            }
                        });

            }
        });

        setupUI(findViewById(R.id.inscription));

    }

    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utilitaire.hideSoftKeyboard(Inscription.this);
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


}
