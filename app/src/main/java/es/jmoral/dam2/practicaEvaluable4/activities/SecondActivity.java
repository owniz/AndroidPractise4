package es.jmoral.dam2.practicaEvaluable4.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import es.jmoral.dam2.practicaEvaluable4.dialogs.DialogoNombreNivel;
import es.jmoral.dam2.practicaEvaluable4.R;

/**
 * Pantalla del juego en la que nos aparecerá nuestro dialogo personalizado con las opciones de la
 * partida, una vez definidas empezaremos a jugar
 */

public class SecondActivity extends BaseActivity implements DialogoNombreNivel.OnDialogoNombreNivel, View.OnClickListener {

    // constantes para guardar la velocidad y el progreso actual
    private static final String KEY_VELOCIDAD = "KEY_VELOCIDAD";
    private static final String KEY_PROGRESO = "KEY_PROGRESO";

    // componentes de la actividad
    private TextView tvNombre, tvFase, tvProgreso;
    private Button boton1, boton2, boton3, boton4;
    private ProgressBar progressBar;

    // instancia de nuestra clase para gestionar las tareas
    private ControlProgesoTask controlProgesoTask;

    // variables del juego
    private ArrayList<Button> arrayBotones;
    private int numFase, contadorBotones, progreso, velocidad;

    // bundle para guardar el estado del juego
    private Bundle bundleEstado;

    // boolean para saber si la prtida esta en curso o no
    private boolean partidaAcabada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // pantalla completa
        setImmersiveMode();

        // inicializamos los componentes
        setUpViews();

        // mostramos el dialogo inicial
        setDialogo();

