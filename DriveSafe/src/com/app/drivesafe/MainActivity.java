package com.app.drivesafe;

import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

public class MainActivity extends TabActivity {
	
    @SuppressLint("ShowToast")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TabHost host = getTabHost();
        TabSpec t1, t2, t3;
        Intent i1, i2, i3;
        Resources res = getResources();
        
        //Añadimos cada tab, que al ser pulsadas abren sus respectivas Activities
        i1 = new Intent().setClass(this, VelocidadActivity.class);
        i2 = new Intent().setClass(this, RegistroActivity.class);
        i3 = new Intent().setClass(this, AjustesActivity.class);
        
        t1 = host.newTabSpec("Velocidad").setIndicator("Velocidad", res.getDrawable(R.drawable.ico_velocimetro)).setContent(i1);
        t2 = host.newTabSpec("Registros").setIndicator("Registros", res.getDrawable(R.drawable.ico_registro)).setContent(i2);
        t3 = host.newTabSpec("Ajustes").setIndicator("Ajustes", res.getDrawable(R.drawable.ico_ajustes)).setContent(i3);
        
        host.addTab(t1);
        host.addTab(t2);
        host.addTab(t3);
        
        this.activarGPS();
        this.activarWiFi();
        //this.activarRedes();
        //AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Toast.makeText(this, "Estado: " +this.netCheckin(), Toast.LENGTH_LONG).show();
        //this.turnGPSOnOff();
        //Toast toast = new Toast(this);
        
    }
    
    public void activarGPS()
    {
    	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    	startActivity(intent);
    }
    
    /**
     * Metodo que activa el GPS al abrir la aplicacion
     */
    private void turnGPSOnOff(){
    	  String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
    	  if(!provider.contains("gps")){
    	    final Intent poke = new Intent();
    	    poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
    	    poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
    	    poke.setData(Uri.parse("3")); 
    	    sendBroadcast(poke);
    	    //Toast.makeText(this, "Your GPS is Enabled",Toast.LENGTH_SHORT).show();
    	  }
    }
    
    /**
     * Metodo para habilitar el Wi-Fi
     */
    private void activarWiFi()
    {
    	WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE); 
    	wifi.setWifiEnabled(true);
    }
    
    
    private void activarRedes()
    {
    	LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	
    	locManager.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);
    	
    }
}

