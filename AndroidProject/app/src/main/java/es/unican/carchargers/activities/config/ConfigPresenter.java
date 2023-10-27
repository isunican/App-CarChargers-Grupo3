package es.unican.carchargers.activities.config;

import static es.unican.carchargers.constants.EOperator.fromId;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.unican.carchargers.common.LocationComparator;
import es.unican.carchargers.constants.ECountry;
import es.unican.carchargers.constants.ELocation;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.ICallBack;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.service.APIArguments;

public class ConfigPresenter implements IConfigContract.Presenter {

    /** the view controlled by this presenter */
    private IConfigContract.View view;

    /** a cached list of charging stations currently shown */
    private List<Charger> shownChargers;
    private Double userLat;
    private Double userLon;
    private String chargerType;

    @Override
    public void init(IConfigContract.View view) {
        this.view = view;
        view.init();
    }

    /**
     * This method requests a list of charging stations from the repository, and requests
     * the view to show them.
     */

    @Override
    public void obtainType(String type) {

        if (type != null){
            chargerType = type;
        }
    }
}
