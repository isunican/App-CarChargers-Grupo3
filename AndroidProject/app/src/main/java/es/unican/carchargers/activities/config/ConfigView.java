package es.unican.carchargers.activities.config;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.R;


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
        chargerTypes.add("TJ-45");
        chargerTypes.add("XF-93");
        chargerTypes.add("SW-21");



        Spinner spinner = findViewById(R.id.spnChargerType);
        ArrayAdapter<String> adapterSPN = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, chargerTypes);
        adapterSPN.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSPN);

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