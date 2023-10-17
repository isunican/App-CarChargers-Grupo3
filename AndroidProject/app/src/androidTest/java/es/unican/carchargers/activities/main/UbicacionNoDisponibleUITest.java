package es.unican.carchargers.activities.main;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import androidx.constraintlayout.utils.widget.MockView;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.unican.carchargers.R;

@RunWith(AndroidJUnit4.class)
public class UbicacionNoDisponibleUITest {
    // Con esta rule se lanza la actividad indicada antes de cada método test y @Before y se
    // cierra después de los test y los métodos @After
    @Rule
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<MainView>(MainView.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Test
    public void UbicacionNoDisponibleTest () {
        //Quitar los permisos de ubicacion
        permissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

        //onView(withId(R.id.tvUbi)).check(matches(withText("no disponible")));
        //Comprobar que no se muestra la lista de cargadores
        onView(ViewMatchers.withId(R.id.lvChargers)).check(doesNotExist());

    }
}
