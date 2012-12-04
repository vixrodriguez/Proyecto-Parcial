package com.app.drivesafe;

import com.app.DataBase.Handler_sqlite;
import com.app.DataBase.TipoOrdenamiento;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class RegistroActivity extends Activity{

	Handler_sqlite handler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registros);
		
		//Aqui le especifico cual es el XML para el dise�o del activity
		
		/*
         * Obteniendo los datos del Spinner para agregar los items
         * del XML lista
         */		
		Spinner sp = (Spinner)findViewById(R.id.listaOrdenamiento);
		
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				ListView list = (ListView) findViewById(R.id.listaRegistro);
				mostrarListaRegistro(list);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		ArrayAdapter<CharSequence> adp = ArrayAdapter.createFromResource
		(this, R.array.lista, android.R.layout.simple_spinner_item);
		
		sp.setAdapter(adp);
		
		
		/*sp.setOnItemSelectedListener(new OnItemSelectedListener() 
		{

			@SuppressLint("ShowToast")
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(this, sp.getSelectedItem().toString(), Toast.LENGTH_LONG);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		}
		);*/
		
		handler = new Handler_sqlite(this); //Aqui instancia la base de datos
		handler.abrir();
		handler.ingresarRegistro(73.5, "30/11/2012","10:17", "Avenida del Bombero");
		handler.cerrar();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		/*handler.abrir(); //Abro la base de datos
		
		ListView list = (ListView) findViewById(R.id.listaRegistro);
		this.mostrarListaRegistro(list);
		handler.cerrar();//Cierro la base de datos*/
	}
	    
	public void mostrarListaRegistro(ListView list)
	{
		TipoOrdenamiento t;
		Spinner sp = (Spinner) findViewById(R.id.listaOrdenamiento);
		
		//Le asigno el valor de ordenamiento segun el escogido en el Spinner
		if(sp.getSelectedItem().toString().equalsIgnoreCase("Velocidad"))
			t = TipoOrdenamiento.PorVelocidad;
		else if(sp.getSelectedItem().toString().equalsIgnoreCase("Hora"))
			t = TipoOrdenamiento.PorHora;
		else 
			t = TipoOrdenamiento.PorFecha;
		
		//Se obtiene los valores de la tabla segun el parametro de ordenamiento
		String registro[] = handler.consultarRegistro(t);
		 
		list.setAdapter(
		new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, registro));
	}
}
