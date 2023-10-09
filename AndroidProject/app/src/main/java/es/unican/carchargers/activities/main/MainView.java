package es.unican.carchargers.activities.main;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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
    private Spinner spinner;
    private ArrayAdapter<String> adapterSPN;
    private List<String> valores;
    private FusedLocationProviderClient fusedLocationClient;

    private double userLat, userLon;
    private boolean USO_UBI;

    private TextView ubi;
    private TextView infoUbi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        USO_UBI = false;

        // Initialize presenter-view connection
        presenter = new MainPresenter();
        presenter.init(this);

        ubi = findViewById(R.id.tvUbi);
        infoUbi = findViewById(R.id.tvInfoUbi);

        loading = findViewById(R.id.imgLoading);
        loading.setVisibility(View.VISIBLE);

        logo = findViewById(R.id.imgLogo);
        logo.setVisibility(View.INVISIBLE);


        spinner = findViewById(R.id.spnSort);
        spinner.setVisibility(View.INVISIBLE);

        fusedLocationClient = new FusedLocationProviderClient(this);


        infoUbi.setVisibility(View.INVISIBLE);
        ubi.setVisibility(View.INVISIBLE);




        //Pide permisos
        if (checkLocationPermission()) {
            // Ya tienes permisos de ubicación, puedes solicitar la ubicación.
            obtenerUbicacion();

        } else {
            // Si no tienes permisos, solicítalos al usuario.
            requestLocationPermission();
        }




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
                    case "Cercanía ↑":

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

                                return  Double.compare(distanceO1,distanceO2);
                            }
                        });
                        adapterChargers.notifyDataSetChanged();
                        eliminaEltoSpinner();

                        break;

                    case "Cercanía ↓":

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

                                return  Double.compare(distanceO2,distanceO1);
                            }
                        });
                        adapterChargers.notifyDataSetChanged();
                        eliminaEltoSpinner();

                        break;

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
        logo.setVisibility(View.VISIBLE);
        loading.setVisibility(View.INVISIBLE);
        infoUbi.setVisibility(View.VISIBLE);
        ubi.setVisibility(View.VISIBLE);


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
                                USO_UBI = true;
                                infoUbi.setText("Ubicación ✓");
                                ubi.setText(userLat + "\n" + userLon);
                                valores.add(1,"Cercanía ↑");
                                valores.add(2,"Cercanía ↓");

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

    public void pulsaUbicacion(){

        Intent intent = new Intent(this, DetailsView.class);
        intent.putExtra(DetailsView.INTENT_USER_LAT, userLat);
        intent.putExtra(DetailsView.INTENT_USER_LON, userLon);
        intent.putExtra(DetailsView.INTENT_CHARGER, "null");

        startActivity(intent);

    }








}