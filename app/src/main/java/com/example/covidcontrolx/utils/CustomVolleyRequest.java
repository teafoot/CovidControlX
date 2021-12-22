package com.example.covidcontrolx.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CustomVolleyRequest extends StringRequest {
    public CustomVolleyRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError { // required to bypass 403 forbidden http request access
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent","Mozilla/5.0");
        return headers;
    }
}