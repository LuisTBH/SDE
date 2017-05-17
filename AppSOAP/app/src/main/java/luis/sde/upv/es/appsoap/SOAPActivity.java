package luis.sde.upv.es.appsoap;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class SOAPActivity extends AppCompatActivity {

    //SOAP_ACTION = NAMESPACE + METHOD_NAME;
    private static final String NAMESPACE = "http://footballpool.dataaccess.eu";
    private static final String METHOD_NAME = "TopGoalScorers";
    private static final String SOAP_ACTION = "http://footballpool.dataaccess.eu/TopGoalScorers";

    private static final String URL = "http://footballpool.dataaccess.eu/data/info.wso";
    //private static final String URL = "http://footballpool.dataaccess.eu/data/info.wso?WSDL";

    TextView textViewRespuesta;
    TextView textViewPichichi;
    TextView textViewRequest;
    TextView textViewResponse;

    String sRespuesta;
    String sPichichi;
    String sRequest;
    String sResponse;

    private class SoapCallTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... val) {
            return soap_call(METHOD_NAME, SOAP_ACTION, NAMESPACE, URL);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(Boolean result) {
            Log.v("MIO", "OnPostExecture Result=" + result);
            if (result) {
                textViewRequest.setText(sRequest);
                textViewResponse.setText(sResponse);
                textViewRespuesta.setText(sRespuesta);
                textViewPichichi.setText(sPichichi);
            }
            else {
                textViewRespuesta.setText("FALLO");
                textViewResponse.setText(sRespuesta);
            }

        }
    }

    public Boolean soap_call(String METHOD_NAME, String SOAP_ACTION, String NAMESPACE, String URL)
    {
        Boolean res = true;
        try {
            // A COMPLETAR
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); //set up request
            request.addProperty("iTopN", "1");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //put all required data into a soap envelope
            envelope.setOutputSoapObject(request);  //prepare request
            HttpTransportSE httpTransport = new HttpTransportSE(URL);
            httpTransport.debug = true;
            httpTransport.call(SOAP_ACTION, envelope); //send request
            sRequest = httpTransport.requestDump;
            sResponse = httpTransport.responseDump;

            SoapObject result=(SoapObject)envelope.getResponse(); //get response
            sRespuesta = result.getProperty(0).toString();

            // El siguiente código coge el nombre del jugador. El formato es sName=NOMBRE APELLIDO;
            // y se extrae el contenido entre sName= y el ;
            String tagInicio="sName=";
            String tagFin=";";
            int i = sRespuesta.indexOf(tagInicio) + tagInicio.length();
            int j = sRespuesta.indexOf(tagFin);
            sPichichi=sRespuesta.substring(i,j);

        } catch (Exception e) {
            sRespuesta = "Error en soap_call " + e.getMessage();
            res = false;
        }
        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soap);
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

        Button boton = (Button)findViewById(R.id.boton);
        textViewRespuesta = (TextView)findViewById(R.id.textRespuesta);
        textViewPichichi = (TextView)findViewById(R.id.textPichichi);
        textViewRequest = (TextView)findViewById(R.id.textRequest);
        textViewResponse = (TextView)findViewById(R.id.textResponse);

        // Ponemos los listeners
        boton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                new SoapCallTask().execute("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_soa, menu);
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
