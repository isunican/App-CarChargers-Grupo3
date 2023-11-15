package es.unican.carchargers.activities.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import es.unican.carchargers.R;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;

public class ChargersArrayAdapter extends ArrayAdapter<Charger> {

    private static final String OPERADOR_NO_IDENTIFICADO = "Operador no identificado";
    public ChargersArrayAdapter(@NonNull Context context, @NonNull List<Charger> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // this is the car charger we want to show here
        Charger charger = getItem(position);

        // create the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_main_item, parent, false);
        }

        // logo

        ImageView iv = convertView.findViewById(R.id.ivLogo);

        if (charger.operator != null ) {
            EOperator operator = EOperator.fromId(charger.operator.id);
            iv.setImageResource(operator.logo);
        }


        // Title
        TextView tv = convertView.findViewById(R.id.tvTitle);
        if (charger.operator != null && charger.operator.title != null) {
            tv.setText(charger.operator.title);
        } else {
            tv.setText(OPERADOR_NO_IDENTIFICADO);
        }



        // Address

        TextView tv2 = convertView.findViewById(R.id.tvAddress);
        String str = String.format("%s (%s)", charger.address.title, charger.address.province);
        tv2.setText(str);


        // Info

        TextView tv3 = convertView.findViewById(R.id.tvInfo);
        tv3.setText(charger.usageCost);

        // Numero Comentarios

        TextView tv4 = convertView.findViewById(R.id.tvComment);

        if (charger.userComments != null && charger.userComments.size() != 0){

            if (charger.userComments.size() > 9) {
                tv4.setText("9+");
            } else {

                tv4.setText(String.valueOf(charger.userComments.size()));
            }
        }

        // Numero Fotos

        TextView tv5 = convertView.findViewById(R.id.tvPhoto);

        if (charger.mediaItems != null && charger.mediaItems.size() != 0){

            if (charger.mediaItems.size() > 9) {
                tv5.setText("9+");
            } else {

                tv5.setText(String.valueOf(charger.mediaItems .size()));
            }
        }
        return convertView;
    }

}
