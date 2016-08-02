package com.shalvahadebayo.digitable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Shalvah on 30/04/2016.
 * A custom adapter for an ExpandableListView
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter
	{
		private Context context;
		private List<String> listDataHeader;
		private HashMap<String, List<String>> listDataChild;

		public MyExpandableListAdapter(Context mContext, List<String> mListDataHeader,
		                               HashMap<String, List<String>> mListDataChild)
		{
			this.context = mContext;
			this.listDataHeader = mListDataHeader;
			this.listDataChild = mListDataChild;
		}


		@Override
		public int getGroupCount()
		{
			return this.listDataHeader.size();
		}

		@Override
		public int getChildrenCount(int groupPosition)
		{
			try
			{
				this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
			} catch (Exception e)
			{
				return 0;
			}
			return this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();

		}

		@Override
		public Object getGroup(int groupPosition)
		{
			return this.listDataHeader.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition)
		{
			return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition)
		{
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition)
		{
			return childPosition;
		}

		@Override
		public boolean hasStableIds()
		{
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
		{
			String headerTitle = (String) getGroup(groupPosition);
			if (convertView == null)
			{
				LayoutInflater li = (LayoutInflater) this.context.getSystemService(Context
						.LAYOUT_INFLATER_SERVICE);
				convertView = li.inflate(R.layout.list_group, null);
			}

			TextView listHeader = (TextView) convertView.findViewById(R.id.listHeader);
			listHeader.setText(headerTitle);
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
		{
			String childText = (String) getChild(groupPosition, childPosition);
			if (convertView == null)
			{
				LayoutInflater li = LayoutInflater.from(context);
				convertView = li.inflate(R.layout.list_item, null);
			}

			TextView listItem = (TextView) convertView.findViewById(R.id.listItem);
			listItem.setText(childText);
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition)
		{
			return true;
		}
	}
