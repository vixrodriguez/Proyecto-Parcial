package com.app.drivesafe;

import com.app.DataBase.Handler_sqlite;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

/**
 * Actividad que maneja los ajustes que requiere el usuario
 * @author Victor S. Rodriguez Cabrera
 *
 */
public class AjustesActivity extends Activity {
	
	private Handler_sqlite handler;
	private Button btnBorrar;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajustes);
        
        handler = new Handler_sqlite(this);
        btnBorrar = (Button) findViewById(R.id.btnBorrar);
        //En caso de que pulse sobre el borraran los registros
        btnBorrar.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				handler.abrir();
				handler.borrar();
				handler.cerrar();
				return false;
			}
		});
    }
}
