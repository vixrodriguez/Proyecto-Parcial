package com.app.drivesafe;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.velocidad);
        
        
        //Sonido de la aplicacion
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        botonSonido = (ToggleButton)findViewById(R.id.btnSonido);
        AssetManager manager1 = this.getAssets();
        AssetManager manager2 = this.getAssets();
        
        //Obtengo los archivos MP3 de la carpeta Assets
        try
        {
        	//dsp1 = manager1.openFd("bajeVelocidad.mp3");
        	//dsp2 = manager2.openFd("infraccion.mp3");
        	
        	dsp1 = manager1.openFd("bajeVelocidad.wav");
        	dsp2 = manager2.openFd("infraccion.wav");
        	
        }catch(Exception e)
        {}
        
        //Definiendo componentes del MAPA
        mapa =(MapView)findViewById(R.id.Mapa);
        mapa.setBuiltInZoomControls(true);
        
        mapControl = mapa.getController();

        //
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
    	
    	locListener = new LocationListener() {
	    	public void onLocationChanged(Location location) {
	    		mostrarPosicion(location);
	    		//mostrarDireccion();
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
    			LocationManager.GPS_PROVIDER, 100, 0, locListener);
    	
    }
	
	/**
	 * Muestra la velocidad del dispositivo segun el provider del GPS
	 * @param loc Ubicacion del dispositivo
	 */
	 private void mostrarPosicion(Location loc) 
	 {	 
		 if(loc != null)   	
		 {
			latitud = loc.getLatitude();
	    	longitud = loc.getLongitude();
			velocidad = loc.getSpeed() * 3.6 ;
	    	
	   		lblVelocidad.setText("Velocidad:"+"\n" + String.valueOf(velocidad) + " km/h");
	   		
	   		Thread hilo = new Thread()
	   		{

				@Override
				public void run() {
					// TODO Auto-generated method stub
					int vel=0;
					while(vel < velocidad)
					{
						barraProgreso.setProgress((int)vel);
						try {
							wait(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						vel++;
					}
					super.run();
				}
	   		};
	   		hilo.start();
	   		
	   		barraProgreso.setProgress((int) velocidad);
	   		
	   		if(velocidad < 40 )
	   		{
	   			//Pintara la barra de color VERDE
	   			barraProgreso.setProgressDrawable(new ColorDrawable(Color.rgb(0, 255, 0))); 
	   		}	
	   		else if(40 <= velocidad  &&  velocidad < 60) 
	   		{
	   			//Pintara la barra de color NARANJA
	   			barraProgreso.setProgressDrawable(new ColorDrawable(Color.rgb(255, 165, 0)));
	   			if(botonSonido!= null && botonSonido.isChecked())
	   				iniciarSonido(dsp1);
	   		}
	   		else
	   		{
	   			//Pintara la barra de color ROJO
	   			barraProgreso.setProgressDrawable(new ColorDrawable(Color.rgb(255, 0, 0)));
	   			if(botonSonido!= null && botonSonido.isChecked())
	   				iniciarSonido(dsp2);
	   		}
	   			
	   		Log.i("", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
	   	}
		else
		{
	   		latitud = longitud = 0;
	   	}
    }
	
	 /**
	  * Muestra la direccion de ubicacion del dispositivo segun Google Maps
	  */
	private void mostrarDireccion()
	{
		List<Address> direcciones = null;
    	
    	Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());
		
		try {
			//Se recibe la lista de direcciones segun la latitud y longitud determinada
			direcciones = gc.getFromLocation(latitud, longitud, 1);
			
			if(direcciones != null && direcciones.size() > 0) {
				Address returnedAddress = direcciones.get(0);
				
				for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++)
				{
					direccion += returnedAddress.getAddressLine(i) + "\n";
				} 
				lblDireccion.setText(direccion);
			  }
			  else{
				  lblDireccion.setText("Direccion: No se encontro");
			  }
		
		} 
		catch (IOException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
			  lblDireccion.setText("Direccion: ERROR");
		}	
	}


	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	//-------------- SONIDO --------------------------------------
	/**
	 * Ejecuta un sonido 
	 * @param dsp Sonido a ejecutar
	 */
	private void iniciarSonido(AssetFileDescriptor dsp)
	{
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
}
