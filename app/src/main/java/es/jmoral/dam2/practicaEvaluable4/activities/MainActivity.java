package es.jmoral.dam2.practicaEvaluable4.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.manolovn.colorbrewer.ColorBrewer;
import com.manolovn.trianglify.TrianglifyView;
import es.jmoral.dam2.practicaEvaluable4.R;
import es.jmoral.dam2.practicaEvaluable4.utils.GeneradorColor;

/**
 * Pantalla principal del juego en la que podremos elegir si jugar o ver las instrucciones
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {

    // librería para generar el fondo de la actividad
    private TrianglifyView trianglifyView;

    // imagenes para usar de botones
    private ImageView ivJugar;
    private ImageView ivInstrucciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // pantalla completa
        setImmersiveMode();

        // inicializamos los componentes y el fondo
        setUpViews();

        // seteamos los listeners
        setListeners();
    }

    // método que inicializa los componentes
    private void setUpViews() {

        // inicialzia fondo e imagenes
        trianglifyView = (TrianglifyView) findViewById(R.id.trianglify);
        ivJugar = (ImageView) findViewById(R.id.imageViewJugar);
        ivInstrucciones = (ImageView) findViewById(R.id.imageViewInstrucciones);

        // define los colores del fondo de forma aleatoria a elegir entre 2 opciones
        trianglifyView.getDrawable().setColorGenerator(
                  new GeneradorColor(ColorBrewer.values()[Math.random() > 0.5 ? 4 : 8]));

        // animación del logo del juego
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animacion_titulo);
        findViewById(R.id.imageViewTitulo).startAnimation(animation);
    }

    // setea los listeners para las imagenes
    private void setListeners() {
        ivJugar.setOnClickListener(this);
        ivInstrucciones.setOnClickListener(this);
    }

    // comportamientos al pulsar las imagenes
    @Override
    public void onClick(View view) {
        int opcionImagenPulsada = view.getId();

        switch(opcionImagenPulsada) {

            // si pulsa JUGAR pasamos a la siguiente actividad
            case R.id.imageViewJugar:
                startActivity(new Intent(this, SecondActivity.class));
                break;

            // si pulsa INSTRUCCIONES mostramos un dialogo con las instrucciones del juego
            // al pulsar ACEPTAR volvemos a poner el modo inmersivo
            case R.id.imageViewInstrucciones:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.como_jugar)
                        .setMessage(R.string.explicacion)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setImmersiveMode();
                            }
                        })
                        .setCancelable(false)
                        .show();
                break;
        }
    }

    // ponemos el modo inmersivo cuando volvamos desde otra actividad
    @Override
    protected void onResume() {
        super.onResume();
        setImmersiveMode();
    }
}
