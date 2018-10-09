package com.exemple.furlan.apptruco.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.exemple.furlan.apptruco.DAO.ConfiguracaoFirebase;
import com.exemple.furlan.apptruco.Model.Player;
import com.exemple.furlan.apptruco.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class PlayersActivity extends Activity {

    private EditText nomeDupla;
    private EditText nomeOponente;
    private EditText placarDupla;
    private EditText placarOponente;
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);

        nomeDupla = findViewById(R.id.edtNomeDupla);
        nomeOponente = findViewById(R.id.edtNomeOponente);
        placarDupla = findViewById(R.id.edtPlacarDupla);
        placarOponente = findViewById(R.id.edtPlacarOponente);
        mDatabaseRef = ConfiguracaoFirebase.getFirebase();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();



        nomeDupla.setText(autenticacao.getCurrentUser().getDisplayName());
    }

    public void SalvarResultado(View view){
        if (!placarDupla.getText().toString().equals("12") && !placarOponente.getText().toString().equals("12")) {
            Toast.makeText(this, "Uma das duplas deve ter 12 pontos para ser a ganhadora!", Toast.LENGTH_LONG).show();
        } else if (placarDupla.getText().toString().length() == 0 || placarOponente.getText().toString().length() == 0) {
            Toast.makeText(this, "Placar das duplas deve ser informado!", Toast.LENGTH_LONG).show();
        } else if (nomeOponente.getText().toString().length() == 0) {
            Toast.makeText(this, "Nome do oponente não foi informado!", Toast.LENGTH_LONG).show();
        } else {
            Player player = new Player();
            player.setId(autenticacao.getCurrentUser().getUid());
            player.setPlayer(nomeDupla.getText().toString());
            player.setPlacarPlayer(placarDupla.getText().toString());
            player.setOponente(nomeOponente.getText().toString());
            player.setPlacarOponente(placarOponente.getText().toString());

            mDatabaseRef.child("players").child(String.valueOf(System.currentTimeMillis())).setValue(player).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(PlayersActivity.this, "Jogo salvo com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(PlayersActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PlayersActivity.this, "Erro ao salvar jogo!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(PlayersActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }
    }

    public void GoToHome(View view) {
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);
    }

    public void GoToLogin(View view) {
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);
    }


}
