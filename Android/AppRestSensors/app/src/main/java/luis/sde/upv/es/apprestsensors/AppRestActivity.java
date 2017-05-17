package luis.sde.upv.es.apprestsensors;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AppRestActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.disca.luigarg5.myfirstapp.MESSAGE";

    private EditText edit_dir_IP;
    private EditText edit_id_Sens;
    private TextView text_valor_actual_Sens;
    private EditText edit_nuevo_val_Sens;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_rest);
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

        edit_dir_IP = (EditText) findViewById(R.id.edit_dir_IP);
        edit_id_Sens = (EditText) findViewById(R.id.edit_id_Sens);
        text_valor_actual_Sens = (TextView) findViewById(R.id.text_valor_actual_Sens);
        edit_nuevo_val_Sens = (EditText) findViewById(R.id.edit_nuevo_val_Sens);
        textView = (TextView) findViewById(R.id.textView);

        edit_dir_IP.setText("http://1-dot-IDENTIFICADOR_OBTENIDO.appspot.com/prj_restgae/sensors");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_rest, menu);
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

    public void enviarValor(View view) {
        // Do something in response to button
        String nuevoValor = edit_nuevo_val_Sens.getText().toString();
        String stringUrl = edit_dir_IP.getText().toString() + "/" + edit_id_Sens.getText().toString();
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrl, "TRUE", "TRUE", nuevoValor);
        } else {
            textView.setText("No network connection available.");
        }
        /*
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(EXTRA_MESSAGE, nuevoValor);
        startActivity(intent);
        */
    }

    /** Called when the user clicks the listarValores button */
    public void listarValores(View view) {
        // Do something in response to button
        String stringUrl = edit_dir_IP.getText().toString();
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrl, "TRUE", "FALSE", "");
        } else {
            textView.setText("No network connection available.");
        }
    }

    /** Called when the user clicks the enviarValor button */
    public void obtenerValor(View view) {
        // Do something in response to button
        // When user clicks button, calls AsyncTask.
        // Before attempting to fetch the URL, makes sure that there is a network connection.
        // Gets the URL from the UI's text field.
        String stringUrl = edit_dir_IP.getText().toString() + "/" + edit_id_Sens.getText().toString();
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrl, "FALSE", "FALSE", "");
        } else {
            textView.setText("No network connection available.");
        }
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {

        private boolean result_in_new_window = false;
        private boolean mensaje_post = false;
        private String value_to_send = "";

        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            result_in_new_window = Boolean.valueOf(urls[1]);
            mensaje_post = Boolean.valueOf(urls[2]);
            value_to_send = urls[3];
            try {
                return accesoUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL: " + urls[0] + " may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (result_in_new_window){
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), DisplayMessageActivity.class);
                intent.putExtra(EXTRA_MESSAGE, result);
                startActivity(intent);
            } else text_valor_actual_Sens.setText(result);
        }

        // Given a URL, establishes an HttpUrlConnection and retrieves
        // the web page content as a InputStream, which it returns as
        // a string.
        private String accesoUrl(String myurl) throws IOException {
            InputStream in = null;
            HttpURLConnection conn = null;
            StringBuilder stringBuilder = null;
            try {
                // 1. Declare a URL Connection
                URL url = new URL(myurl);
                conn = (HttpURLConnection) url.openConnection();
                //conn.setReadTimeout(10000 /* milliseconds */);
                //conn.setConnectTimeout(15000 /* milliseconds */);
                if (mensaje_post) {
                    String urlParameters  = "new_value=" + value_to_send; //"param1=a&param2=b&param3=c"
                    byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
                    int    postDataLength = postData.length;
                    conn.setDoOutput( true );
                    conn.setInstanceFollowRedirects(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty( "charset", "utf-8");
                    conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                    conn.setUseCaches( false );
                    DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
                    wr.write( postData );
                }
                //conn.setDoInput(true);
                // 2. Open InputStream to connection
                conn.connect();
                //int response = conn.getResponseCode();
                // Log.d(DEBUG_TAG, "The response is: " + response);
                //textView.setText("The response is: " + response);
                in = conn.getInputStream();
                // 3. Download and decode the string response using builder
                stringBuilder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            }
            finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return stringBuilder.toString();
        }
    }
}
