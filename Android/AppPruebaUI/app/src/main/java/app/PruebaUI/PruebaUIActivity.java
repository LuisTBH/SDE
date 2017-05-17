package app.PruebaUI;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;

public class PruebaUIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_ui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                //Esto es para hacer el borrado del texto y poner el fondo rojo de nuevo
                TextView texto = (TextView) findViewById(R.id.saludo);
                texto.setText("");
                texto = (TextView) findViewById(R.id.textview2);
                texto.setBackgroundColor(Color.RED);
            }
        });

        Button buttonTexto = (Button)findViewById(R.id.buttonHola);
        buttonTexto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView texto = (TextView) findViewById(R.id.saludo);
                texto.setText("Hola amigo, mi nombre es Gideon");
            }
        });
        Button buttonAmarillo = (Button)findViewById(R.id.buttonAmarillo);
        buttonAmarillo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView texto = (TextView) findViewById(R.id.textview2);
                texto.setBackgroundColor(Color.YELLOW);
            }
        });
        Button btnVerde = (Button)findViewById(R.id.buttonVerde);
        btnVerde.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView texto = (TextView)findViewById(R.id.textview2);
                texto.setBackgroundColor(Color.GREEN);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prueba_ui, menu);
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


}
