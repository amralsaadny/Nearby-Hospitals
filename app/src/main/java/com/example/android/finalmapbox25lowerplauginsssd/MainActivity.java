package com.example.android.finalmapbox25lowerplauginsssd;

//import android.app.appsearch.SearchResult;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.caverock.androidsvg.SVG;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.staticmap.v1.MapboxStaticMap;
import com.mapbox.api.staticmap.v1.StaticMapCriteria;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
//import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import com.mapbox.maps.plugin.gestures.OnMapClickListener;
import com.mapbox.search.CategorySearchEngine;
import com.mapbox.search.CategorySearchOptions;
import com.mapbox.search.MapboxSearchSdk;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.SearchCallback;
import com.mapbox.search.SearchRequestTask;
import com.mapbox.search.result.SearchResult;

//import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute.Builder;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.android.navigation.ui.v5.NavigationView;


// better location liberary
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.mapbox.mapboxsdk.utils.BitmapUtils;



import org.jetbrains.annotations.NotNull;

import java.awt.font.NumericShaper;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


//import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

//import com.mapbox.navigation.base.route.NavigationRoute;

//for search engine

//import com.mapbox.mapboxandroiddemo.R;

/**
 * Use the {@link LocationComponentOptions} builder's pulseEnabled()
 * method to enable basic pulsing of the LocationComponent's pulsing circle.
 */
