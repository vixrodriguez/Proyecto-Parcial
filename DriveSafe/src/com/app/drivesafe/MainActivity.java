package com.app.drivesafe;

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

/**
 * Actividad principa que se encarga del trapaso
 * entre actividades por medio los Tabs de la pantalla
 * @author Victor S. Rodriguez Cabrera
 *
 */
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
        
        this.activarGPS(); //Muestra las configuraciones
        this.activarWiFi(); //Activa el Wi-Fi
    }
    
    /**
     * Muestra la ventana de configuraciones para activar las Redes Inalambricas y el GPS
     */
    public void activarGPS()
    {
    	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    	startActivity(intent);
    }
    
    /**
     * Metodo para habilitar el Wi-Fi
     */
    private void activarWiFi()
    {
    	WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE); 
    	wifi.setWifiEnabled(true);
    }
}

