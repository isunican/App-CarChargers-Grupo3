package es.unican.carchargers.activities.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import es.unican.carchargers.R;
import es.unican.carchargers.constants.EOperator;
import es.unican.carchargers.model.Charger;
import es.unican.carchargers.model.UserComment;

public class CommentsArrayAdapter extends ArrayAdapter<UserComment> {

    public CommentsArrayAdapter(@NonNull Context context, @NonNull List<UserComment> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // this is the user comment we want to show here
        UserComment comment = getItem(position);

        // create the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_details_view_comment, parent, false);
        }


        // User

        TextView tv = convertView.findViewById(R.id.tvCommentUser);
        tv.setText(comment.userName);


        // Date

        TextView tv2 = convertView.findViewById(R.id.tvCommentDate);
        tv2.setText(comment.dateCreated);


        // Comment

        TextView tv3 = convertView.findViewById(R.id.tvCommentContent);
        tv3.setText(comment.comment);


        return convertView;
    }

}
