package es.unican.carchargers.activities.config;

import java.util.List;

import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;

/**
 * The Presenter-View contract for the Main activity.
 * The Main activity shows a list of charging stations.
 */
public interface IConfigContract {

    /**
     * Methods that must be implemented in the Main Presenter.
     * Only the View should call these methods.
     */
    public interface Presenter {
        void init(View view);

        void obtainType(String type);
    }

    /**
     * Methods that must be implemented in the Main View.
     * Only the Presenter should call these methods.
     */
    public interface View {


        void init();
    }
}
