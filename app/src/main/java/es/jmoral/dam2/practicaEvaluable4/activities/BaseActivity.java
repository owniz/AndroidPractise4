package es.jmoral.dam2.practicaEvaluable4.activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Heredamos de AppCompatActivity para añadir un método que ponga las actividadaes en pantalla
 * completa (modo inmersivo)
 */

public class BaseActivity extends AppCompatActivity {

    // método para modo inmersivo
    protected void setImmersiveMode() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );

        // si es superior a 18 recuperamos la configuración anterior y además añadimos otra opción
        // para esas versiones
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            getWindow().getDecorView().setSystemUiVisibility(
                    getWindow().getDecorView().getSystemUiVisibility()
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
    }
}
