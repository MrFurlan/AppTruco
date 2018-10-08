package com.exemple.furlan.apptruco.DAO;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {
    private static FirebaseAuth autenticacao;
    private static DatabaseReference referenciaFirebase;
    private static StorageReference referenciaStorage;

    public static FirebaseAuth getFirebaseAutenticacao() {
        if (autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }

        return autenticacao;
    }

    public static DatabaseReference getFirebase() {
        if (referenciaFirebase == null) {
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }

        return referenciaFirebase;
    }

    public static StorageReference getStorage() {
        if (referenciaStorage == null) {
            referenciaStorage = FirebaseStorage.getInstance().getReference();
        }

        return referenciaStorage;
    }
}

