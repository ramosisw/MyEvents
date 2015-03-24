package mx.itson.myevents.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import mx.itson.myevents.util.ImageLoaderTask;

import mx.itson.myevents.R;
import mx.itson.myevents.models.Evento;

/**
 * Created by Admin on 29/11/2014.
 */
public class EventoAdapter extends BaseAdapter {

    private final Activity activity;
    private final List<Evento> items;

    public EventoAdapter(Activity activity, List<Evento> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Generamos una convertView por motivos de eficiencia
        View v = convertView;
        //Asociamos el layout de la lista que hemos creado
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.fragment_evento_item, null);
        }
        Evento item = items.get(position);
        new ImageLoaderTask("http://myevents.idealabgym.com/Images/Upload/x150/"+item.getImagen(), (ImageView)v.findViewById(R.id.img_evento_imagen)).execute();
        ((TextView)v.findViewById(R.id.lbl_evento_nombre)).setText(item.getNombre());
        ((TextView)v.findViewById(R.id.lbl_evento_fecha)).setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(item.getFechaHora()));
        ((TextView)v.findViewById(R.id.lbl_evento_lugar_nombre)).setText(item.getLugar().getNombre());
        ((TextView)v.findViewById(R.id.lbl_evento_lugar_ciudad_estado)).setText(String.format("%s, %s",item.getLugar().getCiudad(),item.getLugar().getEstado()));
        return v;
    }

}
