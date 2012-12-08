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

/**
 * Actividad que muestra y ordena los datos
 * de la base de datos SQLite de las infracciones
 * cometidas por el usuario.
 * @author Victor S. Rodriguez Cabrera
 *
 */
public class RegistroActivity extends Activity{

	Handler_sqlite handler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registros);
		handler = new Handler_sqlite(this); //Aqui instancia la base de datos
		//Aqui le especifico cual es el XML para el diseño del activity
		
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
		
		this.setProgressBarVisibility(true);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		//Me permite actualizar la base constantemente 
		handler.abrir(); //Abro la base de datos
		
		ListView list = (ListView) findViewById(R.id.listaRegistro);
		this.mostrarListaRegistro(list);
		handler.cerrar();//Cierro la base de datos*/
	}
	
	/**
	 * Muestra los registros en el ListView obtenido desde la BBDD 
	 * @param Recibe la lista en donde se almacenara los datos
	 */
	public void mostrarListaRegistro(ListView list)
	{
		handler.abrir();
		
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
		
		handler.cerrar();
	}
}
