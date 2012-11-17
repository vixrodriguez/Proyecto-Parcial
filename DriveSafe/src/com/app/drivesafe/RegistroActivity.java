package com.app.drivesafe;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RegistroActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registros);
		//Aqui le especifico cual es el XML para el diseño del activity
		
		/*
         * Obteniendo los datos del Spinner para agregar los items
         * del XML lista
         */		
		Spinner sp = (Spinner)findViewById(R.id.listaOrdenamiento);
		ArrayAdapter<CharSequence> adp = ArrayAdapter.createFromResource
		(this, R.array.lista, android.R.layout.simple_spinner_item);
		
		sp.setAdapter(adp);
	}
}
