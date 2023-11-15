package es.unican.carchargers;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.activities.main.MainView;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.Operator;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;
import es.unican.carchargers.repository.service.APIArguments;

public class OnLoadTest {
    @Mock
    private MainView mainView;
    private MainPresenter presenter;
    private IRepository repository;
    private List<Charger> chargers = new ArrayList<>();
    private Charger charger1, charger2, charger3;

    @Before
    public void setUp() {
        mainView = mock(MainView.class);

        charger1 = new Charger();
        charger2 = new Charger();
        charger3 = new Charger();

        chargers.add(charger1);
        chargers.add(charger2);
        chargers.add(charger3);

        repository = Repositories.getSyncFake(chargers);

        when(mainView.getRepository()).thenReturn(repository);
    }

    @Test
    public void onLoadTest() {

        // UGIC.1a
        presenter = new MainPresenter();
        presenter.setView(mainView);

        // Todos los casos true
        presenter.setKeys();

        // Se aplican argumentos a la llamada
        presenter.setUserLat(-29.6866);
        presenter.setUserLon(-123.3046);
        presenter.setTypeCharger(-1);

        presenter.load();

        assertTrue(presenter.getShownChargers() != null);
        assertTrue(presenter.getShownChargers().size() == 3);


        // UGIC.1b
        presenter = new MainPresenter();
        presenter.setView(mainView);

        // No se cumplen algún caso
        presenter.setInit(false);
        presenter.setType(true);
        presenter.setUbi(false);

        // Se aplican argumentos a la llamada
        presenter.setUserLat(-29.6866);
        presenter.setUserLon(-123.3046);
        presenter.setTypeCharger(-1);

        presenter.load();

        assertTrue(presenter.getShownChargers() == null);

        // UGIC.1c
        presenter = new MainPresenter();
        presenter.setView(mainView);

        // Todos los casos true
        presenter.setKeys();

        // No hay argumentos en la llamada

        presenter.load();

        assertTrue(presenter.getShownChargers() != null);
        assertTrue(presenter.getShownChargers().size() == 3);

        verify(mainView, times(2)).getRepository();
        verify(mainView, times(2)).showChargers(chargers);
        verify(mainView, times(2)).showLoadCorrect(chargers.size());
        verify(mainView, times(0)).showLoadError();
    }
}
