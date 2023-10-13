package es.unican.carchargers.activities.main;



import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.unican.carchargers.common.LocationComparator;
import es.unican.carchargers.repository.ICallBack;
import es.unican.carchargers.constants.ECountry;
import es.unican.carchargers.constants.ELocation;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.service.APIArguments;

public class MainPresenter implements IMainContract.Presenter {

    /** the view controlled by this presenter */
    private IMainContract.View view;

    /** a cached list of charging stations currently shown */
    private List<Charger> shownChargers;
    private Double userLat;
    private Double userLon;

    @Override
    public void init(IMainContract.View view) {
        this.view = view;
        view.init();
        load();
    }

    /**
     * This method requests a list of charging stations from the repository, and requests
     * the view to show them.
     */
    private void load() {
        IRepository repository = view.getRepository();

        // set API arguments to retrieve charging stations that match some criteria
        APIArguments args = APIArguments.builder()
                    .setCountryCode(ECountry.SPAIN.code)
                    .setLocation(ELocation.SANTANDER.lat, ELocation.SANTANDER.lon)
                    .setMaxResults(50);



        ICallBack callback = new ICallBack() {
            @Override
            public void onSuccess(List<Charger> chargers) {
                MainPresenter.this.shownChargers =
                        chargers != null ? chargers : Collections.emptyList();
                if(userLat != null && userLon != null) {
                    Collections.sort(chargers, new LocationComparator(userLat, userLon));
                }
                view.showChargers(MainPresenter.this.shownChargers);
                view.showLoadCorrect(MainPresenter.this.shownChargers.size());
            }

            @Override
            public void onFailure(Throwable e) {
                MainPresenter.this.shownChargers = Collections.emptyList();
                view.showLoadError();
            }
        };

        repository.requestChargers(args, callback);

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



    private void loadConFiltrosEmpresas(List<String> filtrosSeleccionados) {
        IRepository repository = view.getRepository();

        // set API arguments to retrieve charging stations that match some criteria
        APIArguments args;
        if(userLat != 0.0 || userLon != 0.0){ // tenemos ubicacion
            args = APIArguments.builder()
                    .setCountryCode(ECountry.SPAIN.code)
                    .setLocation(userLat, userLon)
                    .setMaxResults(50);
        }else { // no tenemos ubicacion
            args = APIArguments.builder()
                    .setCountryCode(ECountry.SPAIN.code)
                    .setLocation(ELocation.SANTANDER.lat, ELocation.SANTANDER.lon)
                    .setMaxResults(50);
        }

        ICallBack callback = new ICallBack() {
            @Override
            public void onSuccess(List<Charger> chargers) {
                MainPresenter.this.shownChargers =
                        chargers != null ? chargers : Collections.emptyList();
                view.showChargers(MainPresenter.this.shownChargers);
                view.showLoadCorrect(MainPresenter.this.shownChargers.size());
            }

            @Override
            public void onFailure(Throwable e) {
                MainPresenter.this.shownChargers = Collections.emptyList();
                view.showLoadError();
            }
        };

        repository.requestChargers(args, callback);

    }

    public void recibeUbi(double uLat, double uLon){
        userLat = uLat;
        userLon = uLon;
        Log.d("[DEBUG EN PRESENTER]","Tenemos ubi:" + userLat+ " " + userLon);
        load();

    }



}
