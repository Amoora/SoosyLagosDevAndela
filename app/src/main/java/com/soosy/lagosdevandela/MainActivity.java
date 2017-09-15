package com.soosy.lagosdevandela;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient();
    ListView mListView;
    ArrayAdapter<Developer> mAdapater;
    MyAdapter myAdapter;
    ArrayList<Developer> developers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String lagosJavaDevURI = "https://api.github.com/search/users?q=language:java+location:lagos";
        new GetLagosDevs().execute(lagosJavaDevURI);

    }

    private abstract class MyAdapter extends BaseAdapter {
        // override other abstract methods here
        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_dev, container, false);
            }
            Developer devs = (Developer) getItem(position);
            ((TextView) convertView.findViewById(R.id.devName))
                    .setText(devs.getLogin());

            ImageView img = ((ImageView) convertView.findViewById(R.id.devImage));
            Picasso.with(getApplicationContext()).load(devs.getAvatar_url()).into(img);
            return convertView;
        }
    }

    class GetLagosDevs extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            ///TODO: Network call

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(strings[0])
                    .build();

            String response = null;
            try {
                //response = getRequest.run(strings[0]);
                 response = client.newCall(request).execute().body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (response != null){
                    JSONObject object = new JSONObject(response);
                    JSONArray jsonArray = object.getJSONArray("items");
                    developers = new ArrayList<>(jsonArray.length());
                    for (int i= 0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Developer newdev = new Developer();
                        newdev.setLogin(jsonObject.getString("login"));
                        newdev.setAvatar_url(jsonObject.getString("avatar_url"));
                        newdev.setUrl(jsonObject.getString("url"));
                        newdev.setHtml_url(jsonObject.getString("html_url"));
                        developers.add(newdev);
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mListView =  (ListView) findViewById(R.id.list_view);
            myAdapter = new MyAdapter() {
                @Override
                public int getCount() {
                    return developers.size();
                }

                @Override
                public Object getItem(int i) {
                    return developers.get(i);
                }

                @Override
                public long getItemId(int i) {
                    return 0;
                }
            };
            mListView.setAdapter(myAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(MainActivity.this, infoDev.class);
                  intent.putExtra("dev", developers.get(i).getLogin());
                    intent.putExtra("avatar", developers.get(i).getAvatar_url());
                    intent.putExtra("url", developers.get(i).getUrl());
                    intent.putExtra("html_url", developers.get(i).getHtml_url());
                startActivity(intent);
                }
            });

        }
    }

}
