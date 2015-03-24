package mx.itson.myevents.views;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

import mx.itson.myevents.R;
import mx.itson.myevents.adapters.EventoAdapter;
import mx.itson.myevents.models.Evento;
import mx.itson.myevents.soap.EventoServiceClient;
import mx.itson.myevents.soap.HomeServiceClient;
import mx.itson.myevents.util.ImageLoaderTask;

public class EventoActivity extends ActionBarActivity {

    int id;
    Evento mEvento;
    EventoLoaderTask mEventoLoaderTask;
    private ImageView imgEvento;
    private TextView evento_nombre;
    private ScrollView scrollEvento;
    private ProgressBar loaderEvento;
    private TextView evento_lugar_nombre;
    private TextView evento_lugar_direccion;
    private TextView evento_fechahora;
    private TextView evento_lugar_ciudad_estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);
        int id=getIntent().getIntExtra("Id",1);
        //Toast.makeText(this,"Cargar el Id: "+id,Toast.LENGTH_LONG).show();
        if(mEventoLoaderTask!=null){
            cargarEvento();
        }
        imgEvento=(ImageView)findViewById(R.id.img_evento);
        evento_nombre=(TextView)findViewById(R.id.lbl_evento_nombre);
        evento_lugar_nombre=(TextView)findViewById(R.id.evento_lugar_nombre);
        evento_lugar_direccion=(TextView)findViewById(R.id.evento_lugar_direccion);
        evento_fechahora=(TextView)findViewById(R.id.evento_fechahora);
        evento_lugar_ciudad_estado=(TextView)findViewById(R.id.evento_lugar_ciudad_estado);
        scrollEvento=(ScrollView)findViewById(R.id.scrollEvento);
        scrollEvento.setVisibility(View.GONE);
        loaderEvento=(ProgressBar)findViewById(R.id.loadingEvento);
        if(mEventoLoaderTask==null){
            mEventoLoaderTask=new EventoLoaderTask(id);
            mEventoLoaderTask.execute((Void) null);
        }
    }


    public void cargarEvento() {
        new ImageLoaderTask("http://myevents.idealabgym.com/Images/Upload/x300/"+mEvento.getImagen(),imgEvento).execute();
        evento_nombre.setText(mEvento.getNombre());
        evento_lugar_nombre.setText(mEvento.getLugar().getNombre());
        evento_lugar_direccion.setText(mEvento.getLugar().getDireccion());
        evento_fechahora.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(mEvento.getFechaHora()));
        evento_lugar_ciudad_estado.setText(mEvento.getLugar().getCiudad()+", "+mEvento.getLugar().getEstado());
        loaderEvento.setVisibility(View.GONE);
        scrollEvento.setVisibility(View.VISIBLE);
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_evento, menu);
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
    }*/
    public class EventoLoaderTask extends AsyncTask<Void, Void, Evento> implements EventoServiceClient.Listener {
        final int id;
        public EventoLoaderTask(int id){
            this.id=id;
        }
        @Override
        protected Evento doInBackground(Void... params) {
            EventoServiceClient eventoServiceClient = new EventoServiceClient(this);
            return eventoServiceClient.getById(id);
        }

        @Override
        protected void onPostExecute(Evento evento) {
            mEvento=evento;
            if(evento!=null)
                cargarEvento();
            else
                onError("No se logro cargar el evento");
        }

        @Override
        public void onError(final String message) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(EventoActivity.this, message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
