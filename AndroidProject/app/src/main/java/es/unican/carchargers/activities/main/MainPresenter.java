package es.unican.carchargers.activities.main;

import android.util.Log;

import java.util.Collections;
import java.util.List;

import es.unican.carchargers.constants.ECountry;
import es.unican.carchargers.constants.ELocation;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.ICallBack;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.service.APIArguments;

public class MainPresenter implements IMainContract.Presenter {

    /**
     * the view controlled by this presenter
     */
    private IMainContract.View view;

    /**
     * a cached list of charging stations currently shown
     */
    private List<Charger> shownChargers;
    private Double userLat;
    private Double userLon;
    private List<EOperator> filtrosAplicar = null;
    private int typeCharger = -1;
    private boolean init = false;
    private boolean ubi = false;
    private boolean type = false;


    @Override
    public void init(IMainContract.View view) {
        this.view = view;
        view.init();
        init = true;
        load();
    }

    /**
     * This method requests a list of charging stations from the repository, and requests
     * the view to show them.
     */
    int j = 0;
    public void load() {

        Log.d("[DEBUG LOAD]", "load numero " + j + ": setUbi: " + ubi + ": setInit: " + init + ": setType: " +  type);
        j++;
        if (!ubi || !init || !type){
            return;
        }
        IRepository repository = view.getRepository();

        // set API arguments to retrieve charging stations that match some criteria

        APIArguments args;
        int[] filtrosAplicarIDs = null;
        try {
            if (filtrosAplicar != null) {
                filtrosAplicarIDs = new int[filtrosAplicar.size()];

                for (int i = 0; i < filtrosAplicar.size(); i++) {
                    filtrosAplicarIDs[i] = filtrosAplicar.get(i).id;
                }
            }
        } catch (Exception e) {
        }
        args = onAPIargs(filtrosAplicarIDs, typeCharger,userLat,userLon);


        ICallBack callback = new ICallBack() {
            @Override
            public void onSuccess(List<Charger> chargers) {
                MainPresenter.this.shownChargers =
                        chargers != null ? chargers : Collections.emptyList();

                view.showChargers(chargers);
                view.showLoadCorrect(chargers.size());
            }

            @Override
            public void onFailure(Throwable e) {
                MainPresenter.this.shownChargers = Collections.emptyList();
                view.showLoadError();
            }
        };
        if (args != null) {
            repository.requestChargers(args, callback);
        }
    }

    public APIArguments onAPIargs(int [] idsFiltros, int typeCharger, Double userLat, Double userLon){

        APIArguments args = APIArguments.builder() // args default
                .setCountryCode(ECountry.SPAIN.code)
                .setLocation(ELocation.SANTANDER.lat, ELocation.SANTANDER.lon)
                .setDistance(500)
                .setMaxResults(100);

        if(typeCharger != -1){//Si no hay filtro seleccionado o el filtro es TODOS
            args.setConnectionTypeId(typeCharger);
        }


        if (userLat != null && userLon != null) { //Solo tenemos ubicacion
            args.setLocation(userLat, userLon);
        }
        if (idsFiltros != null) { //Solo tenemos filtros
            args.setOperatorId(idsFiltros);
        }
        return args;
    }

    @Override
    public void onChargerClicked(int index) {
        if (shownChargers != null && index < shownChargers.size()) {
            Charger charger = shownChargers.get(index);
            view.showChargerDetails(charger);
        }
    }

    @Override
    public void onMenuInfoClicked() {
        view.showInfoActivity();
    }

    @Override
    public void onMenuRefreshClicked() {
        view.obtenerUbicacion();
        view.showLoading();
        load();
    }

    @Override
    public void onMenuUserClicked() {
        view.showUserDetails();
    }


    public void obtainUbi(double uLat, double uLon) {
        if (uLat != 0.0 && uLon != 0.0){
            userLat = uLat;
            userLon = uLon;
        }
        Log.d("[DEBUG EN PRESENTER]", "Tenemos ubi:" + userLat + " " + userLon);
        ubi = true;
        load();
    }

    @Override
    public void obtainType(int idCharger) {
        typeCharger = idCharger;
        Log.d("[DEBUGTYPE]", "Presenter dice: " + typeCharger);
        type = true;
        load();
    }

    @Override
    public void obtainFiltros(List<EOperator> filtrosSeleccionados) {
        filtrosAplicar = filtrosSeleccionados;
        load();
    }

    public void resetButton() {
        load();

    }

    public void setUbi(boolean a) {
        ubi = a;
    }

    public void setInit(boolean a) {
        init = a;
    }

    public void setType(boolean a) {
        type = a;
    }
}
