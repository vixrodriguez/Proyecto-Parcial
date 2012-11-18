package com.app.drivesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AjustesActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajustes);
        
        /*
         * Obteniendo los datos del Spinner para agregar los items
         * del XML listaHistorial
         */
        Spinner sp = (Spinner)findViewById(R.id.listaBorrarHistorial);
		ArrayAdapter<CharSequence> adp = ArrayAdapter.createFromResource
		(this, R.array.listaBorrarHistorial, android.R.layout.simple_spinner_item);
		
		sp.setAdapter(adp);
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
	   	
	   	//4. 
	   	menu1.setTitle("Velocidad");
	   	menu2.setTitle("Registro");
	   	menu3.setTitle("Ajustes");
	   	
	   	menu1.setIntent(new Intent(this, VelocidadActivity.class));
	   	menu2.setIntent(new Intent(this, RegistroActivity.class));
	   	menu3.setIntent(new Intent(this, AjustesActivity.class));
	}
}
