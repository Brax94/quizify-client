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
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkManager {
    private static final String TAG = "NetworkManager";
    private static NetworkManager instance = null;

    private static final String prefixURL = "http://kane.royrvik.org:8000";
    private static final String clientID = "j4i6D27fFN59K9ZVKtXdy4Z3GoXYe7PjwR82uZiO";
    private static final String clientSecret = "ErceOadGt89TlCjV1JtsFs8Z6wSurqfCjDfiIut6e2SGA5YGhLxO8pkwve2VvwVme2NRzVoAnTZAtEToySrIeLcYWPbK2cT6zaydOE29oIvTmNWVAPsHQIzae1B3jWEh";
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
                        Log.d(TAG + ": ", "Login error response code: " + error.networkResponse.data);
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
                        listener.getResult(error.getMessage() + " :This is an error", null);
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

    public void getSingleGame(int id, final APIObjectResponseListener<String, JSONObject> listener) {
        String url = prefixURL + "/games/" + id + "/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(null, response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG + ": ", "Error Response code: " + error.getMessage());
                        listener.getResult(error.getMessage() + " :This is an error code", null);
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
                            System.out.println("Successful invite");
                            listener.getResult(null, Utils.jsonToMap(response));
                        } catch (JSONException e) {
                            System.out.println("Something went wrong jsonexception");
                            listener.getResult("Server responded with invalid data", null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error.networkResponse.data);
                        error.printStackTrace();
                        Log.d("INVITE_ERROR", error.toString());
                        Log.d( "INVITE_VOLLY_ERROR",error.networkResponse.data + " : " +
                                error.networkResponse.toString());
                        listener.getResult(error.getMessage() + " , THIS IS AN ERROR", null);
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

    public void getCategories(final APIObjectResponseListener<String,JSONArray>  listener) {
        String url = prefixURL + "/categories/";

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

    public void searchPlayer(String username,final APIObjectResponseListener<String, JSONObject> listener) {

        String url = prefixURL + "/search_by_username/";

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("username", username);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, new JSONObject(jsonParams), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                            listener.getResult(null, response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG + ": ", "Error Response code: " + error.getMessage());
                        listener.getResult(error.getMessage() + ": THIS IS AN ERROR", null);
                    }
                });

        requestQueue.add(jsObjRequest);
    }

    public void newRound(int gameId, int categoryId, final APIObjectResponseListener<String,JSONObject> listener) {
        String url = prefixURL + "/rounds/";

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("game", gameId);
        jsonParams.put("category", categoryId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            listener.getResult(null, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.data);
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


    public void saveRound(int gameId, int score, final APIObjectResponseListener<String, JSONObject> listener) {
        String url = prefixURL + "/rounds/" + gameId + "/";

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("score", score);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(null, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.data);
                        listener.getResult(error.getMessage() + "Cool error is error!", null);
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
    public void acceptInvite(int gameId, int categoryId, final APIObjectResponseListener<String,JSONObject> listener) {
        String url = prefixURL + "/accept_invite/";

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("game", gameId);
        jsonParams.put("category", categoryId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(jsonParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.getResult(null, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.data);
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
}
