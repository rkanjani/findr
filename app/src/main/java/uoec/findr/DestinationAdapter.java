package uoec.findr;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rohan on 2017-01-14.
 * Some code taken from https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
 */

public class DestinationAdapter extends ArrayAdapter<Point> {
    private ArrayList<Point> destinations;

    public DestinationAdapter(Context context, ArrayList<Point> destinations) {
        super(context, 0, destinations);
        this.destinations = destinations;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        //Access HASHMAP and get point object
        final Point destination = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.destination_item, parent, false);
        }
        // Lookup view for data population
        TextView distance = (TextView) convertView.findViewById(R.id.distance);
        TextView name = (TextView) convertView.findViewById(R.id.name);

        LinearLayout row = (LinearLayout) convertView.findViewById(R.id.destination_row);


        // Populate the data into the template view using the data object
        name.setText(destination.name);
        //Figure out how to get distance from ROOT
        //distance.setText(point.hometown);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Access the row position here to get the correct data item
                Point endPoint = getItem(position);

                //Replace barcode capture activity with map
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("startPoint", destinations.get(0).getId());
                intent.putExtra("endPoint", endPoint.getId());
                getContext().startActivity(intent);
            }
        });


        // Return the completed view to render on screen
        return convertView;
    }

}
