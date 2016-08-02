package com.shalvahadebayo.digitable;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity
	{

		ImageView indicator1;
		ImageView indicator2;
		ImageView indicator3;
		ImageView[] indicators = new ImageView[]{indicator1, indicator2, indicator3};
		int[] colorList;
		int page;
		ArgbEvaluator evaluator = new ArgbEvaluator();
		Button nextBtn;
		Button finishBtn;
		Button skipBtn;
		/**
		 * The {@link ViewPager} that will host the section contents.
		 */
		private ViewPager mViewPager;

		@Override
		protected void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);

			//colors for each section
			int color1 = ContextCompat.getColor(this, android.R.color.holo_green_dark);
			int color2 = ContextCompat.getColor(this, android.R.color.holo_blue_light);
			int color3 = ContextCompat.getColor(this, R.color.colorPrimary);

			colorList = new int[]{color1, color2, color3};

			setContentView(R.layout.activity_welcome);

			// Create the adapter that will return a fragment for each of the three
			// primary sections of the activity.
			/*
			The {@link android.support.v4.view.PagerAdapter} that will provide
		  fragments for each of the sections. We use a
		  {@link FragmentPagerAdapter} derivative, which will keep every
		  loaded fragment in memory. If this becomes too memory intensive, it
		  may be best to switch to a
		  {@link android.support.v4.app.FragmentStatePagerAdapter}.
		 */
			SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

			indicators[0] = (ImageView) findViewById(R.id.intro_indicator_0);
			indicators[1] = (ImageView) findViewById(R.id.intro_indicator_1);
			indicators[2] = (ImageView) findViewById(R.id.intro_indicator_2);

			// Set up the ViewPager with the sections adapter.
			mViewPager = (ViewPager) findViewById(R.id.vcontainer);
			assert mViewPager != null;
			mViewPager.setAdapter(mSectionsPagerAdapter);
			mViewPager.setCurrentItem(page);
			updateIndicators(page);

			nextBtn = (Button) findViewById(R.id.intro_btn_next);
			finishBtn = (Button) findViewById(R.id.intro_btn_finish);
			skipBtn = (Button) findViewById(R.id.intro_btn_skip);

			nextBtn.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						mViewPager.setCurrentItem(page, true);
						page++;
					}
				});

			finishBtn.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						finish();
						Intent i = new Intent(getApplication(), SetupActivity.class);
						startActivity(i);

					}
				});

			skipBtn.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						finish();
						Intent i = new Intent(getApplication(), SetupActivity.class);
						startActivity(i);
					}
				});


			mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
				{
					@Override
					public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
					{
			 /*
	       color update
          */
						int colorUpdate = (Integer) evaluator.evaluate(positionOffset, colorList[position],
								colorList[position == 2 ? position : position + 1]);
						mViewPager.setBackgroundColor(colorUpdate);
					}

					@Override
					public void onPageSelected(int position)
					{
						page = position;
						updateIndicators(page);
						switch (position)
						{
							case 0:
								mViewPager.setBackgroundColor(colorList[0]);
								break;
							case 1:
								mViewPager.setBackgroundColor(colorList[1]);
								break;
							case 2:
								mViewPager.setBackgroundColor(colorList[2]);
								break;
						}
						nextBtn.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
						finishBtn.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
					}


					@Override
					public void onPageScrollStateChanged(int state)
					{

					}
				});
		}

		@Override
		public void onBackPressed()
		{
			super.onBackPressed();
		}

		void updateIndicators(int position)
		{
			for (int i = 0; i < indicators.length; i++)
			{
				indicators[i].setBackgroundResource(
						i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
				);
			}
		}

		/**
		 * A placeholder fragment containing a simple view.
		 */
		public static class PlaceholderFragment extends Fragment
			{
				/**
				 * The fragment argument representing the section number for this
				 * fragment.
				 */
				private static final String ARG_SECTION_NUMBER = "section_number";

				public PlaceholderFragment()
				{
				}

				/**
				 * Returns a new instance of this fragment for the given section
				 * number.
				 */
				public static PlaceholderFragment newInstance(int sectionNumber)
				{
					PlaceholderFragment fragment = new PlaceholderFragment();
					Bundle args = new Bundle();
					args.putInt(ARG_SECTION_NUMBER, sectionNumber);
					fragment.setArguments(args);
					return fragment;
				}

				@Override
				public View onCreateView(LayoutInflater inflater, ViewGroup container,
				                         Bundle savedInstanceState)
				{
					View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
					TextView textView = (TextView) rootView.findViewById(R.id.section_label);
					ImageView iView = (ImageView) rootView.findViewById(R.id.welcome_img);
					switch (getArguments().getInt(ARG_SECTION_NUMBER))
					{
						case 1:
							textView.setText("Digitise your timetable");
							iView.setImageDrawable(getResources().getDrawable(R.drawable.timetable));
							break;
						case 2:
							textView.setText("Keep track of your assignments");
							iView.setImageDrawable(getResources().getDrawable(R.drawable.projects));
							break;
						case 3:
							textView.setText("Stay informed on your interests");
							iView.setImageDrawable(getResources().getDrawable(R.drawable.news));
							break;
					}
					return rootView;
				}
			}


		/**
		 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
		 * one of the sections/tabs/pages.
		 */
		public class SectionsPagerAdapter extends FragmentPagerAdapter
			{

				public SectionsPagerAdapter(FragmentManager fm)
				{
					super(fm);
				}

				@Override
				public Fragment getItem(int position)
				{
					// getItem is called to instantiate the fragment for the given page.
					// Return a PlaceholderFragment (defined as a static inner class below).
					switch (position)
					{
						case 1:
							return PlaceholderFragment.newInstance(position + 1);
						case 2:
							return PlaceholderFragment.newInstance(position + 1);
					}
					return PlaceholderFragment.newInstance(position + 1);
				}

				@Override
				public int getCount()
				{
					// Show 3 total pages.
					return 3;
				}

				@Override
				public CharSequence getPageTitle(int position)
				{
					switch (position)
					{
						case 0:
							return "SECTION 1";
						case 1:
							return "SECTION 2";
						case 2:
							return "SECTION 3";
					}
					return null;
				}
			}
	}
