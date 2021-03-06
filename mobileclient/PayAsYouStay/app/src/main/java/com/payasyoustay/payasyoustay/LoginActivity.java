package com.payasyoustay.payasyoustay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void login(View view) {
        if (true) {
            Intent intent = new Intent(this, HouseListActivity.class);
            Bundle bundle = new Bundle();
            TextView userTextView = (TextView)findViewById(R.id.user);
            bundle.putString("user", userTextView.getText().toString());
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
