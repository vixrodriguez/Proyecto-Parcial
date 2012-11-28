package com.app.drivesafe;

import android.os.Bundle;
//import android.app.Activity;
import android.app.TabActivity;

public class MainActivity extends TabActivity {

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        /*TabHost host = getTabHost();
        //Añadimos cada tab, que al ser pulsadas abren sus respectivas Activities
        host.addTab(host.newTabSpec("Velocidad").setIndicator("Velocidad").setContent(new Intent(this, VelocidadActivity.class)));
        host.addTab(host.newTabSpec("Registro").setIndicator("Registros").setContent(new Intent(this, RegistroActivity.class)));
        host.addTab(host.newTabSpec("Ajustes").setIndicator("Ajustes").setContent(new Intent(this, AjustesActivity.class)));*/
    
        
    }
}
