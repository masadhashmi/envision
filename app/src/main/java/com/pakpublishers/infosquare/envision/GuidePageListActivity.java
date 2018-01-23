package com.pakpublishers.infosquare.envision;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ScrollingTabContainerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pakpublishers.infosquare.envision.adapters.ChannelSpinnerAdapter;
import com.pakpublishers.infosquare.envision.adapters.ListGuidePageAdapter;
import com.pakpublishers.infosquare.envision.adapters.TVChannelsAdapter;
import com.pakpublishers.infosquare.envision.adapters.TVGuideAdapter;
import com.pakpublishers.infosquare.envision.objects.channel;
import com.pakpublishers.infosquare.envision.objects.schedule;
import com.pakpublishers.infosquare.envision.utils.ImageLoader;
import com.pakpublishers.infosquare.envision.utils.ServiceClient_ListWise_LoadStationChannelsAndShow;
import com.pakpublishers.infosquare.envision.utils.ServiceClient_LoadStationChannelsAndShow;
import com.pakpublishers.infosquare.envision.utils.ThisToThat;
import com.pakpublishers.infosquare.envision.utils.TimeZone_App;
import com.pakpublishers.infosquare.envision.utils.WidthCalculator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class GuidePageListActivity extends Activity {

    public static boolean[] STACK_FILLED_STATUS = new boolean[]{false, false, false, false, false};
    public static boolean[] STACK_IS_INPROGRESS = new boolean[]{false, false, false, false, false};

    String USER_ID = "99";
    String USER_Property = "UTAI";
    String TIMEZONE_NUMBER = "19800";


    TextView TextView_BackButton=null;
    LinearLayout toolbar=null;
    ImageView ImageView_Channel=null;
    public ImageLoader imageLoader;
    ListView ListView_Guides=null;
    ListGuidePageAdapter DayDataAdp=null;
    ChannelSpinnerAdapter cadp=null;
    Spinner Spinner_Channels=null;

    Button Button_PreviousDay=null;
    Button Button_NextDay=null;

    public Handler CurrentDataHandler=null;
   LinearLayout ProgressDialougeMain=null;
    LinearLayout ProgressDialouge=null;

    HorizontalScrollView HorizontalScrolView_Days = null;
    LinearLayout LinearLayout_DaysFirstDefaulControl=null;
    LinearLayout ScheduleDays_Controls = null;
    LinearLayout Seekbar_ShowTracker = null;
    LinearLayout LinearLayout_NowButtonContainer = null;
    Button Button_Now = null;
    TextView Seekbar_Day = null;
    TextView SeekBar_Time = null;

    boolean HideNowButtonAnimationStarted=false;

    ArrayList<Integer> days = new ArrayList<Integer>();
    ArrayList<String> days_Strings = new ArrayList<String>();

    public static float scale = 0;
    final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
    public static int SDefault=24;// default position
    public static int S_ShiftedPos=55; // shifted position



    int SelectedDayIndex = 0;

    public static int sliderDefaultPositon = SDefault;

    String ChannelName="";
    public  String ChannelStationID="";
    String ChannelImageURL="";

    @Override
    protected void onResume() {
        super.onResume();
        try {

//            ListViewChannels.setAdapter(tvchanneladp);
//            ListViewTVGuides.setAdapter(tgguideadp);
//
//            tvchanneladp.notifyDataSetChanged();
//            tgguideadp.notifyDataSetChanged();
//
//
//            ListViewChannels.refreshDrawableState();
//            ListViewTVGuides.refreshDrawableState();
//
//            ListViewTVGuides.setVisibility(View.VISIBLE);

        } catch (Exception er) {
        }
    }

    public void moveSliderToDefaultPosition() {

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) Seekbar_ShowTracker.getLayoutParams();


        layoutParams.leftMargin = sliderDefaultPositon;

        Seekbar_ShowTracker.setLayoutParams(layoutParams);


    }

    public void showNowButton(boolean val) {
        if (val) {
            sliderDefaultPositon = (int)(S_ShiftedPos*scale);
            if (LinearLayout_NowButtonContainer.getVisibility() != View.VISIBLE) {

                moveSliderToDefaultPosition();
                LinearLayout_NowButtonContainer.setVisibility(View.VISIBLE);

                TranslateAnimation animation = new TranslateAnimation(-200, 0, 0, 0);
                animation.setDuration(520);
                //  animation.setFillAfter(true);

                animation.setAnimationListener(new Animation.AnimationListener() {


                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                LinearLayout_NowButtonContainer.startAnimation(animation);
            }
        } else {
            sliderDefaultPositon = (int)(SDefault*scale);
            if (LinearLayout_NowButtonContainer.getVisibility() == View.VISIBLE) {

                moveSliderToDefaultPosition();
                TranslateAnimation animation = new TranslateAnimation(0, -100, 0, 0);
                animation.setDuration(520);
                // animation.setFillAfter(true);
                if(!HideNowButtonAnimationStarted) {
                    animation.setAnimationListener(new Animation.AnimationListener() {


                        @Override
                        public void onAnimationStart(Animation animation) {
                            HideNowButtonAnimationStarted = true;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            LinearLayout_NowButtonContainer.setVisibility(View.GONE);
                            HideNowButtonAnimationStarted = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


                    LinearLayout_NowButtonContainer.startAnimation(animation);
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page_list);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        ChannelName= getIntent().getExtras().getString("chanelname");
        ChannelStationID= getIntent().getExtras().getString("chanelstationid");
        ChannelImageURL= getIntent().getExtras().getString("chanelimage");
        int selectedindex=getIntent().getExtras().getInt("selectedindex");

        initiateControls();
        moveSliderToDefaultPosition();
        initiateListners();

        CurrentDataHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case 1:
                    {
                      //  Spinner_Channels.setVisibility(View.GONE);
                        //ChannelStationID=cadp.tempValues.get(position).getChannel_id();
                        try {
                            moveToToday(false);
                            initiateDayData(0);
                        }catch (Exception er){}
                        break;
                    }
                    case 3:
                    {
                        try{

                            String Time= ListGuidePageAdapter.myArrayList.get(ListView_Guides.getFirstVisiblePosition()).getList_datetime();


                            SimpleDateFormat pastformate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            SimpleDateFormat pastformate2 = new SimpleDateFormat("hh:mm aa");
                            Date d= null;
                            try {
                                d = pastformate1.parse(Time);

                                try {
                                    d = TimeZone_App.GetLocaldatetime(d);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }catch (Exception er){}

                            String RealTime =  pastformate2.format(d);


                           // String tim=msg.getData().getString("time");
                            SeekBar_Time.setText(RealTime);
                        }catch (Exception er){}
                        break;
                    }
                    case 4:
                    {
                        try{
                        initiateDayData(0);
                        showProgressbar(false);
                    }catch (Exception er){}
                        break;
                    }

                }
            }
        };

        loadChannels(selectedindex);
        showProgressbar(true);

        Spinner_Channels.setSelection(selectedindex);

        showProgressbar(true);
        new Thread(){
            @Override
            public void run() {
                super.run();
                if(ServiceClient_ListWise_LoadStationChannelsAndShow.Channels ==null) {
                    loadDay1Data(0);
                }
                CurrentDataHandler.sendEmptyMessage(4);
            }
        }.start();




    }

    AdapterView.OnItemSelectedListener selctionchangelistenr= new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //ChannelSpinnerAdapter.SelectedPosition=position;
            cadp.notifyDataSetChanged();
            ///ChannelStationID=cadp.tempValues.get(position).getChannel_id();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void loadChannels(int selectedindex) {
        Resources res = getResources();
        cadp=new ChannelSpinnerAdapter(this,android.R.layout.simple_list_item_1, TVChannelsAdapter.myArrayList,res,selectedindex);
        Spinner_Channels.setAdapter(cadp);
        cadp.notifyDataSetChanged();

        Spinner_Channels.setOnItemSelectedListener(selctionchangelistenr);

    }

    public void showProgressbar(boolean val)
    {
        if(val)
        {
            ProgressDialougeMain.setVisibility(View.VISIBLE);
            ProgressDialouge.startAnimation(AnimationUtils.loadAnimation(this, R.anim.progresssanimation));
        }else
        {
            ProgressDialouge.clearAnimation();
            ProgressDialougeMain.setVisibility(View.GONE);

        }
    }
    SimpleDateFormat pastformate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private void loadDataBasedOnUTCtimeForNextDay(String RealTime) {

        if(ServiceClient_ListWise_LoadStationChannelsAndShow.isBUSY)
        {
            return;
        }
        clearListView();
        Date RealTimeDate = null;
        try {
            RealTimeDate = pastformate.parse(RealTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date RealUTCTimeDate = null;// To UTC
        try {
            RealUTCTimeDate = TimeZone_App.GetUTCdatetime(RealTimeDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (RealUTCTimeDate != null) {
            String RealUTCTime = pastformate.format(RealUTCTimeDate);
            //  Log.e("Read Time", "Local:" + RealTime + ", UTC:" + RealUTCTime);


            String intMonth = (String) android.text.format.DateFormat.format("MM", RealUTCTimeDate); //06
            String year = (String) android.text.format.DateFormat.format("yyyy", RealUTCTimeDate); //2013
            String day = (String) android.text.format.DateFormat.format("dd", RealUTCTimeDate); //20
            String localday = (String) android.text.format.DateFormat.format("dd", RealTimeDate); //20
            String hour = (String) android.text.format.DateFormat.format("HH", RealUTCTimeDate); //20
            String minutes = (String) android.text.format.DateFormat.format("mm", RealUTCTimeDate); //20

            // String StartDate = year + "-" + intMonth + "-" + day + " " + hour + ":" + minutes + ":00";

            //int startdayindex=-1;
            int _day = Integer.parseInt(localday);
            int newdayindex = -1;
            for (int j = 0; j < days.size(); j++) {

                if (days.get(j) == _day) {
                    newdayindex = j;
                    break;
                }
            }

            if (newdayindex != -1 ) {


                if (!STACK_FILLED_STATUS[newdayindex]) {
                    // Log.e("Scaning:", days.get(newdayindex) + "");
                    if (newdayindex != 0) {
                        if (!ServiceClient_ListWise_LoadStationChannelsAndShow.isBUSY) {
                            Date parsed = new Date();

                            //   parsed = new Date(parsed.getTime() + (i * 24 * 60 * ONE_MINUTE_IN_MILLIS));

                            String StartDate = year + "-" + intMonth + "-" + day + " 00:00:00";

                            String EndDate = year + "-" + intMonth + "-" + day + " 23:59:00";

                            // Start Date Formate:"2016-10-06 13:05:00"
                            loadTVGuides(false,newdayindex, newdayindex, USER_ID, USER_Property, StartDate, EndDate);
                        }
                    }


                }
            }

        }

    }

    public void clearListView()
    {
        ArrayList<schedule> daydata = new ArrayList<schedule>();
        DayDataAdp = new ListGuidePageAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, daydata, CurrentDataHandler);
        ListView_Guides.setAdapter(DayDataAdp);

    }

    public void loadTVGuides(boolean ProgressBarclickable,final int startdayindex, final int DayIndex, final String user_id, final String user_property, final String start_date, final String end_date) {
//        if(DayIndex>1)
//        {
//            return;
//        }
        if (ServiceClient_ListWise_LoadStationChannelsAndShow.isBUSY) {
            return;
        }
        STACK_IS_INPROGRESS[DayIndex] = true;

        showProgressbar(true);

        final ServiceClient_ListWise_LoadStationChannelsAndShow Sc= new ServiceClient_ListWise_LoadStationChannelsAndShow();
        Sc.CompleteURL = "http://myevisiontv.com/mvc/api-v1/home/";
        Sc.DayIndex = DayIndex;
        Sc.StartDate = start_date;
        Sc.Parameters = "user_id=" + user_id + "&user_property=" + user_property + "&start_date=" + start_date + "&end_date=" + end_date;


        //   Sc.Parameters+="&time_zone="+TimeZone_App.getoffsetdifference();
        Sc.cntx = getApplicationContext();
        ServiceClient_ListWise_LoadStationChannelsAndShow.ReferenceHandler = new Handler() {
            @Override
            public void handleMessage(Message m) {
                switch (m.what) {


                    case ServiceClient_ListWise_LoadStationChannelsAndShow.FAILURE: {

                        Bundle b = m.getData();
                        try {
                            showProgressbar(false);
                            Toast.makeText(GuidePageListActivity.this, b.getString("error"), Toast.LENGTH_LONG).show();

                        } catch (Exception er) {
                        }
                        break;
                    }
                    case ServiceClient_ListWise_LoadStationChannelsAndShow.SUCCESS: {
                        // ListViewChannels.setVisibility(View.GONE);

                        loadRestDayData(DayIndex);
                        ArrayList<schedule> daydata = ServiceClient_ListWise_LoadStationChannelsAndShow.getChannel(ChannelStationID).getShow_ScheduleList(DayIndex);
                        DayDataAdp = new ListGuidePageAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, daydata, CurrentDataHandler);
                        ListView_Guides.setAdapter(DayDataAdp);
                        DayDataAdp.notifyDataSetChanged();
                        showProgressbar(false);


                        for(int k=startdayindex; k<=DayIndex;k++)
                        {
                            STACK_FILLED_STATUS[k] = true;
                        }
                        //STACK_FILLED_STATUS[DayIndex] = true;


                        //int width = LinearLayout_TimingBar.getWidth();


                        //ListViewChannels.deferNotifyDataSetChanged();
                        //ListViewTVGuides.deferNotifyDataSetChanged();
                        // ListViewChannels.setVisibility(View.VISIBLE);
                        try{
                            String Time=daydata.get(0).getList_datetime();
                            SimpleDateFormat pastformate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            SimpleDateFormat pastformate2 = new SimpleDateFormat("hh:mm aa");
                            Date d= null;
                            try {
                                d = pastformate1.parse(Time);

                                try {
                                    d = TimeZone_App.GetLocaldatetime(d);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            }catch (Exception er){}

                            String RealTime =  pastformate2.format(d);

                            SeekBar_Time.setText(RealTime);
                        }catch (Exception er){}
                        showProgressbar(false);
                        ServiceClient_ListWise_LoadStationChannelsAndShow.isBUSY = false;
                        //ListViewTVGuides.setSelection(0);
                        //  ListViewTVGuides.setScrollY(1);
                        //ListViewTVGuides.setScrollY(0);

                        break;
                    }

                }
            }
        };
        ServiceClient_ListWise_LoadStationChannelsAndShow.isBUSY = true;
        Sc.start();
    }


    SimpleDateFormat pastformate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat pastformate_onlyday = new SimpleDateFormat("dd");
    SimpleDateFormat pastformate2 = new SimpleDateFormat("hh:mm aa");
    public String getLocalCurrentDayFromDate(String UTCTime,boolean isLocaltime)
    {

        Date d= null;
        try {
            d = pastformate1.parse(UTCTime);

            try {
                if(!isLocaltime) {
                    d = TimeZone_App.GetLocaldatetime(d);
                }
            } catch (ParseException e) {

            }

        }catch (Exception er){}

        return pastformate_onlyday.format(d);
    }


    public void loadRestDayData(int Day)
    {// for alll other days except first

        TextView tv = (TextView) ScheduleDays_Controls.getChildAt(Day).findViewById(R.id.TextView_CompleteTime);
        String Time = tv.getText().toString();

        int CurrentDay = Integer.parseInt( (getLocalCurrentDayFromDate(Time,true)));

        for(int j=0; j<ServiceClient_ListWise_LoadStationChannelsAndShow.Channels.size(); j++) {

            ArrayList<schedule> daydata = null;
            channel CurrentChannel=null;
            try {
                CurrentChannel= ServiceClient_LoadStationChannelsAndShow.Channels.get(j);
                daydata = CurrentChannel.getShow_ScheduleList(Day);

            } catch (Exception er) {

            }
            ArrayList<schedule> thisdaycompiledata=new ArrayList<schedule>();
            ArrayList<schedule> Nextdaycompiledata=new ArrayList<schedule>();

            if ( daydata != null && daydata.size() > 0) {

                if (!STACK_FILLED_STATUS[Day]) {

                    for (int i = 0; i < daydata.size(); i++) {
                        schedule s = daydata.get(i);


                        int itrativeDay =Integer.parseInt( (getLocalCurrentDayFromDate(s.getList_datetime(),false)));

                        if (CurrentDay !=(itrativeDay)) {
                            // channel cc= ServiceClient_ListWise_LoadStationChannelsAndShow.getChannel(CurrentChannel.getChannel_station_id());

                            //CurrentChannel.getShow_ScheduleList(Day+1).add(s);
                            //daydata.remove(i);
                            Nextdaycompiledata.add(s);

                        }
                        else
                        {
                            thisdaycompiledata.add(s);
                        }
                    }
                    try {
                        CurrentChannel.setShow_ScheduleList(Day, thisdaycompiledata);

                        CurrentChannel.setShow_ScheduleList(Day + 1, Nextdaycompiledata);
                    }catch (Exception er){}

                }
            }
        }
        STACK_FILLED_STATUS[Day] = true;
    }


    public void loadDay1Data(int Day)
    {
        if(Day==0) {
            ServiceClient_ListWise_LoadStationChannelsAndShow.Channels = ServiceClient_LoadStationChannelsAndShow.Channels;
        }

        for(int j=0; j<ServiceClient_ListWise_LoadStationChannelsAndShow.Channels.size(); j++) {

            ArrayList<schedule> daydata = null;
            channel CurrentChannel=null;
            try {
                CurrentChannel= ServiceClient_LoadStationChannelsAndShow.Channels.get(j);
                CurrentChannel.clearRestDays();

                daydata = CurrentChannel.getShow_ScheduleList(Day);
            } catch (Exception er) {

            }
            ArrayList<schedule> thisdaycompiledata=new ArrayList<schedule>();
            ArrayList<schedule> Nextdaycompiledata=new ArrayList<schedule>();

            if ( daydata != null && daydata.size() > 0) {

                if (!STACK_FILLED_STATUS[0]) {
                    TextView tv = (TextView) ScheduleDays_Controls.getChildAt(Day).findViewById(R.id.TextView_CompleteTime);
                    String Time = tv.getText().toString();

                    int CurrentDay = Integer.parseInt(getLocalCurrentDayFromDate(Time,true));
                    for (int i = 0; i < daydata.size(); i++) {
                        schedule s = daydata.get(i);


                        int itrativeDay = Integer.parseInt(getLocalCurrentDayFromDate(s.getList_datetime(),false));

                        if (CurrentDay != itrativeDay) {
                           // channel cc= ServiceClient_ListWise_LoadStationChannelsAndShow.getChannel(CurrentChannel.getChannel_station_id());

                            //CurrentChannel.getShow_ScheduleList(Day+1).add(s);
                            //daydata.remove(i);
                            Nextdaycompiledata.add(s);

                        }
                        else
                        {
                            thisdaycompiledata.add(s);
                        }
                    }
                    CurrentChannel.setShow_ScheduleList(Day,thisdaycompiledata);
                    CurrentChannel.setShow_ScheduleList(Day+1,Nextdaycompiledata);

                }
            }
        }
        STACK_FILLED_STATUS[0] = true;
    }


    private void initiateDayData(int Day) {

        try {
            SeekBar_Time.setText("");
            ArrayList<schedule> daydata=null;
            try {
                daydata = ServiceClient_ListWise_LoadStationChannelsAndShow.getChannel(ChannelStationID).getShow_ScheduleList(Day);
            }catch (Exception er){

            }

            if ( Day==0 && daydata!=null && daydata.size() > 0) {



                DayDataAdp = new ListGuidePageAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, daydata, CurrentDataHandler);
                ListView_Guides.setAdapter(DayDataAdp);
                DayDataAdp.notifyDataSetChanged();
                try{
                    String Time=daydata.get(0).getList_datetime();
                    SimpleDateFormat pastformate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat pastformate2 = new SimpleDateFormat("hh:mm aa");
                    Date d= null;
                    try {
                        d = pastformate1.parse(Time);

                        try {
                            d = TimeZone_App.GetLocaldatetime(d);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }catch (Exception er){}

                    String RealTime =  pastformate2.format(d);

                    SeekBar_Time.setText(RealTime);
                }catch (Exception er){}

                showProgressbar(false);
            } else {


                try {
                    daydata = ServiceClient_ListWise_LoadStationChannelsAndShow.getChannel(ChannelStationID).getShow_ScheduleList(Day);
                }catch (Exception er){

                }
                if ( STACK_FILLED_STATUS[Day]==true &&  daydata!=null && daydata.size() > 0) {
                    DayDataAdp = new ListGuidePageAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, daydata, CurrentDataHandler);
                    ListView_Guides.setAdapter(DayDataAdp);
                    DayDataAdp.notifyDataSetChanged();
                    try{
                        String Time=daydata.get(0).getList_datetime();
                        SimpleDateFormat pastformate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat pastformate2 = new SimpleDateFormat("hh:mm aa");

                        Date d= null;
                        try {
                            d = pastformate1.parse(Time);

                            try {
                                d = TimeZone_App.GetLocaldatetime(d);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }catch (Exception er){}

                        String RealTime =  pastformate2.format(d);

                        SeekBar_Time.setText(RealTime);
                    }catch (Exception er){}
                    showProgressbar(false);
                }
                else {
                    Date d = new Date();
                    String dayofmonth = days.get(Day).toString();
                    TextView tv= (TextView)ScheduleDays_Controls.getChildAt(Day).findViewById(R.id.TextView_CompleteTime);
                    String tm=tv.getText().toString();

                   // SimpleDateFormat pastformate2 = new SimpleDateFormat("yyyy-MM-" + dayofmonth + " HH:mm:ss");
                   // String RealTime = pastformate2.format(d);

                    loadDataBasedOnUTCtimeForNextDay(tm);
                }
                //Load from page
            }

            if (Day <= 0) {
                Button_PreviousDay.setVisibility(View.GONE);
            }
            else
            {
                Button_PreviousDay.setVisibility(View.VISIBLE);
            }
            if (Day >= 5) {
                Button_NextDay.setVisibility(View.GONE);
            }
            else
            {
                Button_NextDay.setVisibility(View.VISIBLE);
            }
        }catch (Exception er){}


    }


    public void initilizeScheduleDays() {
        ////ScheduleDays_Controls
        //  Date NowDate = new Date();

        SimpleDateFormat format = new SimpleDateFormat("dd");
        Date parsed = new Date();

        //parsed = new Date(parsed.get + (60 * ONE_MINUTE_IN_MILLIS));
        String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", parsed);//Thursday
        String stringMonth = (String) android.text.format.DateFormat.format("MMM", parsed); //Jun
        String intMonth = (String) android.text.format.DateFormat.format("MM", parsed); //06
        String year = (String) android.text.format.DateFormat.format("yyyy", parsed); //2013
        String day = (String) android.text.format.DateFormat.format("dd", parsed); //20

        days.clear();
        days.add(Integer.parseInt(day));

        float scale = getResources().getDisplayMetrics().density;
        int width = (int) (80 * scale);
        int height = (int) (50 * scale);

        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View Schedulelayout = (View) vi.inflate(R.layout.schedule_day_layout, null);

        Schedulelayout.setLayoutParams(new LinearLayout.LayoutParams(width, height));

        TextView TextView_ScheduleDayENG = (TextView) Schedulelayout.findViewById(R.id.TextView_ScheduleDayENG);
        TextView TextView_ScheduleDayDD = (TextView) Schedulelayout.findViewById(R.id.TextView_ScheduleDayDD);
        TextView TextView_CompleteTime = (TextView) Schedulelayout.findViewById(R.id.TextView_CompleteTime);

        SimpleDateFormat pastformate1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        TextView_CompleteTime.setText(pastformate1.format(parsed));


        TextView_ScheduleDayDD.setText(day);


        TextView_ScheduleDayENG.setText("Today");
        days_Strings.add("Today");
        ScheduleDays_Controls.removeAllViews();

        ScheduleDays_Controls.addView(Schedulelayout);


        for (int i = 0; i < 20; i++) {

            final int DayNumber=i;
            parsed = new Date(parsed.getTime() + (24 * 60 * ONE_MINUTE_IN_MILLIS));
            dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", parsed);//Thursday
            stringMonth = (String) android.text.format.DateFormat.format("MMM", parsed); //Jun
            intMonth = (String) android.text.format.DateFormat.format("MM", parsed); //06
            year = (String) android.text.format.DateFormat.format("yyyy", parsed); //2013
            day = (String) android.text.format.DateFormat.format("dd", parsed); //20

            days.add(Integer.parseInt(day));
            days_Strings.add(dayOfTheWeek);

            View Schedulelayouti = null;
            if (i < 4) {
                Schedulelayouti = (View) vi.inflate(R.layout.schedule_day_layout, null);
            } else {
                Schedulelayouti = (View) vi.inflate(R.layout.schedule_disableday_layout, null);
            }
            Schedulelayouti.setLayoutParams(new LinearLayout.LayoutParams(width, height));
            TextView TextView_ScheduleDayENGi = (TextView) Schedulelayouti.findViewById(R.id.TextView_ScheduleDayENG);
            TextView TextView_ScheduleDayDDi = (TextView) Schedulelayouti.findViewById(R.id.TextView_ScheduleDayDD);
            TextView TextView_CompleteTimei = (TextView) Schedulelayouti.findViewById(R.id.TextView_CompleteTime);

            String tm=pastformate1.format(parsed);

            TextView_CompleteTimei.setText(tm);


            TextView_ScheduleDayDDi.setText(day);
            TextView_ScheduleDayENGi.setText(dayOfTheWeek.substring(0, 3));
            Schedulelayouti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        scroolFocusOnDay(DayNumber + 1,true);
                    }catch (Exception er){}
                }
            });
            ScheduleDays_Controls.addView(Schedulelayouti);
        }

    }


    private void initiateControls() {
        TextView_BackButton=(TextView)findViewById(R.id.TextView_BackButton);
        toolbar=(LinearLayout)findViewById(R.id.toolbar);
        ImageView_Channel=(ImageView)findViewById(R.id.ImageView_Channel);
        ListView_Guides=(ListView)findViewById(R.id.ListView_Guides);
        LinearLayout_NowButtonContainer = (LinearLayout) findViewById(R.id.LinearLayout_NowButtonContainer);
        Button_Now = (Button) findViewById(R.id.Button_Now);
        SeekBar_Time = (TextView) findViewById(R.id.SeekBar_Time);
        Seekbar_Day = (TextView) findViewById(R.id.Seekbar_Day);
        ProgressDialougeMain=(LinearLayout)findViewById(R.id.ProgressDialougeMain);
        Spinner_Channels=(Spinner)findViewById(R.id.Spinner_Channels);

        Button_PreviousDay= (Button) findViewById(R.id.Button_PreviousDay);
        Button_NextDay= (Button) findViewById(R.id.Button_NextDay);
        ProgressDialouge=(LinearLayout)findViewById(R.id.ProgressDialouge);


        ScheduleDays_Controls = (LinearLayout) findViewById(R.id.ScheduleDays_Controls);
        HorizontalScrolView_Days = (HorizontalScrollView) findViewById(R.id.HorizontalScrolView_Days);
        LinearLayout_DaysFirstDefaulControl=(LinearLayout)findViewById(R.id.LinearLayout_DaysFirstDefaulControl);
        Seekbar_ShowTracker = (LinearLayout) findViewById(R.id.Seekbar_ShowTracker);

        scale = getResources().getDisplayMetrics().density;

        sliderDefaultPositon =(int)(SDefault*scale);

        imageLoader = new ImageLoader(getApplicationContext());
        imageLoader.DisplayImage(ChannelImageURL, ImageView_Channel);

        initilizeScheduleDays();
        showNowButton(false);
        moveToToday(false);


        TextView_BackButton.setText(ChannelName);
        setTitle("");
    }

    public synchronized void scroolFocusOnDay(int day,final boolean loaddataalso) {

    //    showProgressbar(true);
        if(day>0)
        {
            showNowButton(true);
        }
        else
        {
            showNowButton(false);
        }
        int DayWidth=0;
       // if(day!=0) {
            int deultfistcontrolwidth = LinearLayout_DaysFirstDefaulControl.getWidth();
            DayWidth = ScheduleDays_Controls.getChildAt(0).getWidth();
            DayWidth = (int) (day * DayWidth);
            if (DayWidth != 0) {
                DayWidth -= scale * 30;
            }
        //}
        int y = 0;
        SelectedDayIndex=day;
        try {
            Seekbar_Day.setText(days_Strings.get(day ));

        }catch (Exception er){}
        ObjectAnimator xTranslate = ObjectAnimator.ofInt(HorizontalScrolView_Days, "scrollX", DayWidth);
        ObjectAnimator yTranslate = ObjectAnimator.ofInt(HorizontalScrolView_Days, "scrollY", y);

        AnimatorSet animators = new AnimatorSet();
        animators.setDuration(1000L);
        animators.playTogether(xTranslate, yTranslate);
        animators.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animator arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                // TODO Auto-generated method stub
                if(loaddataalso) {
                    initiateDayData(SelectedDayIndex);
                }
            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                // TODO Auto-generated method stub

            }
        });
        animators.start();
    }

    public void moveToToday(boolean loaddataalso) {

        showNowButton(false);
        scroolFocusOnDay(0,loaddataalso);

    }
    private void initiateListners() {
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button_Now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToToday(true);
            }
        });

        Button_NextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    scroolFocusOnDay(SelectedDayIndex+1,true);

                }catch (Exception er){}
            }
        });
        Button_PreviousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(SelectedDayIndex>0) {
                        scroolFocusOnDay(SelectedDayIndex - 1,true);
                    }

                }catch (Exception er){}
            }
        });
    }

}