public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {

    private PermissionsManager permissionsManager;
    public static MapboxMap mapboxMap;

    //mapview not static

    public  MapView mapView;

    private Point originPosition;
    private Point distinationPosition;
    private Marker distionationMarker;

  //  private Point originLocation;

   // double originLocationlat;
   // double originLocationLong;

    //navigation
    public  NavigationMapRoute navigationMapRoute;
    private static final String TAG ="MainActivity";

    //new location variabele from GPS app
    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationReques;
    LocationCallback mLocationCallback;
    //var to get the long and att
    private int locationRequestCode = 1000;
    private double wayLatitude = 0.0, wayLongitude = 0.0;




    //for catagories places  serch engine

    private CategorySearchEngine categorySearchEngine;
    private SearchRequestTask searchRequestTask;
    private List<? extends SearchResult> resultssss;
   // MarkerViewManager markerViewManager ;
    //MarkerView markerView;
    //List<MarkerOptions> mymarker=new ArrayList<MarkerOptions>();
    //List<MarkerView> mymarker2=new ArrayList<MarkerView>();
  // private Symbol symbol;

   //navigation
    public  Button soundButton;
    private static final String ID_ICON_AIRPORT = "airport";
   // private static final String MAKI_ICON_CIRCLE = "fire-station-15";
   private static final String MAKI_ICON_CIRCLE = "Trailhead";

  // Uri path = Uri.parse("android.resource:/com.example.android.finalmapbox25lowerplauginsssd" + R.drawable.ic_heart);


    private static final String MAKI_ICON_HEART="ic_heart";
    private static final String IC_MENU_MYLOCATION ="ic_menu_mylocation";
    private static final String MAKI_MENU_MARKER="ic_marker";




    double newlat;
    double newlong;
    Symbol symbol;
    SymbolManager symbolManager;
    SymbolOptions nearbyOptions;
    Button bottun;











    private final SearchCallback searchCallback = new SearchCallback() {

        @Override
        public void onResults(@NonNull List<? extends SearchResult> results, @NonNull ResponseInfo responseInfo) {
            if (results.isEmpty()) {
                Log.i("SearchApiExample", "No category search results");

            } else {
                Log.i("SearchApiExample", "Category search results: " + results);
                //عك
                resultssss= results;

             //marker

                //View annotaion
                symbolManager  = new SymbolManager(mapView, mapboxMap, mapboxMap.getStyle());
                SymbolOptions symbolOptions = new SymbolOptions();
                for (int i = 0;i<resultssss.size();i++) {


                    newlat = resultssss.get(i).getCoordinate().latitude();
                    newlong = resultssss.get(i).getCoordinate().longitude();


                   // symbol = symbolManager.create(symbolOptions);
                    //anther example
                    // create nearby symbols
                    //drawable = resources.getDrawable(resources.obtainTypedArray(R.array.array_name).getResourceId(array_index, 0), context.getTheme());

/*
                    Resources res = getResources();
                    String mDrawableName = "ic_heart";
                    int resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
                    Drawable drawable = res.getDrawable(resID );
                    drawable.setAlpha(R.drawable.ic_heart);

                    String pathName = "res/drawable/ic_heart.xml";
                    Bitmap b = BitmapFactory.decodeFile(pathName);


 */





                     nearbyOptions = new SymbolOptions()
                            .withLatLng(new LatLng(newlat, newlong))
                            // .withLatLng(new LatLng(40.6934, -73.9766))
                             .withIconImage(MAKI_ICON_HEART)

                          //.withIconImage(MAKI_ICON_CIRCLE)
                           //  .withIconImage(b.toString())
                           // .withTextField("hospital")






                            .withIconColor(ColorUtils.colorToRgbaString(Color.YELLOW))
                            .withIconSize(2.5f)
                            .withSymbolSortKey(5.0f)
                            .withDraggable(false);
                    symbolManager.create(nearbyOptions);









                    System.out.println("hello "+resultssss.get(i).getName());
                }


                symbolManager.addClickListener(new OnSymbolClickListener() {
                    @Override
                    public boolean onAnnotationClick(Symbol symbol) {

                        //intent for navigatio
                        double symbolLat=symbol.getLatLng().getLatitude();
                        double symbolLong=symbol.getLatLng().getLongitude();




                        Uri gmmIntentUri = Uri.parse("google.navigation:q="+symbolLat+","+symbolLong);


                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);


                   return true;
                    }
                });







/*
                for (int i = 0;i<resultssss.size();i++){
                    markerOptions.position(new LatLng(resultssss.get(i).getCoordinate().latitude(),resultssss.get(i).getCoordinate().longitude()));

                    mymarker.add(markerOptions);
                    mymarker.get(i).title(mymarker.get(i).getTitle());

                   // mapboxMap.addMarkers()
                    System.out.println("in elseeee"+resultssss.get(i).getDescriptionText());
                    mapboxMap.addMarkers(Collections.singletonList(mymarker.get(i)));

                }
                for (int i = 0;i<resultssss.size();i++){

                   // mapboxMap.addMarker(mymarker.get(i));
                    mapboxMap.addMarkers(Collections.singletonList(mymarker.get(i)));

                }
                */
                /*
                Point point ;
                for (int i = 0;i<resultssss.size();i++) {
                   // mapboxMap.addMarker(new MarkerOptions().position(point));
                    point=resultssss.get(i).getCoordinate();
                    LatLng lt=point.

                    mapboxMap.addMarker(new MarkerOptions().position(resultssss.get(i).getCoordinate(),resultssss.get(i).getCoordinate().longitude()),mapView);
                   // mymarker2.add(markerView);
                   // MarkerViewManager.addMarker(mymarker2.get(i));

*/


          /*
            for (int i = 0;i<resultssss.size();i++) {


                newlat=resultssss.get(i).getCoordinate().latitude();
                newlong=resultssss.get(i).getCoordinate().longitude();


                mapboxMap.addMarker(new MarkerOptions()




                        .position(new LatLng(newlat,newlong))


                        .title("hospital"));




            }



           */
                /*
                newlat=resultssss.get(0).getCoordinate().latitude();
                newlong=resultssss.get(0).getCoordinate().longitude();


                mapboxMap.addMarker(new MarkerOptions()




                        .position(new LatLng(newlat,newlong))


                        .title("hospital"));
*/








            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            Log.i("SearchApiExample", "Search error", e);
        }



    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        //crete objects for new location fromGPS app
        mFusedLocationClient =LocationServices.getFusedLocationProviderClient(this);
        mLocationReques=LocationRequest.create();
        mLocationCallback = new LocationCallback();
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


        //soundButton = findViewById(R.id.soundButton);



        //for catagories or search engine categroies
        //intialization of mapbox search sdk

       // searchRequestTask = categorySearchEngine.search("clinic", options, searchCallback);




        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //Navgation








    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        MainActivity.this.mapboxMap = mapboxMap;
        //mapboxMap.addOnMapClickListener(MainActivity.this);
        mapView.setMaximumFps(500);
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {

            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
                bottun=(Button)findViewById(R.id.button);

                Drawable myIcon = getResources().getDrawable(R.drawable.ic_marker);
                style.addImage(MAKI_ICON_HEART, myIcon);
                style.addImage("ic_marker",myIcon);


                bottun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        enableLocationComponent(style);
                    }
                });





              //
               // style.addImage("my_image", R.drawable.ic_heart);

                //style.addImageAsync("heartIcon", Drawable.createFromPath(String.valueOf(R.drawable.ic_heart)));

              // style.addImage("my_image", Bitmap.createBitmap(R.drawable.ic_heart));




                //marker


              //  resultssss.forEach((n) -> markerOptions.position(new LatLng(n.getCoordinate().latitude(),n.getCoordinate().longitude())));
               // markerOptions.position(new LatLng(40.6914, -73.9753));
               // resultssss.forEach((n) -> markerOptions.setPosition(new LatLng(n.getAddress())));
               // markerOptions.setPosition(new LatLng())
               // String s =resultssss.forEach((n) ->n.getAddress().getPlace());

               //
                // Toast.makeText(MainActivity.this,resultssss.forEach((n) ->n.getAddress().getPlace()),Toast.LENGTH_LONG).show();


                MapboxSearchSdk.initialize(getApplication(),
                        getString(R.string.access_token),
                        LocationEngineProvider.getBestLocationEngine(MainActivity.this));

                //categries
                categorySearchEngine = MapboxSearchSdk.getCategorySearchEngine();
                final CategorySearchOptions options = new CategorySearchOptions.Builder()
                        .limit(30)
                        .build();
                searchRequestTask = categorySearchEngine.search("hospital", options, searchCallback);


                // annotaion


        // Use the manager to draw the symbol.
                /*11111
                for (int i = 0;i<resultssss.size();i++) {


                    newlat = resultssss.get(i).getCoordinate().latitude();
                    newlong = resultssss.get(i).getCoordinate().longitude();

                    symbol = symbolManager.create(symbolOptions);
                    //anther example
                    // create nearby symbols
                    SymbolOptions nearbyOptions = new SymbolOptions()
                            .withLatLng(new LatLng(newlat, newlong))
                            .withIconImage(MAKI_ICON_CIRCLE)
                            .withIconColor(ColorUtils.colorToRgbaString(Color.YELLOW))
                            .withIconSize(2.5f)
                            .withSymbolSortKey(5.0f)
                            .withDraggable(true);
                    symbolManager.create(nearbyOptions);
                }

                 */








                // markerViewManager.addMarker(new MarkerView(new LatLng(40.6726, -73.9184),mapView));

