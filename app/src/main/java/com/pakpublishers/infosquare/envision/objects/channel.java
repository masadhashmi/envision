package com.pakpublishers.infosquare.envision.objects;

import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pakpublishers.infosquare.envision.MainActivity;
import com.pakpublishers.infosquare.envision.R;
import com.pakpublishers.infosquare.envision.ShowDetailActivity;
import com.pakpublishers.infosquare.envision.adapters.TVChannelsAdapter;
import com.pakpublishers.infosquare.envision.utils.ServiceClient_LoadStationChannelsAndShow;
import com.pakpublishers.infosquare.envision.utils.ThisToThat;
import com.pakpublishers.infosquare.envision.utils.TimeZone_App;
import com.pakpublishers.infosquare.envision.utils.WidthCalculator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by M Asad Hashmi on 10/5/2016.
 */

public class channel {

    //public static String TodayStartedTime="notconfigured";
    public ArrayList<ArrayList<schedule>> show_ScheduleList = new ArrayList<ArrayList<schedule>>();
    String channel_id;
    String channel_name;
    String channel_property;
    String channel_station_id;
    String tvstation_logo;
    String tvstation_name;

    public void clearRestDays()
    {
        for(int i=1; i<show_ScheduleList.size(); i++)
        {
            show_ScheduleList.get(i).clear();
        }
    }
    public void removeChildofDay(int day,int child)
    {
        show_ScheduleList.get(day).remove(child);
    }


    public boolean isLayoutAssembled = false;

    public static View TimingBar = null;

    public View CurrentView = null;

    public channel() {
        for (int i = 0; i < 5; i++) {
            show_ScheduleList.add(new ArrayList<schedule>());
        }
    }

