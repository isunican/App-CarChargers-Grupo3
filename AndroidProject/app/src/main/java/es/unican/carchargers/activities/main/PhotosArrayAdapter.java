package es.unican.carchargers.activities.main;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.unican.carchargers.R;
import es.unican.carchargers.model.MediaItem;

public class PhotosArrayAdapter extends ArrayAdapter<MediaItem> {

    int numTag = 0;

    public PhotosArrayAdapter(@NonNull Context context, @NonNull List<MediaItem> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // this is the user comment we want to show here
        MediaItem mediaItem = getItem(position);



        // create the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_details_view_photo, parent, false);
        }
        // Photo (ha de estar enabled y en el caso de que sea un video se inyecta su miniatura)

        if (mediaItem.isEnabled && !mediaItem.isVideo) {

            ImageView iv = convertView.findViewById(R.id.ivPhoto);
            iv.setTag("imagen" + numTag);
            numTag++;
            String imageUrl = mediaItem.itemUrl;
            Picasso.get().load(imageUrl).resize(0 , 600).centerCrop().into(iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopUp(iv,imageUrl);

                }
            });
        }
        return convertView;
    }

    private void showPopUp(ImageView imageView,String imageUrl) {

        ImageView popupImage = new ImageView(getContext());
        Picasso.get().load(imageUrl).into(popupImage);

        // Crear el popup
        PopupWindow popupWindow = new PopupWindow(
                popupImage,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        View darkView = new View(getContext());
        darkView.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.black));
        darkView.setAlpha(0.7f); // Ajusta la opacidad según sea necesario

        // Agregar el fondo oscuro al popup
        ViewGroup darkOverlay = new FrameLayout(getContext());
        darkOverlay.addView(darkView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        darkOverlay.addView(popupImage);


        // Mostrar el popup en el centro de la pantalla
        popupWindow.setContentView(darkOverlay);
        popupWindow.showAtLocation(imageView, Gravity.CENTER, 0, 0);


        darkOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


    }



}