// Create symbol manager object.
             //   SymbolManager symbolManager = new SymbolManager(mapView, mapboxMap, style);

// Add click listeners if desired.
                /*
                symbolManager.addClickListener(symbol ->
                        );

                symbolManager.addLongClickListener(symbol -> {

                });
*/
// Set non-data-driven properties.

// Create a SymbolManager.
               //  symbolManager = new SymbolManager(mapView, mapboxMap, style);

// Set non-data-driven properties.
              //  symbolManager.setIconAllowOverlap(true);
            // symbolManager.setTextAllowOverlap(true);

// Create a symbol at the specified location.


// Use the manager to draw the symbol.
                //symbol = symbolManager.create(symbolOptions);
               /*
                GeoJsonOptions geoJsonOptions = new GeoJsonOptions().withTolerance(0.4f);


                symbolManager.setIconAllowOverlap(true);
                symbolManager.setIconTranslate(new Float[]{-4f,5f});



                SymbolOptions symbolOptions = new SymbolOptions()
                        .withLatLng(new LatLng(40.6834, -73.9879))
                        .withIconImage(String.valueOf(R.drawable.mapbox_compass_icon))
                        .withIconColor(String.valueOf(android.R.mipmap.sym_def_app_icon))
                        .withIconSize(10.3f);

                symbolManager.setIconAllowOverlap(true);

*/
/*
                MapboxStaticMap staticImage = MapboxStaticMap.builder()
                        .accessToken(getString(R.string.access_token))
                        .styleId(StaticMapCriteria.LIGHT_STYLE)
                        .cameraPoint(Point.fromLngLat(40.6881, -73.9710)) // Image's centerpoint on map
                        .cameraZoom(13)
                        .width(320) // Image width
                        .height(320) // Image height
                        .retina(true) // Retina 2x image will be returned
                        .build();

*/























            }
        });






    }






    @SuppressWarnings( {"MissingPermission"})
    public void enableLocationComponent(@NonNull Style loadedMapStyle) {
       /*
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Enable the most basic pulsing styling by ONLY using
            // the `.pulseEnabled()` method
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this)
                    .pulseEnabled(true)
                    .build();

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
            //locationComponent.zoomWhileTracking(30);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
             originLocationLong=locationComponent.getLastKnownLocation().getLongitude();
            originLocationlat=  locationComponent.getLastKnownLocation().getLatitude();



        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }

        */
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //ActivityCompat.requestPermissions(MainActivity.this, permissions, 1000);
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    locationRequestCode);
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        } else {
            //permsision granted
            mFusedLocationClient.requestLocationUpdates(mLocationReques,mLocationCallback,null );
            // Enable the most basic pulsing styling by ONLY using
            // the `.pulseEnabled()` method
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this)
                    .pulseEnabled(true)
                    .build();

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);
            //locationComponent.zoomWhileTracking(30);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
            /*
            wayLongitude=locationComponent.getLastKnownLocation().getLongitude();
            wayLatitude=  locationComponent.getLastKnownLocation().getLatitude();


             */

        }


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                        if (location != null) {
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();

                        }
                    });
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }


        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {




                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        if (mapView != null) {
            mapView.onStart();
        }

        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
      //  markerViewManager.removeMarker(markerView);
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            ViewGroup parent = (ViewGroup) mapView.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
        }
        searchRequestTask.cancel();
        //markerViewManager.onDestroy();

        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }



