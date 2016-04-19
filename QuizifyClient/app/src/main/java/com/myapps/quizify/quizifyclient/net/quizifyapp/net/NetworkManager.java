package com.myapps.quizify.quizifyclient.net.quizifyapp.net;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkManager {
    private static final String TAG = "NetworkManager";
    private static NetworkManager instance = null;

    private static final String prefixURL = "http://kane.royrvik.org:8000";
    private static final String clientID = "VoquKbj42SBvq9bpfcSsFIbnUOcsihQ2roHL0js9";
    private static final String clientSecret = "P8WnEHBUpFRDR09jFnCzZ2wHQvDF8B5JyFHagTM27gjFHaGIhinT3sPCC0DpfGgvlZlge1nNWUUSh0rX16MimCbil1yAkrSGe2RB2mc7jEKnQe2QrW7UcoUMVbqtAIvy";
    private static final String grantType = "password";

    private static String authKey = null;

    //for Volley API
    public RequestQueue requestQueue;

    private NetworkManager(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized NetworkManager getInstance(Context context) {
        if (null == instance)
            instance = new NetworkManager(context);
        return instance;
    }

    // This is so you don't need to pass context each time
    public static synchronized NetworkManager getInstance() {
        if (null == instance) {
            throw new IllegalStateException(NetworkManager.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public void register(String email, String username, String password, final APIAuthenticationResponseListener<String> listener) {

        String url = prefixURL + "/register/";

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("email", email);
        jsonParams.put("username", username);
        jsonParams.put("password", password);
        jsonParams.put("client_id", clientID);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG + ": ", "Register Response : " + response.toString());
                        try {
                            authKey = response.getString("token");
                            listener.getResult(null);
                        } catch (JSONException e) {
                            listener.getResult("Token not returned");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG + ": ", "Register error response code: " + error.networkResponse.statusCode);
                        listener.getResult("Something went wrong, email may be registerd, password may be to weak, or username may be taken");
                    }
                });

        requestQueue.add(request);
    }

    public void login(String username, String password, final APIAuthenticationResponseListener<String> listener) {
        String url = prefixURL + "/login/";

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("username", username);
        jsonParams.put("password", password);
        jsonParams.put("client_id", clientID);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG + ": ", "Login Response : " + response.toString());
                        try {
                            authKey = response.getString("token");
                            Log.d("ELIAS", authKey);
                            listener.getResult(null);
                        } catch (JSONException e) {
                            listener.getResult("Token not returned");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG + ": ", "Login error response code: " + error.networkResponse.statusCode);
                        listener.getResult("Something went wrong");
                    }
                });

        requestQueue.add(request);
    }

    public void getGames(final APIObjectResponseListener<String, JSONArray> listener) {
        String url = prefixURL + "/games/";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, new JSONArray(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                            listener.getResult(null, response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG + ": ", "Error Response code: " + error.getMessage());
                        listener.getResult(error.getMessage(), null);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params =  new HashMap<>();
                params.put("Authorization", "Bearer " + authKey);
                return params;
            }
        };

        requestQueue.add(request);
    }

    public void sendInvite(String id, final APIObjectResponseListener<String, Map<String, Object>> listener) {
        String url = prefixURL + "/games/";

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("player2", id);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listener.getResult(null, Utils.jsonToMap(response));
                        } catch (JSONException e) {
                            listener.getResult("Server responded with invalid data", null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.getResult(error.getMessage(), null);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params =  new HashMap<>();
                params.put("Authorization", "Bearer " + authKey);
                return params;
            }
        };

        requestQueue.add(request);
    }

    public void getCateogries(final APIObjectResponseListener<String, Map<String, Object>> listener) {
        String url = prefixURL + "/categories/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listener.getResult(null, Utils.jsonToMap(response));
                        } catch (JSONException e) {
                            listener.getResult("Server responded with invalid data", null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG + ": ", "Error Response code: " + error.getMessage());
                        listener.getResult(error.getMessage(), null);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params =  new HashMap<>();
                params.put("Authorization", "Bearer " + authKey);
                return params;
            }
        };

        requestQueue.add(request);
    }

    public void getPlayers(final APIObjectResponseListener<String, Map<String, Object>> listener) {
        String url = prefixURL + "/players/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listener.getResult(null, Utils.jsonToMap(response));
                        } catch (JSONException e) {
                            listener.getResult("Server responded with invalid data", null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG + ": ", "Error Response code: " + error.getMessage());
                        listener.getResult(error.getMessage(), null);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params =  new HashMap<>();
                params.put("Authorization", "Bearer " + authKey);
                return params;
            }
        };

        requestQueue.add(request);
    }

    public boolean existsValidToken(){
        if(authKey == null){
            return false;
        }
        //:TODO: Create token-check with server
        return true;
    }
    public void destroy(){
        this.instance = null;
    }

    public void acceptInvite() {
        // TODO: Create custom URL
        //          - Should create new round
        //          - Send notification to player that invited
    }

    public void saveRound() {
        // TODO: Create custom POST url in API
    }
}
