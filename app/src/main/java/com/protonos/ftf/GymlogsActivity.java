package com.protonos.ftf;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;
import com.shawnlin.numberpicker.NumberPicker;

public class GymlogsActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private double np1 = 0;
	private double np2 = 0;
	private double np3 = 0;
	private HashMap<String, Object> addlog = new HashMap<>();
	private double pid_calc = 0;
	private double pdiv_calc = 0;
	private double time_calc = 0;
	private double edit_pos = 0;
	private HashMap<String, Object> history = new HashMap<>();
	private String tmp = "";
	
	private ArrayList<String> progress_types = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> log = new ArrayList<>();
	private ArrayList<String> backlog = new ArrayList<>();
	
	private LinearLayout linear1;
	private ListView listview1;
	private TextView textview1;
	private LinearLayout linear3;
	private LinearLayout linear2;
	private LinearLayout linear4;
	private LinearLayout linear5;
	private EditText edittext2;
	private Spinner spinner1;
	private NumberPicker numberpicker1;
	private TextView sep1;
	private NumberPicker numberpicker2;
	private TextView sep2;
	private NumberPicker numberpicker3;
	private TextView sep3;
	private Button button1;
	private Button button2;
	
	private SharedPreferences pref;
	private Calendar calendar = Calendar.getInstance();
	private Calendar timecalc = Calendar.getInstance();
	private AlertDialog.Builder dia;
	private TimerTask delay;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.gymlogs);
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
		linear1 = findViewById(R.id.linear1);
		listview1 = findViewById(R.id.listview1);
		textview1 = findViewById(R.id.textview1);
		linear3 = findViewById(R.id.linear3);
		linear2 = findViewById(R.id.linear2);
		linear4 = findViewById(R.id.linear4);
		linear5 = findViewById(R.id.linear5);
		edittext2 = findViewById(R.id.edittext2);
		spinner1 = findViewById(R.id.spinner1);
		numberpicker1 = findViewById(R.id.numberpicker1);
		sep1 = findViewById(R.id.sep1);
		numberpicker2 = findViewById(R.id.numberpicker2);
		sep2 = findViewById(R.id.sep2);
		numberpicker3 = findViewById(R.id.numberpicker3);
		sep3 = findViewById(R.id.sep3);
		button1 = findViewById(R.id.button1);
		button2 = findViewById(R.id.button2);
		pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		dia = new AlertDialog.Builder(this);
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				edit_pos = _position;
				spinner1.setSelection((int)(Double.parseDouble(log.get((int)_position).get("type").toString())));
				edittext2.setEnabled(false);
				spinner1.setEnabled(false);
				button1.setText("Update");
				edittext2.setText(log.get((int)_position).get("name").toString());
				button2.setVisibility(View.VISIBLE);
				delay = new TimerTask() {
					@Override
					public void run() {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (log.get((int)_position).get("type").toString().equals("0")) {
									numberpicker1.setValue(Integer.parseInt(log.get((int)_position).get("pdiv").toString()));
									numberpicker2.setValue(Integer.parseInt(log.get((int)_position).get("pid").toString()) / Integer.parseInt(log.get((int)_position).get("pdiv").toString()));
									
									np1 = Integer.parseInt(log.get((int)_position).get("pdiv").toString());
									np2 = Integer.parseInt(log.get((int)_position).get("pid").toString()) / Integer.parseInt(log.get((int)_position).get("pdiv").toString());
								}
								if (log.get((int)_position).get("type").toString().equals("1")) {
									time_calc = Double.parseDouble(log.get((int)_position).get("pid").toString());
									numberpicker1.setValue( (int) (time_calc / 60) / 60);
									np1 = (time_calc / 60) / 60;
									time_calc = time_calc - ((Integer.parseInt(String.valueOf((long)((time_calc / 60) / 60))) * 60) * 60);
									numberpicker2.setValue( (int) time_calc / 60);
									np2 = time_calc / 60;
									time_calc = time_calc - (Integer.parseInt(String.valueOf((long)(time_calc / 60))) * 60);
									numberpicker3.setValue( (int) time_calc);
									np3 = time_calc;
								}
								if (log.get((int)_position).get("type").toString().equals("2")) {
									numberpicker1.setValue(Integer.parseInt(String.valueOf((long)(Double.parseDouble(log.get((int)_position).get("pid").toString()) / 1000))));
									numberpicker2.setValue(Integer.parseInt(String.valueOf((long)((Double.parseDouble(log.get((int)_position).get("pid").toString()) % 1000) / 100))));
									
									np1 = Integer.parseInt(String.valueOf((long)(Double.parseDouble(log.get((int)_position).get("pid").toString()) / 1000)));
									np2 = Integer.parseInt(String.valueOf((long)((Double.parseDouble(log.get((int)_position).get("pid").toString()) % 1000) / 100)));
								}
								if (log.get((int)_position).get("type").toString().equals("3")) {
									numberpicker1.setValue(Integer.parseInt(log.get((int)_position).get("pid").toString()));
									np1 = Integer.parseInt(log.get((int)_position).get("pid").toString());
								}
							}
						});
					}
				};
				_timer.schedule(delay, (int)(100));
			}
		});
		
		listview1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				dia.setTitle("Delete ?");
				dia.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						log.remove((int)(_position));
						FileUtil.writeFile("/storage/emulated/0/Documents/FitTheFix/gymlog.json", new Gson().toJson(log));
						listview1.setAdapter(new Listview1Adapter(log));
						((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
					}
				});
				dia.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				dia.create().show();
				return true;
			}
		});
		
		spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				if (_position == 0) {
					numberpicker1.setVisibility(View.VISIBLE);
					numberpicker2.setVisibility(View.VISIBLE);
					numberpicker3.setVisibility(View.GONE);
					numberpicker1.setMaxValue(99);
					numberpicker1.setMinValue(1);
					numberpicker2.setMaxValue(99);
					numberpicker2.setMinValue(1);
					
					numberpicker1.setValue(1);
					numberpicker2.setValue(1);
					sep1.setText("|");
					sep2.setText("");
					sep3.setText("Sets/Reps");
				}
				if (_position == 1) {
					numberpicker1.setVisibility(View.VISIBLE);
					numberpicker2.setVisibility(View.VISIBLE);
					numberpicker3.setVisibility(View.VISIBLE);
					numberpicker1.setMaxValue(23);
					numberpicker1.setMinValue(0);
					numberpicker2.setMaxValue(59);
					numberpicker2.setMinValue(0);
					numberpicker3.setMaxValue(59);
					numberpicker3.setMinValue(0);
					
					numberpicker1.setValue(0);
					numberpicker2.setValue(0);
					numberpicker3.setValue(0);
					sep1.setText(":");
					sep2.setText(":");
					sep3.setText("h/m/s");
				}
				if (_position == 2) {
					numberpicker1.setVisibility(View.VISIBLE);
					numberpicker2.setVisibility(View.VISIBLE);
					numberpicker3.setVisibility(View.GONE);
					numberpicker1.setMaxValue(999);
					numberpicker1.setMinValue(0);
					numberpicker2.setMaxValue(9);
					numberpicker2.setMinValue(0);
					
					numberpicker1.setValue(0);
					numberpicker2.setValue(0);
					sep1.setText(",");
					sep2.setText("");
					sep3.setText("Kg");
				}
				if (_position == 3) {
					numberpicker1.setVisibility(View.VISIBLE);
					numberpicker2.setVisibility(View.GONE);
					numberpicker3.setVisibility(View.GONE);
					numberpicker1.setMaxValue(100);
					numberpicker1.setMinValue(1);
					numberpicker1.setValue(1);
					sep1.setText("");
					sep2.setText("");
					sep3.setText("%");
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> _param1) {
				
			}
		});
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (edittext2.getText().toString().equals("")) {
					((EditText)edittext2).setError("Can't be empty");
				}
				else {
					if (spinner1.getSelectedItemPosition() == 0) {
						pdiv_calc = np1;
						pid_calc = np1 * np2;
						numberpicker1.setValue(1);
						numberpicker2.setValue(1);
						np1 = 1;
						np2 = 1;
						np3 = 0;
					}
					if (spinner1.getSelectedItemPosition() == 1) {
						pdiv_calc = 0;
						pid_calc = ((np1 * 60) * 60) + ((np2 * 60) + np3);
						numberpicker1.setValue(0);
						numberpicker2.setValue(0);
						numberpicker3.setValue(0);
						np1 = 0;
						np2 = 0;
						np3 = 0;
					}
					if (spinner1.getSelectedItemPosition() == 2) {
						pdiv_calc = 0;
						pid_calc = (np1 * 1000) + (np2 * 100);
						numberpicker1.setValue(0);
						numberpicker2.setValue(0);
						np1 = 0;
						np2 = 0;
						np3 = 0;
					}
					if (spinner1.getSelectedItemPosition() == 3) {
						pdiv_calc = 100;
						pid_calc = np1;
						numberpicker1.setValue(1);
						np1 = 1;
						np2 = 0;
						np3 = 0;
					}
					calendar = Calendar.getInstance();
					addlog = new HashMap<>();
					backlog.clear();
					if (button1.getText().toString().equals("Update")) {
						backlog = new Gson().fromJson(log.get((int)edit_pos).get("history").toString(), new TypeToken<ArrayList<String>>(){}.getType());
						log.get((int)edit_pos).put("pid", String.valueOf((long)(pid_calc)));
						log.get((int)edit_pos).put("pdiv", String.valueOf((long)(pdiv_calc)));
						log.get((int)edit_pos).put("date", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(calendar.getTime()));
						backlog.add(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(calendar.getTime()).concat("/".concat(String.valueOf((long)(pid_calc)).concat("/".concat(String.valueOf((long)(pdiv_calc)))))));
						log.get((int)edit_pos).put("history", new Gson().toJson(backlog));
							Toast toast = Toast.makeText(getApplicationContext(),"Updated", Toast.LENGTH_SHORT);
							toast.show();
					}
					else {
						addlog.put("name", edittext2.getText().toString());
						addlog.put("date", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(calendar.getTime()));
						addlog.put("type", String.valueOf((long)(spinner1.getSelectedItemPosition())));
						addlog.put("pid", String.valueOf((long)(pid_calc)));
						addlog.put("pdiv", String.valueOf((long)(pdiv_calc)));
						backlog.add(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(calendar.getTime()).concat("/".concat(String.valueOf((long)(pid_calc)).concat("/".concat(String.valueOf((long)(pdiv_calc)))))));
						addlog.put("history", new Gson().toJson(backlog));
						log.add(addlog);
							Toast toast = Toast.makeText(getApplicationContext(),"Added", Toast.LENGTH_SHORT);
							toast.show();
					}
					FileUtil.writeFile("/storage/emulated/0/Documents/FitTheFix/gymlog.json", new Gson().toJson(log));
					log = new Gson().fromJson(FileUtil.readFile("/storage/emulated/0/Documents/FitTheFix/gymlog.json"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
					listview1.setAdapter(new Listview1Adapter(log));
					((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
					button1.setText("Add new");
					button2.setVisibility(View.GONE);
					edittext2.setText("");
					edittext2.setEnabled(true);
					spinner1.setEnabled(true);
				}
			}
		});
		
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				spinner1.setSelection((int)(0));
				button1.setText("Add new");
				button2.setVisibility(View.GONE);
				edittext2.setEnabled(true);
				spinner1.setEnabled(true);
			}
		});
	}
	
	private void initializeLogic() {
		setTitle("FitTheFix - Gym Logs");
		progress_types.add("Repetitions");
		progress_types.add("Time");
		progress_types.add("Weight");
		progress_types.add("Perfection");
		spinner1.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, progress_types));
		((ArrayAdapter)spinner1.getAdapter()).notifyDataSetChanged();
		sep1.setText("");
		sep2.setText("");
		sep3.setText("");
		button2.setVisibility(View.GONE);
		numberpicker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			    @Override
			    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				        np1 = newVal;
				    }
		});
		numberpicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			    @Override
			    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				        np2 = newVal;
				    }
		});
		numberpicker3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			    @Override
			    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				        np3 = newVal;
				    }
		});
		
		np1 = 1;
		np2 = 1;
		np3 = 0;
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		if (FileUtil.readFile("/storage/emulated/0/Documents/FitTheFix/gymlog.json").length() > 10) {
			log = new Gson().fromJson(FileUtil.readFile("/storage/emulated/0/Documents/FitTheFix/gymlog.json"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		}
		listview1.setAdapter(new Listview1Adapter(log));
		((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
	}
	public class Listview1Adapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.gymlog, null);
			}
			
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			final TextView textview2 = _view.findViewById(R.id.textview2);
			
			textview1.setText(log.get((int)_position).get("name").toString());
			if (log.get((int)_position).get("type").toString().equals("0")) {
				textview2.setText(String.valueOf((long)(Double.parseDouble(_data.get((int)_position).get("pdiv").toString()))).concat(" | ".concat(String.valueOf((long)(Double.parseDouble(_data.get((int)_position).get("pid").toString()) / Double.parseDouble(_data.get((int)_position).get("pdiv").toString()))))));
			}
			if (log.get((int)_position).get("type").toString().equals("1")) {
				time_calc = Double.parseDouble(_data.get((int)_position).get("pid").toString());
				textview2.setText(String.valueOf((long)((time_calc / 60) / 60)));
				time_calc = time_calc - ((Integer.parseInt(String.valueOf((long)((time_calc / 60) / 60))) * 60) * 60);
				textview2.setText(textview2.getText().toString().concat(" : "));
				textview2.setText(textview2.getText().toString().concat(String.valueOf((long)(time_calc / 60))));
				time_calc = time_calc - (Integer.parseInt(String.valueOf((long)(time_calc / 60))) * 60);
				textview2.setText(textview2.getText().toString().concat(" : "));
				textview2.setText(textview2.getText().toString().concat(String.valueOf((long)(time_calc))));
			}
			if (log.get((int)_position).get("type").toString().equals("2")) {
				textview2.setText(String.valueOf(Double.parseDouble(_data.get((int)_position).get("pid").toString()) / 1000).concat(" kg"));
			}
			if (log.get((int)_position).get("type").toString().equals("3")) {
				textview2.setText(_data.get((int)_position).get("pid").toString().concat(" %"));
			}
			
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