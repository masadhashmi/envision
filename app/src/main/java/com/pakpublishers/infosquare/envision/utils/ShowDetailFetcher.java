package com.pakpublishers.infosquare.envision.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.pakpublishers.infosquare.envision.adapters.TVChannelsAdapter;
import com.pakpublishers.infosquare.envision.objects.channel;
import com.pakpublishers.infosquare.envision.objects.episode;
import com.pakpublishers.infosquare.envision.objects.schedule;
import com.pakpublishers.infosquare.envision.objects.series_detail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by M Asad Hashmi on 10/9/2016.
 */

public class ShowDetailFetcher extends  Thread {



    /**
     * Created by M Asad Hashmi on 10/5/2016.
     */



        public static boolean isBUSY=false;
        public String Parameters = "";
        public String CompleteURL = "";

        public static Handler ReferenceHandler = null;
        public final static int SUCCESS = 1;
    public final static int FAILURE = -1;
        public final static int START_PARSING = 2;
        public ArrayList<episode> EpisodesList = null;
        public series_detail Series_Detail_Object=null;
        public Context cntx = null;

        @Override
        public void run() {

            Log.e("Web URL", CompleteURL);
            Log.e("Parameters", Parameters);
            URL url = null;
            HttpURLConnection urlConnection = null;
            try {


                url = new URL(CompleteURL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(60000);
                urlConnection.setConnectTimeout(60000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                //Parameters = "user_id="+user_id+"&user_property="+user_property+"&start_date="+start_date+"&end_date="+end_date;
                OutputStream output = new BufferedOutputStream(urlConnection.getOutputStream());
                output.write(Parameters.getBytes());
                output.flush();


                InputStream inputStream;
                if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    inputStream = urlConnection.getInputStream();
                } else {
                    inputStream = urlConnection.getErrorStream();
                }
                String ResponseResult = getStringFromInputStream(inputStream);

                Log.d("Response",ResponseResult);

                //m.obj=ResponseResult;
//
                JSONObject jsonObject = new JSONObject(ResponseResult);
                JSONObject series_detailJSON= jsonObject.getJSONObject("series_details");
                JSONArray episode_listJSON = jsonObject.getJSONArray("episode_list");

                Series_Detail_Object=new series_detail();
                Series_Detail_Object.parse(series_detailJSON);

                Log.e("Live","["+Series_Detail_Object.getLive()+"]");
                Log.e("Status:","["+Series_Detail_Object.getStatus()+"]");
                Log.e("recording_id:","["+Series_Detail_Object.getRecording_id()+"]");

                EpisodesList = new ArrayList<episode>();
                for (int i = 0; i < episode_listJSON.length(); i++) {
                    JSONObject ch = (JSONObject) episode_listJSON.get(i);
                    episode c = new episode();
                    c.parse(ch);

                    EpisodesList.add(c);


                }
                Message m = new Message();
                Bundle b=new Bundle();
            //    b.putString("response",ResponseResult);
                m.what = SUCCESS;
                m.setData(b);
                ReferenceHandler.sendMessage(m);


            } catch (Exception e) {
                Message m = new Message();
                Bundle b=new Bundle();
                //    b.putString("response",ResponseResult);
                m.what = FAILURE;
                m.setData(b);
                ReferenceHandler.sendMessage(m);
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }

        }

        public static String getStringFromInputStream(InputStream stream) throws IOException {
            int n = 0;
            char[] buffer = new char[1024 * 4];
            InputStreamReader reader = new InputStreamReader(stream, "UTF8");
            StringWriter writer = new StringWriter();
            while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
            return writer.toString();
        }


}
