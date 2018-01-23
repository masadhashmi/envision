package com.pakpublishers.infosquare.envision;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.net.LinkAddress;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.pakpublishers.infosquare.envision.adapters.TVChannelsAdapter;
import com.pakpublishers.infosquare.envision.adapters.TVGuideAdapter;
import com.pakpublishers.infosquare.envision.adapters.UnScrollableListView;
import com.pakpublishers.infosquare.envision.objects.channel;
import com.pakpublishers.infosquare.envision.utils.ServiceClient_LoadStationChannelsAndShow;
import com.pakpublishers.infosquare.envision.utils.ThisToThat;
import com.pakpublishers.infosquare.envision.utils.TimeZone_App;
import com.pakpublishers.infosquare.envision.utils.WidthCalculator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    String USER_ID = "99";
    String USER_Property = "UTAI";
    String TIMEZONE_NUMBER = "19800";

    public static boolean ISTVGUIDE_DATA_LOADING=false;

    String SeekBar_Movement_Detected_LastDay="";
    String SeekBar_Movement_Detected_LastMonth="";


    static boolean Enable_calculateAndDispayTime = true;

    final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
    final int TOTAL_TIMING_BLOCKS = 240;
    final int TIMINGBLOCKSPADINGLEFT = 70;
    ArrayList<Integer> days = new ArrayList<Integer>();
    ArrayList<String> days_Strings = new ArrayList<String>();

    public static float scale = 0;

    boolean isNotScrolling = true;

    public static int SDefault=24;// default position
    public static int S_ShiftedPos=55; // shifted position

    public static int sliderDefaultPositon = SDefault;

    boolean doScanning = true;

    public static boolean[] STACK_FILLED_STATUS = new boolean[]{false, false, false, false, false};
    public static boolean[] STACK_IS_INPROGRESS = new boolean[]{false, false, false, false, false};


    public static float TOTAL_TIMING_BLOCK_WIDTH = 0;
    public static float TOTAL_SCHEDULEDAYS_WIDTH = 0;
    public static float SEEKBAR_WIDTH = 0;
    public static float TIMINGBLOCKS_PADINGLEFTVALUE = 0;

    LinearLayout Seekbar_ShowTracker = null;
    View CustomeLayoutSlider_Thumb = null;
    LinearLayout ScheduleDays_Controls = null;
    TextView SeekBar_Time = null;
    TextView Seekbar_Day = null;
    ListView ListViewTVGuides = null;
    UnScrollableListView ListViewChannels = null;
    HorizontalScrollView ScrolView_TvGuide = null;
    View ProgressDialogAnim = null;
    View ProgressDialogBOX = null;
    LinearLayout LinearLayout_NowButtonContainer = null;
    Button Button_Now = null;
    HorizontalScrollView HorizontalScrolView_Days = null;
    LinearLayout LinearLayout_DaysFirstDefaulControl=null;
    LinearLayout LinearyLayout_ChanelListOverlay=null;


    int SelectedDayIndex = 0;
    TVChannelsAdapter tvchanneladp = null;
    TVGuideAdapter tgguideadp = null;

    LinearLayout LinearLayout_TimingBar = null;


    private int _xDelta;
    // private int _yDelta;

    public void drawTextOnSeekBar(SeekBar cseekbar, String day, String Time) {
        Bitmap bmp = drawTextToBitmap(R.drawable.slider, "Thu", "05:30 PM");
        Drawable d = new BitmapDrawable(getResources(), bmp);
        cseekbar.setThumb(d);
    }

    public Bitmap drawTextToBitmap(int resourceId, String mText, String mTime) {
        try {
            DisplayMetrics metrics = getResources().getDisplayMetrics();

            Resources resources = getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);

            android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
            // set default bitmap config if none
            if (bitmapConfig == null) {
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
            }
            // resource bitmaps are imutable,
            // so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);
            // new antialised Paint
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // text color - #3D3D3D
            paint.setColor(Color.WHITE);
            // text size in pixels
            paint.setTextSize((int) (14 * scale));
            // text shadow
            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);

            // draw text to the Canvas center
            Rect bounds = new Rect();
            paint.getTextBounds(mText, 0, mText.length(), bounds);
            int x = ((bitmap.getWidth() * metrics.densityDpi - bounds.width() * metrics.densityDpi) / (6 * metrics.densityDpi));
            int y = 20;//(bitmap.getHeight() + bounds.height())/5;


            paint.getTextBounds(mTime, 0, mTime.length(), bounds);
            int tx = ((bitmap.getWidth() * metrics.densityDpi - bounds.width() * metrics.densityDpi) / (6 * metrics.densityDpi));
            int ty = 35;//(bitmap.getHeight() + bounds.height())/5;


            canvas.drawText(mText, x * scale, y * scale, paint);
            canvas.drawText(mTime, tx * scale, ty * scale, paint);


            return bitmap;
        } catch (Exception e) {
            // TODO: handle exception


            return null;
        }

    }

    public Bitmap getBitmapofSlider() {
        //Define a bitmap with the same size as the view
        //  int vWidth=1000;// view.getWidth();
        // int vHeight=1000;//view.getHeight();
        //Bitmap returnedBitmap = Bitmap.createBitmap(vWidth, vHeight ,Bitmap.Config.ARGB_8888);

        Bitmap returnedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slider);
        //notBuilder.setLargeIcon(largeIcon);


        //Bind a canvas to it
