package com.app.drivesafe;


import com.google.android.maps.MapActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;


public class VelocidadActivity extends MapActivity {

	private double latitud, longitud, precision, velocidad;
	
	private TextView lblVelocidad;
	private CheckBox chkEstado;
	private SeekBar barraProgreso;
	
	private LocationManager locManager;
	private LocationListener locListener;
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.velocidad);
        
        chkEstado = (CheckBox) findViewById(R.id.chkEstado);
        chkEstado.setText("Proveedor: OFF");
        chkEstado.setEnabled(false);
        
        lblVelocidad = (TextView) findViewById(R.id.lblVelocidad);
        lblVelocidad.setText("Velocidad:" + "\n0.0 km/h");
        
        barraProgreso = (SeekBar) findViewById(R.id.barraProgreso);
        barraProgreso.setEnabled(false);
        
        comenzarLocalizacion();
	}
	
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
    	
    	//Nos registramos para recibir actualizaciones de la posición
    	locListener = new LocationListener() {
	    	public void onLocationChanged(Location location) {
	    		mostrarPosicion(location);
	    	}
	    	public void onProviderDisabled(String provider){
	    		chkEstado.setText("Proveedor: OFF");
	    		chkEstado.setChecked(false);
	    	}
	    	public void onProviderEnabled(String provider){
	    		chkEstado.setText("Proveedor: ON ");
	    		chkEstado.setChecked(true);
	    	}
	    	public void onStatusChanged(String provider, int status, Bundle extras){
	    		Log.i("", "Provider Status: " + status);
	    	}
    	};
    	
    	locManager.requestLocationUpdates(
    			LocationManager.GPS_PROVIDER, 300, 0, locListener);
    }
	
	 private void mostrarPosicion(Location loc) {
		 if(loc != null)   	
		 {
			
			latitud = loc.getLatitude();
	    	longitud = loc.getLongitude();
	   		precision = loc.getAccuracy();
	   		velocidad = loc.getSpeed() * 3.6 ;
	   		lblVelocidad.setText("Velocidad:"+"\n" + String.valueOf(velocidad) + " km/h");
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
	   		}
	   		else
	   		{
	   			//Pintara la barra de color ROJO
	   			barraProgreso.setProgressDrawable(new ColorDrawable(Color.rgb(255, 0, 0)));
	   		}
	   			
	   		
	   		Log.i("", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));
	   		
	   	}
		 else
		 {
	   		latitud = longitud = precision = 0;
	   	}
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    //getMenuInflater().inflate(R.menu.activity_velocidad, menu);
	  	crearMenuOpciones(menu);
	    return true;
	}
	    
	public void crearMenuOpciones(Menu menuOpciones)
	{
	 	//1.Creo una instancia de menu
		//group, id, orden 
	   	MenuItem menu1 = menuOpciones.add(0,0,0,"Velocidad");
	   	MenuItem menu2 = menuOpciones.add(0,1,1,"Registro");
	   	MenuItem menu3 = menuOpciones.add(0,2,2,"Ajustes");
	   	
	   	//2. seteo una tecla de atajo
	   	menu1.setAlphabeticShortcut('V'); //tecla de acceso
	   	menu2.setAlphabeticShortcut('R');
	   	menu3.setAlphabeticShortcut('A');
	   	
	   	//3. Seteo Iconos
	   	menu1.setIcon(R.drawable.ico_velocimetro);
	   	menu2.setIcon(R.drawable.ico_registro);
	   	menu3.setIcon(R.drawable.ico_ajustes);
	   	
	   	menu1.setIntent(new Intent(this, VelocidadActivity.class));
	   	menu2.setIntent(new Intent(this, RegistroActivity.class));
	   	menu3.setIntent(new Intent(this, AjustesActivity.class));
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
