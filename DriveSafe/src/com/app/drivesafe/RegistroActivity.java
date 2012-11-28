package com.app.drivesafe;

import com.app.DataBase.Handler_sqlite;
import com.app.DataBase.TipoOrdenamiento;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
		handler.ingresarRegistro(20.0, "16/11/2012","13:15", "Guayaquil");
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
	   	
	   	//4. Asigno los activity que se ejecutaran por cada opcion
	   	menu1.setIntent(new Intent(this, VelocidadActivity.class));
	   	menu2.setIntent(new Intent(this, RegistroActivity.class));
	   	menu3.setIntent(new Intent(this, AjustesActivity.class));
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
