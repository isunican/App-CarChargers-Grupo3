package es.unican.carchargers.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.R;
import es.unican.carchargers.model.Charger;




public class UserView extends AppCompatActivity  {

   // public static final String INTENT_CHARGER = "INTENT_CHARGER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText("Usuario");
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