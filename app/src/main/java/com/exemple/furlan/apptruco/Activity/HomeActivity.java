package com.exemple.furlan.apptruco.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.exemple.furlan.apptruco.R;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
