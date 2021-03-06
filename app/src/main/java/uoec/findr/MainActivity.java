package uoec.findr;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import uoec.findr.com.varvet.barcodereadersample.barcode.BarcodeCaptureActivity;

public class MainActivity extends AppCompatActivity {

    private static final int BARCODE_READER_REQUEST_CODE = 0;
    private HashMap<Integer, Point> pointHashMap;
    private List<Point> points;
    private GraphModel graphModel;
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //create database
        DBHandler db = new DBHandler(this);
        db.onUpgrade(db.getWritableDatabase(), 1, 2);

        //initialize db with some points
        //db.addPoint(new Point("PointA", 1060, 1052, 3));
        //db.addPoint(new Point("PointB", 408, 1052, 3));
        //db.addPoint(new Point("PointC", 408, 624, 3));
        /*db.addNeighbour(1,2);
        db.addNeighbour(2,3);
        db.getAllNeighbours();*/

        List<Point> points = db.getAllPoints();

        ListIterator<Point> iter = points.listIterator();
        pointHashMap = new HashMap<>();
        while (iter.hasNext()){
            Point p = iter.next();
            pointHashMap.put(p.getId(), p);
        }

        int startPointID = getIntent().getIntExtra("startPoint", -1);
        int endPointID = getIntent().getIntExtra("endPoint", -1);

        if (startPointID != -1 && endPointID != -1) {
            graphModel = new GraphModel(pointHashMap);

            List<Point> path = graphModel.findPath(Integer.toString(startPointID), Integer.toString(endPointID));
            points.addAll(path);
        }
        
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
                startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
            }
        });

        ImageView mapView = (ImageView) findViewById(R.id.map);
        mapView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.map, 600, 600));

        createPoints();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    //ADD CODE TO GO TO NEXT INTENT

                    Intent intent = new Intent(getApplicationContext(), PotentialDestinations.class);
                    intent.putExtra("startPoint", Integer.parseInt(barcode.displayValue));
                    startActivity(intent);

                } else {
                    System.out.println("things BROKE");
                }
            } else {
                System.out.println("barcode returned error!");

            }
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    private void createPoints() {
        points = new ArrayList<>();
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);

        for (int i = 0; i < points.size() - 1; i++) {
            Point point1 = points.get(i);
            Point point2 = points.get(i + 1);

            LineView lineView = new LineView(getBaseContext(), point1, point2);
            layout.addView(lineView);
        }
    }
}
