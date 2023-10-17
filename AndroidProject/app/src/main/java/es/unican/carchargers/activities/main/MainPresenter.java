package es.unican.carchargers.activities.main;



import static es.unican.carchargers.constants.EOperator.fromId;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import es.unican.carchargers.common.ApplicationConstants;
import es.unican.carchargers.common.LocationComparator;
import es.unican.carchargers.constants.EOperator;
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
                    .setCountryCode(ECountry.SPAIN.code).setMaxResults(50);



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


    /*
    Método que carga la lista de cargadores filtrados por compañía
     */
    public void loadConFiltrosEmpresas(List<EOperator> filtrosSeleccionados) {
        IRepository repository = view.getRepository();

        // set API arguments to retrieve charging stations that match some criteria
        APIArguments args = APIArguments.builder()
                .setCountryCode(ECountry.SPAIN.code)
                .setLocation(ELocation.SANTANDER.lat, ELocation.SANTANDER.lon)
                .setMaxResults(50);

        Log.d("[DEBUG EN PRESENTER]","Los filtros son:");
        for(EOperator e: filtrosSeleccionados){
            Log.d("[DEBUG EN PRESENTER]",e.toString());
        }


        ICallBack callback = new ICallBack() {
            @Override
            public void onSuccess(List<Charger> chargers) {
                MainPresenter.this.shownChargers =
                        chargers != null ? chargers : Collections.emptyList();
                List<Charger> chargerResultado = new ArrayList<>();

                Log.d("[DEBUG EN PRESENTER]", "En la lista hubo " + chargers.size() + "elementos");


                    for(Charger c: chargers){

                        EOperator e = fromId(c.operator.id);
                        if (filtrosSeleccionados.contains(e)) {
                            chargerResultado.add(c);
                        }
                    }
                Log.d("[DEBUG EN PRESENTER]","En la lista hay actualmente "+chargerResultado.size()+"elementos");
                if(userLat != null && userLon != null) {
                Collections.sort(chargers, new LocationComparator(userLat, userLon));
                }
                view.showChargers(chargerResultado);
                view.showLoadCorrect(chargerResultado.size());
            }

            @Override
            public void onFailure(Throwable e) {
                MainPresenter.this.shownChargers = Collections.emptyList();
                view.showLoadError();
            }
        };

        repository.requestChargers(args, callback);
    }

    public void obtainUbi(double uLat, double uLon){
        userLat = uLat;
        userLon = uLon;
        Log.d("[DEBUG EN PRESENTER]","Tenemos ubi:" + userLat+ " " + userLon);
        load();
        view.setLocation(uLat, uLon);
    }
    public void resetButton (){
        load();

    }



}
