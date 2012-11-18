package com.app.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;		//Crea la base de datos
import android.database.sqlite.SQLiteOpenHelper;	//Manipula la base de datos

import static android.provider.BaseColumns._ID;

public class Handler_sqlite extends SQLiteOpenHelper{

	public Handler_sqlite(Context context) {
		super(context, "dbDS", null, 1);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Crea la base de datos desde que inicia la App
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		/*Se crea la tabla donde se almacenara los datos de las infracciones
		cometidas por el conductor*/
		
		String query = "CREATE TABLE Conductor("+
		_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
		"velocidad NUMERIC," + 
		"fecha TEXT," +
		"horaActual TEXT," +
		"lugar TEXT" +
		")";
		
		db.execSQL(query); //Ejecuta la sentencia de creacion de la tabla
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Metodo que permite la insercion de registros en la base de datos
	 * @param velocidad Velocidad actual que lleva el conductor
	 * @param fecha Fecha que se cometio la infraccion
	 * @param horaActual Hora en que se produjo la infraccion
	 * @param lugar Lugar que se cometio la infraccion
	 */
	public void ingresarRegistro
	(double velocidad, String fecha, String horaActual, String lugar )
	{
		//Se instancia un contenedor de valores para el registro de los datos
		ContentValues valores = new ContentValues();
		
		//Agregando los datos obtenidos con sus respectivos atributos de la DB
		valores.put("velocidad", velocidad);
		valores.put("fecha", fecha);
		valores.put("horaActual", horaActual);
		valores.put("lugar", lugar);
		
		//Agrega el conjunto de valores a la base de datos
		this.getWritableDatabase().insert("Conductor", null, valores);
	}
	
	/**
	 * Metodo que realiza la consulta de toda la tabla de la BBDD
	 */
	@SuppressWarnings("null")
	public String[] consultarRegistro()
	{
		int id, vel, f, h, l, indice=0;
		String registro[], temp; //Referencia que apuntara al registro actual que se encuentre
		
		//String que especifica que columnas deseo obtener para el SELECT
		String columnas[] = {_ID,"velocidad","fecha","horaActual","lugar"};
		
		//Obtengo el curso de la tabla que contiene el query solicitado
		Cursor c = this.getReadableDatabase().query("Conductor",columnas,null,null,null,null,null);
		
		//Obtengo el indice de cada uno de los registros de la tabla
		id = c.getColumnIndex(_ID);
		vel = c.getColumnIndex("velocidad");
		f = c.getColumnIndex("fecha");
		h = c.getColumnIndex("horaActual");
		l = c.getColumnIndex("lugar");
		
		//Mientras tenga filas, se sigue desplazando el curso
		//y obteniendo los datos
		registro = new String[c.getCount()];
		for( c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
		{
			temp = "Id: "+ c.getString(id) + "\n"+
					 "Velocidad: " + c.getString(vel) + "\n" +
					 "Fecha: " + c.getString(f) + "\n" +
					 "horaActual: " + c.getString(h) + "\n" +
					 "Lugar: " + c.getString(l) + "\n\n\n";
			registro[indice] = temp;
			indice++;
		}
		return registro;
	}
	/**
	 * Metodo que permite abrir la base de datos
	 */
	public void abrir()
	{
		this.getWritableDatabase();
	}
	
	/**
	 * Metodo que permite cerrar la base de datos
	 */
	public void cerrar()
	{
		this.close();
	}

}
