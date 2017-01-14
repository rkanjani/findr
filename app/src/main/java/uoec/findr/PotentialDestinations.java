package uoec.findr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class PotentialDestinations extends AppCompatActivity {

    private HashMap<Integer, Point> pointHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potential_destinations);

        int startPoint = getIntent().getIntExtra("startPoint", -1);

        //create database
        DBHandler db = new DBHandler(this);

        db.getAllNeighbours();

        List<Point> points = db.getAllPoints();
        ListIterator<Point> iter = points.listIterator();
        pointHashMap = new HashMap<>();
        while (iter.hasNext()){
            Point p = iter.next();
            pointHashMap.put(p.getId(), p);
        }

        GraphModel graph = new GraphModel(pointHashMap);

        //Go through Linked List and grab Point object from HM
        ArrayList<Point> destinations = new ArrayList();


        for(int x=1; x<=pointHashMap.values().size();x++){
            destinations.add(pointHashMap.get(x));
        }

        destinations.add(0, pointHashMap.get(startPoint));

        //Load adapter with the list of neighbours
        DestinationAdapter adapter = new DestinationAdapter(this, destinations);

        //Load custom adapter
        ListView listView = (ListView) findViewById(R.id.potentialDestinations);
        listView.setAdapter(adapter);

        adapter.addAll(destinations);

    }
}
