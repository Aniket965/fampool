package com.scibot.scibots.fampool;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;

import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.everything.providers.android.calendar.Calendar;
import me.everything.providers.android.calendar.CalendarProvider;
import me.everything.providers.android.calendar.Event;

public class MainActivity extends AppCompatActivity implements LocationListener {
    public static ArrayList<String> nameOfEvent = new ArrayList<String>();
    public static ArrayList<String> startDates = new ArrayList<String>();
    public static ArrayList<String> endDates = new ArrayList<String>();
    public static ArrayList<String> descriptions = new ArrayList<String>();
    public static ArrayList<String> eventLocations = new ArrayList<String>();
    public static ArrayList<Double> latitude = new ArrayList<>();
    public static ArrayList<Double> longitude = new ArrayList<>();
    public static ArrayList<Double> Interceptlatitude = new ArrayList<>();
    public static ArrayList<Double> Interceptlongitude = new ArrayList<>();
    public static ArrayList<Double> polercurrentlat = new ArrayList<>();
    public static ArrayList<Double> polercurrentlong = new ArrayList<>();
    public static ArrayList<Double> polerdestinationlat = new ArrayList<>();
    public static ArrayList<Double> polerdestinationlong = new ArrayList<>();
    public static ArrayList<String> poolingplaces = new ArrayList<>();


    double currentlat = 28.8431162,currentlong = 77.1053973;
    int day,month;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent3 = getIntent();
        Bundle b = intent3.getExtras();

        day=b.getInt("day");
        month=b.getInt("month");

        TextView textview =  (TextView) findViewById(R.id.dateshown);

        textview.setText( day + ", "+ getMonthName(month));

        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> email =  new ArrayList<>();

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                Cursor cur1 = cr.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);


                while (cur1.moveToNext()) {


                    String name=cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                    String emails = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                    if(emails != null){
                        names.add(name);
                        email.add(emails);

                    }
                }
                cur1.close();
            }
        }


        /*For calenders */

        ArrayList<String> events = readCalendarEvent(MainActivity.this);
        Log.d("events" , startDates.toString());

 // starting

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MainActivity();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        for(int i = 0 ; i < 1 ;i++) {

            getlatandlong(eventLocations.get(i));

            if(whetherinSameDirection(angleFromCoordinate(currentlat,currentlong,latitude.get(i),longitude.get(i)),angleFromCoordinate(28.6700478,77.1796675,28.5166817,77.2580425))){

                if(whetherinRightDistance(currentlat,currentlong,latitude.get(i),longitude.get(i),28.6700478,77.1796675,28.5166817,77.2580425)){
                 Interceptscoordinates(currentlat,currentlong,28.6700478,77.1796675,angleFromCoordinate(currentlat,currentlong,latitude.get(i),longitude.get(i)),angleFromCoordinate(28.6700478,77.1796675,28.5166817,77.2580425));
//                    poolingplaces.add(getLocationName(Interceptlatitude.get(i),Interceptlongitude.get(i)));
                    Log.d("pooling location" ,getLocationName(28.6666337,77.196873));

                }
            }


        }


        // get the context by invoking ``getApplicationContext()``, ``getContext()``, ``getBaseContext()`` or ``this`` when in the activity class
//        RideRequestButton requestButton = new RideRequestButton(MainActivity.this);
//        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_main);
//        layout.addView(requestButton);
//        RideParameters rideParams = new RideParameters.Builder()
//                // Optional product_id from /v1/products endpoint (e.g. UberX). If not provided, most cost-efficient product will be used
//                .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d")
//                // Required for price estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of dropoff location
//                .setDropoffLocation(
//                        37.775304f, -122.417522, "Uber HQ", "1455 Market Street, San Francisco")
//                // Required for pickup estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of pickup location
//                .setPickupLocation(37.775304f, -122.417522, "Uber HQ", "1455 Market Street, San Francisco")
//                // Required for price estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of dropoff location.
//                .setDropoffLocation(37.795079f, -122.4397805, "Embarcadero", "One Embarcadero Center, San Francisco")
//                .build();
//// set parameters for the RideRequestButton instance
//        requestButton.setRideParameters(rideParams);












    }
    public static String getMonthName(int month){
        switch(month+1){
            case 1:
                return "Jan";

            case 2:
                return "Feb";

            case 3:
                return "Mar";

            case 4:
                return "Apr";

            case 5:
                return "May";

            case 6:
                return "Jun";

            case 7:
                return "Jul";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";
        }

        return "";
    }

    /**/
    private  boolean whetherinRightDistance(Double l1, Double l2 ,Double l3 ,Double l4,Double lo1, Double lo2 ,Double lo3 ,Double lo4){
        return (finddistance(l1,lo1,l4,lo4) < finddistance(l3,lo3,l4,lo4));
    }


    private Boolean whetherinSameDirection(Double a1 ,Double a2 ) {
    return ((a1 -a2) < 90 && (a1-a2) > - 90);
    }

    private String getLocationName(Double lat,Double lon){
        String addr = new String();
        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
        try
        {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            addr =  addresses.get(0).toString();


        }
        catch (IOException e)
        {
            e.printStackTrace();

        }


    return addr;
    }

    public void Interceptscoordinates(double lat1, double long1, double lat2,
                            double long2,double a1, double a2){
        double c1,c2,lat,lon;
        c1 = lat1 -a1*long1;
        c2 = lat2 -a2*long2;
        lon=(c2-c1)/(a1-a2);
        lat=a1*lon+c1;
        Interceptlatitude.add(lat);
        Interceptlongitude.add(lon);

    }



    public  void getlatandlong(String location){
        if(Geocoder.isPresent()){
            try {

                Geocoder gc = new Geocoder(this);
                List<Address> addresses= gc.getFromLocationName(location, 1); // get the found Address Objects

               // A list to save the coordinates if they are available
                for(Address a : addresses){
                    if(a.hasLatitude() && a.hasLongitude()){
                       latitude.add(28.6666337);
                        longitude.add(77.196873);
                    }
                }
            } catch (IOException e) {
                // handle the exception
            }
        }

    }
    public double angleFromCoordinate(double lat1, double long1, double lat2,
                                       double long2) {

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        brng = 360 - brng; // count degrees counter-clockwise - remove to make clockwise

        return brng;
    }



    public static ArrayList<String> readCalendarEvent(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, null,
                        null, null);
        cursor.moveToFirst();

        String CNames[] = new String[cursor.getCount()];


        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();
        for (int i = 0; i < CNames.length; i++) {

            nameOfEvent.add(cursor.getString(1));
            startDates.add(getDate(Long.parseLong(cursor.getString(3))));
            descriptions.add(cursor.getString(2));
            eventLocations.add(cursor.getString(5));
            CNames[i] = cursor.getString(1);
            cursor.moveToNext();

        }
        return nameOfEvent;
    }

    public static String getDate(long milliSeconds) {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        Date date = new Date(milliSeconds);
        return formatter.format(date);

    }

   public Double finddistance(Double lat1 ,Double lon1,Double lat2 ,Double lon2) {
    double R = 6371e3;
       double φ1 = lat1*3.14/180;
       double φ2 = lat2*3.14/180;
       double Δφ = (lat1-lat2)*3.14/180;
       double Δλ = (lon2-lon1)*3.14/180;
double       a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
               Math.cos(φ1) * Math.cos(φ2) *
                       Math.sin(Δλ/2) * Math.sin(Δλ/2);
       double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
return R*c;

   }


    @Override
    public void onLocationChanged(Location location) {

//        currentlat = location.getLatitude();
//
//        currentlong = location.getLongitude();

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
