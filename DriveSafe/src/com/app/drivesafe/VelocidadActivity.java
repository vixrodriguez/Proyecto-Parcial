package com.app.drivesafe;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.app.DataBase.Handler_sqlite;
import com.app.gps.MyOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Actividad que se encarga de mostrar al usuario su velocidad, direccion y
 * su ubicacion en el mapa. Ademas informa al usuario si excedio si o no
 * el limite de velocidad.
 * @author Victor S. Rodriguez Cabrera
 *
 */

public class VelocidadActivity extends MapActivity implements OnCompletionListener {

	private double latitud, longitud, velocidad;
	
	private TextView lblVelocidad;
	private ProgressBar barraProgreso;
	private TextView lblDireccion;
	
	private LocationManager locManager;
	private LocationListener locListener;
	
	private String direccion = "";
	
	//Elementos para el sonido
	private ToggleButton botonSonido;
	private MediaPlayer player;
	private AssetFileDescriptor dsp1;
	private AssetFileDescriptor dsp2; 
	
	//Elemento de mapa
	private MapView mapa;
	private MapController mapControl;
	private List<Overlay> mapOverlays;
	private Drawable drawable;
	private MyOverlay itemizedOverlay;
	private GeoPoint punto;
	private OverlayItem overlayitem;
	private Geocoder gc;
	
