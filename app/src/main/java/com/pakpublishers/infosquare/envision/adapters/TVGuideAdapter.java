package com.pakpublishers.infosquare.envision.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pakpublishers.infosquare.envision.MainActivity;
import com.pakpublishers.infosquare.envision.R;
import com.pakpublishers.infosquare.envision.objects.channel;
import com.pakpublishers.infosquare.envision.objects.schedule;
import com.pakpublishers.infosquare.envision.utils.ImageLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by Asad on 8/19/2016.
 */
public class TVGuideAdapter extends ArrayAdapter<channel> {

    //public static ArrayList<channel> myArrayList=new ArrayList<channel>();

    public static channel Selected = null;
    Context contx = null;
    Handler ReferenceHandler = null;
    // public ImageLoader imageLoader;


    public TVGuideAdapter(Context context, int textViewResourceId, Handler h) {
        super(context, textViewResourceId, TVChannelsAdapter.myArrayList);
        contx = context;

        ReferenceHandler = h;
        // imageLoader = new ImageLoader(context.getApplicationContext());
    }

    public void ClearList() {
        TVChannelsAdapter.myArrayList.clear();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final int pos = position;

        View v = convertView;

        if (v == null) {
            //LayoutInflater vi = (LayoutInflater) contx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = TVChannelsAdapter.myArrayList.get(pos).CurrentView;//vi.inflate(R.layout.guideitem, null);

        }
        if (v != null) {
            try {
                LinearLayout CurentContainer=(LinearLayout)v;
                //if(!TVChannelsAdapter.myArrayList.get(pos).isLayoutAssembled)
                //{
//                    float DefaultDayMinimumWidth=((5 * 60 * MainActivity.scale))*24;
//                    for(int i=1; i<4; i++) {
//                        try {
//                            LinearLayout PreviousSubContainer = (LinearLayout) CurentContainer.getChildAt(i - 1);
//                            LinearLayout NextSubContainer = (LinearLayout) CurentContainer.getChildAt(i + 1);
//                            LinearLayout CurrentSubContainer = (LinearLayout) CurentContainer.getChildAt(i);
//
//                            int previousDayWidth = PreviousSubContainer.getWidth();
//                            //if (previousDayWidth != 0) {
//                                int previousControlMinimumWidth = (int) (DefaultDayMinimumWidth - (previousDayWidth - DefaultDayMinimumWidth));
//                                //TVChannelsAdapter.myArrayList.get(pos).isLayoutAssembled=true;
//                                CurrentSubContainer.setMinimumWidth(previousControlMinimumWidth);
//                            //}
//                        }catch (Exception er){}
//                    }

                //}
                return TVChannelsAdapter.myArrayList.get(pos).CurrentView;
//                LinearLayout li = (LinearLayout) v;
//                li.removeAllViews();
//                //if(li.getChildCount()<=0) {
//                    li.addView(TVChannelsAdapter.myArrayList.get(pos).CurrentView);
//               // }
//                v=li;
            }catch (Exception er){}
        }
        return v;
    }

}

