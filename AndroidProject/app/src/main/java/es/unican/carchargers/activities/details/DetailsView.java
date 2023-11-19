package es.unican.carchargers.activities.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import es.unican.carchargers.R;
import es.unican.carchargers.activities.main.PhotosArrayAdapter;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Address;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.ConnectionType;
import es.unican.carchargers.activities.main.CommentsArrayAdapter;


/**
 * Charging station details view. Shows the basic information of a charging station.
 */

public class DetailsView extends AppCompatActivity  {

    public static final String INTENT_CHARGER = "INTENT_CHARGER";
    public static final String UBICACION = "Ubicación: ";

    double lat;
    double lon;

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
        webview.getSettings().setJavaScriptEnabled(true);
        //Lista de comentarios
        TextView tvComment = findViewById(R.id.tvCommentsCount);

        ExpandableHeightListView lvComments = (ExpandableHeightListView) findViewById(R.id.lvComments);
        //Lista de fotos
        TextView tvPhoto = findViewById(R.id.tvPhotosCount);

        ExpandableHeightListView lvPhotos = (ExpandableHeightListView) findViewById(R.id.lvPhotos);

        // Get Charger from the intent that triggered this activity
        charger = Parcels.unwrap(getIntent().getExtras().getParcelable(INTENT_CHARGER));
        if (charger != null) {

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
        StringBuilder builder = new StringBuilder(informacion);


        builder.append("Número de puntos de conexión: " + charger.numberOfPoints + "\n");

        // Metemos ubicacion en el campo info
        anhadirInfoUbicacion(charger.address, builder);

        // Metemos coste en el campo info
        anhadirInfoCoste(charger.usageCost,builder);


        

        // Metemos el tipo de cargador en el campo info
        anhadirInfoTipoCargador(charger.getConnectionTypes(), builder);

        informacion = builder.toString();
      
        informacion.trim();
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
        if (charger.userComments != null && charger.userComments.size() != 0) {
            tvComment.setText("Comentarios (" + String.valueOf(charger.userComments.size()) + ")");
        } else {
            tvComment.setText("Comentarios (0)");
        }

        //Muestreo de comentarios
        if (charger.userComments != null){
        CommentsArrayAdapter commentArrayAdapter = new CommentsArrayAdapter(this, charger.userComments);
        lvComments.setAdapter(commentArrayAdapter);
        lvComments.setExpanded(true);
        } else {
            List<String> noComments = new ArrayList<>();
            noComments.add("No existen comentarios\nde este punto de carga.");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noComments);
            lvComments.setAdapter(adapter);
        }

        //Cálculo de numero de fotos
        if (charger.mediaItems != null && charger.mediaItems.size() != 0) {
            tvPhoto.setText("Fotos (" + String.valueOf(charger.mediaItems.size()) + ")");
        } else {
            tvPhoto.setText("Fotos (0)");
        }

        //Muestreo de fotos
        if (charger.mediaItems != null){
            PhotosArrayAdapter photoArrayAdapter = new PhotosArrayAdapter(this, charger.mediaItems);
            lvPhotos.setAdapter(photoArrayAdapter);
            lvPhotos.setExpanded(true);
        } else {
            List<String> noPhotos = new ArrayList<>();
            noPhotos.add("No existen fotos\nde este punto de carga.");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, noPhotos);
            lvPhotos.setAdapter(adapter);
        }
    }


    private void anhadirInfoTipoCargador(Set<ConnectionType> connectionTypes, StringBuilder builder) {
        if (connectionTypes == null || connectionTypes.isEmpty()) {
            builder.append("Tipo de cargador: No encontrado" + "\n");
        } else {
            for (ConnectionType c: connectionTypes) {
                builder.append("Tipo de cargador: " + c.title + "\n");
            }
        }

    }

    private void anhadirInfoCoste(String coste, StringBuilder builder) {

        if (coste == null) {
            builder.append("Precio por carga: Desconocido" + "\n");
        } else if (coste.isBlank()) {
            builder.append("Precio por carga: Desconocido" + "\n");
        } else {
            builder.append("Precio por carga: " + charger.usageCost + "\n");
        }
    }
    private void anhadirInfoUbicacion(Address address, StringBuilder builder) {

        if (address == null || (address.town == null && address.province == null)) {
            builder.append(UBICACION + "Provincia y ciudad no disponible\n");
        } else if (address.town == null) {
            builder.append(UBICACION + address.province + "\n");
        } else if (address.province == null) {
            builder.append(UBICACION + address.town + "\n");
        } else {
            builder.append(UBICACION + address.town + ", " + address.province + "\n");
        }
    }

    public void pulsaWeb(){
        Uri uri = Uri.parse(charger.operator.website);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }


}