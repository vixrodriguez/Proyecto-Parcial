package com.app.drivesafe;

import android.app.Activity;
import android.os.Bundle;
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
}
