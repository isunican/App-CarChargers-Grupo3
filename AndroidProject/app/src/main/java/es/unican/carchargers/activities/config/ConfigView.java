package es.unican.carchargers.activities.config;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.R;
import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.constants.ECharger;
import es.unican.carchargers.constants.EOperator;


public class ConfigView extends AppCompatActivity  {



    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_view);

        actionBar = getSupportActionBar();

        // Establece el nuevo nombre para la ActionBar
        if (actionBar != null) {
            actionBar.setTitle("Configuración");
        }



        List<String> chargerTypes = new ArrayList<>();
        for (int i = 0; i < ECharger.values().length; i++) {

            /*if (ECharger.values()[i].toString().equalsIgnoreCase("GENERIC")){

                chargerTypes.add("TODOS");

            } else {

             */


                chargerTypes.add(ECharger.values()[i].toString());
            //}
        }
        for (String s:chargerTypes){
            Log.d("[DEBUG SPINNER]", s );
        }




        Spinner spinner = findViewById(R.id.spnChargerType);
        ArrayAdapter<String> adapterSPN = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, chargerTypes);
        adapterSPN.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSPN);
        SharedPreferences sharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selection = spinner.getSelectedItem().toString();
                int idSelection;
                /*
                if(selection.equalsIgnoreCase("TODOS")){
                    idSelection = -1;
                } else {


                 */

                    idSelection = ECharger.valueOf(selection).id;
                //}
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("charger-type", selection);


                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // En caso de que no se seleccione nada
            }
        });

        // Para cargar el valor guardado (puedes hacerlo en el método onCreate)
        String valorGuardado = sharedPreferences.getString("charger-type", "");
        if (!valorGuardado.isEmpty()) {
            int index = adapterSPN.getPosition(valorGuardado);
            if (index != -1) {
                spinner.setSelection(index);
            }
        }

        // Agrega un Listener para el Spinner
        /*
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String elem = parentView.getItemAtPosition(position).toString();
                switch(elem)
                {
                    case "":
                        break;


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada si no se selecciona nada
            }
        });
        
         */


    }




}