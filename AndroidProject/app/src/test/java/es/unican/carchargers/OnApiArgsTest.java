package es.unican.carchargers;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static es.unican.carchargers.constants.ELocation.SANTANDER;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;

import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.activities.main.MainView;

import es.unican.carchargers.constants.ECountry;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.service.APIArguments;

import es.unican.carchargers.repository.ICallBack;

public class OnApiArgsTest {

    private MainPresenter presenter;
    private MainView mainView;
    private IRepository repository;
    private APIArguments args;

    @Before
    public void setUp() {
        mainView = mock(MainView.class);
        repository = mock(IRepository.class);
        presenter = new MainPresenter();
    }

    @Test
    public void onAPiArgsTest() {

        // UGIC.1a
        args = presenter.onAPIargs(null, -1, null, null);
        assertEquals(ECountry.SPAIN.code, args.getCountryCode());
        assertArrayEquals(null, args.getOperatorIds());
        assertTrue(SANTANDER.lat == args.getLocationLatitude());
        assertTrue(SANTANDER.lon == args.getLocationLongitude());
        assertTrue(100 == args.getMaxResults());
        assertThrows(NullPointerException.class, () -> args.getConnectionTypeId());
        assertTrue(500 == args.getDistance());

        // UGIC.1b
        args = presenter.onAPIargs(new int[]{3,2,34,5}, -1, null, null);
        assertEquals(ECountry.SPAIN.code, args.getCountryCode());
        assertArrayEquals(new Integer[]{3,2,34,5}, args.getOperatorIds());
        assertTrue(SANTANDER.lat == args.getLocationLatitude());
        assertTrue(SANTANDER.lon == args.getLocationLongitude());
        assertTrue(100 == args.getMaxResults());
        assertThrows(NullPointerException.class, () -> args.getConnectionTypeId());
        assertTrue(500 == args.getDistance());

        // UGIC.1c
        args = presenter.onAPIargs(null, 8, null, null);
        assertEquals(ECountry.SPAIN.code, args.getCountryCode());
        assertArrayEquals(null, args.getOperatorIds());
        assertTrue(SANTANDER.lat == args.getLocationLatitude());
        assertTrue(SANTANDER.lon == args.getLocationLongitude());
        assertTrue(100 == args.getMaxResults());
        assertTrue(8 == args.getConnectionTypeId());
        assertTrue(500 == args.getDistance());

        // UGIC.1d
        args = presenter.onAPIargs(null, -1, -29.6866, null);
        assertEquals(ECountry.SPAIN.code, args.getCountryCode());
        assertArrayEquals(null, args.getOperatorIds());
        assertTrue(SANTANDER.lat == args.getLocationLatitude());
        assertTrue(SANTANDER.lon == args.getLocationLongitude());
        assertTrue(100 == args.getMaxResults());
        assertThrows(NullPointerException.class, () -> args.getConnectionTypeId());
        assertTrue(500 == args.getDistance());

        // UGIC.1e
        args = presenter.onAPIargs(new int[]{3,2,24,5}, -8, -29.6866, -123.3046);
        assertEquals(ECountry.SPAIN.code, args.getCountryCode());
        assertArrayEquals(new Integer[]{3,2,24,5}, args.getOperatorIds());
        assertTrue(-29.6866 == args.getLocationLatitude());
        assertTrue(-123.3046 == args.getLocationLongitude());
        assertTrue(100 == args.getMaxResults());
        assertTrue(-8 == args.getConnectionTypeId());
        assertTrue(500 == args.getDistance());
    }
}