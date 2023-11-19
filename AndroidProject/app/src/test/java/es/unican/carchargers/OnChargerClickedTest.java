package es.unican.carchargers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.activities.main.MainView;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;

public class OnChargerClickedTest {
    @Mock
    IMainContract.View mainView;
    IMainContract.Presenter presenter;
    @Captor
    ArgumentCaptor<Charger> captor;
    Charger c1,c2,c3;

    @Before
    public void ini() {
        MockitoAnnotations.openMocks(this);
        presenter = new MainPresenter();

        c1 = new Charger();
        c1.operator.id = 91; // Repsol
        c2 = new Charger();
        c2.operator.id = 3324; // Zunder
        c3 = new Charger();
        c3.operator.id = 2247; // Iberdrola
    }
    @Test
    public void testOnChargerClickedValidIndex() {
        int i = 0;

        List<Charger> chargers = new ArrayList<>();
        chargers.add(c1);
        chargers.add(c2);
        chargers.add(c3);

        IRepository repo = Repositories.getSyncFake(chargers);
        when(mainView.getRepository()).thenReturn(repo);


        presenter.setKeys();
        presenter.init(mainView);
        presenter.onChargerClicked(i);


        verify(mainView).showChargerDetails(captor.capture());
        assertEquals(c1.id,captor.getValue().id);
    }
}
