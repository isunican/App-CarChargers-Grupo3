package es.unican.carchargers.activities.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.unican.carchargers.R;
import es.unican.carchargers.activities.details.DetailsView;
import es.unican.carchargers.activities.info.InfoActivity;
import es.unican.carchargers.common.ApplicationConstants;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;
import pl.droidsonroids.gif.GifImageView;

@AndroidEntryPoint
public class MainView extends AppCompatActivity implements IMainContract.View {

    /**
     * repository is injected with Hilt
     */
    @Inject
    IRepository repository;

    /**
     * presenter that controls this view
     */
    IMainContract.Presenter presenter;
    private GifImageView loading;
    private ImageView logo;

    private FusedLocationProviderClient fusedLocationClient;
    private double userLat, userLon;
    private TextView infoUbi;
    private boolean[] checked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoUbi = findViewById(R.id.tvInfoUbi);

        loading = findViewById(R.id.imgLoading);
        loading.setVisibility(View.VISIBLE);

        logo = findViewById(R.id.imgLogo);
        logo.setVisibility(View.INVISIBLE);

        // Initialize presenter-view connection
        presenter = new MainPresenter();
        presenter.init(this);


        fusedLocationClient = new FusedLocationProviderClient(this);

        infoUbi.setVisibility(View.INVISIBLE);


        EOperator[] filtros = EOperator.values();

        infoUbi.setText("Ubicación ☒");

        checked = new boolean[EOperator.values().length];


        //Comprobar permisos de locaclización de usuario
        //Si no los tiene, se solicitan al usuario
        if (checkLocationPermission()) {
            // Ya tienes permisos de ubicación, puedes solicitar la ubicación.
            obtenerUbicacion();
            infoUbi.setText("Ubicación ✓");
        } else {
            // Si no tienes permisos, solicítalos al usuario.
            requestLocationPermission();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    /*
    Menú superior de la pantalla
    Opción 1: vista información
    Opción 2: menú de filtros
    */
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

    /*
    Método que muestra la lista de cargadores disponibles
    */
    @Override
    public void showChargers(List<Charger> chargers) {
        ChargersArrayAdapter adapterChargers = new ChargersArrayAdapter(this, chargers);

        ListView listView = findViewById(R.id.lvChargers);
        listView.setAdapter(adapterChargers);
        logo.setVisibility(View.VISIBLE);
        loading.setVisibility(View.INVISIBLE);
        infoUbi.setVisibility(View.VISIBLE);
        /*
        if (userLat != 0.0 && userLon != 0.0){
            ordenaPorUbi(0);
        }
        */
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

    @Override
    public void setLocation(double uLat, double uLon) {
        Log.d("[DEBUG]", "Latitud: " + uLat + "Longitud: " + uLat);
        infoUbi.setText("Ubicación ✓");
        //ubi.setText(uLat + "\n" + uLat);
    }

    /*
    Método que comprueba que la app posea los permisos de ubicación
     */
    private boolean checkLocationPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) return true;
        else return false;
    }

    /*
    Método que solicita los permisos de ubicación
     */
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    /*
    Método que obtiene la ubicación del usuario
     */
    public void obtenerUbicacion() {
        if (ApplicationConstants.isLocationMocked()) {
            presenter.obtainUbi(ApplicationConstants.getLatMock(), ApplicationConstants.getLonMock());
            return;
        }
        //Toast.makeText(MainView.this, "Obtener ubicacion ejecutado", Toast.LENGTH_SHORT).show();
        if (ApplicationConstants.isLocationMocked()) {
            userLat = ApplicationConstants.getLatMock();
            userLon = ApplicationConstants.getLonMock();
            presenter.obtainUbi(ApplicationConstants.getLatMock(), ApplicationConstants.getLonMock());
            //setLocation(userLat, userLon);
            return;
        }
        //Comprueba los permisos de ubicación
        if (checkLocationPermission()) {
            //Obtiene la ultima ubicación del usuario
            fusedLocationClient.getLastLocation().addOnCompleteListener(this, new OnCompleteListener<Location>() {
                        //
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                Location location = task.getResult();

                                //Toast.makeText(MainView.this, "Latitud: " + latitude + ", Longitud: " + longitude, Toast.LENGTH_SHORT).show();
                                userLat = location.getLatitude();
                                userLon = location.getLongitude();
                                Log.d("[DEBUG]", "Latitud: " + userLat + "Longitud: " + userLon);
                                infoUbi.setText("Ubicación ☑");
                                presenter.obtainUbi(userLat, userLon);
                            } else {
                                // ubicación no disponible, se vuelve a pedir permiso
                                requestLocationPermission();
                                mostrarDialogoUbicacion();
                            }
                        }
                    });
        } else {

        }

    }

    /*
    Método que solicita la ubicación en el caso de que no esté disponible
     */
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

    /*
    Método que muestra el menú desplegable de filtros
     */
    private void mostrarDialogoFiltros() {
        //En filtros contenemos todas las empresas
        ArrayList<EOperator> filtrosSeleccionados = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Strings de elementos seleccionados en el filtro
        String[] filtrosStrings = new String[EOperator.values().length];
        //Definición de elementos disponibles para elegir en el filtro
        for ( int i = 0; i < EOperator.values().length; i++){
            filtrosStrings[i] = EOperator.values()[i].toString();
        }

        builder.setTitle("Filtros de ubicación:")
                .setMultiChoiceItems(filtrosStrings, checked, new DialogInterface.OnMultiChoiceClickListener() {
                    //Listener que define las compañías seleccionadas al aplicar el filtro
                    @Override
                    public void onClick(DialogInterface dialog, int index,
                                        boolean isChecked) {
                        EOperator filtro = EOperator.valueOf(filtrosStrings[index]);

                        if (isChecked) {
                            checked[index] = true;
                            // If the user checked the item, add it to the selected items
                            filtrosSeleccionados.add(filtro);
                        } else if (filtrosSeleccionados.contains(filtro)) {
                            // Else, if the item is already in the array, remove it
                            checked[index] = false;
                            filtrosSeleccionados.remove(filtro);
                        }
                    }
                });

        //Aplica el filtro seleccionado
        builder.setPositiveButton("Aplicar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loading.setVisibility(View.VISIBLE);
                presenter.loadConFiltrosEmpresas(filtrosSeleccionados);
            }


        });
        builder.setNegativeButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loading.setVisibility(View.VISIBLE);
                presenter.resetButton();
                for (int i = 0; i < checked.length; i++){
                    checked[i] = false;
                }

            }

        });
        builder.show();
    }
}
