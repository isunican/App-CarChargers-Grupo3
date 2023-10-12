package es.unican.carchargers.activities.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.unican.carchargers.R;
import es.unican.carchargers.activities.details.DetailsView;
import es.unican.carchargers.activities.info.InfoActivity;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;
import pl.droidsonroids.gif.GifImageView;

@AndroidEntryPoint
public class MainView extends AppCompatActivity implements IMainContract.View {

    /** repository is injected with Hilt */
    @Inject IRepository repository;

    /** presenter that controls this view */
    IMainContract.Presenter presenter;
    private GifImageView loading;
    private ImageView logo;
    private ChargersArrayAdapter adapterChargers;
    private List<Charger> listaChargers;
    private ArrayAdapter<String> adapterSPN;
    private List<String> valores;
    private FusedLocationProviderClient fusedLocationClient;

    private double userLat, userLon;
    private TextView ubi;
    private TextView infoUbi;
    private String[] filtros;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize presenter-view connection
        presenter = new MainPresenter();
        presenter.init(this);

        ubi = findViewById(R.id.tvUbi);
        infoUbi = findViewById(R.id.tvInfoUbi);

        loading = findViewById(R.id.imgLoading);
        loading.setVisibility(View.VISIBLE);

        logo = findViewById(R.id.imgLogo);
        logo.setVisibility(View.INVISIBLE);




        fusedLocationClient = new FusedLocationProviderClient(this);

        infoUbi.setVisibility(View.INVISIBLE);
        ubi.setVisibility(View.INVISIBLE);


        filtros = new String[EOperator.values().length];
        for (int i = 0; i < EOperator.values().length; i++){
            filtros[i] = EOperator.values()[i].toString();
        }





        //Pide permisos
        if (checkLocationPermission()) {
            // Ya tienes permisos de ubicación, puedes solicitar la ubicación.
            obtenerUbicacion();

        } else {
            // Si no tienes permisos, solicítalos al usuario.
            requestLocationPermission();
        }






        infoUbi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                obtenerUbicacion();
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
            case R.id.menuItemFiltro:
                mostrarDialogoFiltros();
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
        logo.setVisibility(View.VISIBLE);
        loading.setVisibility(View.INVISIBLE);
        infoUbi.setVisibility(View.VISIBLE);
        ubi.setVisibility(View.VISIBLE);
        if (userLat != 0.0 && userLon != 0.0){
            ordenaPorUbi(0);
        }




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





    private boolean checkLocationPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) return true;
        else return false;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1
        );
    }




    public void obtenerUbicacion() {
        //Toast.makeText(MainView.this, "Obtener ubicacion ejecutado", Toast.LENGTH_SHORT).show();

        if (checkLocationPermission()) {
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                Location location = task.getResult();
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();


                                //Toast.makeText(MainView.this, "Latitud: " + latitude + ", Longitud: " + longitude, Toast.LENGTH_SHORT).show();
                                userLat = latitude;
                                userLon = longitude;
                                infoUbi.setText("Ubicación ✓");
                                ubi.setText(userLat + "\n" + userLon);

                            } else {
                                // ubicación no disponible
                                Toast.makeText(MainView.this, "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                                requestLocationPermission();
                                mostrarDialogoUbicacion();

                            }
                        }
                    });
        } else {

        }

    }

    private void mostrarDialogoUbicacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ubicación desactivada o no alcanzable");
        builder.setMessage("Para usar esta función,por favor asegúrese de tener la ubicación habilitada en la configuración del dispositivo.");
        builder.setPositiveButton("Abrir config.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }



    public void ordenaPorUbi(int modo){
        //modo 0 = sort de cerca a lejos
        //modo 1 = sort de lejos a cerca
        listaChargers.sort(new Comparator<Charger>() {
            @Override
            public int compare(Charger o1, Charger o2) {
                double lat1 = Double.parseDouble(o1.address.latitude);
                double lon1 = Double.parseDouble(o1.address.longitude);
                double lat2 = Double.parseDouble(o2.address.latitude);
                double lon2 = Double.parseDouble(o2.address.longitude);

                final int R = 6371; // Radius of the earth

                double latDistance = Math.toRadians(lat1 - userLat);
                double lonDistance = Math.toRadians(lon1 - userLon);
                double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(userLat))
                        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                double distanceO1 = R * c * 1000; // convert to meters
                distanceO1 = Math.pow(distanceO1, 2);

                latDistance = Math.toRadians(lat2 - userLat);
                lonDistance = Math.toRadians(lon2 - userLon);
                a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(userLat))
                        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
                c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                double distanceO2 = R * c * 1000; // convert to meters
                distanceO2 = Math.pow(distanceO2, 2);

                if (modo == 0){
                    return  Double.compare(distanceO1,distanceO2);
                }else{
                    return  Double.compare(distanceO2,distanceO1);
                }
            }
        });
        adapterChargers.notifyDataSetChanged();
    }


    private void mostrarDialogoFiltros() {
        //En filtros contenemos todas las empresas
        ArrayList<String> filtrosSeleccionados = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filtros Aplicables")
                .setMultiChoiceItems(filtros, null, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int index,
                                                boolean isChecked) {


                                String filtro = filtros[index];

                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    filtrosSeleccionados.add(filtro);
                                } else if (filtrosSeleccionados.contains(filtro)) {
                                    // Else, if the item is already in the array, remove it
                                    filtrosSeleccionados.remove(filtro);
                                }
                            }
                        });

        for (String elemento : filtrosSeleccionados) {
            Toast.makeText(MainView.this, elemento, Toast.LENGTH_SHORT).show();
        }


        builder.setPositiveButton("Aplicar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (String elemento : filtrosSeleccionados) {
                    Toast.makeText(MainView.this, elemento, Toast.LENGTH_SHORT).show();
                }
                aplicarFiltro(filtrosSeleccionados);


            }
        });


        builder.show();

    }



    //ESTO FALLA!!!
    public void aplicarFiltro(ArrayList<String> filtrosAplicados) {


        for (Charger c : listaChargers) {
            if (!filtrosAplicados.contains(c.operator.title)) {
                listaChargers.remove(c);
            }
        }

        adapterChargers = new ChargersArrayAdapter(this, listaChargers);


        ListView listView = findViewById(R.id.lvChargers);
        listView.setAdapter(adapterChargers);


        // Notifica al adaptador personalizado que los datos han cambiado
        if (adapterChargers != null) {
            adapterChargers.notifyDataSetChanged();
        }

        for (Charger c : listaChargers) {
           Toast.makeText(MainView.this,listaChargers.size(),Toast.LENGTH_SHORT).show();
        }

    }












}