//        Canvas canvas = new Canvas(returnedBitmap);
//        //Get the view's background
//        Drawable bgDrawable =view.getBackground();
//        if (bgDrawable!=null)
//            //has background drawable, then draw it on the canvas
//            bgDrawable.draw(canvas);
//        else
//            //does not have background drawable, then draw white background on the canvas
//            canvas.drawColor(Color.WHITE);
//        // draw the view on the canvas
//        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    public int showTimingOfCurrentControlAtPostion(int CurrentSeekBarPostion, boolean calculateSelcteditem) {
        int totalChilds = ScheduleDays_Controls.getChildCount();
//        RelativeLayout testv = (RelativeLayout) ScheduleDays_Controls.getChildAt(0);
//        Rect textvDrawingrect=new Rect();
//        testv.getGlobalVisibleRect(textvDrawingrect);
//
//        Log.e("firstviewpos:",""+textvDrawingrect.left);
        for (int i = 0; i < totalChilds; i++) {

            RelativeLayout v = (RelativeLayout) ScheduleDays_Controls.getChildAt(i);
            Rect vRect = new Rect();
            v.getGlobalVisibleRect(vRect);

            float ViewX = vRect.left;//v.getX();
            if (ViewX >= 0) {
                float ViewWidth = v.getWidth();
                float ViewXPlusWidth = ViewX + ViewWidth;//840
                if (CurrentSeekBarPostion >= ViewX && CurrentSeekBarPostion <= vRect.right) {

                    if (calculateSelcteditem) {
                        SelectedDayIndex = i;
                    }
                    if (doScanning) {
                        if (!STACK_FILLED_STATUS[i]) {
                           // Log.e("Scaning:", i + "");
                            if (i != 0) {
                                if (!ServiceClient_LoadStationChannelsAndShow.isBUSY) {
                                    Date parsed = new Date();

                                    parsed = new Date(parsed.getTime() + (i * 24 * 60 * ONE_MINUTE_IN_MILLIS));

                                    String intMonth = (String) android.text.format.DateFormat.format("MM", parsed); //06
                                    String year = (String) android.text.format.DateFormat.format("yyyy", parsed); //2013
                                    String day = (String) android.text.format.DateFormat.format("dd", parsed); //20

                                    String StartDate = year + "-" + intMonth + "-" + day + " 00:00:00";

                                    String EndDate = year + "-" + intMonth + "-" + day + " 23:59:00";

                                    // Start Date Formate:"2016-10-06 13:05:00"
                                    loadTVGuides(false,i, i, USER_ID, USER_Property, StartDate, EndDate);
                                }
                            }
                        }
                    }

                    TextView tv = (TextView) ((LinearLayout) v.getChildAt(0)).getChildAt(0);
                    TextView tvday = (TextView) ((LinearLayout) v.getChildAt(0)).getChildAt(1);
                    TextView tvmonth = (TextView) ((LinearLayout) v.getChildAt(0)).findViewById(R.id.TextView_ScheduleMonth);
                    SeekBar_Movement_Detected_LastDay=tvday.getText().toString().trim();
                    SeekBar_Movement_Detected_LastMonth=tvmonth.getText().toString().trim();
                    Log.e("month",""+SeekBar_Movement_Detected_LastMonth);
                    Seekbar_Day.setText(tv.getText());

                    int PositonInsideView = (int) (CurrentSeekBarPostion - ViewX);
                    if (ViewWidth != 0) {

                        int CurrentPercentage = (int) (((float) PositonInsideView / (float) ViewWidth) * 100);
                        // Log.e("detail", "ViewX[" + ViewX + "], ViewWidth[" + ViewWidth + "], ViewXPlusWidth[" + ViewXPlusWidth + "],PositonInsideView[" + PositonInsideView + "]," + CurrentPercentage + "%");
                        return CurrentPercentage;
                    } else {
                        return 0;
                    }


                }
            }


        }
        return -1;
    }

    public void showProgressBar(boolean val, boolean clickable) {


        ProgressDialogBOX.setClickable(clickable);

        if (val) {
            ProgressDialogBOX.setVisibility(View.VISIBLE);
            ProgressDialogAnim.startAnimation(AnimationUtils.loadAnimation(this, R.anim.progresssanimation));
        } else {

            ProgressDialogBOX.setVisibility(View.GONE);
        }
    }

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Seekbar_ShowTracker = (LinearLayout) findViewById(R.id.Seekbar_ShowTracker);
        ScheduleDays_Controls = (LinearLayout) findViewById(R.id.ScheduleDays_Controls);
        SeekBar_Time = (TextView) findViewById(R.id.SeekBar_Time);
        Seekbar_Day = (TextView) findViewById(R.id.Seekbar_Day);
        ListViewTVGuides = (ListView) findViewById(R.id.ListViewTVGuides);
        ScrolView_TvGuide = (HorizontalScrollView) findViewById(R.id.ScrolView_TvGuide);
        ListViewChannels = (UnScrollableListView) findViewById(R.id.ListViewChannels);
        ProgressDialogAnim = (View) findViewById(R.id.ProgressDialouge);
        ProgressDialogBOX = (View) findViewById(R.id.ProgressDialougeMain);
        LinearLayout_TimingBar = (LinearLayout) findViewById(R.id.LinearLayout_TimingBar);
        LinearLayout_NowButtonContainer = (LinearLayout) findViewById(R.id.LinearLayout_NowButtonContainer);
        Button_Now = (Button) findViewById(R.id.Button_Now);
        HorizontalScrolView_Days = (HorizontalScrollView) findViewById(R.id.HorizontalScrolView_Days);
        LinearLayout_DaysFirstDefaulControl=(LinearLayout)findViewById(R.id.LinearLayout_DaysFirstDefaulControl);
        LinearyLayout_ChanelListOverlay=(LinearLayout)findViewById(R.id.LinearyLayout_ChanelListOverlay);
        scale = getResources().getDisplayMetrics().density;

        sliderDefaultPositon =(int)(SDefault*scale);


        TOTAL_TIMING_BLOCK_WIDTH = (float) (60 * (float) TOTAL_TIMING_BLOCKS * scale);
        TOTAL_SCHEDULEDAYS_WIDTH = (float) (5 * 80 * scale);
        SEEKBAR_WIDTH = (float) Seekbar_ShowTracker.getWidth();
        TIMINGBLOCKS_PADINGLEFTVALUE = (float) (TIMINGBLOCKSPADINGLEFT * scale);

        showNowButton(false);
        moveSliderToDefaultPosition();

        //initilizeHandler();
        initilizeSeekBar();
        initiateListners();
        loadTVGuideScrollView();

        Date parsed = new Date();


        try {
            parsed = TimeZone_App.GetUTCdatetime(parsed);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //parsed = new Date(parsed.getTime() + (24 * 60 * ONE_MINUTE_IN_MILLIS));

        String intMonth = (String) android.text.format.DateFormat.format("MM", parsed); //06
        String year = (String) android.text.format.DateFormat.format("yyyy", parsed); //2013
        String day = (String) android.text.format.DateFormat.format("dd", parsed); //20
        String hour = (String) android.text.format.DateFormat.format("HH", parsed); //20
        String minutes = (String) android.text.format.DateFormat.format("mm", parsed); //20

        String StartDate = year + "-" + intMonth + "-" + day + " " + hour + ":" + minutes + ":00";

        parsed = new Date(parsed.getTime() + (24 * 60 * ONE_MINUTE_IN_MILLIS));

        intMonth = (String) android.text.format.DateFormat.format("MM", parsed); //06
        year = (String) android.text.format.DateFormat.format("yyyy", parsed); //2013
        // day = (String) android.text.format.DateFormat.format("dd", parsed); //20

        String EndDate = year + "-" + intMonth + "-" + day + " 23:59:00";

        setClockTime();
        // Start Date Formate:"2016-10-06 13:05:00"
        loadTVGuides(true, 0,0, USER_ID, USER_Property, StartDate, EndDate);

    }

    private void initiateListners() {

        Button_Now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToToday();
            }
        });


        LinearyLayout_ChanelListOverlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN: {
//
//                        return  true;
//
//                    }
//                    case MotionEvent.ACTION_MOVE: {
//                        return  true;
//
//                    }
//
//                    case MotionEvent.ACTION_UP: {
//                        return  false;
//
//                    }
//                }
                return false;

            }
        });




    }

    public void moveToToday() {
        Enable_calculateAndDispayTime = false;
        showNowButton(false);
        Enable_calculateAndDispayTime = false;
        scroolDayBarTo(0);
        Enable_calculateAndDispayTime = false;
        scroolToTop(0);
        Enable_calculateAndDispayTime = false;


    }

    //http://myevisiontv.com/mvc/api-v1/home/
    //user_id=17&user_property=UTAI&start_date=2016-10-06 13:05:00&end_date=2016-10-07 13:05:00
    public void loadTVGuides(boolean ProgressBarclickable,final int startdayindex, final int DayIndex, final String user_id, final String user_property, final String start_date, final String end_date) {
//        if(DayIndex>1)
//        {
//            return;
//        }
        if (ServiceClient_LoadStationChannelsAndShow.isBUSY) {
            return;
        }
        STACK_IS_INPROGRESS[DayIndex] = true;
        showProgressBar(true, ProgressBarclickable);

        final ServiceClient_LoadStationChannelsAndShow Sc = new ServiceClient_LoadStationChannelsAndShow();
        Sc.CompleteURL = "http://myevisiontv.com/mvc/api-v1/home/";
        Sc.DayIndex = DayIndex;
        Sc.StartDate = start_date;
        Sc.Parameters = "user_id=" + user_id + "&user_property=" + user_property + "&start_date=" + start_date + "&end_date=" + end_date;


        //   Sc.Parameters+="&time_zone="+TimeZone_App.getoffsetdifference();
        Sc.cntx = getApplicationContext();
        ServiceClient_LoadStationChannelsAndShow.ReferenceHandler = new Handler() {
            @Override
            public void handleMessage(Message m) {
                switch (m.what) {
                    case 11: {
                        try {
                            ThisToThat tt = (ThisToThat) m.obj;
                            tt.Container.addView(tt.Item);
                        }catch (Exception er){}
                        break;
                    }
                    case 12: {
                        try {
                            WidthCalculator tt = (WidthCalculator) m.obj;
                            //tt.PreviousSubContainer.setMinimumWidth(tt.PreviousSubContainer_minWidth);
                            //tt.NextSubcontainer.setMinimumWidth(tt.NextSubcontainer_minWidth);
                            tt.CurrentSubcontainer.setMinimumWidth(tt.CurrentSubcontainer_minWidth);
                            tgguideadp.notifyDataSetChanged();
                            // tt.NextSubcontainer.setMinimumWidth(tt.NextSubcontainer_minWidth);
                        } catch (Exception er) {
                        }
                        break;
                    }
                    case 13: {
                        try{
                        Bundle s = m.getData();
                        Intent i = new Intent(getApplicationContext(), ShowDetailActivity.class);
                        i.putExtra("scheduleid", s.getString("scheduleid"));
                        i.putExtra("showtype", s.getString("showtype"));
                        i.putExtra("seriesid", s.getString("seriesid"));

                        i.putExtra("stationid", s.getString("stationid"));
                        i.putExtra("team1", s.getString("team1"));
                        i.putExtra("team2", s.getString("team2"));
                        i.putExtra("list_datetime", s.getString("list_datetime"));
                        i.putExtra("showname", s.getString("showname"));
                        i.putExtra("showtime", s.getString("showtime"));
                        i.putExtra("showtypeid", s.getString("showtypeid"));
                        i.putExtra("live", s.getString("live"));


                        startActivity(i);
                        }catch (Exception er){}
                        break;
                    }
                    case ServiceClient_LoadStationChannelsAndShow.FAILURE: {
                        try {
                        Bundle b = m.getData();

                            showProgressBar(false, false);
                            Toast.makeText(MainActivity.this, b.getString("error"), Toast.LENGTH_LONG).show();

                        } catch (Exception er) {
                        }
                        ISTVGUIDE_DATA_LOADING=false;
                        ServiceClient_LoadStationChannelsAndShow.isBUSY=false;
                        break;
                    }
                    case ServiceClient_LoadStationChannelsAndShow.SUCCESS: {
                        // ListViewChannels.setVisibility(View.GONE);
                        try{
                        if (DayIndex == 0) {
                            ArrayList<channel> chnls = Sc.Channels;
                            tvchanneladp = new TVChannelsAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, chnls, this);
                            ListViewChannels.setAdapter(tvchanneladp);

                            //--- Timing Bar Calculation..


                            Date TimingBarStartTime = new Date();
                            try {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                TimingBarStartTime = format.parse(chnls.get(0).show_ScheduleList.get(DayIndex).get(0).getList_datetime());
                            } catch (Exception er) {
                            }

                            channel.developerTimingBar(getApplicationContext(), TOTAL_TIMING_BLOCKS, TimingBarStartTime);
                            LinearLayout_TimingBar.removeAllViews();
                            LinearLayout_TimingBar.addView(channel.TimingBar);

                            tvchanneladp.notifyDataSetChanged();
                            tgguideadp = new TVGuideAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, this);
                            ListViewTVGuides.setAdapter(tgguideadp);
                            tgguideadp.notifyDataSetChanged();
                        }


                        for(int k=startdayindex; k<=DayIndex;k++)
                        {
                            STACK_FILLED_STATUS[k] = true;
                        }
                        //STACK_FILLED_STATUS[DayIndex] = true;


                        //int width = LinearLayout_TimingBar.getWidth();


                        //ListViewChannels.deferNotifyDataSetChanged();
                        //ListViewTVGuides.deferNotifyDataSetChanged();
                        // ListViewChannels.setVisibility(View.VISIBLE);


                        //ListViewTVGuides.setSelection(0);
                        //  ListViewTVGuides.setScrollY(1);
                        //ListViewTVGuides.setScrollY(0);
                        }catch (Exception er){}
                        showProgressBar(false, false);
                        ServiceClient_LoadStationChannelsAndShow.isBUSY = false;
                        ISTVGUIDE_DATA_LOADING=false;
                        break;
                    }

                }
            }
        };
        ServiceClient_LoadStationChannelsAndShow.isBUSY = true;
        Sc.start();
        ISTVGUIDE_DATA_LOADING=true;
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
        TextView TextView_ScheduleMonth= (TextView) Schedulelayout.findViewById(R.id.TextView_ScheduleMonth);


        TextView_ScheduleMonth.setText(intMonth);

        TextView_ScheduleDayDD.setText(day);
        TextView_ScheduleDayENG.setText("Today");
        days_Strings.add("Today");
        ScheduleDays_Controls.removeAllViews();

        ScheduleDays_Controls.addView(Schedulelayout);


        for (int i = 0; i < 20; i++) {

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
            TextView TextView_ScheduleMonthi= (TextView) Schedulelayouti.findViewById(R.id.TextView_ScheduleMonth);

            if(TextView_ScheduleMonthi!=null)
            TextView_ScheduleMonthi.setText(intMonth);
            if(TextView_ScheduleDayDDi!=null)
            TextView_ScheduleDayDDi.setText(day);
            if(TextView_ScheduleDayENGi!=null)
            TextView_ScheduleDayENGi.setText(dayOfTheWeek.substring(0, 3));

            ScheduleDays_Controls.addView(Schedulelayouti);
        }

    }


    boolean HideNowButtonAnimationStarted=false;
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

    public void initilizeSeekBar() {

        initilizeScheduleDays();

        Seekbar_ShowTracker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                final int X = (int) event.getRawX();
                final int halfwidthofseekbar = Seekbar_ShowTracker.getWidth() / 2;
                // final int Y = (int) event.getRawY();
                //if (isNotScrolling) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        try {
                            doScanning = false;
                            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                            _xDelta = X - lParams.leftMargin;
                            //_yDelta = Y - lParams.topMargin;
                            Seekbar_ShowTracker.setBackgroundResource(R.drawable.sliderup);
                            // Seekbar_ShowTracker.setPadding(Seekbar_ShowTracker.getPaddingLeft(), Seekbar_ShowTracker.getPaddingTop() - 7, Seekbar_ShowTracker.getPaddingRight(), Seekbar_ShowTracker.getPaddingBottom());
                            TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -50*scale);
                            animation.setDuration(50);
                            animation.setFillAfter(true);

                            animation.setAnimationListener(new Animation.AnimationListener() {


                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    Seekbar_ShowTracker.setBackgroundResource(R.drawable.sliderup);
                                    //   SeekBar_Layout.setPadding(SeekBar_Layout.getPaddingLeft(),SeekBar_Layout.getPaddingTop()-120,SeekBar_Layout.getPaddingRight(),SeekBar_Layout.getPaddingBottom());
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                            v.startAnimation(animation);
                            //v.setAlpha(0.50f);
                        } catch (Exception er) {
                        }

                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        try {
                            doScanning = false;
                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();

                            layoutParams.leftMargin = X - _xDelta;
                            int CurrentSeekBarPositon = layoutParams.leftMargin + halfwidthofseekbar;
                            //SeekBar_Time.setText(CurrentSeekBarPositon + "");
                            v.setLayoutParams(layoutParams);

                            int percentagePostioninView = showTimingOfCurrentControlAtPostion(CurrentSeekBarPositon, true);
                            if (percentagePostioninView != -1) {
                                int TotalOneDayMinutes = 1440;
                                int CurrentMinutes = (TotalOneDayMinutes * percentagePostioninView) / 100;

                                int Hours = CurrentMinutes / 60;
                                int RemaningMinutes = CurrentMinutes % 60;
                                String AMPM = "a.m.";
                                if (Hours > 12) {
                                    AMPM = "p.m.";
                                    Hours = Hours - 12;
                                } else {
                                }
                                if (RemaningMinutes > 60) {
                                    Hours++;
                                    RemaningMinutes = RemaningMinutes / 60;
                                }

                                String FormatedHours = String.format("%02d", Hours);

                                String FormatedRemaningMinutes = String.format("%02d", RemaningMinutes);
                                SeekBar_Time.setText(FormatedHours + ":" + FormatedRemaningMinutes + " " + AMPM);
                            }
                        } catch (Exception er) {
                        }

                        break;
                    }

                    case MotionEvent.ACTION_UP: {
                        try {
                            Seekbar_ShowTracker.setBackgroundResource(R.drawable.slider);
                            //Seekbar_ShowTracker.setPadding(Seekbar_ShowTracker.getPaddingLeft(), Seekbar_ShowTracker.getPaddingTop() + 7, Seekbar_ShowTracker.getPaddingRight(), Seekbar_ShowTracker.getPaddingBottom());
                            TranslateAnimation animation = new TranslateAnimation(0, 0, -50*scale, 0);
                            animation.setDuration(500);
                            animation.setFillAfter(true);
                            //  animation.setInterpolator(new BounceInterpolator());
                            animation.setAnimationListener(new Animation.AnimationListener() {


                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    //   SeekBar_Layout.setPadding(SeekBar_Layout.getPaddingLeft(),SeekBar_Layout.getPaddingTop()-120,SeekBar_Layout.getPaddingRight(),SeekBar_Layout.getPaddingBottom());
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            v.startAnimation(animation);
                            //final View vv=v;


                            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                            float CurrentSeekBarPositon = layoutParams.leftMargin + halfwidthofseekbar;
                            float percentage = (CurrentSeekBarPositon / ScheduleDays_Controls.getWidth()) * 100;

                            float timingbarwidth = LinearLayout_TimingBar.getWidth();
                            int correctscrollpostion = (int) (timingbarwidth * percentage) / 100;

                            Log.e("droppercentage", "" + percentage);
                            doScanning = false;
                            Log.e("Selected", "" + SelectedDayIndex);
                            moveSliderToDefaultPosition();

                            //if (SelectedDayIndex > 0) {
                                //showNowButton(true);
                                scroolFocusOnDay(SelectedDayIndex);

                            //} else {
                            //    showNowButton(false);
                           // }
                            //------------------------------------------------------------
                            String tim = SeekBar_Time.getText().toString();
                            int _day = days.get(SelectedDayIndex);
                            int hr = Integer.parseInt(tim.substring(0, 2));
                            int min = Integer.parseInt(tim.substring(3, 5));
                            String ampm = tim.substring(6, 9).trim().toLowerCase();



                            if (ampm.equals("a.m") && hr == 0) {
                                hr = 12;
                            }

                            LinearLayout cv = getViewFromTime(_day, hr, min, ampm);
                            if (cv != null) {
                                Enable_calculateAndDispayTime = false;
                                scroolAndFocusToControl(0, cv);
                                Enable_calculateAndDispayTime=false;

                                //2016-10-06 13:05:00

                                SimpleDateFormat format2 = new SimpleDateFormat("yyyy-");
                                Date d= new Date();


                                String date= format2.format(d);
                                date+=SeekBar_Movement_Detected_LastMonth+"-"+SeekBar_Movement_Detected_LastDay+" 23:59:00";

                                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                //Date UTCdate = format.parse(date);
                                loadDataBasedOnUTCtimeForNextDay(date);
                                //String utcdate=format.format(TimeZone_App.getUTCTime(UTCdate));


                            }
                            //  scroolToTop(correctscrollpostion);

                            ////---- Loading Date Data

                        } catch (Exception er) {
                        }
                        break;
                    }
                }
                // }
                return true;
            }
        });
    }

    public synchronized  void calculateAndDispayTime() {
        try {
            LinearLayout LR = ((LinearLayout) (LinearLayout_TimingBar.getChildAt(0)));
            int chields = LR.getChildCount();
            float seekbarwidth = Seekbar_ShowTracker.getWidth();
            float ContactPoint = sliderDefaultPositon + seekbarwidth / 2;
            //Log.e("Contact Point:", "" + ContactPoint + "," + seekbarwidth);
            //Seekbar_ShowTracker.setX(ContactPoint);
            for (int i = 0; i < chields; i++) {
                try {
                    LinearLayout CurrentChild = (LinearLayout) LR.getChildAt(i);
                    Rect vRect = new Rect();
                    CurrentChild.getGlobalVisibleRect(vRect);
                    int arr[] = new int[2];
                    CurrentChild.getLocationOnScreen(arr);
                    // Log.e("visibility",i+",["+arr[0]+","+arr[1]+"]");

//                Log.e("childs",chields+","+i);

                    if (vRect.left >= 0 && arr[0] + CurrentChild.getWidth() >= 0) {


                        if (ContactPoint > vRect.left && ContactPoint < vRect.right) {
                            try {
                                //Log.e("Detail", "ContactPoint, Left, right,index,currentchild,Childs => [" + ContactPoint + "," + vRect.left + "," + vRect.right + "," + i +","+i+ "," + chields + "]");
                                //Log.e("Detail", "ContactPoint, Left, right,index,currentchild,Childs => [" + ContactPoint + "," + vRect2.left + "," + vRect2.right + "," + i +",1," + chields + "]");
                                TextView tv = (TextView) CurrentChild.getChildAt(0);
                                TextView timevalue = (TextView) CurrentChild.findViewById(R.id.TextView_HiddenTime);

                                TextView TextView_TimeFrame = (TextView) CurrentChild.findViewById(R.id.TextView_TimeFrame);
                                TextView TextView_HiddenRealTime = (TextView) CurrentChild.findViewById(R.id.TextView_HiddenRealTime);
                                float cwidth = CurrentChild.getWidth();
                                float obtainedvalue = (vRect.right - ContactPoint);
                                int percentage = 100 - (int) ((obtainedvalue / cwidth) * 100);
                                int minuteslength = 30;
                                if (tv.getText().toString().toLowerCase().trim().equals("now")) {
                                    minuteslength += 30;
                                }
                                int percentof30 = (minuteslength * percentage) / 100;

                                String time = timevalue.getText().toString();
                                try {
                                    if (Enable_calculateAndDispayTime) {


                                        String RealTime = TextView_HiddenRealTime.getText().toString();


                                        int day = Integer.parseInt(time.substring(0, 2));

                                        //Log.e("day, index", day + "," + SelectedDayIndex);
                                        try {
                                            if (days.get(SelectedDayIndex) != day) {
                                                int newdayindex = -1;
                                                for (int j = 0; j < days.size(); j++) {
                                                    if (days.get(j) == day) {
                                                        newdayindex = j;
                                                        break;
                                                    }
                                                }
                                                if (newdayindex != -1) {
                                                    SelectedDayIndex = newdayindex;
                                                    Seekbar_Day.setText(days_Strings.get(SelectedDayIndex));
                                                    scroolFocusOnDay(SelectedDayIndex);

                                                }
                                                Log.e("after day, index", day + "," + SelectedDayIndex);
                                            }
                                        } catch (Exception rt) {
                                            int k = 0;
                                            k += 1;

                                        }
                                        loadDataBasedOnUTCtimeForNextDay(RealTime);
                                    }
                                } catch (Exception er) {
                                }
                                String hours = time.substring(3, 6);
                                String min = time.substring(7, 9).trim();
                                String AMPM = time.substring(9).trim();
                                int minutes = Integer.parseInt(min);
                                minutes += percentof30;
                                String fulltime = hours + ":" + String.format("%02d", minutes) + " " + AMPM;
                                String Nowtime = TextView_TimeFrame.getText().toString();

                                //Log.e("Time",""+fulltime);

                                if (!Nowtime.toLowerCase().trim().equals("now")) {
                                    SeekBar_Time.setText(fulltime);//tv.getText() + "," + vRect.left + "," + cwidth + "," + percentof30);
                                } else {
                                    Date d = new Date();
                                    SimpleDateFormat format2 = new SimpleDateFormat("hh:mm aa");
                                    String clocktime = format2.format(d);
                                    SeekBar_Time.setText(clocktime);//tv.getText() + "," + vRect.left + "," + cwidth + "," + percentof30);
                                }

                            } catch (Exception er) {

                                String k = er.toString();
                                k = "";
                                return;
                            }
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                                return;
                            }
                            // return;
                            //break;
                        }
                    }
                } catch (Exception er) {
                    Log.e("Error", "" + er.toString());
                }
            }
        }catch (Exception er){}
    }
    public void setClockTime()
    {
        try {
            Date d = new Date();
            SimpleDateFormat format2 = new SimpleDateFormat("hh:mm aa");
            String clocktime = format2.format(d);
            SeekBar_Time.setText(clocktime);
        }catch (Exception er){}
    }

    SimpleDateFormat pastformate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private void loadDataBasedOnUTCtimeForNextDay(String RealTime) {

        try {
            if (ServiceClient_LoadStationChannelsAndShow.isBUSY) {
                return;
            }
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
                String hour = (String) android.text.format.DateFormat.format("HH", RealUTCTimeDate); //20
                String minutes = (String) android.text.format.DateFormat.format("mm", RealUTCTimeDate); //20

                // String StartDate = year + "-" + intMonth + "-" + day + " " + hour + ":" + minutes + ":00";

                int startdayindex = -1;
                int _day = Integer.parseInt(day);
                int newdayindex = -1;
                for (int j = 0; j < days.size(); j++) {
                    if (startdayindex == -1) {
                        if (!STACK_FILLED_STATUS[j]) {
                            startdayindex = j;
                        }
                    }
                    if (days.get(j) == _day) {
                        newdayindex = j;
                        break;
                    }
                }

                if (newdayindex != -1 && startdayindex != -1) {
                    String StartDay = days.get(startdayindex).toString();

                    if (!STACK_FILLED_STATUS[newdayindex]) {
                        // Log.e("Scaning:", days.get(newdayindex) + "");
                        if (newdayindex != 0) {
                            if (!ServiceClient_LoadStationChannelsAndShow.isBUSY) {
                                Date parsed = new Date();

                                //   parsed = new Date(parsed.getTime() + (i * 24 * 60 * ONE_MINUTE_IN_MILLIS));

                                String StartDate = year + "-" + intMonth + "-" + days.get(startdayindex) + " 00:00:00";

                                String EndDate = year + "-" + intMonth + "-" + day + " 23:59:00";

                                // Start Date Formate:"2016-10-06 13:05:00"
                                loadTVGuides(false, startdayindex, newdayindex, USER_ID, USER_Property, StartDate, EndDate);
                            }
                        }


                    }
                }

            }
        }catch (Exception er){}

    }


    ViewTreeObserver.OnScrollChangedListener onscrollchangelistner = new ViewTreeObserver.OnScrollChangedListener() {

        float lastScrollX = 0;

        @Override
        public void onScrollChanged() {
            try {

                float scrollX = ScrolView_TvGuide.getScrollX();
                scrollX -= TIMINGBLOCKS_PADINGLEFTVALUE;
                if (scrollX > 0) {
                    //isScrolled=true;
                    isNotScrolling = false;

                    if ((int) lastScrollX != (int) scrollX) {

                        calculateAndDispayTime();

                        lastScrollX = scrollX;
                    } else {
                        isNotScrolling = true;
                        doScanning = true;
                        Enable_calculateAndDispayTime = true;

                        //isScrolled=false;
                    }
                }
            } catch (Exception er) {
            }
        }
    };


    public void scroolFocusOnDay(int day) {

        if(day>0)
        {
            showNowButton(true);
        }
        else
        {
            showNowButton(false);
        }
        int DayWidth=0;
        if(day!=0) {
            int deultfistcontrolwidth = LinearLayout_DaysFirstDefaulControl.getWidth();
            DayWidth = ScheduleDays_Controls.getChildAt(0).getWidth();
            DayWidth = (int) (day * DayWidth);
            if (DayWidth != 0) {
                DayWidth -= scale * 30;
            }
        }

        int y = 0;
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

            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                // TODO Auto-generated method stub

            }
        });
        animators.start();
    }


    public void scroolDayBarTo(int x) {

        int y = 0;
        ObjectAnimator xTranslate = ObjectAnimator.ofInt(HorizontalScrolView_Days, "scrollX", x);
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

            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                // TODO Auto-generated method stub

            }
        });
        animators.start();
    }


    public void scroolToTop(int x) {

        int y = 0;
        ObjectAnimator xTranslate = ObjectAnimator.ofInt(ScrolView_TvGuide, "scrollX", x);
        ObjectAnimator yTranslate = ObjectAnimator.ofInt(ScrolView_TvGuide, "scrollY", y);

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

            }

            @Override
            public void onAnimationCancel(Animator arg0) {
                // TODO Auto-generated method stub

            }
        });
        animators.start();
    }

    public LinearLayout getViewFromTime(int day, int hour, int minute, String ampm) {
        try {
            LinearLayout LR = ((LinearLayout) (LinearLayout_TimingBar.getChildAt(0)));
            int chields = LR.getChildCount();
            //float seekbarwidth=Seekbar_ShowTracker.getWidth();
            // float ContactPoint = sliderDefaultPositon + seekbarwidth/2;
            //Log.e("Contact Point:", "" + ContactPoint+","+seekbarwidth);
            //Seekbar_ShowTracker.setX(ContactPoint);

            for (int i = 0; i < chields; i++) {
                try {
                    LinearLayout CurrentChild = (LinearLayout) LR.getChildAt(i);
                    Rect vRect = new Rect();
                    CurrentChild.getGlobalVisibleRect(vRect);
                    // if (vRect.left >= 0) {
                    TextView timevalue = (TextView) CurrentChild.findViewById(R.id.TextView_HiddenTime);
                    String time = timevalue.getText().toString();
                    int Day = Integer.parseInt(time.substring(0, 2));
                    int Hours = Integer.parseInt(time.substring(4, 6));
                    int Min = Integer.parseInt(time.substring(7, 9).trim());
                    String AMPM = time.substring(9).trim().toLowerCase().replace(".", "").replace(".", "");
                    if (day == days.get(0) && hour < Hours) {
                        return CurrentChild;
                    }
                    ampm = ampm.toLowerCase().replace(".", "").replace(".", "");
                    if (Day == day && AMPM.equals(ampm) && Hours == hour && Min <= minute && minute <= (Min + 30)) {

                        return CurrentChild;
                    }

                    //}
                } catch (Exception er) {
                    Log.e("Error", "" + er.toString());

                }
            }
        }catch (Exception er){}
        return null;
    }

    public void scroolAndFocusToControl(int x, View v) {
        try {
            Rect r = new Rect();
            v.getDrawingRect(r);
            x = r.left;
            x = (int) v.getX();
            int y = 0;

            ObjectAnimator xTranslate = ObjectAnimator.ofInt(ScrolView_TvGuide, "scrollX", x);
            ObjectAnimator yTranslate = ObjectAnimator.ofInt(ScrolView_TvGuide, "scrollY", y);

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

                }

                @Override
                public void onAnimationCancel(Animator arg0) {
                    // TODO Auto-generated method stub

                }
            });
            animators.start();
        }catch (Exception er){}
    }

    int MainListScrollState = 0;
    int ChanelListScrollState = 0;

    public void loadTVGuideScrollView() {

//        final Handler h=new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//
//                ListViewTVGuides.setSelectionFromTop(msg.what, ListViewChannels.getChildAt(0).getTop());
//            }
//        };


//        ListViewChannels.setOnScrollListener(new AbsListView.OnScrollListener() {
//
//
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                ChanelListScrollState= scrollState;
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//
//
//                try {
//                    if (MainListScrollState == 0) {
//
//                        ListViewTVGuides.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop());
//                    }
//                } catch (Exception tr) {
//                }
//
//            }
//        });

        ListViewTVGuides.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                MainListScrollState = scrollState;

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                try {
                    if (ChanelListScrollState == 0) {
                        ListViewChannels.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop());
                    }
                } catch (Exception tr) {
                }

            }
        });

        //if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            ScrolView_TvGuide.getViewTreeObserver().addOnScrollChangedListener(onscrollchangelistner);
        //} else {

       // }


    }
}