package es.unican.carchargers.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import es.unican.carchargers.R;

@RunWith(AndroidJUnit4.class)
public class FiltroCompanhiaOkUITest {
    // Con esta rule se lanza la actividad indicada antes de cada método test y @Before y se
    // cierra después de los test y los métodos @After
    //@Rule
    //public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<MainView>(MainView.class);

    @Rule
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);


    @Test
    public void filtroCompanhiaOkTest () {
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext());
        onView(withId(R.id.menuItemFiltro)).perform(click());
        onView(withText("REPSOL")).perform(click());
        onView(withText("Aplicar")).perform(click());
        DataInteraction contacto = onData(anything()).inAdapterView(withId(R.id.lvChargers)).atPosition(0);
        //para comprobar que la lista resultante esta ok comprobar que la compañía a la que pertenecen es REPSOL con un bucle
        //ListView listaOk = onView(allOf(withId()));

    }
}