package es.unican.carchargers.activities.details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import es.unican.carchargers.R;
import es.unican.carchargers.activities.main.ChargersArrayAdapter;
import es.unican.carchargers.activities.main.CommentsArrayAdapter;
import es.unican.carchargers.activities.main.MainView;
import es.unican.carchargers.constants.EComment;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.UserComment;
import es.unican.carchargers.repository.IRepository;


/**
 * Charging station details view. Shows the basic information of a charging station.
 */

public class DetailsView extends AppCompatActivity  {

    public static final String INTENT_CHARGER = "INTENT_CHARGER";

    double lat, lon;

    Charger charger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_view);


        // Link to view elements
        ImageView ivLogo = findViewById(R.id.ivLogo);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvDireccion = findViewById(R.id.tvDireccion);
        TextView tvInfo = findViewById(R.id.tvInfo);
        TextView tvWeb = findViewById(R.id.tvPaginaWeb);
        //Web que muestra el mapa
        WebView webview = findViewById(R.id.web);
        webview.getSettings().setJavaScriptEnabled(false);
        //Lista de comentarios
        TextView tvComment = findViewById(R.id.tvCommentsCount);
        ListView lvComments = findViewById(R.id.lvComments);


        // Get Charger from the intent that triggered this activity
        charger = Parcels.unwrap(getIntent().getExtras().getParcelable(INTENT_CHARGER));
        if (charger != null) {
            //Toast.makeText(getApplicationContext(), "Latitud: " + lat + " Longitud: " + lon, Toast.LENGTH_SHORT).show();

            lat = Double.parseDouble(charger.address.latitude);
            lon = Double.parseDouble(charger.address.longitude);

        } else {
            Toast.makeText(getApplicationContext(), "El cargador es null", Toast.LENGTH_SHORT).show();
        }

        String html1 = "https://maps.google.com/maps?q=";
        String coma = ",";
        String html2 = "&hl=es&z=14&amp;output=embed";
        String web = "<iframe src=\"" + html1 + lat + coma + lon + html2 + "\" width=\"100%\" height=\"100%\" style=\"border: 0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>";
        String webConfig = "<style>html{background-color: #f0f0f0;}</style>";
        webview.loadData(web + webConfig, "text/html", null);


        // Set logo
        int resourceId = EOperator.fromId(charger.operator.id).logo;
        ivLogo.setImageResource(resourceId);


        // Set Infos
        if (!charger.address.title.isBlank() || charger.address.title != null) {
            tvDireccion.setText(charger.address.title);
        } else {
            Toast.makeText(getApplicationContext(), "No hay address", Toast.LENGTH_SHORT).show();
            tvTitle.setText("Dirección desconocida");
        }

        if (!charger.id.isBlank() || charger.id != null) {
            tvTitle.setText(charger.operator.title);
        } else {
            Toast.makeText(getApplicationContext(), "No hay empresa", Toast.LENGTH_SHORT).show();
            tvTitle.setText("Empresa desconocida");

        }


        //Metemos info en el campo info
        String informacion = "";

        informacion = informacion + "Número de puntos de conexión: " + charger.numberOfPoints + "\n";


        try {
            if ((charger.address.town.isBlank()) && (charger.address.province.isBlank())) {
                informacion = informacion + "Ubicación: Provincia y ciudad no disponible\n";
            } else if (charger.address.town.isBlank()) {
                informacion = informacion + "Ubicación: " + charger.address.province + "\n";
            } else if (charger.address.province.isBlank()) {
                informacion = informacion + "Ubicación: " + charger.address.town + "\n";
            } else {
                informacion = informacion + "Ubicación: " + charger.address.town + ", " + charger.address.province + "\n";
            }
        } catch (NullPointerException e) {
            if (charger.address.province == null && charger.address.province == null) {
                informacion = informacion + "Ubicación: Provincia y ciudad no disponible\n";
            } else if (charger.address.town == null) {
                informacion = informacion + "Ubicación: " + charger.address.province + "\n";
            } else if (charger.address.province == null) {
                informacion = informacion + "Ubicación: " + charger.address.town + "\n";
            }

        }

        try {
            if (charger.usageCost.isBlank()) {
                informacion = informacion + "Precio por carga: Desconocido" + "\n";
            } else {
                informacion = informacion + "Precio por carga: " + charger.usageCost + "\n";
            }
        } catch (NullPointerException e) {
            informacion = informacion + "Precio por carga: Desconocido" + "\n";
        }


        tvInfo.setText(informacion);

        //Metemos la pagina web
        try{
        if(!charger.operator.website.isBlank() ||!charger.operator.website.isEmpty()) {
        tvWeb.setText(charger.operator.website);
        }
        }catch(Exception e){
            tvWeb.setText("No disponemos de una página web de contacto.");
        }

        //Cálculo de numero de comentarios
        if (charger.getChargerComments() == 0) {
            tvComment.setText("Comentarios (0)");
        } else {
            tvComment.setText("Comentarios (" + String.valueOf(charger.getChargerComments()) + ")");
        }

        //Muestreo de comentarios
        if (charger.userComments != null){
        CommentsArrayAdapter commentArrayAdapter = new CommentsArrayAdapter(this, charger.userComments);
        lvComments.setAdapter(commentArrayAdapter);
        } else {
            List<String> noComments = new ArrayList<>();
            noComments.add("No existen comentarios\nde este punto de carga.");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noComments);
            lvComments.setAdapter(adapter);

        }


    }

    public void pulsaWeb(View view){
        Uri uri = Uri.parse(charger.operator.website);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }


}