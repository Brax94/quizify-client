package com.myapps.quizify.quizifyclient.mainMenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myapps.quizify.quizifyclient.R;
import com.myapps.quizify.quizifyclient.game.CategoryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by eliasbragstadhagen on 15/04/16.
 */
public class CustomRelativeAdapter extends BaseAdapter{

    ArrayList<JSONObject> result;
    Context context;
    String functionTag;
    boolean playable;
    private static LayoutInflater inflater = null;
    SharedPreferences prefs;

    public CustomRelativeAdapter(MainMenuActivity activity, ArrayList<JSONObject> jsonArray, String functionTag, boolean playable){
        result = jsonArray;
        context = activity;
        this.playable = playable;
        this.functionTag = functionTag;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return result.get(position);
    }

    @Override
    public long getItemId(int position) {
        int n = 0;
        try {
            n = result.get(position).getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return n;
    }

    public class Holder{
        TextView username;
        TextView score;
        TextView functionTag;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.custom_relative_adapter, parent, false);
        String split = " : ";

        holder.username = (TextView) rowView.findViewById(R.id.username);
        try {
            if(result.get(position).getJSONObject("player1").getString("username").equals(prefs.getString("username", "#notavalidname"))){
            holder.username.setText(result.get(position).getJSONObject("player2").getString("username"));
          //  holder.score.setText(result.get(position).getJSONObject("total_score").getString("player1") + " : " +
            //        result.get(position).getJSONObject("total_score").getString("player2"));
            }
            else{
                holder.username.setText(result.get(position).getJSONObject("player1").getString("username"));
                holder.score.setText(result.get(position).getJSONObject("total_score").getString("player2") + split +
                        result.get(position).getJSONObject("total_score").getString("player1"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        holder.score = (TextView) rowView.findViewById(R.id.score);
        //TODO: Fix score as soon as it is contained within json
        //Score holder dummy:
        Random rand = new Random();
        //String dummy = rand.nextInt(6) + " : " + rand.nextInt(6);
        //holder.score.setText(dummy);


        holder.functionTag = (TextView)  rowView.findViewById(R.id.functionTag);
        holder.functionTag.setText(functionTag);


        if(playable) {
            holder.functionTag.setEnabled(true);
            holder.functionTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, CategoryActivity.class);
                    try {
                        i.putExtra("game_id", result.get(position).getInt("id"));
                        i.putExtra("game_type", functionTag);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("ELIAS_ERROR", "SOMETHING WENT VERY WRONG WITH FINDING ID");
                    }
                    context.startActivity(i);

                }
            });
        }
        return rowView;
    }
}
