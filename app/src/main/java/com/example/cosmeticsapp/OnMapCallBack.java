package com.example.cosmeticsapp;

import com.google.android.gms.maps.model.LatLng;

public interface OnMapCallBack {
    void showAlert(String distance, LatLng start, LatLng end, String key);
}
