package com.pakpublishers.infosquare.envision.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pakpublishers.infosquare.envision.GuidePageListActivity;
import com.pakpublishers.infosquare.envision.R;
import com.pakpublishers.infosquare.envision.objects.channel;
import com.pakpublishers.infosquare.envision.objects.schedule;
import com.pakpublishers.infosquare.envision.utils.ImageLoader;
import com.pakpublishers.infosquare.envision.utils.ServiceClient_LoadStationChannelsAndShow;
import com.pakpublishers.infosquare.envision.utils.TimeZone_App;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Asad on 8/19/2016.
 */
public class ListGuidePageAdapter extends ArrayAdapter<schedule> {

    public static ArrayList<schedule> myArrayList=new ArrayList<schedule>();

    public static schedule Selected=null;
    Context contx=null;
    Handler ReferenceHandler=null;
    public ImageLoader imageLoader;




    public ListGuidePageAdapter(Context context, int textViewResourceId, ArrayList<schedule> items, Handler h) {
        super(context, textViewResourceId, items);
        contx=context;
        myArrayList=items;
        ReferenceHandler=h;
        imageLoader = new ImageLoader(context.getApplicationContext());
    }
    public void ClearList()
    {
        myArrayList.clear();
    }


    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        final int pos=position;

        View v = convertView;

        if (v == null ) {
            LayoutInflater vi = (LayoutInflater)contx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listguidepage_listitem, null);
        }

        TextView TextView_ShowTime=(TextView)v.findViewById(R.id.TextView_ShowTime);
        TextView TextView_ShowName=(TextView)v.findViewById(R.id.TextView_ShowName);
        TextView TextView_ShowDetail=(TextView)v.findViewById(R.id.TextView_ShowDetail);
        TextView TextView_Description=(TextView)v.findViewById(R.id.TextView_Description);


       final schedule s= myArrayList.get(pos);

        if(TextView_ShowTime!=null)
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("hh:mm aa");
            Date d= null;
            try {
                d = format.parse(myArrayList.get(pos).getList_datetime());

                try {
                    d= TimeZone_App.GetLocaldatetime(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String formatedtime=format2.format(d);

                Message m=new Message();
                m.what=3;
                Bundle b=new Bundle();
                b.putString("time",formatedtime);
                m.setData(b);
                ReferenceHandler.sendMessage(m);
                TextView_ShowTime.setText(formatedtime);
            } catch (ParseException e) {
                e.printStackTrace();
            }


         //   TextView_ShowTime.setText(s.getList_datetime());
        }
        if(TextView_Description!=null)
        {
            TextView_Description.setText(s.getDescription());
        }
        if(TextView_ShowName!=null)
        {
            TextView_ShowName.setText(s.getShow_name());
        }
        if(TextView_ShowDetail!=null)
        {
            TextView_ShowDetail.setText(s.getShow_type());
        }
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()== MotionEvent.ACTION_UP) {
//                    Intent i=new Intent(contx, GuidePageListActivity.class);
//                    i.putExtra("scheduleid", s.getSchedule_id());
//                    i.putExtra("showtype", s.getShow_type());
//                    i.putExtra("seriesid", s.getSeries_id());
//
//                    i.putExtra("stationid", s.getStation_id());
//                    i.putExtra("team1", s.getTeam1());
//                    i.putExtra("team2", s.getTeam2());
//                    i.putExtra("list_datetime", s.getList_datetime());
//                    i.putExtra("showname", s.getShow_name());
//                    i.putExtra("showtime", s.getList_datetime());
//                    i.putExtra("showtypeid", s.getShow_type_id());
//                    i.putExtra("live", s.getLive());
//
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    contx.startActivity(i);


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
                    b.putString("live",s.getLive());

                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat format12 = new SimpleDateFormat("hh:mm aa");

                    Date StartDate = null;
                    try {
                        StartDate = format.parse(s.getList_datetime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        StartDate= TimeZone_App.GetLocaldatetime(StartDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String STime = format12.format(StartDate.getTime());

                    Date EndDate = null;
                    try {
                        EndDate = format.parse(s.getList_endtime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        EndDate= TimeZone_App.GetLocaldatetime(EndDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String ETime=format12.format(EndDate.getTime());



                    b.putString("showtime", STime + " - " + ETime);


                    m.what = 13;
                    m.setData(b);

                    ServiceClient_LoadStationChannelsAndShow.ReferenceHandler.sendMessage(m);
                }
                return true;
            }
        });
        return v;
    }

}

