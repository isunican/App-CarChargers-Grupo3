package es.unican.carchargers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static es.unican.carchargers.constants.ELocation.SANTANDER;

import org.junit.Before;
import org.junit.Test;

import es.unican.carchargers.activities.main.MainPresenter;
import es.unican.carchargers.activities.main.MainView;
import es.unican.carchargers.constants.ECountry;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.service.APIArguments;

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
    public void onAPiArgsTest1() {
    /**
        // UGIC.1a
        args = presenter.onAPIargs(null, -1, null, null);
        assertEquals(ECountry.SPAIN.code, args.getCountryCode());
        assertArrayEquals(new Integer[0], args.getOperatorIds());
        assertEquals(SANTANDER.lat, args.getLocationLatitude(), 0.1);
        assertEquals(SANTANDER.lon, args.getLocationLongitude(), 0.1);
        assertEquals(100, args.getMaxResults());
        assertThrows(NullPointerException.class, () -> args.getConnectionTypeId());
        assertEquals(500,  args.getDistance(), 0.1);

        // UGIC.1b
        args = presenter.onAPIargs(new int[]{3,2,34,5}, -1, null, null);
        assertEquals(ECountry.SPAIN.code, args.getCountryCode());
        assertArrayEquals(new Integer[]{3,2,34,5}, args.getOperatorIds());
        assertEquals(SANTANDER.lat, args.getLocationLatitude(), 0.1);
        assertEquals(SANTANDER.lon, args.getLocationLongitude(), 0.1);
        assertEquals(100, args.getMaxResults());
        assertThrows(NullPointerException.class, () -> args.getConnectionTypeId());
        assertEquals(500, args.getDistance(), 0.1);

        // UGIC.1c
        args = presenter.onAPIargs(null, 8, null, null);
        assertEquals(ECountry.SPAIN.code, args.getCountryCode());
        assertArrayEquals(new Integer[0], args.getOperatorIds());
        assertEquals(SANTANDER.lat, args.getLocationLatitude(), 0.1);
        assertEquals(SANTANDER.lon, args.getLocationLongitude(),0.1);
        assertEquals(100, args.getMaxResults());
        assertEquals(8, args.getConnectionTypeId());
        assertEquals(500, args.getDistance(), 0.1);

        // UGIC.1d
        args = presenter.onAPIargs(null, -1, -29.6866, null);
        assertEquals(ECountry.SPAIN.code, args.getCountryCode());
        assertArrayEquals(new Integer[0], args.getOperatorIds());
        assertEquals(SANTANDER.lat, args.getLocationLatitude(), 0.1);
        assertEquals(SANTANDER.lon,  args.getLocationLongitude(), 0.1);
        assertEquals(100, args.getMaxResults());
        assertThrows(NullPointerException.class, () -> args.getConnectionTypeId());
        assertEquals(500, args.getDistance(), 0.1);

        // UGIC.1e
        args = presenter.onAPIargs(new int[]{3,2,24,5}, -8, -29.6866, -123.3046);
        assertEquals(ECountry.SPAIN.code, args.getCountryCode());
        assertArrayEquals(new Integer[]{3,2,24,5}, args.getOperatorIds());
        assertEquals(-29.6866, args.getLocationLatitude(), 0.1);
        assertEquals(-123.3046, args.getLocationLongitude(), 0.1);
        assertEquals(100, args.getMaxResults());
        assertEquals(-8, args.getConnectionTypeId());
        assertEquals(500, args.getDistance(), 0.1);
     */
    }


}