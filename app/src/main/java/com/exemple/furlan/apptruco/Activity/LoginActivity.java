package com.exemple.furlan.apptruco.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.exemple.furlan.apptruco.DAO.ConfiguracaoFirebase;
import com.exemple.furlan.apptruco.Model.Usuarios;
import com.exemple.furlan.apptruco.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends Activity {
    private EditText edtEmail;
    private EditText edtSenha;
    private Button btnEntrar;
    private FirebaseAuth autenticacao;
    private Usuarios usuarios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.edtEmailLogin);
        edtSenha = (EditText) findViewById(R.id.edtSenhaLogin);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtEmail.getText().toString().equals("") && !edtSenha.getText().toString().equals("")) {
                    usuarios = new Usuarios();
                    usuarios.setEmail(edtEmail.getText().toString());
                    usuarios.setSenha(edtSenha.getText().toString());

                    validarLogin();
                } else {
                    Toast.makeText(LoginActivity.this, "Preencha os campos de e-mail e senha!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void validarLogin() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuarios.getEmail(), usuarios.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    abrirHome();

                    Toast.makeText(LoginActivity.this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "E-mail ou senha inválidos!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirHome() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }

    public void GoToLogin(View view) {
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);
    }

    public void GoToRegister(View view) {
        Intent i = new Intent(this,RegisterActivity.class);
        startActivity(i);
    }
}
