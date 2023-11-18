package es.unican.carchargers.activities.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.unican.carchargers.R;
import es.unican.carchargers.activities.details.DetailsView;
import es.unican.carchargers.activities.info.InfoActivity;
import es.unican.carchargers.activities.config.ConfigView;
import es.unican.carchargers.common.ApplicationConstants;
import es.unican.carchargers.constants.ECharger;
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



    private FusedLocationProviderClient fusedLocationClient;
    private double userLat;
    private double userLon;
    private boolean[] checked;
    private ActionBar actionBar;
    private  SharedPreferences sharedPreferences;
    private int idSelection;
    private boolean locationActivated = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        loading = findViewById(R.id.imgLoading);
        loading.setVisibility(View.VISIBLE);
        //PRUEBA: LLAMAR AL MENU EN EL ONCREATE
        //invalidateOptionsMenu();






        // Initialize presenter-view connection
        presenter = new MainPresenter();
        presenter.init(this);


        fusedLocationClient = new FusedLocationProviderClient(this);

        actionBar = getSupportActionBar();




        if (actionBar != null) {
            actionBar.setTitle(" ");
        }


        checked = new boolean[EOperator.values().length];

        sharedPreferences = getSharedPreferences("MiPref", Context.MODE_PRIVATE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (userLat == 0.0 && userLon == 0.0) {
            if (checkLocationPermission()) {
                // Ya tienes permisos de ubicación, puedes solicitar la ubicación.
                obtenerUbicacion();
            } else {
                // Si no tienes permisos, solicítalos al usuario.
                requestLocationPermission();
                mostrarDialogoUbicacion();
            }
        }

        String valorGuardado = sharedPreferences.getString("charger-type", "TODOS");
        int idSelectionOnResume = ECharger.valueOf(valorGuardado).id;
        if (idSelectionOnResume != idSelection){
            loading.setVisibility(View.VISIBLE);
            idSelection = idSelectionOnResume;
            presenter.obtainType(idSelectionOnResume);
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(locationActivated){
            menu.findItem(R.id.menuItemLocationON).setVisible(true);
            menu.findItem(R.id.menuItemLocationOFF).setVisible(false);
        } else {
            menu.findItem(R.id.menuItemLocationON).setVisible(false);
            menu.findItem(R.id.menuItemLocationOFF).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
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

            case R.id.menuItemRefresh:
                presenter.onMenuRefreshClicked();
                return true;
            case R.id.menuItemUser:
                presenter.onMenuUserClicked();

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
        ChargersArrayAdapter adapterChargers = new ChargersArrayAdapter(this, chargers);

        ListView listView = findViewById(R.id.lvChargers);
        listView.setAdapter(adapterChargers);

        loading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoadCorrect(int chargers) {
        if (chargers == 0){
            Toast.makeText(this, String.format("No hay cargadores que cumplan los filtros actuales.", chargers),
                    Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this, String.format("Cargados %d cargadores", chargers),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoadError() {

        if(isInternetAvailable()){
            Toast.makeText(this, "No se ha podido cargar la información.", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this, "No se ha podido cargar la información por problemas de red.", Toast.LENGTH_LONG).show();
        loading.setVisibility(View.INVISIBLE);
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
    //@Override
    public void showUserDetails() {
        Intent intent = new Intent(this, ConfigView.class);
        startActivity(intent);
    }

    private boolean checkLocationPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        boolean isTrue = false;
        if (result == PackageManager.PERMISSION_GRANTED) isTrue = true;
        return isTrue;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1
        );
    }


    private void mostrarDialogoUbicacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ubicación desactivada o no alcanzable");
        builder.setMessage("Para usar esta función, por favor asegúrese de tener la ubicación habilitada en la configuración del dispositivo.");
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

    public void obtenerUbicacion() {


        if (ApplicationConstants.isLocationMocked()) { // implementacion necesaria para los tests, no se ejecuta de normal
            userLat = ApplicationConstants.getLatMock();
            userLon = ApplicationConstants.getLonMock();
            presenter.obtainUbi(ApplicationConstants.getLatMock(), ApplicationConstants.getLonMock());

            locationActivated = true;

            return;
        }

        if (checkLocationPermission()) {
            CancellationToken c = new CancellationToken() {
                CancellationToken devolver;

                @NonNull
                @Override
                public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                    return devolver;
                }

                @Override
                public boolean isCancellationRequested() {
                    return false;
                }
            };
            fusedLocationClient.getCurrentLocation(100, c)
                    .addOnSuccessListener(this, new OnSuccessListener<>() {
                        @SuppressLint("RestrictedApi")
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                userLat = location.getLatitude();
                                userLon = location.getLongitude();

                                locationActivated = true;
                                MainView.this.invalidateOptionsMenu();

                                presenter.obtainUbi(userLat, userLon);
                            } else {
                                // ubicación no disponible
                                mostrarDialogoUbicacion();
                                presenter.obtainUbi(0.0, 0.0);
                            }
                        }
                    });
        } else {
            mostrarDialogoUbicacion();
        }

    }




    private void mostrarDialogoFiltros() {
        //En filtros contenemos todas las empresas
        ArrayList<EOperator> filtrosSeleccionados = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] filtrosStrings = new String[EOperator.values().length];
        for (int i = 0; i < EOperator.values().length; i++) {
            filtrosStrings[i] = EOperator.values()[i].toString();
        }

        builder.setTitle(Html.fromHtml("<font color='" + Color.parseColor("#8BC34A") + "'>Filtros de Compañía</font>", Html.FROM_HTML_MODE_LEGACY))
                .setMultiChoiceItems(filtrosStrings, checked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index,
                                        boolean isChecked) {

                        EOperator filtro = EOperator.valueOf(filtrosStrings[index]);

                        if (isChecked) {
                            if (!filtrosSeleccionados.contains(filtro) && checked[index]) {
                                // If the user checked the item, add it to the selected items
                                filtrosSeleccionados.add(filtro);
                            }
                        } else if (!isChecked && filtrosSeleccionados.contains(filtro)) {
                            // Else, if the item is already in the array, remove it
                            checked[index] = false;
                            filtrosSeleccionados.remove(filtro);
                        }
                    }
                });


        builder.setPositiveButton("Aplicar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loading.setVisibility(View.VISIBLE);
                presenter.obtainFiltros(filtrosSeleccionados);
            }


        });
        builder.setNegativeButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loading.setVisibility(View.VISIBLE);
                presenter.obtainFiltros(null);
                presenter.resetButton();
                for (int i = 0; i < checked.length; i++) {
                    checked[i] = false;
                }

            }

        });
        builder.show();
    }

    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
    }

    public static boolean isInternetAvailable() {
        String host = "www.google.com";  // Servidor al que se realizará el ping
        int timeout = 1500; // Tiempo de espera para el ping en milisegundos

        try {
            Process process = Runtime.getRuntime().exec("ping -c 1 -w " + timeout + " " + host);
            int exitCode = process.waitFor();
            return (exitCode == 0); // El valor de retorno 0 indica una respuesta exitosa
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        return false; // Si ocurre una excepción, asumimos que no hay conectividad
    }

}