    public static void developerTimingBar(Context contx, int NumberofUnites, Date TimingBarStartTime) {
        float scale = contx.getResources().getDisplayMetrics().density;
        int width = (int) (5 * 30 * scale);
        int nowwidth = (int) (5 * 60 * scale);

        final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs

        SimpleDateFormat pastformate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("dd, hh:mm aa");
        Date parsed = TimingBarStartTime;

        parsed = new Date(parsed.getTime() + (60 * ONE_MINUTE_IN_MILLIS));
        try {
            parsed=TimeZone_App.GetLocaldatetime(parsed);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //System.out.println(parsed.toString());

        //int TiminBarActualWidth = (int) (5 * TimingBarWidth * scale);

        //int RemaningWidth = TiminBarActualWidth - nowwidth;
        // int totalCounts = RemaningWidth / width;
        NumberofUnites = NumberofUnites - 2;
        LayoutInflater vi = (LayoutInflater) contx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout timingbarcontainer = (LinearLayout) vi.inflate(R.layout.timingbarcontainer, null);

        LinearLayout LL = (LinearLayout) vi.inflate(R.layout.timingbaritem, null);
        LL.setLayoutParams(new LinearLayout.LayoutParams(nowwidth, (int) (scale * 20)));

        TextView TextView_TimeFrame = (TextView) LL.findViewById(R.id.TextView_TimeFrame);
        TextView TextView_HiddenTime=(TextView)LL.findViewById(R.id.TextView_HiddenTime);
        TextView TextView_HiddenRealTime=(TextView)LL.findViewById(R.id.TextView_HiddenRealTime);
        TextView_TimeFrame.setText("Now");
        String tme = format.format(parsed.getTime());
        TextView_HiddenRealTime.setText(pastformate.format(parsed.getTime()));
        TextView_HiddenTime.setText(tme);

        timingbarcontainer.addView(LL);



        for (int i = 0; i < NumberofUnites; i++) {
            LinearLayout Tframe = (LinearLayout) vi.inflate(R.layout.timingbaritem, null);
            Tframe.setLayoutParams(new LinearLayout.LayoutParams(width, (int) (scale * 20)));

            TextView TframeTextView_TimeFrame = (TextView) Tframe.findViewById(R.id.TextView_TimeFrame);
            TextView TframeTextView_HiddenTime=(TextView)Tframe.findViewById(R.id.TextView_HiddenTime);
            TextView TTextView_HiddenRealTime=(TextView)Tframe.findViewById(R.id.TextView_HiddenRealTime);


            int tm = (i + 1) * 2;

            //String parseddatetime = parsed.toString();
            String ttime = format.format(parsed.getTime());

            TframeTextView_TimeFrame.setText(ttime);
            TframeTextView_HiddenTime.setText(ttime);
            TTextView_HiddenRealTime.setText(pastformate.format(parsed.getTime()));
            timingbarcontainer.addView(Tframe);

            parsed = new Date(parsed.getTime() + (30 * ONE_MINUTE_IN_MILLIS));
        }
        TimingBar = timingbarcontainer;

    }

    public void developerView(final Context contx, int dayindex, channel newChanelToAddToSavedOne, String StartDateTime) {

        final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
        float scale = contx.getResources().getDisplayMetrics().density;
        LayoutInflater vi = (LayoutInflater) contx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout LL = null;

        if (dayindex == 0) {
            LL = (LinearLayout) vi.inflate(R.layout.scheduleitemtoplayout, null);// Main Container
        } else {
            LL = (LinearLayout) CurrentView;
        }

      //  float DefaultDayMinimumWidth = ((5 * 60 * scale)) * 24;
        float DefaultDayHeight = scale * 70;

        LinearLayout DayOneContainer = null;  // Today
        LinearLayout DayTwoContainer = null;  // Day 2
        LinearLayout DayThreeContainer = null;// Day 3
        LinearLayout DayFourContainer = null; // Day 4
        LinearLayout DayFiveContainer = null; // Day 5

        if (dayindex == 0) {
            LinearLayout.LayoutParams DefaultParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) DefaultDayHeight);


            DayOneContainer = (LinearLayout) vi.inflate(R.layout.scheduleitemtoplayout, null);
            DayOneContainer.setTag("day1");
            DayOneContainer.setLayoutParams(DefaultParams);
           // DayOneContainer.setMinimumWidth((int) DefaultDayMinimumWidth);
            //DayOneContainer.setBackgroundColor(Color.RED);

            DayTwoContainer = (LinearLayout) vi.inflate(R.layout.scheduleitemtoplayout, null);
            DayTwoContainer.setLayoutParams(DefaultParams);
            DayTwoContainer.setTag("day2");
           // DayTwoContainer.setMinimumWidth((int) DefaultDayMinimumWidth);
            //DayTwoContainer.setBackgroundColor(Color.GREEN);

            DayThreeContainer = (LinearLayout) vi.inflate(R.layout.scheduleitemtoplayout, null);
            DayThreeContainer.setLayoutParams(DefaultParams);
            DayThreeContainer.setTag("day3");
           // DayThreeContainer.setMinimumWidth((int) DefaultDayMinimumWidth);
            //DayThreeContainer.setBackgroundColor(Color.BLUE);

            DayFourContainer = (LinearLayout) vi.inflate(R.layout.scheduleitemtoplayout, null);
            DayFourContainer.setLayoutParams(DefaultParams);
            DayFourContainer.setTag("day4");
           // DayFourContainer.setMinimumWidth((int) DefaultDayMinimumWidth);
            //DayFourContainer.setBackgroundColor(Color.MAGENTA);

            DayFiveContainer = (LinearLayout) vi.inflate(R.layout.scheduleitemtoplayout, null);
            DayFiveContainer.setLayoutParams(DefaultParams);
            DayFiveContainer.setTag("day5");
          //  DayFiveContainer.setMinimumWidth((int) DefaultDayMinimumWidth);
            // DayFiveContainer.setBackgroundColor(Color.YELLOW);

            LL.addView(DayOneContainer);
            LL.addView(DayTwoContainer);
            LL.addView(DayThreeContainer);
            LL.addView(DayFourContainer);
            LL.addView(DayFiveContainer);
        } else {
            show_ScheduleList.set(dayindex, newChanelToAddToSavedOne.getShow_ScheduleList(dayindex));
        }

