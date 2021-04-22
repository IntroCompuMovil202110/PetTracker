package com.example.pettracker;

import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.mapboxsdk.utils.BitmapUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static android.graphics.Color.parseColor;
import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.expressions.Expression.color;
import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.expressions.Expression.interpolate;
import static com.mapbox.mapboxsdk.style.expressions.Expression.lineProgress;
import static com.mapbox.mapboxsdk.style.expressions.Expression.linear;
import static com.mapbox.mapboxsdk.style.expressions.Expression.literal;
import static com.mapbox.mapboxsdk.style.expressions.Expression.match;
import static com.mapbox.mapboxsdk.style.expressions.Expression.stop;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineCap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineGradient;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineJoin;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.lineWidth;

/**
 * Make a directions request with the Mapbox Directions API and then draw a line behind a moving
 * SymbolLayer icon which moves along the Directions response route.
 */
public class MapsActivity extends AppCompatActivity implements MapboxMap.OnMapClickListener, PermissionsListener {

    private static final String ORIGIN_ICON_ID = "origin-icon-id";
    private static final String DESTINATION_ICON_ID = "destination-icon-id";
    private static final String ROUTE_LAYER_ID = "route-layer-id";
    private static final String ROUTE_LINE_SOURCE_ID = "route-source-id";
    private static final String ICON_LAYER_ID = "icon-layer-id";
    private static final String ICON_SOURCE_ID = "icon-source-id";
    private MapView mapView;
    private MapboxMap map;
    private String currentMapStyle;
    private DirectionsRoute currentRoute;
    private MapboxDirections client;

    // User location
    private FusedLocationProviderClient locationProvider;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    // Light sensor
    private SensorManager sensorManager;
    private SensorEventListener sensorEventListener;

