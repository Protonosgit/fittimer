package com.protonos.ftf;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.content.*;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shawnlin.numberpicker.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;

public class PowerActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private double iter = 0;
	
	private ArrayList<HashMap<String, Object>> exercises = new ArrayList<>();
	
	private TextView textview2;
	private TextView textview3;
	private ImageView imageview1;
	private LinearLayout linear1;
	private Button button1;
	private TextView textview4;
	private Button button2;
	
	private TimerTask timerA;
	private SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.power);
		initialize(_savedInstanceState);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
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
		_app_bar = findViewById(R.id._app_bar);
		_coordinator = findViewById(R.id._coordinator);
		_toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		textview2 = findViewById(R.id.textview2);
		textview3 = findViewById(R.id.textview3);
		imageview1 = findViewById(R.id.imageview1);
		linear1 = findViewById(R.id.linear1);
		button1 = findViewById(R.id.button1);
		textview4 = findViewById(R.id.textview4);
		button2 = findViewById(R.id.button2);
		pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!(iter == 0)) {
					iter--;
				}
				textview2.setText(exercises.get((int)iter).get("name").toString());
				textview3.setText(String.valueOf((long)(Double.parseDouble(exercises.get((int)iter).get("duration").toString()) / 1000)).concat(" sec"));
				textview4.setText(String.valueOf((long)(iter + 1)).concat(" / ".concat(String.valueOf((long)(exercises.size())))));
				imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(exercises.get((int)iter).get("image").toString(), 1024, 1024));
			}
		});
		
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!(iter == (exercises.size() - 1))) {
					iter++;
				}
				textview2.setText(exercises.get((int)iter).get("name").toString());
				textview3.setText(String.valueOf((long)(Double.parseDouble(exercises.get((int)iter).get("duration").toString()) / 1000)).concat(" sec"));
				textview4.setText(String.valueOf((long)(iter + 1)).concat(" / ".concat(String.valueOf((long)(exercises.size())))));
				imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(exercises.get((int)iter).get("image").toString(), 1024, 1024));
			}
		});
	}
	
	private void initializeLogic() {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setTitle("FitTheFix - Power");
		exercises = new Gson().fromJson(FileUtil.readFile(pref.getString("path", "")), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		if (pref.getString("shuffle", "").equals("true")) {
			Collections.shuffle(exercises);
		}
		iter = 0;
		textview2.setText(exercises.get((int)iter).get("name").toString());
		textview3.setText(String.valueOf((long)(Double.parseDouble(exercises.get((int)iter).get("duration").toString()) / 1000)).concat(" sec"));
		textview4.setText(String.valueOf((long)(iter + 1)).concat(" / ".concat(String.valueOf((long)(exercises.size())))));
		imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(exercises.get((int)iter).get("image").toString(), 1024, 1024));
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