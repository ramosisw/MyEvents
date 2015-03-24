package mx.itson.myevents.soap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.itson.myevents.models.Evento;
import mx.itson.myevents.models.Lugar;

/**
 * Created by Admin on 27/11/2014.
 */
public class HomeServiceClient {
    static String NAMESPACE = "http://localhost/Servicios/Home";
    static String URL = "http://myevents.idealabgym.com/Servicios/HomeService.asmx";
    //Estos se definen en cada uno de los metodos
    String SOAP_ACTION, METHOD;
    Listener mListener;

    public interface Listener {
        void onError(String message);
    }


    public HomeServiceClient(Listener listener) {
        this.mListener = listener;
    }

    public List<Evento> getEventos() {
        METHOD = "Eventos";
        SOAP_ACTION = String.format("%s/%s", NAMESPACE, METHOD);
        SoapObject solicitud = new SoapObject(NAMESPACE, METHOD);
        List<Evento> eventos = new ArrayList<Evento>();
        SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        Envoltorio.dotNet = true;
        Envoltorio.setOutputSoapObject(solicitud);
        HttpTransportSE TransporteHttp = new HttpTransportSE(URL);
        try {
            TransporteHttp.call(SOAP_ACTION, Envoltorio);
            SoapObject response = (SoapObject) Envoltorio.getResponse();
            for (int i = 0; i < response.getPropertyCount(); i++) {
                SoapObject eventoSOAP = (SoapObject) response.getProperty(i);
                Evento evento = new Evento();
                evento.setId(Integer.parseInt(eventoSOAP.getProperty("Id").toString()));
                evento.setNombre(String.valueOf(eventoSOAP.getProperty("Nombre")));
                evento.setEdadMinima(Integer.parseInt(eventoSOAP.getProperty("EdadMinima").toString()));
                evento.setImagen(eventoSOAP.getProperty("Imagen").toString());
                evento.setPatrocinadores(eventoSOAP.getProperty("Patrocinadores").toString());
                evento.setObservaciones(eventoSOAP.hasProperty("Observaciones") ? eventoSOAP.getProperty("Observaciones").toString() : null);

                DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                evento.setFechaHora(formatter.parse(eventoSOAP.getProperty("FechaHora").toString().replace("T"," ")));

                SoapObject lugarSOAP = (SoapObject) eventoSOAP.getProperty("Lugar");
                Lugar lugar = new Lugar();
                lugar.setId(Integer.parseInt(lugarSOAP.getProperty("Id").toString()));
                lugar.setCiudad(lugarSOAP.getProperty("Ciudad").toString());
                lugar.setEstado(lugarSOAP.getProperty("Estado").toString());
                lugar.setDireccion(lugarSOAP.getProperty("Direccion").toString());
                lugar.setNombre(lugarSOAP.getProperty("Nombre").toString());
                lugar.setTelefono(lugarSOAP.getProperty("Telefono").toString());

                evento.setLugar(lugar);


                eventos.add(evento);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mListener.onError(e.getMessage());
        }
        return eventos;
    }
    public boolean login(String correo, String contrasena) {
        METHOD = "Login";
        SOAP_ACTION = String.format("%s/%s",NAMESPACE,METHOD);
        SoapObject solicitud = new SoapObject(NAMESPACE, METHOD);

        solicitud.addProperty("correo", correo);
        solicitud.addProperty("contrasena", contrasena);

        SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        Envoltorio.dotNet = true;
        Envoltorio.setOutputSoapObject(solicitud);
        HttpTransportSE TransporteHttp = new HttpTransportSE(URL);
        try {
            TransporteHttp.call(SOAP_ACTION, Envoltorio);
            SoapPrimitive objSoap = (SoapPrimitive) Envoltorio.getResponse();
            if (objSoap != null) {
                String value = objSoap.getValue().toString();
                return value.equals("true");
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mListener.onError(e.getMessage());
        }
        return false;
    }
}