	//BBDD
	private Handler_sqlite handler;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.velocidad);
        
        //Base de datos
        handler = new Handler_sqlite(this); //Aqui instancia la base de datos
        
        //Sonido de la aplicacion
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        AssetManager manager1 = this.getAssets();
        AssetManager manager2 = this.getAssets();
        player = new MediaPlayer();
        
        //Obtengo los archivos MP3 de la carpeta Assets
        try
        {	
        	dsp1 = manager1.openFd("bajeVelocidad.wav");
        	dsp2 = manager2.openFd("infraccion.wav");
        	
        }catch(Exception e)
        {}
        
        //Definiendo componentes del MAPA
        mapa =(MapView)findViewById(R.id.Mapa);
        mapa.setBuiltInZoomControls(true);
        
        mapControl = mapa.getController();

        mapOverlays = mapa.getOverlays();
        drawable = this.getResources().getDrawable(R.drawable.logo);
		
		mapControl = mapa.getController();
		mapControl.setZoom(6);
        
		gc = new Geocoder(this, Locale.getDefault());
		
        //Componentes de la Actividad
        lblVelocidad = (TextView) findViewById(R.id.lblVelocidad);
        lblVelocidad.setText("Velocidad:" + "\n0.0 km/h");
        lblVelocidad.setTextColor(Color.WHITE);
        
        barraProgreso = (ProgressBar) findViewById(R.id.barraProgreso);
        barraProgreso.setEnabled(false);
        
        lblDireccion = (TextView) findViewById(R.id.lblDireccion);
        lblDireccion.setText("Direccion: ");
        lblDireccion.setTextColor(Color.WHITE);
        
        comenzarLocalizacion();
	}
	
	//--------- Posicionamientos y Uso del GPS
	private void comenzarLocalizacion()
    {
    	//Obtenemos una referencia al LocationManager
    	locManager = 
    		(LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	
    	//Obtenemos la última posición conocida
    	Location loc = 
    		locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
    	//Mostramos la última posición conocida
    	mostrarPosicion(loc);
    	
		//Agregando el listener al localizador
    	locListener = new LocationListener() {
	    	public void onLocationChanged(Location location) {
	    		mostrarPosicion(location);
	    	}
	    	public void onProviderDisabled(String provider){
	    	}
	    	public void onProviderEnabled(String provider){
	    	}
	    	public void onStatusChanged(String provider, int status, Bundle extras){
	    		Log.i("", "Provider Status: " + status);
	    	}
    	};
    	
    	//Nos registramos para recibir actualizaciones de la posición
    	
    	locManager.requestLocationUpdates(
    			LocationManager.GPS_PROVIDER, 1000, (float) 0.1, locListener);
    	
    }
	
	/**
	 * Muestra la velocidad del dispositivo segun el provider del GPS.
	 * @param loc Ubicacion del dispositivo.
	 */
	 private void mostrarPosicion(Location loc) 
	 {	
		 botonSonido = (ToggleButton)findViewById(R.id.btnSonido);
		 if(loc != null)   	
		 {
			 
			mostrarDireccion(loc);
			 
			latitud = loc.getLatitude();
	    	longitud = loc.getLongitude();
			velocidad = loc.getSpeed() * 3.6 ;
			
	   		lblVelocidad.setText("Velocidad:"+"\n" + String.format("%.2f", velocidad) + " km/h");
		 
	   		//Item de ubicacion del objeto en el mapa
	   		punto = new GeoPoint(
	   				this.conversion_Decimal_Entero(latitud), 
	   				this.conversion_Decimal_Entero(longitud));
	   		overlayitem = new OverlayItem(punto, "Recorrido","Usted se encuentra aqui");
	   		
	   		itemizedOverlay = new MyOverlay(drawable, this);
	   		itemizedOverlay.addOverlay(overlayitem);
	   		
	   		if(mapOverlays.size() > 0)
	   			mapOverlays.set(0,itemizedOverlay);
	   		else
	   			mapOverlays.add(itemizedOverlay);
	   		
			mapControl.animateTo(punto);
	   		
	   		barraProgreso.setProgress((int) velocidad);
	   		
	   		if(velocidad < 40 )
	   		{
	   			//Pintara la barra de color VERDE
	   			barraProgreso.setDrawingCacheBackgroundColor(Color.GREEN);
	   		}	
	   		else if(40 <= velocidad  &&  velocidad < 60) 
	   		{
	   			//Pintara la barra de color NARANJA
	   			barraProgreso.setDrawingCacheBackgroundColor(Color.rgb(255, 165, 0));
	   			if(botonSonido!= null && botonSonido.isChecked())
	   				iniciarSonido(dsp1);
	   		}
	   		else
	   		{
	   			//Pintara la barra de color ROJO
	   			barraProgreso.setDrawingCacheBackgroundColor(Color.RED);
	   			if(botonSonido!= null && botonSonido.isChecked())
	   				iniciarSonido(dsp2);
	   			
	   			//Instanciando la fecha
	   			
	   			Date fecha = new Date();
	   			
	   			handler.abrir();
	   			handler.ingresarRegistro(
	   					velocidad,
	   					fecha.getDay()+" / "+fecha.getMonth()+" / "+fecha.getYear(),
	   					fecha.getHours() + " : " + fecha.getMinutes() + " : "+ fecha.getSeconds(), direccion);
	   			handler.cerrar();
	   		}
	   			
	   		Log.i("", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
	   	}
		else
		{
	   		latitud = longitud = 0;
	   	}
    }
	 
	 /**
	  * Convierte la notacion decimal en entera
	  * @param valor Recibe el valor decimal: 
	  * Ej: -2.87866892
	  * @return El valor en forma entera 
	  * Ej: -2878668  
	  */
	 private int conversion_Decimal_Entero(double valor)
	 {
		 return (int) (valor * Math.pow(10,6));
	 }
	
	 /**
	  * Muestra la direccion de ubicacion del dispositivo segun Google Maps
	  */
	private void mostrarDireccion(Location loc)
	{
		gc = new Geocoder(this);
		direccion = " ";		
		
		try {
			//Se recibe la lista de direcciones segun la latitud y longitud determinada
			List<Address> direcciones = gc.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
			
			if(direcciones != null && direcciones.size() > 0) {
				Address returnedAddress = direcciones.get(0);
				
				for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++)
				{
					if(!(i == returnedAddress.getMaxAddressLineIndex() - 1))
						direccion += returnedAddress.getAddressLine(i) + " , ";
					else
						direccion += returnedAddress.getAddressLine(i);
				} 
				lblDireccion.setText("Direccion: "+direccion);
			  }
			  else{
				  lblDireccion.setText("Direccion: No se encontro");
			  }
		
		} 
		catch (Exception e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
			  lblDireccion.setText("Direccion: No localizado");
		}	
	}

	

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return true;
	}
	
	//-------------- SONIDO --------------------------------------
	/**
	 * Ejecuta un sonido 
	 * @param dsp Sonido a ejecutar
	 */
	private void iniciarSonido(AssetFileDescriptor dsp)
	{
		player = new MediaPlayer();
		try {
			player.setDataSource(dsp.getFileDescriptor(), dsp.getStartOffset(), dsp.getLength());
			player.prepare();
			
			//Reproduccion
			player.start();
			player.setOnCompletionListener(this);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void onCompletion(MediaPlayer mp) {
		player.stop(); //Detiene la ejecucion del sonido
	}
	
	//--------------------------------------------------------------------
}
