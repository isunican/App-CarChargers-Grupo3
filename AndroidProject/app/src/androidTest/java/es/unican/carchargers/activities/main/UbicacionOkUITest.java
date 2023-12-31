package es.unican.carchargers.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static es.unican.carchargers.utils.Matchers.hasElements;

import android.content.Context;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;
import es.unican.carchargers.R;
import es.unican.carchargers.common.ApplicationConstants;
import es.unican.carchargers.common.RepositoriesModule;
import es.unican.carchargers.repository.IRepository;
import es.unican.carchargers.repository.Repositories;

/*
 * Example UI Test using Hilt dependency injection
 * Documentation: https://developer.android.com/training/dependency-injection/hilt-testing
 * This test also uses an HTTP Idling Resource
*/

@HiltAndroidTest
@UninstallModules(RepositoriesModule.class)
@RunWith(AndroidJUnit4.class)
public class UbicacionOkUITest {

    @Rule(order = 0)  // the Hilt rule must execute first
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule(MainView.class);

    //Rule que establece los permisos de usuario de ubicación como aceptados
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);


    // necesito el context para acceder a recursos, por ejemplo un json con datos fake
    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @BeforeClass
    public static void setupClass() {
        //Se establece una ubicación del usuario simulada (Facultad de Ciencias Universidad de Cantabria)
        ApplicationConstants.setLocationMock(43.4709312, -3.8016632);

    }

    @AfterClass
    public static void teardownClass() {
        //HTTPIdlingResource.getInstance().finish();
    }

    // inject a fake repository that loads the data from a local json file
    // IMPORTANT: all the tests in this class must use this repository
    @BindValue IRepository repository = Repositories
            .getFake(context.getResources().openRawResource(R.raw.chargers_es_ubi));

    @Test
    public void ubicacionOkTest() {
        //Comprobar el numero de elementos visibles en la lista
        onView(withId(R.id.lvChargers)).check(matches(hasElements(9)));
        onView(ViewMatchers.withId(R.id.lvChargers)).check(matches(isDisplayed()));
        //Comprobar el primer elemento
        onData(anything()).inAdapterView(withId(R.id.lvChargers)).atPosition(0).
                onChildView(withId(R.id.tvAddress)).check(matches(withText("Parking Plaza de Pombo (Cantabria)")));
        //Comprobar un elemento intermedio
        onData(anything()).inAdapterView(withId(R.id.lvChargers)).atPosition(2).
                onChildView(withId(R.id.tvAddress)).check(matches(withText("Parking Plaza de Portugalete (Castilla y León)")));
        //Comprobar el ultimo elemento
        onData(anything()).inAdapterView(withId(R.id.lvChargers)).atPosition(8).
                onChildView(withId(R.id.tvAddress)).check(matches(withText("Parking Central (Andalusia)")));
    }
}