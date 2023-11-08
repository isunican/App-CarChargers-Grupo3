package es.unican.carchargers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.activities.main.IMainContract;
import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;
public class OnChargerClickTest {
    @Mock
    IMainContract.View mainView;
    //@InjectMocks
    IMainContract.Presenter presenter;
    //@Captor
    ArgumentCaptor<Charger> capturador;
    List<Charger> shownChargers;
    Charger c1,c2,c3;



    @Test
    public void testOnChargerClickedWithValidIndex() {
        MockitoAnnotations.openMocks(this); // Inicializar los mocks
        capturador = ArgumentCaptor.forClass(Charger.class);
        presenter = new MainPresenter();

        c1 = new Charger();
        c1.operator.id = 91; // Repsol
        c2 = new Charger();
        c2.operator.id = 3324; // Zunder
        c3 = new Charger();
        c3.operator.id = 2247; // Iberdrola

        // Datos de prueba
        List<Charger> chargers = new ArrayList<>();

        chargers.add(c1);
        chargers.add(c2);
        chargers.add(c3);
        IRepository iRepository = Repositories.getFake(chargers);
        when(mainView.getRepository()).thenReturn(iRepository);


        int validIndex = 0; // Índice válido

        // Llamar al método bajo prueba
        presenter.init(mainView);
        presenter.onChargerClicked(validIndex);

        // Verificar que se llame a showChargerDetails con el cargador correcto
        verify(mainView).showChargerDetails(capturador.capture());
        Charger chargerCap = capturador.getValue();
        assertEquals(chargerCap,chargers.get(validIndex));
    }

}