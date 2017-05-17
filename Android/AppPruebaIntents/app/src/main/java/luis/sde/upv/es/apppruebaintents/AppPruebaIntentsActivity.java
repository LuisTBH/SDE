package luis.sde.upv.es.apppruebaintents;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.TextView;

public class AppPruebaIntentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_prueba_intents);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Button btn_web = (Button) findViewById(R.id.btn_web);
        Button btn_telefono = (Button) findViewById(R.id.btn_telefono);
        Button btn_mapa = (Button) findViewById(R.id.btn_mapa);
        Button btn_contacto = (Button) findViewById(R.id.btn_contacto);


        btn_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.upv.es"));
                startActivity(i);
            }
        });
        ////En la tablet que tengo no tiene teléfono y cierra la aplicación. Supongo que un móvil irá bien.
        btn_telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+963877000"));
                startActivity(i);
            }
        });
        //En la tablet que tengo no accede a los mapas y cierra la aplicación. Supongo que un móvil irá bien.
        btn_mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:39.48167,-0.34361"));
                startActivity(i);
            }
        });

        btn_contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView texto = (TextView) findViewById(R.id.texto_contacto);
                texto.setText("Vamos a seleccionar un contacto...");
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivityForResult(i, 123);
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        TextView texto = (TextView) findViewById(R.id.texto_contacto);
        texto.setText("Recibida respuesta...");
        if (requestCode == 123) { //Por si lanzamos otras actividades pendientes de respuesta
            if (resultCode == RESULT_OK) {
                // Devuelve un URI que identifica el contacto
                // Ahora hay que buscar su contenido y coger la columna de nombre
                Cursor c = getContentResolver().query(data.getData(), null, null, null, null);
                if (c.moveToFirst()) {
                    String name = c.getString(c.getColumnIndexOrThrow(
                            ContactsContract.Contacts.DISPLAY_NAME));

                    texto.setText(name);
                }
                ;
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_prueba_intents, menu);
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
