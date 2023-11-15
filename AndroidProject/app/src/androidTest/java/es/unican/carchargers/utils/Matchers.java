package es.unican.carchargers.utils;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import es.unican.carchargers.R;

public class Matchers {

    /**
     * Metodo para comprobar en los test de interfaz si una lista está vacía.
     * Para utilizarlo se usa:
     *      onView(withId(R.id.id_de_la_lista)).check(matches(isNotEmpty()))
     * @return Matcher<View>
     */
    public static Matcher<View> isNotEmpty() {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                ListView lv = (ListView) view;
                int count = lv.getCount();
                return count > 0;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should not be empty");
            }
        };
    }
    /**
     * Metodo para comprobar en los test de interfaz si una lista tiene "elemNumber" elementos.
     * Para utilizarlo se usa:
     *      onView(withId(R.id.id_de_la_lista)).check(matches(hasElements()))
     * @return Matcher<View>
     */
    public static Matcher<View> hasElements(int numElem) {
        return new TypeSafeMatcher<View>() {
            @Override public boolean matchesSafely (final View view) {
                ListView lv = (ListView) view;
                int count = lv.getCount();
                return count == numElem;
            }

            @Override public void describeTo (final Description description) {
                description.appendText ("ListView should have the same elements as the parameter defined");
            }
        };
    }

    /**
     * Metodo para comprobar en los test de interfaz si el elemento de una lista contiene unos iconos.
     * Para utilizarlo se usa:
     *      onView(withId(R.id.id_de_la_lista)).check(matches(withDrawable(R.drawable.tu_logo_esperado)));
     * @return Matcher<View>
     */
    public static Matcher<View> hasCorrectIcon(final Drawable expectedIcon) {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(final View view) {

                // Asumiendo que el icono está en un ImageView con la identificación R.id.icon
                ImageView imageView = (ImageView) view;

                Drawable icon = imageView.getDrawable();
                if (expectedIcon == null && icon == null) {
                    return true;
                }
                if (expectedIcon == null || icon == null) {
                    return false;
                }
                return expectedIcon.getConstantState().equals(icon.getConstantState());

                }

            @Override
            public void describeTo(final Description description) {
                description.appendText("ListView item should have the correct icon");
            }
        };
    }
}

