package es.unican.carchargers.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.unican.carchargers.R;
import es.unican.carchargers.activities.details.DetailsView;
import es.unican.carchargers.activities.info.InfoActivity;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;

@AndroidEntryPoint
public class MainView extends AppCompatActivity implements IMainContract.View {

    /** repository is injected with Hilt */
    @Inject IRepository repository;

    /** presenter that controls this view */
    IMainContract.Presenter presenter;

    //adaptador de la lista de cargadores
    private ChargersArrayAdapter adapterChargers;

    private List<Charger> listaChargers;
    private Spinner spinner;
    private ArrayAdapter<String> adapterSPN;
    private List<String> valores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize presenter-view connection
        presenter = new MainPresenter();
        presenter.init(this);

        spinner = findViewById(R.id.spnSort);
        spinner.setVisibility(View.INVISIBLE);
        // Define la lista de valores
         valores = new ArrayList<>(Arrays.asList("Ordena por:","Localizacion A-Z ↑", "Localizacion Z-A ↓","Empresa A-Z ↑", "Empresa Z-A ↓"));


        // Configura el adaptador para el Spinner
        adapterSPN = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, valores);
        adapterSPN.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSPN);

        // Agrega un Listener para el Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String sortType = parentView.getItemAtPosition(position).toString();
                switch(sortType)
                {
                    case "Localizacion A-Z ↑":

                        Collections.sort(listaChargers);
                        adapterChargers.notifyDataSetChanged();
                        eliminaEltoSpinner();
                        break;

                    case "Localizacion Z-A ↓":
                        Collections.sort(listaChargers,Collections.reverseOrder());
                        adapterChargers.notifyDataSetChanged();
                        eliminaEltoSpinner();
                        break;

                    case "Empresa A-Z ↑":

                        listaChargers.sort(new Comparator<Charger>() {
                            @Override
                            public int compare(Charger o1, Charger o2) {
                                return  o1.operator.title.compareTo(o2.operator.title);
                            }
                        });
                        adapterChargers.notifyDataSetChanged();
                        eliminaEltoSpinner();
                        break;

                    case "Empresa Z-A ↓":
                        listaChargers.sort(new Comparator<Charger>() {
                            @Override
                            public int compare(Charger o1, Charger o2) {
                                return  o2.operator.title.compareTo(o1.operator.title);
                            }
                        });
                        adapterChargers.notifyDataSetChanged();
                        eliminaEltoSpinner();
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada si no se selecciona nada
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemInfo:
                presenter.onMenuInfoClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void init() {
        // initialize listener to react to touch selections in the list
        ListView lv = findViewById(R.id.lvChargers);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.onChargerClicked(position);
            }
        });
    }

    @Override
    public IRepository getRepository() {
        return repository;
    }

    @Override
    public void showChargers(List<Charger> chargers) {
        adapterChargers = new ChargersArrayAdapter(this, chargers);
        listaChargers = chargers;

        ListView listView = findViewById(R.id.lvChargers);
        listView.setAdapter(adapterChargers);
        spinner.setVisibility(View.VISIBLE);

    }

    @Override
    public void showLoadCorrect(int chargers) {
        Toast.makeText(this, String.format("Cargados %d cargadores", chargers),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoadError() {
        Toast.makeText(this, "Error cargando cargadores", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showChargerDetails(Charger charger) {
        Intent intent = new Intent(this, DetailsView.class);
        intent.putExtra(DetailsView.INTENT_CHARGER, Parcels.wrap(charger));
        startActivity(intent);
    }

    @Override
    public void showInfoActivity() {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    public void eliminaEltoSpinner(){
        if (valores.contains("Ordena por:")){
            valores.remove("Ordena por:");
            adapterSPN.notifyDataSetChanged();
        }


    }


}