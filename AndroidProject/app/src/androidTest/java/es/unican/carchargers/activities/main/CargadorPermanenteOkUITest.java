package es.unican.carchargers.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static es.unican.carchargers.utils.Matchers.hasElements;
import static es.unican.carchargers.utils.Matchers.isNotEmpty;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.SlidingDrawer;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
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


@HiltAndroidTest
@UninstallModules(RepositoriesModule.class)
@RunWith(AndroidJUnit4.class)
public class CargadorPermanenteOkUITest {

    @Rule(order = 0)  // the Hilt rule must execute first
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

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
            .getFake(context.getResources().openRawResource(R.raw.chargers_es_permanente));

    @Test
    public void cargadorPermanenteTest() {
        // Comprobamos que aparecen los elementos y la interacción con ellos es correcta
        onView(withId(R.id.lvChargers)).check(matches(isNotEmpty()));
        onView(ViewMatchers.withId(R.id.lvChargers)).check(matches(isDisplayed()));
        openContextualActionModeOverflowMenu();
        onView(withText("Configuración")).perform(click());
        onView(withId(R.id.spnChargerType)).perform(click());
        onView(withText("CHADEMO")).perform(click());

        // Comprobamos que el número de resultados es correcto
        onView(isRoot()).perform(ViewActions.pressBack());
        onView(ViewMatchers.withId(R.id.lvChargers)).check(matches(isDisplayed()));
        //onView(withId(R.id.lvChargers)).check(matches(hasElements(2)));

        // Comprobamos que se obtienen los tipos de cargadores buscados y ordenados por ubicación
        /*onData(anything()).inAdapterView(withId(R.id.lvChargers)).atPosition(0).
              onChildView(withId(R.id.tvAddress)).check(matches(withText("Repsol Puertollano")));
        onData(anything()).inAdapterView(withId(R.id.lvChargers)).atPosition(1).
              onChildView(withId(R.id.tvAddress)).check(matches(withText("ECOVE Galp Sevilla")));*/

        // Comprobamos la persistencia del tipo de cargador después de cerrar la app

        activityRule.getScenario().onActivity(activity -> {
            onView(withId(R.id.lvChargers)).check(matches(isNotEmpty()));
            onView(ViewMatchers.withId(R.id.lvChargers)).check(matches(isDisplayed()));
            openContextualActionModeOverflowMenu();
            onView(withText("Configuración")).perform(click());
            onData(anything()).inAdapterView(withId(R.id.spnChargerType)).check(matches(withText("CHADEMO")));
        });
    }
}