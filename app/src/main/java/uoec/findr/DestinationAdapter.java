package uoec.findr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rohan on 2017-01-14.
 * Some code taken from https://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView
 */

public class DestinationAdapter extends ArrayAdapter<Integer>{
    public DestinationAdapter(Context context, ArrayList<Integer> destinations) {
        super(context, 0, destinations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position


        //Access HASHMAP and get point object
        //Point destination = getItem(position);


        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.destination_item, parent, false);
        }
        // Lookup view for data population
        TextView distance = (TextView) convertView.findViewById(R.id.distance);
        TextView name = (TextView) convertView.findViewById(R.id.name);



        // Populate the data into the template view using the data object
        //name.setText(destination.name);
        //Figure out how to get distance from ROOT
        //distance.setText(point.hometown);
        

        // Return the completed view to render on screen
        return convertView;
    }

}
