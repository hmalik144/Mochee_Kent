package com.example.h_mal.mochee;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.h_mal.mochee.Fragment6_parts.Enquiry;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by h_mal on 07/10/2017.
 */

public class Fragment_Six extends Fragment{

    MapView mapView;
    GoogleMap map;
    private DatabaseReference mDatabase;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment6, container, false);

        final EditText name = (EditText) rootView.findViewById(R.id.nameET);
        final EditText email = (EditText) rootView.findViewById(R.id.editText2);
        final EditText subject = (EditText) rootView.findViewById(R.id.editText);
        final EditText message = (EditText) rootView.findViewById(R.id.editText4);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button button = (Button) rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Enquiry enquiry = new Enquiry(name.getText().toString(),email.getText().toString(),subject.getText().toString(),message.getText().toString());
                mDatabase.child("Enquiry").push().setValue(enquiry);
            }
        });

        mapView = (MapView) rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                map = mMap;

                // For showing a move to my location button
//                map.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
                LatLng mocheeLocation = new LatLng(51.5226044,-0.118918);
                map.addMarker(new MarkerOptions().position(mocheeLocation).title("Mochee")).showInfoWindow();

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(mocheeLocation).zoom(14).build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse("https://goo.gl/maps/nz4ukSBXrbvRwGvX6"));
                        getActivity().startActivity(intent);
                        return false;
                    }
                });
            }
        });

        return rootView;

    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
