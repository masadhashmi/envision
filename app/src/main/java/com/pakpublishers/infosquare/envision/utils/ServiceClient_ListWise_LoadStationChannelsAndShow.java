package com.pakpublishers.infosquare.envision.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.pakpublishers.infosquare.envision.adapters.TVChannelsAdapter;
import com.pakpublishers.infosquare.envision.objects.channel;
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
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by M Asad Hashmi on 10/5/2016.
 */

public class ServiceClient_ListWise_LoadStationChannelsAndShow extends Thread {


    public static boolean isBUSY=false;
    public String Parameters = "";
    public String CompleteURL = "";
    public String StartDate="";
    public int DayIndex = -1;
    public static Handler ReferenceHandler = null;
    public final static int SUCCESS = 1;
    public final static int FAILURE= -101;
    public final static int START_PARSING = 2;
    public static ArrayList<channel> Channels = null;
    series_detail series_DetailObject=null;
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
            urlConnection.setReadTimeout(2*60000);
            urlConnection.setConnectTimeout(2*60000);
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


            //m.obj=ResponseResult;

            JSONObject jsonObject = new JSONObject(ResponseResult);
            JSONArray channel_list = jsonObject.getJSONArray("channel_list");
            JSONArray shedule_list = jsonObject.getJSONArray("shedule_list");


            if(ServiceClient_ListWise_LoadStationChannelsAndShow.Channels==null) {
                ServiceClient_ListWise_LoadStationChannelsAndShow.Channels= new ArrayList<channel>();

            for (int i = 0; i < channel_list.length(); i++) {
                JSONObject ch = (JSONObject) channel_list.get(i);
                channel c = new channel();
                c.setChannel_id(ch.get("channel_id").toString());
                c.setChannel_name(ch.get("channel_name").toString());
                c.setChannel_property(ch.get("channel_property").toString());
                c.setChannel_station_id(ch.get("channel_station_id").toString());
                c.setTvstation_logo(ch.get("tvstation_logo").toString());
                c.setTvstation_name(ch.get("tvstation_name").toString());
                ServiceClient_ListWise_LoadStationChannelsAndShow.Channels.add(c);
            }
            }
            int LastChanelIndex = 0;
            channel LastChanel = null;
            String _ResponseStartDate=StartDate;

            for (int i = 0; i < shedule_list.length(); i++) {
                JSONObject sh = (JSONObject) shedule_list.get(i);
                schedule c = new schedule();
                c.setBlackWhite(sh.get("blackWhite").toString());
                c.setCh_logo(sh.get("ch_logo").toString());
                c.setCh_name(sh.get("ch_name").toString());
                c.setDescription(sh.get("description").toString());
                c.setDescriptive_video(sh.get("descriptive_video").toString());
                c.setDuration(sh.get("duration").toString());
                c.setEducational(sh.get("educational").toString());
                c.setEpisode_parts(sh.get("episode_parts").toString());
                c.setEpisode_part_num(sh.get("episode_part_num").toString());
                c.setEpisode_title(sh.get("episode_title").toString());
                c.setEpisodic(sh.get("episodic").toString());
                c.setHd(sh.get("hd").toString());
                c.setIn_progress(sh.get("in_progress").toString());
                c.setLeague(sh.get("league").toString());
                c.setListing_id(sh.get("listing_id").toString());
                c.setList_datetime(sh.get("list_datetime").toString());
                c.setList_endtime(sh.get("list_endtime").toString());
                c.setLive(sh.get("live").toString());
                c.setMovie_poster(sh.get("movie_poster").toString());
                c.setMovie_still(sh.get("movie_still").toString());
                c.setNeww(sh.get("new").toString());
                c.setPoster(sh.get("poster").toString());
                c.setProgramming(sh.get("programming").toString());
                c.setProperty(sh.get("property").toString());
                c.setRating(sh.get("rating").toString());
                c.setRepeat_time(sh.get("repeat_time").toString());
                c.setSchedule_id(sh.get("schedule_id").toString());
                c.setSeries_id(sh.get("series_id").toString());
                c.setShow_id(sh.get("show_id").toString());
                c.setShow_name(sh.get("show_name").toString());
                c.setShow_type(sh.get("show_type").toString());
                c.setShow_type_id(sh.get("show_type_id").toString());
                c.setStar_rating(sh.get("star_rating").toString());
                c.setStation_id(sh.get("station_id").toString());
                c.setSubtitled(sh.get("subtitled").toString());
                c.setTeam1(sh.get("team1").toString());
                c.setTeam2(sh.get("team2").toString());
                c.setTvstation_logo(sh.get("tvstation_logo").toString());
                c.setTvstation_name(sh.get("tvstation_name").toString());
                if (LastChanel == null || !LastChanel.getChannel_station_id().equals(c.getStation_id())) {
                    for (int j = 0; j < ServiceClient_ListWise_LoadStationChannelsAndShow.Channels.size(); j++) {
                        channel cc = ServiceClient_ListWise_LoadStationChannelsAndShow.Channels.get(j);
                        if (cc.getChannel_station_id().equals(c.getStation_id())) {
                            LastChanel = cc;
                            LastChanelIndex = j;
                            break;
                        }

                    }
                }
                if(i==0 && DayIndex==0)
                {

                    _ResponseStartDate= c.getList_datetime();
                }
                ServiceClient_ListWise_LoadStationChannelsAndShow.Channels.get(LastChanelIndex).show_ScheduleList.get(DayIndex).add(c);

            }

//            for (channel c : ServiceClient_ListWise_LoadStationChannelsAndShow.Channels
//                    ) {
//                if (DayIndex == 0) {
//                    c.developerView(cntx, DayIndex, null,_ResponseStartDate);
//                } else {
//                    channel savedchannel = TVChannelsAdapter.getChangelByStationID(c.getChannel_station_id());
//                    if (savedchannel != null)
//                        savedchannel.developerView(cntx, DayIndex, c,_ResponseStartDate);
//                }
//            }

            Message m = new Message();
            m.what = SUCCESS;
            ReferenceHandler.sendMessage(m);
        }
        catch (SocketTimeoutException er)
        {
            Message m = new Message();
            m.what = FAILURE;
            Bundle b=new Bundle();
            b.putString("error","Response timed out from server.");
            m.setData(b);
            ReferenceHandler.sendMessage(m);

        }
        catch (Exception e) {
            Message m = new Message();
            m.what = FAILURE;
            Bundle b=new Bundle();
            b.putString("error","Server Error: "+e.getMessage().toString());
            m.setData(b);
            ReferenceHandler.sendMessage(m);
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

    }

    public static channel getChannel(String ChannelStationID)
    {
        try {
            String cid = ChannelStationID.trim().toLowerCase();
            for (channel c : ServiceClient_ListWise_LoadStationChannelsAndShow.Channels
                    ) {
                String cChannelid=c.getChannel_station_id();
                try {
                    if (c.getChannel_station_id().trim().toLowerCase().equals(cid)) {
                        return c;
                    }
                }catch (Exception er){}
            }
        }catch (Exception er){}
        return null;
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