        // seteamos los listeners
        setlisteners();
    }

    // método para inicializar y configurar los componentes
    private void setUpViews() {

        // componentes de la actividad
        tvNombre = (TextView) findViewById(R.id.textViewTitulo);
        tvFase = (TextView) findViewById(R.id.textViewFase);
        tvProgreso = (TextView) findViewById(R.id.textViewProgreso);

        // botones
        boton1 = (Button) findViewById(R.id.buttonTopLeft);
        boton2 = (Button) findViewById(R.id.buttonTopRight);
        boton3 = (Button) findViewById(R.id.buttonBottomLeft);
        boton4 = (Button) findViewById(R.id.buttonBottomRight);

        // barra de progreso
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // color de la barra
        progressBar.getProgressDrawable().setColorFilter(
                Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);

        // groso de la barra
        progressBar.setScaleY(3f);

        // añadimos los botones a un ArrayList de botones
        arrayBotones = new ArrayList<>();
        arrayBotones.add(boton1);
        arrayBotones.add(boton2);
        arrayBotones.add(boton3);
        arrayBotones.add(boton4);

        // numeramos y desordenamos los botones
        numerarBotones(desordenarArray());
    }

    // seteamos los listeners de los botones
    private void setlisteners() {
        boton1.setOnClickListener(this);
        boton2.setOnClickListener(this);
        boton3.setOnClickListener(this);
        boton4.setOnClickListener(this);
    }

    // muestra el dialogo y asigna la interfaz implementada
    private void setDialogo() {
        DialogoNombreNivel dialogoNombreNivel = DialogoNombreNivel.newInstance();
        dialogoNombreNivel.setOnDialogoNombreNivel(this);
        dialogoNombreNivel.setCancelable(false);
        dialogoNombreNivel.show(getSupportFragmentManager(), null);
    }

    // define el comportamiento cuando aceptamos el dialogo gracias a la interfaz que hemos creado
    // en la clase DialogoNombreNivel
    @Override
    public void onAceptarDialogo(String titulo, int velocidad) {

        // partida iniciado
        partidaAcabada = false;

        // estado inicial de las variables del juego
        this.velocidad = velocidad;
        progreso = 0;
        contadorBotones = 1;
        numFase = 1;

        // modo immersivo
        setImmersiveMode();

        // si el EditText del nombre no esta vacío lo escribimos sustituyendo el título del juego
        if(!titulo.trim().isEmpty())
            tvNombre.setText(titulo);

        // vamos indicando en que fase se encuentra el jugador
        tvFase.setText(getString(R.string.fase, String.valueOf(numFase)));

        // instaciamos una tarea y la ejecutamos
        controlProgesoTask = new ControlProgesoTask();
        controlProgesoTask.execute(this.velocidad, progreso);
    }

    // definimos comportamiento de los botones del juego
    @Override
    public void onClick(View view) {
        Button botonPresionado = (Button) view;

        // cada vez que pulsamos un botón se suma +1 al contador de éste, cuando llega a 5
        // cancelamos la tarea
        if(Integer.valueOf(botonPresionado.getText().toString()) == contadorBotones) {
            botonPresionado.setEnabled(false);
            contadorBotones++;

            if(contadorBotones == 5)
                controlProgesoTask.cancel(false);
        }
    }

    // numera y activa los botones
    private void numerarBotones(ArrayList<Integer> arrayEnteros) {
        for(int i = 0; i < arrayEnteros.size(); i ++) {
            arrayBotones.get(i).setEnabled(true);
            arrayBotones.get(i).setText(String.valueOf(arrayEnteros.get(i)));
        }
    }

    // devolvemos el ArrayList que contiene el texto de los botones desordenado
    private static ArrayList<Integer> desordenarArray() {
        ArrayList<Integer> arrayEnteros = new ArrayList<>();

        for(int i = 1; i <= 4; i++) {
            arrayEnteros.add(i);
        }

        Collections.shuffle(arrayEnteros);

        return arrayEnteros;
    }

    // si la tarea está ejecutandose guardamos el estado del juego para poder recuperarlo
    // por si por ejemplo entra una llamada o minimizas el juego
    @Override
    protected void onPause() {
        super.onPause();

        if(controlProgesoTask != null) {
            controlProgesoTask.cancel(true);
            controlProgesoTask = null;
            bundleEstado = new Bundle();
            bundleEstado.putInt(KEY_VELOCIDAD, velocidad);
            bundleEstado.putInt(KEY_PROGRESO, progreso);
        }
    }

    // recuperamos el estado del juego para volver a iniciarlo
    @Override
    protected void onResume() {
        super.onResume();

        if(bundleEstado != null && !partidaAcabada) {
            controlProgesoTask = new ControlProgesoTask();
            controlProgesoTask.execute(bundleEstado.getInt(KEY_VELOCIDAD), bundleEstado.getInt(KEY_PROGRESO));
        }
    }

    // clase que gestiona el progreso del juego
    private class ControlProgesoTask extends AsyncTask<Integer, Integer, Integer> {

        // devuelve el progreso de la barra
        @Override
        protected Integer doInBackground(Integer... integers) {
            while(progreso <= 100) {
                SystemClock.sleep(integers[0]);
                controlProgesoTask.publishProgress(progreso);
                progreso++;

                // si se cancela sale del bucle
                if(isCancelled())
                    break;
            }

            return progreso;
        }

        // actualiza el progreso de la barra y lo muestra en el TextView
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
            tvProgreso.setText(values[0] + getString(R.string.slash) + progressBar.getMax());
        }

        // cuando la barra de progreso llega a 100 termina el juego por lo que muestra un dialogo
        // indicando hasta que fase has llegado
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            // termina partida
            partidaAcabada = true;

            // dialogo con la información
            new AlertDialog.Builder(SecondActivity.this)
                    .setTitle(R.string.fin)

                    // si anteriormente no ha elegido un nombre no lo muestra en el dialogo
                    .setMessage(getString(R.string.enhorabuena,
                            tvNombre.getText().toString().equals(getString(R.string.random_buttons)) ?
                            getString(R.string.empty) : getString(R.string.space) + tvNombre.getText()) + numFase)
                    .setPositiveButton(R.string.continuar, new DialogInterface.OnClickListener() {

                        // si pulsa CONTINUAR JUGANDO volvemos al dialogo con las opciones de partida
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            tvNombre.setText(getString(R.string.random_buttons));
                            setDialogo();
                            numerarBotones(desordenarArray());
                        }
                    })
                    .setNegativeButton(R.string.inicio, new DialogInterface.OnClickListener() {

                        // si pulsa VOLVER regresamos a la pantalla principal
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }

        // si pulsamos los 4 botones correctamente termina la tarea e inicia una nueva con
        // mayor velocidad
        @Override
        protected void onCancelled(Integer integer) {
            super.onCancelled(integer);

            // comprobamos que haya pulsado los 4 botones correctamente para volver a colocar
            // todas las variables y opciones del juego
            if(contadorBotones == 5) {
                controlProgesoTask = new ControlProgesoTask();

                numerarBotones(desordenarArray());
                tvFase.setText(getString(R.string.fase, String.valueOf(++numFase)));
                contadorBotones = 1;
                progressBar.setProgress(0);
                progreso = 0;

                // nos aseguramos que nunca sea un número negativo el tiempo de espera del proceso
                // y vamos restando el tiempo en 5 milisegundos
                velocidad = velocidad - 5 <= 0 ? 1 : velocidad - 5;

                // ejecutamos una nueva tarea (nueva fase)
                controlProgesoTask.execute(velocidad, progreso);
            }
        }
    }
}
