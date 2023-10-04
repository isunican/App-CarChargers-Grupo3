package es.unican.carchargers.activities.details;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import es.unican.carchargers.R;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;




/**
 * Charging station details view. Shows the basic information of a charging station.
 */

public class DetailsView extends AppCompatActivity  {

    public static final String INTENT_CHARGER = "INTENT_CHARGER";
    double lat, lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);


        // Link to view elements
        ImageView ivLogo = findViewById(R.id.ivLogo);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvId = findViewById(R.id.tvId);
        TextView tvInfo = findViewById(R.id.tvInfo);
        TextView tvWeb = findViewById(R.id.tvPaginaWeb);



        // Get Charger from the intent that triggered this activity
        Charger charger = Parcels.unwrap(getIntent().getExtras().getParcelable(INTENT_CHARGER));
        lat = Double.parseDouble(charger.address.latitude);
        lon = Double.parseDouble(charger.address.longitude);
        //Toast.makeText(getApplicationContext(), "Latitud: " + lat + " Longitud: " + lon, Toast.LENGTH_SHORT).show();

        //Web que muestra el mapa

        WebView webview = findViewById(R.id.web);
        webview.getSettings().setJavaScriptEnabled(true);

        String html1 = "https://maps.google.com/maps?q=";
        String coma = ",";
        String html2 = "&hl=es&z=14&amp;output=embed";
        String web = "<iframe src=\"" + html1 + lat + coma + lon + html2 + "\" width=\"100%\" height=\"100%\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>";
        webview.loadData(web, "text/html", null);


        // Set logo
        int resourceId = EOperator.fromId(charger.operator.id).logo;
        ivLogo.setImageResource(resourceId);

        // Set Infos
        tvTitle.setText(charger.operator.title);
        tvId.setText(charger.id);


        //Metemos info en el campo info
        String informacion = "";
        informacion = informacion + charger.address.title + "\n";
        informacion = informacion + "Número de puntos de conexión: " +charger.numberOfPoints + "\n";
        informacion = informacion + "Ubicación: " +charger.address.town + ", " + charger.address.province + "\n";
        informacion = informacion + "Precio por carga:" + charger.usageCost + "\n";

        tvInfo.setText(informacion);

        //Metemos la pagina web

        tvWeb.setText(charger.operator.website);


    }
}