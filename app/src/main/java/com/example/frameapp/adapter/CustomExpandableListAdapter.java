package com.example.frameapp.adapter;

import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.frameapp.EditProjectScreen;
import com.example.frameapp.R;
import com.example.frameapp.models.ToolsModel;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ToolsModel expandableListTitle;
    private Integer selectedPosition = 0;


    public CustomExpandableListAdapter(Context context,ToolsModel expandableListTitle ) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return expandableListTitle.get(listPosition).getLayers().get(expandedListPosition).getTitle();
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        ImageView image = (ImageView) convertView.findViewById(R.id.image) ;
        expandedListTextView.setText(expandableListTitle.get(listPosition).getLayers().get(expandedListPosition).getTitle());

            if (expandableListTitle.get(listPosition).getLayers().get(expandedListPosition).getShapeImage()!=null)
            {
                if (expandableListTitle.get(listPosition).getLayers().get(expandedListPosition).getShapeImage().contains("http://"))
                {
                    Glide.with(context).load("https://"+expandableListTitle.get(listPosition).getLayers().get(expandedListPosition).getShapeImage().substring(6)).placeholder(R.drawable.circle).into(image);
                }else  {
                    Glide.with(context).load(expandableListTitle.get(listPosition).getLayers().get(expandedListPosition).getShapeImage().toString()).placeholder(R.drawable.circle).into(image);
                }
            }else  {
                image.setVisibility(View.GONE);
            }
        expandedListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (expandableListTitle.get(listPosition).getLayers().get(expandedListPosition).getShapeImage()!=null || expandableListTitle.get(listPosition).getLayers().get(expandedListPosition).getShapeImage()=="")
                    {
                        if (expandableListTitle.get(listPosition).getLayers().get(expandedListPosition).getShapeImage().contains("http://"))
                        {
                            EditProjectScreen.Companion.getGetToolId().getToolID("https://"+expandableListTitle.get(listPosition).getLayers().get(expandedListPosition).getShapeImage().substring(6),"",0);
                        }else  {
                            EditProjectScreen.Companion.getGetToolId().getToolID(String.valueOf(expandableListTitle.get(listPosition).getLayers().get(expandedListPosition).getShapeImage()),"",0);
                        }
                    } else {
                        EditProjectScreen.Companion.getGetToolId().getToolID(String.valueOf(expandableListTitle.get(listPosition).getLayers().get(expandedListPosition).getId()), expandableListTitle.get(listPosition).getLayers().get(expandedListPosition).getColorCode(),expandableListTitle.get(listPosition).getLayers().get(expandedListPosition).getThickness());
                    }
                        EditProjectScreen.Companion.getGetToolType().getToolType(String.valueOf(expandableListTitle.get(listPosition).getLayers().get(expandedListPosition).getTool_id()));
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return expandableListTitle.get(listPosition).getLayers().size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition).getTitle();
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
//        Toast.makeText(context,""+listTitle,Toast.LENGTH_LONG).show();
        Log.d("res ===> ",""+getGroup(listPosition));
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }

        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}