    // Adjust variables below to change the example's UI
    private Point originPoint = Point.fromLngLat(-74.1898188, 4.6324525);
    private Point destinationPoint = Point.fromLngLat(-74.06485, 4.628944);
    private static final float LINE_WIDTH = 6f;
    private static final String ORIGIN_COLOR = "#2096F3";
    private static final String DESTINATION_COLOR = "#F84D4D";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_maps);

        // Maps Style for luminosity sensor

        // Setup the MapView
        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                MapsActivity.this.map = mapboxMap;
                currentMapStyle = Style.DARK;
                mapboxMap.setStyle(new Style.Builder().fromUri(Style.DARK)
                        .withImage(ORIGIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                                getResources().getDrawable(R.drawable.blue_marker)))
                        .withImage(DESTINATION_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                                getResources().getDrawable(R.drawable.red_marker))), new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        initSources(style);
                        initLayers(style);
                        enableLocationComponent(style);

                        // Get the directions route from the Mapbox Directions API
                        getRoute(mapboxMap, originPoint, destinationPoint);

                        mapboxMap.addOnMapClickListener(MapsActivity.this);

                        Toast.makeText(MapsActivity.this, "Toca el mapa para cambiar el destino y calcular la ruta.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        initLightSensor();
    }

    private void enableLocationComponent(@NonNull Style loadedStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(MapsActivity.this)) {
            // Get an instance of the component
            LocationComponent locationComponent = map.getLocationComponent();
            // Activate with options
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(this, loadedStyle).build());
            locationProvider = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
            locationRequest = createLocationRequest();
            turnOnLocation();
            if(ContextCompat.checkSelfPermission(MapsActivity.this, PermissionsManagerPT.FINE_LOCATION_PERMISSION_NAME) == PackageManager.PERMISSION_GRANTED){
                // Enable to make component visible
                locationComponent.setLocationComponentEnabled(true);
                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        Location location = locationResult.getLastLocation();
                        if (location != null) {
                            originPoint = Point.fromLngLat(location.getLongitude(), location.getLatitude());
                            moveCameraToCoordinate(location.getLatitude(), location.getLongitude());
                        }
                    }
                };
            }
        } else {
            PermissionsManager permissionsManager = new PermissionsManager(MapsActivity.this);
            permissionsManager.requestLocationPermissions(MapsActivity.this);
        }
    }

    private void turnOnLocation(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(MapsActivity.this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(MapsActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });
        task.addOnFailureListener(MapsActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED: {
                        try{
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(MapsActivity.this, PermissionsManagerPT.LOCATION_PERMISSION_ID);
                            moveCameraToCoordinate(destinationPoint.latitude(), destinationPoint.longitude());
                        }
                        catch (IntentSender.SendIntentException sendEx) {
                            Log.e("LocationAPP", sendEx.getMessage());
                            Toast.makeText(MapsActivity.this, "Es necesario activar el GPS para detectar su ubicación", Toast.LENGTH_SHORT);
                        }
                        break;
                    }
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Toast.makeText(MapsActivity.this, "Es necesario activar el GPS para detectar su ubicación", Toast.LENGTH_SHORT);
                        break;
                }
            }
        });
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(MapsActivity.this, PermissionsManagerPT.FINE_LOCATION_PERMISSION_NAME) == PackageManager.PERMISSION_GRANTED) {
            locationProvider.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            Toast.makeText(MapsActivity.this,"Para esta funcionalidad es necesario otorgar permisos a la localización", Toast.LENGTH_SHORT);
        }
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void initLightSensor() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if( event.sensor.getType() == Sensor.TYPE_LIGHT)
                {
                    if (map != null) {
                        Log.i("entra", "entra con " + event.values[0] + " origin " + originPoint + " destination " + destinationPoint);
                        if (event.values[0] < 35 && !currentMapStyle.equalsIgnoreCase(Style.DARK)) {
                            //DarkStyle
                            Log.i("DARK", "DARK con " + event.values[0]);
                            currentMapStyle = Style.DARK;
                            map.setStyle(new Style.Builder().fromUri(Style.DARK)
                                    .withImage(ORIGIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                                            getResources().getDrawable(R.drawable.blue_marker)))
                                    .withImage(DESTINATION_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                                            getResources().getDrawable(R.drawable.red_marker))), new Style.OnStyleLoaded() {
                                @Override
                                public void onStyleLoaded(@NonNull Style style) {
                                    initSources(style);
                                    initLayers(style);
                                    enableLocationComponent(style);

                                    // Get the directions route from the Mapbox Directions API
                                    getRoute(map, originPoint, destinationPoint);

                                    map.addOnMapClickListener(MapsActivity.this);

                                    Toast.makeText(MapsActivity.this, "Toca el mapa para cambiar el destino y calcular la ruta.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if(event.values[0] >= 35 && !currentMapStyle.equalsIgnoreCase(Style.LIGHT)){
                            //LightStyle
                            Log.i("LIGHT", "LIGHT con " + event.values[0]);
                            currentMapStyle = Style.LIGHT;
                            map.setStyle(new Style.Builder().fromUri(Style.LIGHT)
                                    .withImage(ORIGIN_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                                            getResources().getDrawable(R.drawable.blue_marker)))
                                    .withImage(DESTINATION_ICON_ID, BitmapUtils.getBitmapFromDrawable(
                                            getResources().getDrawable(R.drawable.red_marker))), new Style.OnStyleLoaded() {
                                @Override
                                public void onStyleLoaded(@NonNull Style style) {
                                    initSources(style);
                                    initLayers(style);
                                    enableLocationComponent(style);

                                    // Get the directions route from the Mapbox Directions API
                                    getRoute(map, originPoint, destinationPoint);

                                    map.addOnMapClickListener(MapsActivity.this);

                                    Toast.makeText(MapsActivity.this, "Toca el mapa para cambiar el destino y calcular la ruta.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorManager.registerListener(sensorEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void moveCameraToCoordinate(double latitude, double longitude){
        map.animateCamera( CameraUpdateFactory.newLatLngZoom( new LatLng(latitude, longitude),12));
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(MapsActivity.this, "La localización no está activada.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted){
            enableLocationComponent(map.getStyle());
        } else {
            Toast.makeText(MapsActivity.this, "Es necesario activar el permiso para acceder a la localización.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Add the route and marker sources to the map
     */
    private void initSources(@NonNull Style loadedMapStyle) {
        loadedMapStyle.addSource(new GeoJsonSource(ROUTE_LINE_SOURCE_ID, new GeoJsonOptions().withLineMetrics(true)));
        loadedMapStyle.addSource(new GeoJsonSource(ICON_SOURCE_ID, getOriginAndDestinationFeatureCollection()));
    }

    /**
     * Util method that returns a {@link FeatureCollection} with the latest origin and destination locations.
     *
     * @return a {@link FeatureCollection} to be used for creating a new {@link GeoJsonSource} or
     * updating a source's GeoJSON.
     */
    private FeatureCollection getOriginAndDestinationFeatureCollection() {
        Feature originFeature = Feature.fromGeometry(originPoint);
        originFeature.addStringProperty("originDestination", "origin");
        Feature destinationFeature = Feature.fromGeometry(destinationPoint);
        destinationFeature.addStringProperty("originDestination", "destination");
        return FeatureCollection.fromFeatures(new Feature[] {originFeature, destinationFeature});
    }

    /**
     * Add the route and marker icon layers to the map
     */
    private void initLayers(@NonNull Style loadedMapStyle) {
        // Add the LineLayer to the map. This layer will display the directions route.
        loadedMapStyle.addLayer(new LineLayer(ROUTE_LAYER_ID, ROUTE_LINE_SOURCE_ID).withProperties(
                lineCap(Property.LINE_CAP_ROUND),
                lineJoin(Property.LINE_JOIN_ROUND),
                lineWidth(LINE_WIDTH),
                lineGradient(interpolate(
                        linear(), lineProgress(),

                        // This is where the gradient color effect is set. 0 -> 1 makes it a two-color gradient
                        // See LineGradientActivity for an example of a 2+ multiple color gradient line.
                        stop(0f, color(parseColor(ORIGIN_COLOR))),
                        stop(1f, color(parseColor(DESTINATION_COLOR)))
                ))));

        // Add the SymbolLayer to the map to show the origin and destination pin markers
        loadedMapStyle.addLayer(new SymbolLayer(ICON_LAYER_ID, ICON_SOURCE_ID).withProperties(
                iconImage(match(get("originDestination"), literal("origin"),
                        stop("origin", ORIGIN_ICON_ID),
                        stop("destination", DESTINATION_ICON_ID))),
                iconIgnorePlacement(true),
                iconAllowOverlap(true),
                iconOffset(new Float[] {0f, -4f})));
    }

    /**
     * Make a request to the Mapbox Directions API. Once successful, pass the route to the
     * route layer.
     *
     * @param mapboxMap   the Mapbox map object that the route will be drawn on
     * @param origin      the starting point of the route
     * @param destination the desired finish point of the route
     */
    private void getRoute(MapboxMap mapboxMap, Point origin, Point destination) {
        client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_WALKING)
                .accessToken(getString(R.string.mapbox_access_token))
                .build();
        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                // You can get the generic HTTP info about the response
                Timber.d("Response code: %s", response.code());

                if (response.body() == null) {
                    Timber.e("No se encontraron rutas, asegúrese de tener el usuario y token correcto.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Timber.e("No se encontraron rutas");
                    return;
                }

                // Get the Direction API response's route
                currentRoute = response.body().routes().get(0);

                if (currentRoute != null) {
                    if (mapboxMap != null) {
                        mapboxMap.getStyle(new Style.OnStyleLoaded() {
                            @Override
                            public void onStyleLoaded(@NonNull Style style) {

                                // Retrieve and update the source designated for showing the directions route
                                GeoJsonSource originDestinationPointGeoJsonSource = style.getSourceAs(ICON_SOURCE_ID);

                                if (originDestinationPointGeoJsonSource != null) {
                                    originDestinationPointGeoJsonSource.setGeoJson(getOriginAndDestinationFeatureCollection());
                                }

                                // Retrieve and update the source designated for showing the directions route
                                GeoJsonSource lineLayerRouteGeoJsonSource = style.getSourceAs(ROUTE_LINE_SOURCE_ID);

                                // Create a LineString with the directions route's geometry and
                                // reset the GeoJSON source for the route LineLayer source
                                if (lineLayerRouteGeoJsonSource != null) {
                                    // Create the LineString from the list of coordinates and then make a GeoJSON
                                    // FeatureCollection so we can add the line to our map as a layer.
                                    LineString lineString = LineString.fromPolyline(currentRoute.geometry(), PRECISION_6);
                                    lineLayerRouteGeoJsonSource.setGeoJson(Feature.fromGeometry(lineString));
                                }
                            }
                        });
                    }
                } else {
                    Timber.d("La ruta está en null");
                    Toast.makeText(MapsActivity.this,
                            "La ruta no puede ser dibujada en este momento.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Toast.makeText(MapsActivity.this,
                        "Falló el llamado al API de rutas. Intenta más tarde.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onMapClick(@NonNull LatLng mapClickPoint) {
        // Move the destination point to wherever the map was tapped
        destinationPoint = Point.fromLngLat(mapClickPoint.getLongitude(), mapClickPoint.getLatitude());

        // Get a new Directions API route to that new destination and eventually re-draw the
        // gradient route line.
        getRoute(map, originPoint, destinationPoint);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel the Directions API request
        if (client != null) {
            client.cancelCall();
        }
        if (map != null) {
            map.removeOnMapClickListener(this);
        }
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}