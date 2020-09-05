package com.foodi.WSSoap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.foodi.WebServices.Asynchtask;

import org.json.JSONException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Map;

public class SOAPWork extends AsyncTask<String,Long,String> {

    //Variable con los datos para pasar al web service
    private Map<String, String> datos;
    private Map<String, byte[]> datos2;
    //Url del servicio web
    private String url= "http://192.168.101.12:8080/Foodi_srv/";

    //Actividad para mostrar el cuadro de progreso
    private Context actividad;
    private String dos;

    //Resultado
    private String xml=null;

    //Clase a la cual se le retorna los datos dle ws
    private Asynchtask callback=null;

    //Nombre del metodo a llamar del webservice
    private String method_name = "procesar";

    //Espacio de nombres del web service
    private String namespace = "http://WebService/";


    ProgressDialog progDailog;


    public  SOAPWork(String urlWebService, Map<String, String> data, Context activity, Asynchtask callback) {
        this.url=urlWebService;
        this.datos=data;
        this.actividad=activity;
        this.callback=callback;
    }

    public  SOAPWork(String urlWebService, Map<String, byte[]> data, Context activity, Asynchtask callback,String dos) {
        this.url=urlWebService;
        this.datos2=data;
        this.actividad=activity;
        this.callback=callback;
        this.dos=dos;
    }


    public SOAPWork(String urlWebService, Map<String, String> map, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener clickListener){

    }

    protected void onPreExecute() {
        super.onPreExecute();
        progDailog = new ProgressDialog(actividad);
        progDailog.setMessage("Procesando solicitud...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String SOAP_ACTION = this.namespace + this.method_name;
        try {
            //System.setProperty("http.keepAlive", "false");
            SoapObject request = new SoapObject(this.namespace, this.method_name);
            if(dos==null){
            for (String key : datos.keySet()) {
                //System.out.println(key + "=" + datos.get(key));
                request.addProperty(key, datos.get(key));
                Log.i("EnvioWS: ", key + "=" + datos.get(key));
            }}
            else {
                for (String key : datos2.keySet()) {
                    //System.out.println(key + "=" + datos.get(key));
                    request.addProperty(key, datos2.get(key));
                    Log.i("EnvioWS: ", key + "=" + datos2.get(key));
                }
            }
            //request.addProperty("name", "Randy");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.dotNet = false;
            envelope.setOutputSoapObject(request);
            HttpTransportSE ht = new HttpTransportSE(this.url);
            ht.call(SOAP_ACTION, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            Log.i("response", response.toString());
            return response.toString();
        } catch (Exception e) {
            Log.i("Error: ", e.getMessage());
            return e.getMessage();
        }
    }
    
    @Override
    protected void onPostExecute(String response){
        super.onPostExecute(response);
        this.xml=response;
        progDailog.dismiss();
        //Retorno los datos
        try {
            callback.processFinish(this.xml);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getDatos() {
        return datos;
    }

    public void setDatos(Map<String, String> datos) {
        this.datos = datos;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Context getActividad() {
        return actividad;
    }

    public void setActividad(Context actividad) {
        this.actividad = actividad;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public ProgressDialog getProgDailog() {
        return progDailog;
    }

    public void setProgDailog(ProgressDialog progDailog) {
        this.progDailog = progDailog;
    }

    public String getMethod_name() {return method_name;}

    public void setMethod_name(String method_name) {this.method_name = method_name; }

    public String getNamespace() {return namespace; }

    public void setNamespace(String namespace) {this.namespace = namespace; }
}
