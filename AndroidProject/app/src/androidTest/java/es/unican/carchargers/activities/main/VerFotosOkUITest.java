
package es.unican.carchargers.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static es.unican.carchargers.utils.Matchers.hasElements;
import static es.unican.carchargers.utils.Matchers.isNotEmpty;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.CoreMatchers;
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
public class VerFotosOkUITest {

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
            .getFake(context.getResources().openRawResource(R.raw.chargers_es_fotos));

    @Test
    public void cargadorPermanentePruebaTest() {

        // PRUEBA 00: ÉXITO CON FOTOS

        // Comprobamos que aparecen los elementos y la interacción con ellos es correcta
        onView(withId(R.id.lvChargers)).check(matches(isNotEmpty()));
        onView(withId(R.id.lvChargers)).check(matches(isDisplayed()));

        // Seleccionamos el primer cargador de la lista
        onData(anything()).inAdapterView(withId(R.id.lvChargers)).atPosition(0).perform(click());

        //Comprobar que hay una foto
        onView(withId(R.id.tvPhotosCount)).check(matches(withText("Fotos (1)")));
        onView(withId(R.id.lvPhotos)).check(matches(hasElements(1)));

        // Comprueba que la foto es correcta
        //String tagEsperada = "imagen1";
        //onData(anything()).inAdapterView(withId(R.id.lvPhotos)).atPosition(0).perform(click());
        //onView(withId(R.id.ivPhoto)).check(matches(isDisplayed()));
       // onView(CoreMatchers.allOf(withId(R.id.ivPhoto), withTagValue(is((Object) tagEsperada)))).check(matches(isDisplayed()));
    }
}

