package com.pakpublishers.infosquare.envision.adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pakpublishers.infosquare.envision.GuidePageListActivity;
import com.pakpublishers.infosquare.envision.R;
import com.pakpublishers.infosquare.envision.objects.channel;
import com.pakpublishers.infosquare.envision.utils.ImageLoader;

import java.util.ArrayList;

/**
 * Created by M Asad Hashmi on 10/26/2016.
 */

public class ChannelSpinnerAdapter extends ArrayAdapter<channel> {

    public ImageLoader imageLoader;
        private GuidePageListActivity activity;

        public Resources res;
        public ArrayList<channel> tempValues=null;
        LayoutInflater inflater;
        public static int SelectedPosition=0;

        /*************  CustomAdapter Constructor *****************/
        public ChannelSpinnerAdapter (
                GuidePageListActivity activitySpinner,
                int textViewResourceId,
                ArrayList objects,
                Resources resLocal,
                int selecctedpos
        )
        {
            super(activitySpinner, textViewResourceId, objects);

            /********** Take passed values **********/
            activity = activitySpinner;
            tempValues     = objects;
            res      = resLocal;
            SelectedPosition=selecctedpos;
            imageLoader = new ImageLoader(activity.getApplicationContext().getApplicationContext());

            /***********  Layout inflator to call external xml layout () **********************/
            inflater = (LayoutInflater)activity.getSystemService(activity.getApplicationContext().LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomSelectedView(position, convertView, parent);
        }

        // This funtion called for each row ( Called data.size() times )
        public View getCustomSelectedView(final int position, View convertView, ViewGroup parent) {

            /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
            View row = inflater.inflate(R.layout.channel_spinneritem, parent, false);

            ImageView ChanelImage=(ImageView)row.findViewById(R.id.ChanelImageView);
            TextView TextView_ChannelName=(TextView)row.findViewById(R.id.TextView_ChannelName);
            ImageView SelectedTick=(ImageView) row.findViewById(R.id.SelectedTick);

            /***** Get each Model object from Arraylist ********/

            if(ChanelImage!=null)
            {
                ChanelImage.setVisibility(View.GONE);
                //imageLoader.////DisplayImage(tempValues.get(position).getTvstation_logo(), ChanelImage);
            }

            if(TextView_ChannelName!=null)
            {
                TextView_ChannelName.setTextColor(Color.WHITE);

                TextView_ChannelName.setText(tempValues.get(position).getChannel_name());
            }
            if(SelectedTick!=null)
            {
                SelectedTick.setVisibility(View.GONE);

            }




            return row;
        }
    public View getCustomView(final int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.channel_spinneritem, parent, false);

        ImageView ChanelImage=(ImageView)row.findViewById(R.id.ChanelImageView);
        TextView TextView_ChannelName=(TextView)row.findViewById(R.id.TextView_ChannelName);
        ImageView SelectedTick=(ImageView) row.findViewById(R.id.SelectedTick);
        LinearLayout LinearLayout_ItemView=(LinearLayout) row.findViewById(R.id.LinearLayout_ItemView);

        /***** Get each Model object from Arraylist ********/

        if(ChanelImage!=null)
        {
            imageLoader.DisplayImage(tempValues.get(position).getTvstation_logo(), ChanelImage);
        }

        if(ChanelImage!=null)
        {
            TextView_ChannelName.setText(tempValues.get(position).getChannel_name());
        }
        if(SelectedPosition==position)
        {
            SelectedTick.setVisibility(View.VISIBLE);

        }
        else
        {
            SelectedTick.setVisibility(View.GONE);
        }
        if(LinearLayout_ItemView!=null)
        {
            LinearLayout_ItemView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(event.getAction()==MotionEvent.ACTION_DOWN) {

                        SelectedPosition = position;
                        Message m = new Message();
                        m.what = 1;
                        activity.ChannelStationID=tempValues.get(position).getChannel_station_id();
                        activity.CurrentDataHandler.sendMessage(m);
                        notifyDataSetChanged();
                    }
                    return false;
                }
            });
        }



        return row;
    }


}
