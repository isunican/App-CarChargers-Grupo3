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

import com.squareup.picasso.Picasso;

import java.util.List;

import es.unican.carchargers.R;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.MediaItem;

public class PhotosArrayAdapter extends ArrayAdapter<MediaItem> {

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
        {
            if (mediaItem.isEnabled == true) {
                if (mediaItem.isVideo == false) {
                    ImageView iv = convertView.findViewById(R.id.ivPhoto);
                    String imageUrl = mediaItem.itemUrl;
                    Picasso.get().load(imageUrl).resize(0 , 600).centerCrop().into(iv);

                }
                /*
                else {
                    ImageView iv = convertView.findViewById(R.id.ivPhoto);
                    String imageUrl = mediaItem.itemThumbnailUrl;
                    Picasso.get().load(imageUrl).into(iv);
                }
                */

            }
        }
        return convertView;
    }

}