        LinearLayout CurrentContainer = (LinearLayout) LL.getChildAt(dayindex);
        if (CurrentContainer.getChildCount() > 0) {
            CurrentContainer.removeAllViews();
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format12 = new SimpleDateFormat("hh:mm aa");
            //Date FlowStartTime = TimeZone_App.GetLocaldatetime(format.parse(show_ScheduleList.get(0).get(0).getList_datetime()));

            Date FlowStartTime  =  TimeZone_App.GetLocaldatetime(format.parse(StartDateTime));

            //li.removeAllViews();
            for (int i = 0; i < show_ScheduleList.get(dayindex).size(); i++) {

//                if(i==0)
//                {
//                    show_ScheduleList.get(dayindex).get(i).setLive("1");
//                }

                String StartTime = "";
                String EndTime = "";
                final schedule s = show_ScheduleList.get(dayindex).get(i);

                Date StartDate = null;

                try {
                    StartDate = format.parse(s.getList_datetime());
                    StartDate= TimeZone_App.GetLocaldatetime(StartDate);
                    StartTime = format12.format(StartDate.getTime());

                    Date EndDate = format.parse(s.getList_endtime());
                    EndDate= TimeZone_App.GetLocaldatetime(EndDate);
                    EndTime = format12.format(EndDate.getTime());

                   // CompleteSessionStartDate =  TimeZone_App.GetLocaldatetime(format.parse(StartDateTime)); // format.parse(StartDateTime);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                int width = (int) (5 * Integer.parseInt(s.getDuration()) * scale);

                View showView = vi.inflate(R.layout.show_item, null);


                LinearLayout LinearLayout_ShowContainer = (LinearLayout) showView;//.findViewById(R.id.LinearLayout_ShowContainer);
                if (dayindex == 0) {
                    if (i == 0) {
                        LinearLayout_ShowContainer.setBackgroundResource(R.drawable.show);
                    } else {
                        LinearLayout_ShowContainer.setBackgroundResource(R.drawable.shownotnow);
                    }
                } else {
                    LinearLayout_ShowContainer.setBackgroundResource(R.drawable.shownotnow);
                }


                TextView tv = (TextView) showView.findViewById(R.id.TextViewShowName);
                tv.setText(s.getShow_name());

                TextView TextViewShowType = (TextView) showView.findViewById(R.id.TextViewShowType);
                TextViewShowType.setText(s.getShow_type());

                TextView Show_Time = (TextView) showView.findViewById(R.id.Show_Time);
                Show_Time.setText(StartTime + " - " + EndTime);

                showView.setLayoutParams(new LinearLayout.LayoutParams(width, (int) (scale * 70)));

                if (i == 0) {
                    //Date CSD=TimeZone_App.GetLocaldatetime(CompleteSessionStartDate);
                    if (dayindex == 0) {
                    Long miliseconds =StartDate.getTime()-FlowStartTime.getTime()  ;//CompleteSessionStartDate.getTime();
                    if (miliseconds > 0) {

                            LinearLayout l = new LinearLayout(contx);
                            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) showView.getLayoutParams();
                            long minutes = miliseconds / ONE_MINUTE_IN_MILLIS;
                            int padingleft = (int) (5 * minutes * scale);
                            l.setLayoutParams(new LinearLayout.LayoutParams(padingleft, (int) (scale * 70)));

                            if (dayindex == 0) {
                                CurrentContainer.addView(l);
                            } else {
                                Message m = new Message();
                                m.what = 11;

                                ThisToThat tt = new ThisToThat();
                                tt.Container = CurrentContainer;
                                tt.Item = l;
                                m.obj = tt;
                                ServiceClient_LoadStationChannelsAndShow.ReferenceHandler.sendMessage(m);
                            }
                        }

                        //LinearLayout_ShowContainer.set
                        //showView.setPadding(showView.getPaddingLeft()+ padingleft,showView.getPaddingTop(),showView.getPaddingRight(),showView.getPaddingBottom());
                        //String intMonth = minutes+"";
                        //int k = 0;
                        // k += 1;
                    }
                }
                final String STime=StartTime;
                final String ETime=EndTime;

                showView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        switch (event.getAction()) {
                            case MotionEvent.ACTION_UP: {
                                if(!MainActivity.ISTVGUIDE_DATA_LOADING) {
                                    Message m = new Message();

                                    Bundle b = new Bundle();
                                    b.putString("scheduleid", s.getSchedule_id());
                                    b.putString("showtype", s.getShow_type());
                                    b.putString("seriesid", s.getSeries_id());

                                    b.putString("stationid", s.getStation_id());
                                    b.putString("team1", s.getTeam1());
                                    b.putString("team2", s.getTeam2());
                                    b.putString("list_datetime", s.getList_datetime());
                                    b.putString("showname", s.getShow_name());
                                    b.putString("showtypeid", s.getShow_type_id());
                                    b.putString("live", s.getLive());

                                    b.putString("showtime", STime + " - " + ETime);


                                    m.what = 13;
                                    m.setData(b);

                                    ServiceClient_LoadStationChannelsAndShow.ReferenceHandler.sendMessage(m);
                                }
                                else
                                {
                                    Toast.makeText(contx, "Please wait, downloading is in progress.", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                        }


                        return  true;
                    }
                });


                if (dayindex == 0) {
                    CurrentContainer.addView(showView);
                } else {
                    Message m = new Message();
                    m.what = 11;

                    ThisToThat tt = new ThisToThat();
                    tt.Container = CurrentContainer;
                    tt.Item = showView;
                    m.obj = tt;
                    ServiceClient_LoadStationChannelsAndShow.ReferenceHandler.sendMessage(m);
                }
            }
//            if (dayindex > 0 && dayindex < 4) {
//                //if(show_ScheduleList.get(dayindex).)
//                try {
//                    if (getChannel_name().trim().toLowerCase().equals("msnbc")) {
//                        int k = 0;
//                        k = k = 1;
//                    }
//                    LinearLayout PreviousSubContainer = (LinearLayout) LL.getChildAt(dayindex - 1);
//                    LinearLayout NextSubContainer = (LinearLayout) LL.getChildAt(dayindex + 1);
//                    LinearLayout CurrentSubContainer = (LinearLayout) LL.getChildAt(dayindex);
//
//                    int previousDayWidth = PreviousSubContainer.getWidth();
//                    if (previousDayWidth != 0) {
//
//                        isLayoutAssembled = true;
//                        int previousControlMinimumWidth = (int) (DefaultDayMinimumWidth - (previousDayWidth - DefaultDayMinimumWidth));
////                PreviousSubContainer.setMinimumWidth(previousControlMinimumWidth);
//
//                        //if(previousControlMinimumWidth<DefaultDayMinimumWidth)
//                        // {
//                        //    previousControlMinimumWidth=(int)DefaultDayMinimumWidth;
//                        //}
//
//                        // int currentDayWidth = CurrentSubContainer.getWidth();
//                        // int NextControlMinimumWidth = (int) (DefaultDayMinimumWidth - (currentDayWidth - DefaultDayMinimumWidth));
//                        //              NextSubContainer.setMinimumWidth(NextControlMinimumWidth);
//
//                        WidthCalculator wc = new WidthCalculator();
//                        wc.CurrentSubcontainer = CurrentContainer;
//                        wc.CurrentSubcontainer_minWidth = previousControlMinimumWidth;
//                        //wc.PreviousSubContainer=PreviousSubContainer;
//                        //wc.NextSubcontainer = NextSubContainer;
//                        //wc.NextSubcontainer_minWidth = NextControlMinimumWidth;
//                        //  wc.NextSubcontainer_minWidth=NextControlMinimumWidth;
//
//                        Message m = new Message();
//                        m.what = 12;
//
//
//                        m.obj = wc;
//                        ServiceClient_LoadStationChannelsAndShow.ReferenceHandler.sendMessage(m);
//                    }
//                } catch (Exception er) {
//                    int k = 0;
//                    k += 1;
//                }
//            } else {
//                isLayoutAssembled = true;
//            }

            CurrentView = LL;
            //v = li;
            Log.e("done", channel_name + "");
            //TVChannelsAdapter.myArrayList.get(pos).CurrentView = v;

        } catch (Exception er) {
            er.printStackTrace();
            Log.e("Exception:", er.toString());
        }

    }

