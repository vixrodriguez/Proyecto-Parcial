package com.app.drivesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AjustesActivity extends Activity {
	
	//Itent de menu
	private Intent velocidad, registro;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajustes);
        
        //Creando los menus de cada actividad
        velocidad = new Intent(this, new VelocidadActivity().getClass());
        registro = new Intent(this, new RegistroActivity().getClass());
        
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
	    getMenuInflater().inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId())
		{
			case R.id.MenuVelocidad:
				item.setIntent(velocidad);
				startActivity(velocidad);
				break;
			case R.id.MenuRegistro:
				item.setIntent(registro);
				startActivity(registro);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
