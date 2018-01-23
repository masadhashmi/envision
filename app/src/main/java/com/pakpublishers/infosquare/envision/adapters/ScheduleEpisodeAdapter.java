package com.pakpublishers.infosquare.envision.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pakpublishers.infosquare.envision.MainActivity;
import com.pakpublishers.infosquare.envision.R;
import com.pakpublishers.infosquare.envision.ShowDetailActivity;
import com.pakpublishers.infosquare.envision.objects.channel;
import com.pakpublishers.infosquare.envision.objects.episode;
import com.pakpublishers.infosquare.envision.utils.ImageLoader;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Asad on 8/19/2016.
 */
public class ScheduleEpisodeAdapter extends ArrayAdapter<episode> {

    public static ArrayList<episode> myArrayList = new ArrayList<episode>();

    public static channel Selected = null;
    Context contx = null;
    Handler ReferenceHandler = null;
     //public ImageLoader imageLoader;


    public ScheduleEpisodeAdapter(Context context, int textViewResourceId, ArrayList<episode> arr, Handler rH) {
        super(context, textViewResourceId, arr);
        contx = context;
        ReferenceHandler=rH;
        myArrayList=arr;
       // ReferenceHandler = h;
       //  imageLoader = new ImageLoader(context.getApplicationContext());
    }

    public void ClearList() {
        TVChannelsAdapter.myArrayList.clear();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final int pos=position;

        View v = convertView;

        if (v == null ) {
            LayoutInflater vi = (LayoutInflater)contx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.schedule_item, null);
        }
        //ImageView ImageView_EpisodeImage = (ImageView) v.findViewById(R.id.ImageView_EpisodeImage);
        TextView TextView_EpisodeTitle = (TextView) v.findViewById(R.id.TextView_EpisodeTitle);
        TextView TextView_EpisodeDescription = (TextView) v.findViewById(R.id.TextView_EpisodeDescription);
        TextView TextView_EpisodeTime = (TextView) v.findViewById(R.id.TextView_EpisodeTime);

        TextView TextView_Season_ID = (TextView) v.findViewById(R.id.TextView_Season_ID);
        TextView TextView_EpisodeNo = (TextView) v.findViewById(R.id.TextView_EpisodeNo);



        LinearLayout Button_Record = (LinearLayout) v.findViewById(R.id.Button_Record);
        LinearLayout Button_Cancel = (LinearLayout) v.findViewById(R.id.Button_Cancel);


//        if(ImageView_EpisodeImage!=null)
//        {
//            imageLoader.DisplayImage(myArrayList.get(position).getPoster(), ImageView_EpisodeImage);
//        }
        if(TextView_Season_ID!=null)
        {
            TextView_Season_ID.setText("Season: "+myArrayList.get(pos).getSeason_no());
        }
        if(TextView_EpisodeNo!=null)
        {
            TextView_EpisodeNo.setText("Episode: "+myArrayList.get(pos).getEpisode_no());
        }
        if(TextView_EpisodeTitle!=null)
        {
            TextView_EpisodeTitle.setText(myArrayList.get(pos).getEpisode_title());
        }
        if(TextView_EpisodeDescription!=null)
        {
            TextView_EpisodeDescription.setText(myArrayList.get(pos).getDescription());

        }
        if(TextView_EpisodeTime!=null)
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("EEEE dd, hh:mm aa");
            Date d= null;
            try {
                d = format.parse(myArrayList.get(pos).getList_datetime());

            TextView_EpisodeTime.setText(format2.format(d));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(Button_Record!=null)
        {
            try{
                validateRecordAndCancelButton(myArrayList.get(pos).getRecording_id(),myArrayList.get(pos).getStatus(),Button_Record,Button_Cancel);
            }catch(Exception er){}
            Button_Record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(myArrayList.get(pos).getStatus().trim().equals("")) {
                        Message m = new Message();
                        Bundle b=new Bundle();
                        String recid= myArrayList.get(pos).getEpisode_number();
                        b.putString("recordingid",recid);
                        m.setData(b);
                        m.what = 23;
                        ReferenceHandler.sendMessage(m);
                    }else
                    {
                        Toast.makeText(contx, "Recording already created.", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
        if(Button_Cancel!=null)
        {
            Button_Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!myArrayList.get(pos).getStatus().trim().equals("")) {
                        Message m = new Message();
                        Bundle b=new Bundle();
                        String recid= myArrayList.get(pos).getRecording_id();
                        b.putString("recordingid",recid);
                        m.setData(b);
                        m.what = 24;
                        ReferenceHandler.sendMessage(m);
                    }
                    else
                    {
                        Toast.makeText(contx, "Please record the series first.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        return v;
    }
    public void validateRecordAndCancelButton(String Recordingid, String status,LinearLayout Button_Record,LinearLayout Button_Cancel)
    {

        if(Recordingid.trim().length()>0)
        {
            Button_Record.setBackgroundColor(Color.TRANSPARENT);
            Button_Cancel.setBackgroundResource(R.color.colorOrange);
        }
        else {
            Button_Cancel.setBackgroundColor(Color.TRANSPARENT);
            Button_Record.setBackgroundResource(R.color.colorOrange);
        }

    }

}

