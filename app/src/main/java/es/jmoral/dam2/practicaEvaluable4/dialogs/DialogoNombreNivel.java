package es.jmoral.dam2.practicaEvaluable4.dialogs;

import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import es.jmoral.dam2.practicaEvaluable4.R;

/**
 * En esta clase heredaremos de DialogFragment para crear y gestionar el comportamiento
 * de un dialogo personalizado
 */

public class DialogoNombreNivel extends DialogFragment implements DialogInterface.OnClickListener {

    // listener
    private OnDialogoNombreNivel escuchador;

    // componentes
    private EditText etNombre;
    private RadioGroup radioGroup;

    // crea instancias del dialogo
    public static DialogoNombreNivel newInstance() {
        return new DialogoNombreNivel();
    }

    // inflamos los componentes del dialogo
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.nueva_partida)
                .setView(inflater.inflate(R.layout.dialogo_nombre_nivel, null)) // asignamos nuestro layout personalizado
                .setCancelable(false) // evitamos que pueda pulsarse fuera de el o apretar el boton back
                .setPositiveButton(R.string.jugar, this)
                .setNegativeButton(R.string.inicio, this);

        return builder.create();
    }

    // acción de los botones
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        // inicializamos los componentes
        etNombre = (EditText) ((Dialog) dialogInterface).findViewById(R.id.editTextNombre);
        radioGroup = (RadioGroup) ((Dialog) dialogInterface).findViewById(R.id.radioGroup);

        // recuperamos el nombre introducido por el usuario
        String nombre = etNombre.getText().toString();

        // velocidad del juego y dificultad inicial
        int velocidad = 0;
        int opcionDificultad = radioGroup.getCheckedRadioButtonId();

        // según el RadioButton seleccionado definimos la dificultad (menor número es más dificil)
        switch(opcionDificultad) {
            case R.id.radioButtonFacil:
                velocidad = 60;
                break;
            case R.id.radioButtonMedio:
                velocidad = 40;
                break;
            case R.id.radioButtonDificil:
                velocidad = 20;
                break;
        }

        switch(i) {

            // si pulsa JUGAR llamamas al método para empezar el juego
            case DialogInterface.BUTTON_POSITIVE:
                escuchador.onAceptarDialogo(nombre, velocidad);
                break;

            // si pulsa VOLVER vuelve a la pantalla principal
            case DialogInterface.BUTTON_NEGATIVE:
                getActivity().finish();
                break;
        }
    }

    // interfaz para cuando se pulse el boton JUGAR del dialogo
    public interface OnDialogoNombreNivel {
        void onAceptarDialogo(String nombre, int velocidad);
    }

    // implementa el listener
    public void setOnDialogoNombreNivel(OnDialogoNombreNivel escuchador) {
        this.escuchador = escuchador;
    }
}
