package mx.itson.myevents.soap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import mx.itson.myevents.models.Evento;
import mx.itson.myevents.models.Lugar;

/**
 * Created by Admin on 03/12/2014.
 */
public class EventoServiceClient {
    static String NAMESPACE = "http://localhost/Servicios/Evento";
    static String URL = "http://myevents.idealabgym.com/Servicios/EventoService.asmx";
    //Estos se definen en cada uno de los metodos
    String SOAP_ACTION, METHOD;
    Listener mListener;

    public interface Listener {
        void onError(String message);
    }
    public EventoServiceClient(Listener listener) {
        this.mListener = listener;
    }

    public Evento getById(int id){
        METHOD = "GetById";
        SOAP_ACTION = String.format("%s/%s",NAMESPACE,METHOD);
        SoapObject solicitud = new SoapObject(NAMESPACE, METHOD);
        solicitud.addProperty("id", id);

        SoapSerializationEnvelope Envoltorio = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        Envoltorio.dotNet = true;
        Envoltorio.setOutputSoapObject(solicitud);
        HttpTransportSE TransporteHttp = new HttpTransportSE(URL);

        try {
            TransporteHttp.call(SOAP_ACTION, Envoltorio);
            SoapObject objSoap = (SoapObject) Envoltorio.getResponse();
            Evento evento = new Evento();
            evento.setId(Integer.parseInt(objSoap.getProperty("Id").toString()));
            evento.setNombre(String.valueOf(objSoap.getProperty("Nombre")));
            evento.setEdadMinima(Integer.parseInt(objSoap.getProperty("EdadMinima").toString()));
            evento.setImagen(objSoap.getProperty("Imagen").toString());
            evento.setPatrocinadores(objSoap.getProperty("Patrocinadores").toString());
            evento.setObservaciones(objSoap.hasProperty("Observaciones") ? objSoap.getProperty("Observaciones").toString() : null);

            DateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            evento.setFechaHora(formatter.parse(objSoap.getProperty("FechaHora").toString().replace("T"," ")));

            SoapObject lugarSOAP = (SoapObject) objSoap.getProperty("Lugar");
            Lugar lugar = new Lugar();
            lugar.setId(Integer.parseInt(lugarSOAP.getProperty("Id").toString()));
            lugar.setCiudad(lugarSOAP.getProperty("Ciudad").toString());
            lugar.setEstado(lugarSOAP.getProperty("Estado").toString());
            lugar.setDireccion(lugarSOAP.getProperty("Direccion").toString());
            lugar.setNombre(lugarSOAP.getProperty("Nombre").toString());
            lugar.setTelefono(lugarSOAP.getProperty("Telefono").toString());

            evento.setLugar(lugar);

            return evento;
        } catch (Exception e) {
            e.printStackTrace();
            mListener.onError(e.getMessage());
        }
        return null;
    }
}