//    public ArrayList<schedule> show_ScheduleList = new ArrayList<schedule>();
//    public ArrayList<schedule> showDay1_ScheduleList = new ArrayList<schedule>();
//    public ArrayList<schedule> showDay2_ScheduleList = new ArrayList<schedule>();
//    public ArrayList<schedule> showDay3_ScheduleList = new ArrayList<schedule>();
//    public ArrayList<schedule> showDay4_ScheduleList = new ArrayList<schedule>();


    ///------------------------geter and seter


    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public ArrayList<schedule> getShow_ScheduleList(int dayindex) {
        return show_ScheduleList.get(dayindex);
    }

    public void setShow_ScheduleList(int dayindex, ArrayList<schedule> show_Schedule) {
        this.show_ScheduleList.set(dayindex, show_Schedule);
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getChannel_property() {
        return channel_property;
    }

    public void setChannel_property(String channel_property) {
        this.channel_property = channel_property;
    }

    public String getChannel_station_id() {
        return channel_station_id;
    }

    public void setChannel_station_id(String channel_station_id) {
        this.channel_station_id = channel_station_id;
    }

    public String getTvstation_logo() {
        return tvstation_logo;
    }

    public void setTvstation_logo(String tvstation_logo) {
        this.tvstation_logo = tvstation_logo;
    }

    public String getTvstation_name() {
        return tvstation_name;
    }

    public void setTvstation_name(String tvstation_name) {
        this.tvstation_name = tvstation_name;
    }


}
