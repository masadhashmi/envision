package com.pakpublishers.infosquare.envision.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.pakpublishers.infosquare.envision.GuidePageListActivity;
import com.pakpublishers.infosquare.envision.MainActivity;
import com.pakpublishers.infosquare.envision.R;
import com.pakpublishers.infosquare.envision.objects.channel;
import com.pakpublishers.infosquare.envision.utils.ImageLoader;

import java.util.ArrayList;


/**
 * Created by Asad on 8/19/2016.
 */
public class TVChannelsAdapter extends ArrayAdapter<channel> {

    public static ArrayList<channel> myArrayList=new ArrayList<channel>();

    public static channel Selected=null;
    Context contx=null;
    Handler ReferenceHandler=null;
    public ImageLoader imageLoader;


    public static channel getChangelByStationID(String StationID)
    {
        for (channel c:myArrayList
             ) {
            if(c.getChannel_station_id().equals(StationID))
            {
                return  c;
            }
        }
        return null;
    }

    public TVChannelsAdapter(Context context, int textViewResourceId, ArrayList<channel> items, Handler h) {
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
            v = vi.inflate(R.layout.channel_item, null);
        }

        ImageView ChanelImage=(ImageView)v.findViewById(R.id.ChanelImageView);


        if(ChanelImage!=null)
        {
            imageLoader.DisplayImage(myArrayList.get(position).getTvstation_logo(), ChanelImage);
        }
        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()== MotionEvent.ACTION_UP) {
                    if(!MainActivity.ISTVGUIDE_DATA_LOADING) {
                        Intent i = new Intent(contx, GuidePageListActivity.class);
                        i.putExtra("chanelname", myArrayList.get(pos).getChannel_name());
                        i.putExtra("chanelstationid", myArrayList.get(pos).getChannel_station_id());
                        i.putExtra("chanelimage", myArrayList.get(pos).getTvstation_logo());
                        i.putExtra("selectedindex", pos);


                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        contx.startActivity(i);
                    } else
                    {
                        Toast.makeText(contx, "Please wait, downloading is in progress.", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
        return v;
    }

}

