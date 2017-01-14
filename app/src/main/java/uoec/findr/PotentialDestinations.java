package uoec.findr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class PotentialDestinations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potential_destinations);

        //Go through Linked List and grab Point object from HM
        ArrayList<Point> destinations = new ArrayList<Point>();

        //Load adapter with the list of neighbours
        //DestinationAdapter adapter = new DestinationAdapter(this, neighbours);

        //Load custom adapter
        //ListView listView = (ListView) findViewById(R.id.potentialDestinations);
        //listView.setAdapter(adapter);

        //adapter.addAll(destinations);

    }
}
