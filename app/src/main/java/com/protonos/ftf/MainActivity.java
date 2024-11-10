package com.protonos.ftf;

import android.Manifest;
import android.animation.*;
import android.animation.ObjectAnimator;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.os.Bundle;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.*;
import android.widget.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnAdapterChangeListener;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.shawnlin.numberpicker.*;
import java.io.*;
import java.io.InputStream;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class MainActivity extends AppCompatActivity {
	
	private ArrayList<HashMap<String, Object>> pages = new ArrayList<>();
	
	private LinearLayout linear1;
	private TextView textview1;
	private TextView textview2;
	private ViewPager viewpager1;
	private BottomNavigationView bottomnavigation1;
	
	private Intent intent = new Intent();
	private ObjectAnimator anima = new ObjectAnimator();
	private SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		linear1 = findViewById(R.id.linear1);
		textview1 = findViewById(R.id.textview1);
		textview2 = findViewById(R.id.textview2);
		viewpager1 = findViewById(R.id.viewpager1);
		bottomnavigation1 = findViewById(R.id.bottomnavigation1);
		pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		
		viewpager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int _position, float _positionOffset, int _positionOffsetPixels) {
				
			}
			
			@Override
			public void onPageSelected(int _position) {
				BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigation1);
				// assuming you have defined your BottomNavigationView in your layout XML file with the id "bottom_navigation_view"
				bottomNavigationView.setSelectedItemId(_position);
			}
			
			@Override
			public void onPageScrollStateChanged(int _scrollState) {
				
			}
		});
		
		bottomnavigation1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem item) {
				final int _itemId = item.getItemId();
				viewpager1.setCurrentItem((int)_itemId);
				return true;
			}
		});
	}
	
	private void initializeLogic() {
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("page", "Session");
			pages.add(_item);
		}
		
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("page", "Power");
			pages.add(_item);
		}
		
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("page", "Logs");
			pages.add(_item);
		}
		
		{
			HashMap<String, Object> _item = new HashMap<>();
			_item.put("page", "Config");
			pages.add(_item);
		}
		
		viewpager1.setAdapter(new Viewpager1Adapter(pages));
		bottomnavigation1.getMenu().add(0, 0, 0, "Session").setIcon(R.drawable.ic_av_timer_black);
		bottomnavigation1.getMenu().add(0, 1, 0, "Power").setIcon(R.drawable.ic_assistant_photo_black);
		bottomnavigation1.getMenu().add(0, 2, 0, "Logs").setIcon(R.drawable.ic_directions_walk_black);
		bottomnavigation1.getMenu().add(0, 3, 0, "Config").setIcon(R.drawable.ic_edit_black);
		if (!pref.contains("rest")) {
			pref.edit().putString("rest", "6000").commit();
		}
		if (!pref.contains("path")) {
			pref.edit().putString("path", "/storage/emulated/0/Documents/FitTheFix/workouts/training.json").commit();
			FileUtil.writeFile(pref.getString("path", ""), "[]");
		}
	}
	
	public class Viewpager1Adapter extends PagerAdapter {
		
		Context _context;
		ArrayList<HashMap<String, Object>> _data;
		
		public Viewpager1Adapter(Context _ctx, ArrayList<HashMap<String, Object>> _arr) {
			_context = _ctx;
			_data = _arr;
		}
		
		public Viewpager1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_context = getApplicationContext();
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public boolean isViewFromObject(View _view, Object _object) {
			return _view == _object;
		}
		
		@Override
		public void destroyItem(ViewGroup _container, int _position, Object _object) {
			_container.removeView((View) _object);
		}
		
		@Override
		public int getItemPosition(Object _object) {
			return super.getItemPosition(_object);
		}
		
		@Override
		public CharSequence getPageTitle(int pos) {
			// Use the Activity Event (onTabLayoutNewTabAdded) in order to use this method
			return "page " + String.valueOf(pos);
		}
		
		@Override
		public Object instantiateItem(ViewGroup _container,  final int _position) {
			View _view = LayoutInflater.from(_context).inflate(R.layout.pages, _container, false);
			
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final LinearLayout linear2 = _view.findViewById(R.id.linear2);
			final LinearLayout linear3 = _view.findViewById(R.id.linear3);
			final LinearLayout linear4 = _view.findViewById(R.id.linear4);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			final TextView textview2 = _view.findViewById(R.id.textview2);
			
			if (pages.get((int)_position).get("page").toString().equals("Session")) {
				linear1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)15, 0xFFFFE082));
				textview2.setText("Start session 》》》");
				textview1.setText("Run complete session (auto timer)");
				imageview1.setImageResource(R.drawable.ic_av_timer_black);
				linear1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
						if (FileUtil.readFile(pref.getString("path", "")).length() > 15) {
							intent.setClass(getApplicationContext(), SessionActivity.class);
							startActivity(intent);
						}
						else {
								Toast toast = Toast.makeText(getApplicationContext(),"No workout", Toast.LENGTH_LONG);
								toast.show();
						}
					}
				});
			}
			if (pages.get((int)_position).get("page").toString().equals("Power")) {
				linear1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)15, 0xFFAED581));
				textview2.setText("Start power steps 》》》");
				textview1.setText("Go through training step by step");
				imageview1.setImageResource(R.drawable.ic_assistant_photo_black);
				linear1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
						intent.setClass(getApplicationContext(), PowerActivity.class);
						startActivity(intent);
					}
				});
			}
			if (pages.get((int)_position).get("page").toString().equals("Logs")) {
				linear1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)15, 0xFF80D8FF));
				textview1.setText("Gym Logs");
				textview2.setText("Store your newest scores");
				imageview1.setImageResource(R.drawable.ic_directions_walk_black);
				linear1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
						intent.setClass(getApplicationContext(), GymlogsActivity.class);
						startActivity(intent);
					}
				});
			}
			if (pages.get((int)_position).get("page").toString().equals("Config")) {
				linear1.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)15, 0xFFE0E0E0));
				textview1.setText("Configuration");
				textview2.setText("Set up your workout");
				imageview1.setImageResource(R.drawable.ic_edit_black);
				linear1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View _view) {
						intent.setClass(getApplicationContext(), ConfigActivity.class);
						startActivity(intent);
					}
				});
			}
			
			_container.addView(_view);
			return _view;
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}