/*
    @Override
    public boolean onMapClick(@NonNull LatLng point) {
        if(distionationMarker!=null){
            mapboxMap.removeMarker(distionationMarker);
        }

        distionationMarker=mapboxMap.addMarker(new MarkerOptions().position(point));
        distinationPosition=Point.fromLngLat(point.getLongitude(),point.getLatitude());
        //originPosition=Point.fromLngLat(wayLongitude,wayLatitude);
        //navigation

      //  getRoute(originPosition,distinationPosition);

        startButton.setEnabled(true);
        startButton.setBackgroundResource(R.color.mapbox_blue);
        return true;
    }

 */
    //navigation
    private void getRoute ( Point originLocation,Point distinationPosition) {
        //NavigationRoute.Builder

        NavigationRoute.builder()

                .accessToken(getString(R.string.access_token))
                .origin(originLocation)

                .destination(distinationPosition)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null) {
                            Log.e(TAG, "no route founnd check right user and access token");


                        } else if (response.body().routes().size() == 0) {
                            //do toast later
                            Log.e(TAG, "no routes are found");


                        }
                        DirectionsRoute currentRoute = response.body().routes().get(0);
                        //for to not have multiple routes

                        if (navigationMapRoute != null) {

                           // navigationMapRoute.removeRoute();
                        } else {

                            navigationMapRoute = new NavigationMapRoute(null, mapView, mapboxMap);
                        }
                        // navigationMapRoute.onMapChanged(1);

                       // navigationMapRoute.addRoute(currentRoute);


                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG, "error " + t.getMessage());

                    }
                });
    }



/*
    @Override
    public boolean onMapClick(@NotNull Point point) {
       // getRoute(originPosition,distinationPosition);






        return false;
    }

 */



}