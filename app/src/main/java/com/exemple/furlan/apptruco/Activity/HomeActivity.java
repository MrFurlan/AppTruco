package com.exemple.furlan.apptruco.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.exemple.furlan.apptruco.DAO.ConfiguracaoFirebase;
import com.exemple.furlan.apptruco.R;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends Activity {
    private TextView nomeUsuario;
    private ImageView photUsuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        nomeUsuario = (TextView) findViewById(R.id.textNomeUser);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        nomeUsuario.setText(autenticacao.getCurrentUser().getDisplayName());
    }

    public void GoToUser(View view) {
        Intent i = new Intent(this,UsuarioActivity.class);
        startActivity(i);
    }

    public void GoToPlayers(View view) {
        Intent i = new Intent(this, PlayersActivity.class);
        startActivity(i);
    }

    public void GoToLogin(View view) {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }


}
