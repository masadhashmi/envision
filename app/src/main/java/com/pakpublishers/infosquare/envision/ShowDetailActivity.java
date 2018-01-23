package com.pakpublishers.infosquare.envision;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pakpublishers.infosquare.envision.adapters.ScheduleEpisodeAdapter;
import com.pakpublishers.infosquare.envision.objects.movie_details;
import com.pakpublishers.infosquare.envision.objects.other_details;
import com.pakpublishers.infosquare.envision.objects.series_detail;
import com.pakpublishers.infosquare.envision.objects.sports_show_details;
import com.pakpublishers.infosquare.envision.player.PlayerActivity;
import com.pakpublishers.infosquare.envision.utils.ImageLoader;
import com.pakpublishers.infosquare.envision.utils.Methoud_movie_details;
import com.pakpublishers.infosquare.envision.utils.Methoud_other_details;
import com.pakpublishers.infosquare.envision.utils.Methoud_sports_details;
import com.pakpublishers.infosquare.envision.utils.ShowDetailFetcher;
import com.pakpublishers.infosquare.envision.utils.TimeZone_App;
import com.pakpublishers.infosquare.envision.utils.series_recording_Methoud;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowDetailActivity extends Activity {


    String USER_ID = "99";
    String USER_Property = "UTAI";
    String TIMEZONE_NUMBER = "19800";

    String Scheduleid = "";
    String ShowType = "";
    String SeriesID = "";
    String StationID = "";
    String Team1 = "";
    String Team2 = "";
    String List_DateTime = "";
    String ShowTime = "";
    String ShowTypeID = "";
    String Live = "";

    String RecordingID = "";
    String Status = "";

    String LiveURL = "";

    String ShowName = "";
    View ProgressDialogAnim = null;
    View ProgressDialogBOX = null;

    series_detail CurrentSeriesDetail = null;
    movie_details CurrentMovieDetail = null;
    sports_show_details CurrentSportsDetail = null;
    other_details CurrentOtherDetail = null;


    ImageView ImageView_ShowImage = null;
    TextView TextView_ShowTitle = null;
    TextView TextView_ShowDescription = null;
    TextView TextView_ShowName = null;
    TextView TextView_ShowTimeTitle = null;

    TextView TextView_ShowNameBase = null;
    TextView TextView_ShowType = null;
    TextView TextView_ShowTimeBase = null;
    TextView TextView_Show_DescriptionText = null;

    LinearLayout LinearLayout_Container_Sports = null;
    ImageView ImageView_Team1Image = null;
    TextView TextView_Team1 = null;
    ImageView ImageView_Team2Image = null;
    TextView TextView_Team2 = null;
    TextView Button_RecordText = null;
    TextView Button_RecordCancelText = null;

    LinearLayout Button_Record = null;
    LinearLayout Button_Watch = null;
    LinearLayout Button_Cancel = null;
    ListView ListView_ShowDetails = null;
    //ProgressBar ProgressBar_LoadEpisodes = null;
    LinearLayout LinearLayout_BackButton = null;

    LinearLayout LinearLayout_Container_Movies = null;
    RatingBar RatingBar_Movie = null;

    LinearLayout LinearLayout_EpisodesArea = null;

    //ScrollView ScrollView_ShowDetail = null;

    public static ImageLoader imageLoader;

    public static Handler MainHandler = null;


    public void hideAll() {
        LinearLayout_Container_Movies.setVisibility(View.GONE);
        LinearLayout_Container_Sports.setVisibility(View.GONE);
        LinearLayout_EpisodesArea.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);


        imageLoader = new ImageLoader(getApplicationContext());
        ImageView_ShowImage = (ImageView) findViewById(R.id.ImageView_ShowImage);
        TextView_ShowTitle = (TextView) findViewById(R.id.TextView_ShowTitle);
        TextView_ShowDescription = (TextView) findViewById(R.id.TextView_ShowDescription);
        TextView_ShowName = (TextView) findViewById(R.id.TextView_ShowName);
        TextView_ShowTimeTitle = (TextView) findViewById(R.id.TextView_ShowTimeTitle);

        TextView_ShowNameBase = (TextView) findViewById(R.id.TextView_ShowNameBase);
        TextView_ShowType = (TextView) findViewById(R.id.TextView_ShowType);
        TextView_ShowTimeBase = (TextView) findViewById(R.id.TextView_ShowTimeBase);
        TextView_Show_DescriptionText = (TextView) findViewById(R.id.TextView_Show_DescriptionText);

        Button_Record = (LinearLayout) findViewById(R.id.Button_Record);
        Button_Watch = (LinearLayout) findViewById(R.id.Button_Watch);
        Button_Cancel = (LinearLayout) findViewById(R.id.Button_Cancel);
        //ProgressBar_LoadEpisodes = (ProgressBar) findViewById(R.id.ProgressBar_LoadEpisodes);
        LinearLayout_BackButton = (LinearLayout) findViewById(R.id.LinearLayout_BackButton);
        LinearLayout_EpisodesArea = (LinearLayout) findViewById(R.id.LinearLayout_EpisodesArea);
        LinearLayout_Container_Sports = (LinearLayout) findViewById(R.id.LinearLayout_Container_Sports);

        ImageView_Team1Image = (ImageView) findViewById(R.id.ImageView_Team1Image);
        TextView_Team1 = (TextView) findViewById(R.id.TextView_Team1);
        ImageView_Team2Image = (ImageView) findViewById(R.id.ImageView_Team2Image);
        TextView_Team2 = (TextView) findViewById(R.id.TextView_Team2);
        Button_RecordText = (TextView) findViewById(R.id.Button_RecordText);
        Button_RecordCancelText = (TextView) findViewById(R.id.Button_RecordCancelText);

        ProgressDialogAnim = (View) findViewById(R.id.ProgressDialouge);
        ProgressDialogBOX = (View) findViewById(R.id.ProgressDialougeMain);

        //--Movies
        LinearLayout_Container_Movies = (LinearLayout) findViewById(R.id.LinearLayout_Container_Movies);
        RatingBar_Movie = (RatingBar) findViewById(R.id.RatingBar_Movie);


      //  ScrollView_ShowDetail = (ScrollView) findViewById(R.id.ScrollView_ShowDetail);

        ListView_ShowDetails = (ListView) findViewById(R.id.ListView_ShowDetails);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ListView_ShowDetails.setNestedScrollingEnabled(true);
        }

        Intent ci= getIntent();
        ShowType = getIntent().getStringExtra("showtype");
        Scheduleid = ci.getStringExtra("scheduleid");
        SeriesID = ci.getStringExtra("seriesid");

        StationID = ci.getStringExtra("stationid");
        Team1 = ci.getStringExtra("team1");
        Team2 = ci.getStringExtra("team2");
        List_DateTime = ci.getStringExtra("list_datetime");
        ShowTime = ci.getStringExtra("showtime");
        ShowTypeID = ci.getStringExtra("showtypeid");
        Live = ci.getStringExtra("live");
        ShowName = ci.getStringExtra("showname");


        TextView_ShowDescription.setText("");
        TextView_ShowTitle.setText("");
        if (Live.equals("1")) {
            Button_Watch.setVisibility(View.VISIBLE);
        } else {
            Button_Watch.setVisibility(View.GONE);
        }

       // ScrollView_ShowDetail.scrollTo(0, 0);
        initiateListners();
        hideAll();
        loadHandler();
        showProgressBar(true,true);
        loadAll();



    }

    private void loadHandler() {
        MainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 23:// for recording episode
                    {
                        Bundle b=msg.getData();
                        final String Recordingid=b.getString("recordingid");
                       // Log.e("Ep No",Recordingid);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ShowDetailActivity.this);
                        builder.setMessage("Would You Like To Record It?");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {



                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                    }
                    case 24:// for Canceling episode
                    {
                        Bundle b=msg.getData();
                       final String Recordingid=b.getString("recordingid");
                        Log.e("Ep RecID",Recordingid);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ShowDetailActivity.this);
                        builder.setMessage("Would You Like To Record It?");
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                series_recording(false,true,USER_ID, Scheduleid, USER_Property, "cancel",Recordingid, SeriesID, StationID, List_DateTime, "1", "19800", List_DateTime);

                                dialog.dismiss();
                            }
                        });

                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Do nothing
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();

                        break;
                    }
                }
            }
        };
    }

    private void loadAll() {

        TextView_ShowNameBase.setText(ShowName);
        TextView_ShowName.setText(ShowName);
        TextView_ShowType.setText(ShowType);
        TextView_ShowTimeBase.setText("");
        TextView_Show_DescriptionText.setText("");
        TextView_ShowTimeTitle.setText(ShowTime);


//        Log.e("scheduleid :", Scheduleid);
//        Log.e("showtype :", ShowType);
//        Log.e("seriesid :", SeriesID);
//        Log.e("stationid :", StationID);
//        Log.e("team1 :", Team1);
//        Log.e("team2 :", Team2);
//        Log.e("list_datetime :", List_DateTime);
//        Log.e("ShowTypeID: ", ShowTypeID);

        loadShowDetailsBasedonType();
    }

    private void loadShowDetailsBasedonType() {

        switch (ShowTypeID) {
            //----- Rest of Them Are series
            default: {
                Log.e("loading", "Series");
                Button_RecordText.setText("Record Series");
                Button_RecordCancelText.setText("Cancel Series");
                load_series_details(USER_ID, Scheduleid, USER_Property, SeriesID, "1", TIMEZONE_NUMBER, List_DateTime);
                break;
            }
            //----- Movies
            case "M": {
                Log.e("loading", "Movies");
                load_movie_details(USER_ID, Scheduleid, USER_Property, SeriesID, "1", TIMEZONE_NUMBER, List_DateTime);
                break;
            }
            //----- sports
            case "O": {
                Log.e("loading", "Sports");
                load_sport_details(USER_ID, Scheduleid, USER_Property, SeriesID, "1", TIMEZONE_NUMBER, List_DateTime);
                break;
            }
            //----- All these are OTHERS
            case "V": {
                Log.e("loading", "Others");
                load_other_details(USER_ID, Scheduleid, USER_Property, SeriesID, "1", TIMEZONE_NUMBER, List_DateTime);
                break;
            }
            case "Y": {
                Log.e("loading", "Others");
                load_other_details(USER_ID, Scheduleid, USER_Property, SeriesID, "1", TIMEZONE_NUMBER, List_DateTime);
                break;
            }
            case "!": {
                Log.e("loading", "Others");
                load_other_details(USER_ID, Scheduleid, USER_Property, SeriesID, "1", TIMEZONE_NUMBER, List_DateTime);
                break;
            }
            case "2": {
                Log.e("loading", "Others");
                load_other_details(USER_ID, Scheduleid, USER_Property, SeriesID, "1", TIMEZONE_NUMBER, List_DateTime);
                break;
            }
            case "&": {
                Log.e("loading", "Others");
                load_other_details(USER_ID, Scheduleid, USER_Property, SeriesID, "1", TIMEZONE_NUMBER, List_DateTime);
                break;
            }


        }


    }

    private void initiateListners() {

        Button_Record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(RecordingID.trim().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowDetailActivity.this);
                    builder.setMessage("Would You Like To Record It?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            series_recording(true,false, USER_ID, Scheduleid, USER_Property, "start", RecordingID, SeriesID, StationID, List_DateTime, "1", "19800", List_DateTime);
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }else
                {
                    Toast.makeText(ShowDetailActivity.this, "Already in Recording.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        Button_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!RecordingID.trim().equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowDetailActivity.this);
                builder.setMessage("Would You Like To Record It?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        series_recording(false,false, USER_ID, Scheduleid, USER_Property, "cancel", RecordingID, SeriesID, StationID, List_DateTime, "1", "19800", List_DateTime);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }else
            {
                Toast.makeText(ShowDetailActivity.this, "Please Record the Series first.", Toast.LENGTH_SHORT).show();
            }

            }
        });

        Button_Watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ShowDetailActivity.this);
                builder.setMessage("Would you like to Watch Live?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent i = new Intent(ShowDetailActivity.this, PlayerActivity.class);
                        Bundle b = new Bundle();
                        b.putString("liveurl", LiveURL);
                        i.putExtra("data", b);
                        startActivity(i);
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


            }
        });
        LinearLayout_BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDetailActivity.this.finish();
            }
        });
    }





    public void series_recording(final boolean doRecording, final boolean isOnlyEpisodeRecording, final String user_id, final String schedule_id, final String user_property, final String recording_action, final String recordingid, final String series_id, final String station_id, final String list_datetime, final String Live, final String _TimeZone, final String startDate) {
        showProgressBar(true,true);

        final series_recording_Methoud Sc = new series_recording_Methoud();
        if(!isOnlyEpisodeRecording) {
            Sc.CompleteURL = "http://myevisiontv.com/mvc/api-v1/home/series_recording";
        }else
        {
            Sc.CompleteURL = "http://myevisiontv.com/mvc/api-v1/home/movie_recording";
        }

        if (doRecording) {
            Sc.Parameters = "user_id=" + user_id + "&schedule_id=" + schedule_id + "&user_property=" + user_property + "&recording_action=" + recording_action + "&series_id=" + series_id + "&station_id=" + station_id + "&list_datetime=" + list_datetime + "&live=" + Live + "&time_zone=" + _TimeZone + "&start_date=" + startDate;
        } else {
            if(isOnlyEpisodeRecording)
            {
                Sc.Parameters = "user_id=" + user_id + "&recording_id=" + recordingid + "&recording_action=" + recording_action  ;
            }
            else {
                Sc.Parameters = "user_id=" + user_id + "&schedule_id=" + schedule_id + "&user_property=" + user_property + "&recording_id=" + recordingid + "&recording_action=" + recording_action + "&series_id=" + series_id + "&station_id=" + station_id + "&list_datetime=" + list_datetime + "&live=" + Live + "&time_zone=" + _TimeZone + "&start_date=" + startDate;
            }
        }
        //Sc.Parameters = "user_id=" + user_id + "&schedule_id=" + schedule_id + "&show_type=" + show_type;
        Sc.cntx = getApplicationContext();
        series_recording_Methoud.ReferenceHandler = new Handler() {
            @Override
            public void handleMessage(Message m) {
                switch (m.what) {

                    case series_recording_Methoud.FAILURE: {
                        showProgressBar(false,false);
                        break;
                    }
                    case series_recording_Methoud.SUCCESS: {

                        try {
                            Bundle b = m.getData();
                            String Result = b.getString("result");

                            JSONObject jsonObject = null;
                            String Status = "";

                            try {
                                jsonObject = new JSONObject(Result);
                                Status = jsonObject.getString("status");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            //String messages=jsonObject.getString("messages");

                            //JSONArray record_idList = jsonObject.getJSONArray("record_id");
                            String message = null;
                            if (Status.toLowerCase().trim().equals("success")) {
                                //ScrollView_ShowDetail.scrollTo(0, 0);

                                try {
                                    message = jsonObject.getString("messages");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (doRecording) {
                                    Toast.makeText(ShowDetailActivity.this, message, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ShowDetailActivity.this, "Recording Canceled Successfuly.", Toast.LENGTH_SHORT).show();
                                }
                                loadAll();
                            } else {
                                try {
                                    message = jsonObject.getString("messages");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String code = null;
                                try {
                                    code = jsonObject.getString("code");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                showProgressBar(false,false);
                                Toast.makeText(ShowDetailActivity.this, "Error :" + code + ", " + message, Toast.LENGTH_LONG).show();
                            }
                        }catch (Exception er)
                        {}

                        // showProgressBar(false,false);
                        break;
                    }

                }
            }
        };
        series_recording_Methoud.isBUSY = true;
        Sc.start();
    }

    public void load_sport_details(final String user_id, final String schedule_id, final String user_property, final String series_id, String Live, String _TimeZone, String startDate) {


        showProgressBar(true, true);

        final Methoud_sports_details Sc = new Methoud_sports_details();
        Sc.CompleteURL = "http://myevisiontv.com/mvc/api-v1/home/sports_details";

        Sc.Parameters = "user_id=" + user_id + "&schedule_id=" + schedule_id + "&user_property=" + user_property + "&series_id=" + series_id + "&live=" + Live + "&time_zone=" + _TimeZone + "&start_date=" + startDate;
        //Sc.Parameters = "user_id=" + user_id + "&schedule_id=" + schedule_id + "&show_type=" + show_type;
        Sc.cntx = getApplicationContext();
        Methoud_sports_details.ReferenceHandler = new Handler() {
            @Override
            public void handleMessage(Message m) {
                switch (m.what) {

                    case Methoud_sports_details.SUCCESS: {

                        CurrentSportsDetail = Sc.SportsShow_Detail_Object;
                        TextView_ShowTitle.setText(CurrentSportsDetail.getShow_name());
                        TextView_ShowDescription.setText(CurrentSportsDetail.getCast());

                        LiveURL = CurrentSportsDetail.getLive_url();

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat format2 = new SimpleDateFormat("EEEE, hh:mm aa");
                        Date d = null;

                        try {
                            d = format.parse(CurrentSportsDetail.getList_datetime());
                            d = TimeZone_App.GetLocaldatetime(d);

                            TextView_ShowTimeBase.setText(format2.format(d));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        TextView_Show_DescriptionText.setText(CurrentSportsDetail.getDescription());

                        validateRecordAndCancelButton(CurrentSportsDetail.getRecording_id(), CurrentSportsDetail.getStatus());
                       // imageLoader = new ImageLoader(getApplicationContext());
                        imageLoader.DisplayImage(CurrentSportsDetail.getPoster(), ImageView_ShowImage);
                        try {

                            Log.e("Main Poster", CurrentSportsDetail.getPoster());
                            Log.e("Team1 url", CurrentSportsDetail.getTeam1_url());
                            Log.e("Team2 url", CurrentSportsDetail.getTeam2_url());
                            imageLoader.DisplayImage(CurrentSportsDetail.getTeam1_url(), ImageView_Team1Image);
                            imageLoader.DisplayImage(CurrentSportsDetail.getTeam2_url(), ImageView_Team2Image);
                        } catch (Exception er) {
                        }


                        // ScheduleEpisodeAdapter adp=new ScheduleEpisodeAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,Sc.EpisodesList);
                        // ListView_ShowDetails.setAdapter(adp);
                        // adp.notifyDataSetChanged();

                        LinearLayout_EpisodesArea.setVisibility(View.GONE);

                        if (CurrentSportsDetail.getTeam1().trim().length() > 1) {
                            LinearLayout_Container_Sports.setVisibility(View.VISIBLE);

                            TextView_Team1.setText(CurrentSportsDetail.getTeam1());
                            TextView_Team2.setText(CurrentSportsDetail.getTeam2());


                        }


                        showProgressBar(false, false);
                       // ScrollView_ShowDetail.scrollTo(0, 0);

                        break;
                    }

                }
            }
        };
        Methoud_sports_details.isBUSY = true;
        Sc.start();
    }


    public void load_other_details(final String user_id, final String schedule_id, final String user_property, final String series_id, String Live, String _TimeZone, String startDate) {


        showProgressBar(true, true);

        final Methoud_other_details Sc = new Methoud_other_details();
        Sc.CompleteURL = "http://myevisiontv.com/mvc/api-v1/home/other_details";

        Sc.Parameters = "user_id=" + user_id + "&schedule_id=" + schedule_id + "&user_property=" + user_property + "&series_id=" + series_id + "&live=" + Live + "&time_zone=" + _TimeZone + "&start_date=" + startDate;
        //Sc.Parameters = "user_id=" + user_id + "&schedule_id=" + schedule_id + "&show_type=" + show_type;
        Sc.cntx = getApplicationContext();
        Methoud_other_details.ReferenceHandler = new Handler() {
            @Override
            public void handleMessage(Message m) {
                switch (m.what) {

                    case Methoud_other_details.SUCCESS: {

                        CurrentOtherDetail = Sc.Other_Detail_Object;
                        TextView_ShowTitle.setText(CurrentOtherDetail.getShow_name());
                        TextView_ShowDescription.setText(CurrentOtherDetail.getCast());

                        LiveURL = CurrentOtherDetail.getLive_url();

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat format2 = new SimpleDateFormat("EEEE, hh:mm aa");
                        Date d = null;

                        try {
                            d = format.parse(CurrentOtherDetail.getList_datetime());
                            d = TimeZone_App.GetLocaldatetime(d);

                            TextView_ShowTimeBase.setText(format2.format(d));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        TextView_Show_DescriptionText.setText(CurrentOtherDetail.getDescription());

                        validateRecordAndCancelButton(CurrentOtherDetail.getRecording_id(), CurrentOtherDetail.getStatus());

                        imageLoader.DisplayImage(CurrentOtherDetail.getPoster(), ImageView_ShowImage);

                        // ScheduleEpisodeAdapter adp=new ScheduleEpisodeAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,Sc.EpisodesList);
                        // ListView_ShowDetails.setAdapter(adp);
                        // adp.notifyDataSetChanged();

                        LinearLayout_EpisodesArea.setVisibility(View.GONE);
                        showProgressBar(false, false);
                      //  ScrollView_ShowDetail.scrollTo(0, 0);

                        break;
                    }

                }
            }
        };
        Methoud_other_details.isBUSY = true;
        Sc.start();
    }


    public void load_movie_details(final String user_id, final String schedule_id, final String user_property, final String series_id, String Live, String _TimeZone, String startDate) {


        showProgressBar(true, true);

        final Methoud_movie_details Sc = new Methoud_movie_details();
        Sc.CompleteURL = "http://myevisiontv.com/mvc/api-v1/home/movie_details";

        Sc.Parameters = "user_id=" + user_id + "&schedule_id=" + schedule_id + "&user_property=" + user_property + "&series_id=" + series_id + "&live=" + Live + "&time_zone=" + _TimeZone + "&start_date=" + startDate;
        //Sc.Parameters = "user_id=" + user_id + "&schedule_id=" + schedule_id + "&show_type=" + show_type;
        Sc.cntx = getApplicationContext();
        Methoud_movie_details.ReferenceHandler = new Handler() {
            @Override
            public void handleMessage(Message m) {
                switch (m.what) {

                    case Methoud_movie_details.SUCCESS: {

                        CurrentMovieDetail = Sc.Movie_Detail_Object;
                        TextView_ShowTitle.setText(CurrentMovieDetail.getShow_name());
                        TextView_ShowDescription.setText(CurrentMovieDetail.getCast());

                        LiveURL = CurrentMovieDetail.getLive_url();

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat format2 = new SimpleDateFormat("EEEE, hh:mm aa");
                        Date d = null;

                        try {
                            d = format.parse(CurrentMovieDetail.getList_datetime());
                            d = TimeZone_App.GetLocaldatetime(d);

                            TextView_ShowTimeBase.setText(format2.format(d));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        TextView_Show_DescriptionText.setText(CurrentMovieDetail.getDescription());

                        validateRecordAndCancelButton(CurrentMovieDetail.getRecord_id(), CurrentMovieDetail.getStatus());
                        imageLoader.DisplayImage(CurrentMovieDetail.getPoster(), ImageView_ShowImage);

                        // ScheduleEpisodeAdapter adp=new ScheduleEpisodeAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,Sc.EpisodesList);
                        // ListView_ShowDetails.setAdapter(adp);
                        // adp.notifyDataSetChanged();
                        LinearLayout_Container_Movies.setVisibility(View.VISIBLE);
                        try {
                            RatingBar_Movie.setRating(Float.parseFloat(CurrentMovieDetail.getStar_rating()));
                        } catch (Exception er) {
                        }
                        LinearLayout_EpisodesArea.setVisibility(View.GONE);
                        showProgressBar(false, false);
                       // ScrollView_ShowDetail.scrollTo(0, 0);

                        break;
                    }

                }
            }
        };
        Methoud_movie_details.isBUSY = true;
        Sc.start();
    }


    public void load_series_details(final String user_id, final String schedule_id, final String user_property, final String series_id, String Live, String _TimeZone, String startDate) {


        showProgressBar(true, true);

        final ShowDetailFetcher Sc = new ShowDetailFetcher();
        Sc.CompleteURL = "http://myevisiontv.com/mvc/api-v1/home/series_details";

        Sc.Parameters = "user_id=" + user_id + "&schedule_id=" + schedule_id + "&user_property=" + user_property + "&series_id=" + series_id + "&live=" + Live + "&time_zone=" + _TimeZone + "&start_date=" + startDate;
        //Sc.Parameters = "user_id=" + user_id + "&schedule_id=" + schedule_id + "&show_type=" + show_type;
        Sc.cntx = getApplicationContext();
        ShowDetailFetcher.ReferenceHandler = new Handler() {
            @Override
            public void handleMessage(Message m) {
                switch (m.what) {

                    case ShowDetailFetcher.FAILURE: {
                        showProgressBar(false,false);
                    }
                    case ShowDetailFetcher.SUCCESS: {

                        try {
                            CurrentSeriesDetail = Sc.Series_Detail_Object;
                            TextView_ShowTitle.setText(CurrentSeriesDetail.getShow_name());
                            TextView_ShowDescription.setText(CurrentSeriesDetail.getOverview());

                            LiveURL = CurrentSeriesDetail.getLive_url();

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            SimpleDateFormat format2 = new SimpleDateFormat("EEEE, hh:mm aa");
                            Date d = null;


                            try {
                                d = format.parse(CurrentSeriesDetail.getList_datetime());
                                d = TimeZone_App.GetLocaldatetime(d);

                                TextView_ShowTimeBase.setText(format2.format(d));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            validateRecordAndCancelButton(CurrentSeriesDetail.getRecording_id(), CurrentSeriesDetail.getStatus());
                            TextView_Show_DescriptionText.setText(CurrentSeriesDetail.getDescription());

                            imageLoader.DisplayImage(CurrentSeriesDetail.getPoster(), ImageView_ShowImage);

                            LinearLayout_EpisodesArea.setVisibility(View.VISIBLE);
                            ScheduleEpisodeAdapter adp = new ScheduleEpisodeAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, Sc.EpisodesList, MainHandler);
                            ListView_ShowDetails.setAdapter(adp);
                            adp.notifyDataSetChanged();
                        }catch (Exception er){}
                        showProgressBar(false, false);
                       // ScrollView_ShowDetail.scrollTo(0, 0);

                        break;
                    }

                }
            }
        };
        ShowDetailFetcher.isBUSY = true;
        Sc.start();
    }


    public void validateRecordAndCancelButton(String Recordingid, String status) {
        RecordingID = Recordingid;
        Status = status;
        Log.e("Statusis", "[" + status + "]");
        if (Recordingid.trim().length() > 0) {
            Button_Record.setBackgroundColor(Color.TRANSPARENT);
            Button_Cancel.setBackgroundResource(R.color.colorOrange);
        } else {
            Button_Cancel.setBackgroundColor(Color.TRANSPARENT);
            Button_Record.setBackgroundResource(R.color.colorOrange);
        }

    }


    private void showProgressBar(boolean b, boolean clickable) {

        ProgressDialogBOX.setClickable(clickable);

        if (b) {
            ProgressDialogBOX.setVisibility(View.VISIBLE);
            ProgressDialogAnim.startAnimation(AnimationUtils.loadAnimation(this, R.anim.progresssanimation));
        } else {

            ProgressDialogBOX.setVisibility(View.GONE);
        }

    }

}
