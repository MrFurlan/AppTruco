package com.exemple.furlan.apptruco.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.exemple.furlan.apptruco.DAO.ConfiguracaoFirebase;
import com.exemple.furlan.apptruco.Helper.Base64Custom;
import com.exemple.furlan.apptruco.Helper.Preferencias;
import com.exemple.furlan.apptruco.Model.Usuarios;
import com.exemple.furlan.apptruco.R;
import com.exemple.furlan.apptruco.Util.Util_Img;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;

public class RegisterActivity extends Activity {
    final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 99;
    private ImageButton imbUploadImagem;
    String realPath;

    private EditText edtNome;
    private EditText edtSobrenome;
    private EditText edtSenha;
    private EditText edtConfirmaSennha;
    private EditText edtEmail;
    private Button btnGravar;
    private Usuarios usuarios;
    private FirebaseAuth autenticacao;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imbUploadImagem = findViewById(R.id.imb_registro_uploadImage);

        edtNome = (EditText) findViewById(R.id.edtNomeRegistro);
        edtSobrenome = (EditText) findViewById(R.id.edtSobrenomeRegistro);
        edtSenha = (EditText) findViewById(R.id.edtSenhaRegistro);
        edtConfirmaSennha = (EditText) findViewById(R.id.edtConfirmaSenhaRegistro);
        edtEmail = (EditText) findViewById(R.id.edtEmailRegistro);
        btnGravar = (Button) findViewById(R.id.btnGravarUsuario);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        btnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtSenha.getText().toString().equals(edtConfirmaSennha.getText().toString())) {
                    usuarios = new Usuarios();
                    usuarios.setEmail(edtEmail.getText().toString());
                    usuarios.setSenha(edtSenha.getText().toString());
                    usuarios.setNome(edtNome.getText().toString());
                    usuarios.setSobrenome(edtSobrenome.getText().toString());

                    cadastrarUsuario();
                } else {
                    Toast.makeText(RegisterActivity.this, "As senhas estão diferentes!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cadastrarUsuario() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuarios.getEmail(),
                usuarios.getSenha()
        ).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                    String indentificadorUsuario = Base64Custom.codificarBase64(usuarios.getEmail());

                    Uri file = Uri.fromFile(new File(realPath));

                    String nameArq[] = file.toString().split("/");
                    final String nomeFile = nameArq[nameArq.length - 1].replace(".", "").replace("jpg","");

                    /*StorageReference photoRef = mStorageRef.child("imageUser/"+nomeFile);

                    photoRef.putFile(file)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    usuarios.setPhotoPath(taskSnapshot.getDownloadUrl().toString());
                                }
                            });*/

                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    usuarios.setId(indentificadorUsuario);
                    usuarios.setPhotoPath("");
                    usuarios.salvar();

                    Preferencias  preferencias = new Preferencias(RegisterActivity.this);
                    preferencias.salvarUsuarioPreferencias(indentificadorUsuario, usuarios.getNome());

                    abrirLoginUsuario();
                } else {
                    String erroExcecao = "";

                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = "Digite uma senha mais forte, contendo no mínimo 8 caracteres e letras e números!";
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        erroExcecao = "O e-mail digitado é inválido, digite um novo e-mail!";
                    } catch (FirebaseAuthUserCollisionException e){
                        erroExcecao = "Esse e-mail já está cadastrado no sistema!";
                    } catch (Exception e){
                        erroExcecao = "Erro ao efetuar o cadastro!";
                        e.printStackTrace();
                    }

                    Toast.makeText(RegisterActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirLoginUsuario() {
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    public void CarregarImagem(View view){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

            // 1. on Upload click call ACTION_GET_CONTENT intent
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            // 2. pick image only
            intent.setType("image/*");
            // 3. start activity
            startActivityForResult(intent, 0);

        }else {
            VerificaPermisao();
        }

    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if(resCode == Activity.RESULT_OK && data != null){


            realPath = Util_Img.getRealPathFromURI_API19(this,
                    data.getData());
            Uri uriFromPath = Uri.fromFile(new File(realPath));
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriFromPath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
//            imagemPerfil.setImageBitmap(bitmap);
            imbUploadImagem.setImageBitmap(bitmap);
        }
    }

    public void VerificaPermisao(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){

            new AlertDialog.Builder(this)
                    .setTitle("Permissão requirida!")
                    .setMessage("Precisamos de permissão para acessar sua pasta de imagem!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        }else{
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

    }
}
