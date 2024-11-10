package com.protonos.ftf;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;
import com.shawnlin.numberpicker.NumberPicker;

public class ConfigActivity extends AppCompatActivity {
	
	public final int REQ_CD_PICKER = 101;
	
	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private HashMap<String, Object> workout = new HashMap<>();
	private double item = 0;
	private double numpick2 = 0;
	private String file_pick = "";
	private double iter = 0;
	
	private ArrayList<HashMap<String, Object>> workouts = new ArrayList<>();
	private ArrayList<String> trainings = new ArrayList<>();
	
	private LinearLayout linear1;
	private TextView textview1;
	private LinearLayout linear2;
	private TextView textview2;
	private LinearLayout linear3;
	private ListView listview1;
	private LinearLayout linear6;
	private LinearLayout linear7;
	private NumberPicker numberpicker1;
	private Spinner spinner1;
	private CheckBox checkbox1;
	private Button button4;
	private Button button3;
	private EditText edittext2;
	private LinearLayout linear5;
	private LinearLayout linear4;
	private NumberPicker numberpicker2;
	private ImageView imageview1;
	private Button button1;
	private Button button2;
	
	private SharedPreferences pref;
	private AlertDialog.Builder dia;
	private Intent picker = new Intent(Intent.ACTION_GET_CONTENT);
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.config);
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
		textview1 = findViewById(R.id.textview1);
		linear2 = findViewById(R.id.linear2);
		textview2 = findViewById(R.id.textview2);
		linear3 = findViewById(R.id.linear3);
		listview1 = findViewById(R.id.listview1);
		linear6 = findViewById(R.id.linear6);
		linear7 = findViewById(R.id.linear7);
		numberpicker1 = findViewById(R.id.numberpicker1);
		spinner1 = findViewById(R.id.spinner1);
		checkbox1 = findViewById(R.id.checkbox1);
		button4 = findViewById(R.id.button4);
		button3 = findViewById(R.id.button3);
		edittext2 = findViewById(R.id.edittext2);
		linear5 = findViewById(R.id.linear5);
		linear4 = findViewById(R.id.linear4);
		numberpicker2 = findViewById(R.id.numberpicker2);
		imageview1 = findViewById(R.id.imageview1);
		button1 = findViewById(R.id.button1);
		button2 = findViewById(R.id.button2);
		pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		dia = new AlertDialog.Builder(this);
		picker.setType("image/*");
		picker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				button2.setVisibility(View.VISIBLE);
				button1.setText("Change");
				item = _position;
				numberpicker2.setValue(Integer.parseInt(workouts.get((int)_position).get("duration").toString()) / 1000);
				edittext2.setText(workouts.get((int)_position).get("name").toString());
				imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(workouts.get((int)_position).get("image").toString(), 1024, 1024));
				file_pick = workouts.get((int)_position).get("image").toString();
				numpick2 = Double.parseDouble(workouts.get((int)_position).get("duration").toString());
			}
		});
		
		listview1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				dia.setTitle("Delete");
				dia.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						workouts.remove((int)(_position));
						FileUtil.writeFile(pref.getString("path", ""), new Gson().toJson(workouts));
						listview1.setAdapter(new Listview1Adapter(workouts));
						((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
					}
				});
				dia.setNegativeButton("No", new DialogInterface.OnClickListener() {
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
				pref.edit().putString("path", "/storage/emulated/0/Documents/FitTheFix/workouts".concat(trainings.get((int)(_position)))).commit();
				workouts = new Gson().fromJson(FileUtil.readFile(pref.getString("path", "")), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
				listview1.setAdapter(new Listview1Adapter(workouts));
				((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> _param1) {
				
			}
		});
		
		checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton _param1, boolean _param2) {
				final boolean _isChecked = _param2;
				if (_isChecked) {
					pref.edit().putString("shuffle", "true").commit();
				}
				else {
					pref.edit().putString("shuffle", "false").commit();
				}
			}
		});
		
		button4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				iter = 0;
				for(int _repeat23 = 0; _repeat23 < (int)(workouts.size()); _repeat23++) {
					if (Double.parseDouble(workouts.get((int)iter).get("duration").toString()) > 1000) {
						workouts.get((int)iter).put("duration", String.valueOf((long)(Double.parseDouble(workouts.get((int)iter).get("duration").toString()) - 1000)));
					}
					iter++;
				}
				FileUtil.writeFile(pref.getString("path", ""), new Gson().toJson(workouts));
				listview1.setAdapter(new Listview1Adapter(workouts));
				((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
			}
		});
		
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				iter = 0;
				for(int _repeat10 = 0; _repeat10 < (int)(workouts.size()); _repeat10++) {
					if (Double.parseDouble(workouts.get((int)iter).get("duration").toString()) < 900000) {
						workouts.get((int)iter).put("duration", String.valueOf((long)(Double.parseDouble(workouts.get((int)iter).get("duration").toString()) + 1000)));
					}
					iter++;
				}
				FileUtil.writeFile(pref.getString("path", ""), new Gson().toJson(workouts));
				listview1.setAdapter(new Listview1Adapter(workouts));
				((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
			}
		});
		
		imageview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				startActivityForResult(picker, REQ_CD_PICKER);
			}
		});
		
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (button1.getText().toString().equals("Change")) {
					if (edittext2.getText().toString().length() > 1) {
						workouts.get((int)item).put("name", edittext2.getText().toString());
						workouts.get((int)item).put("image", file_pick);
						workouts.get((int)item).put("duration", String.valueOf((long)(numpick2)));
						edittext2.setText("");
						imageview1.setImageResource(R.drawable.ic_now_wallpaper_black);
						numberpicker2.setValue(1);
						FileUtil.writeFile(pref.getString("path", ""), new Gson().toJson(workouts));
						listview1.setAdapter(new Listview1Adapter(workouts));
						((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
							Toast toast = Toast.makeText(getApplicationContext(),"Changed", Toast.LENGTH_SHORT);
							toast.show();
						button1.setText("Add");
						button2.setVisibility(View.GONE);
					}
					else {
							Toast toast = Toast.makeText(getApplicationContext(),"Input error", Toast.LENGTH_LONG);
							toast.show();
					}
				}
				else {
					if ((edittext2.getText().toString().length() > 1) && ((file_pick.length() > 5) && (numpick2 > 999))) {
						workout = new HashMap<>();
						workout.put("name", edittext2.getText().toString());
						workout.put("image", file_pick);
						workout.put("duration", String.valueOf((long)(numpick2)));
						workouts.add(workout);
						edittext2.setText("");
						imageview1.setImageResource(R.drawable.ic_now_wallpaper_black);
						numberpicker2.setValue(1);
						FileUtil.writeFile(pref.getString("path", ""), new Gson().toJson(workouts));
						listview1.setAdapter(new Listview1Adapter(workouts));
						((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
							Toast toast = Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT);
							toast.show();
					}
					else {
							Toast toast = Toast.makeText(getApplicationContext(),"Input error", Toast.LENGTH_LONG);
							toast.show();
					}
				}
			}
		});
		
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				button2.setVisibility(View.GONE);
				button1.setText("Add");
				item = 0;
				edittext2.setText("");
				file_pick = "";
				imageview1.setImageResource(R.drawable.ic_now_wallpaper_black);
				numberpicker2.setValue(1);
			}
		});
	}
	
	private void initializeLogic() {
		button2.setVisibility(View.GONE);
		numberpicker1.setValue(Integer.parseInt(pref.getString("rest", "")) / 1000);
		numberpicker1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			    @Override
			    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				        pref.edit().putString("rest", String.valueOf(newVal*1000)).commit();
				    }
		});
		numberpicker2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
			    @Override
			    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				        numpick2 = newVal*1000;
				    }
		});
		if (FileUtil.getFileLength(pref.getString("path", "")) > 8) {
			workouts = new Gson().fromJson(FileUtil.readFile(pref.getString("path", "")), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			listview1.setAdapter(new Listview1Adapter(workouts));
			((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
		}
		if (pref.getString("shuffle", "").equals("true")) {
			checkbox1.setChecked(true);
		}
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		
		switch (_requestCode) {
			case REQ_CD_PICKER:
			if (_resultCode == Activity.RESULT_OK) {
				ArrayList<String> _filePath = new ArrayList<>();
				if (_data != null) {
					if (_data.getClipData() != null) {
						for (int _index = 0; _index < _data.getClipData().getItemCount(); _index++) {
							ClipData.Item _item = _data.getClipData().getItemAt(_index);
							_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _item.getUri()));
						}
					}
					else {
						_filePath.add(FileUtil.convertUriToFilePath(getApplicationContext(), _data.getData()));
					}
				}
				file_pick = _filePath.get((int)(0));
				imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(file_pick, 1024, 1024));
			}
			else {
				
			}
			break;
			default:
			break;
		}
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		trainings.clear();
		FileUtil.listDir("/storage/emulated/0/Documents/FitTheFix/workouts", trainings);
		iter = 0;
		for(int _repeat27 = 0; _repeat27 < (int)(trainings.size()); _repeat27++) {
			trainings.set((int)iter, trainings.get((int)(iter)).replace("/storage/emulated/0/Documents/FitTheFix/workouts", ""));
			iter++;
		}
		spinner1.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, trainings));
		((ArrayAdapter)spinner1.getAdapter()).notifyDataSetChanged();
		iter = 0;
		for(int _repeat14 = 0; _repeat14 < (int)(trainings.size()); _repeat14++) {
			if (pref.getString("path", "").replace("/storage/emulated/0/Documents/FitTheFix/workouts", "").equals(trainings.get((int)(iter)))) {
				spinner1.setSelection((int)(iter));
			}
			iter++;
		}
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
				_view = _inflater.inflate(R.layout.workout, null);
			}
			
			final LinearLayout linear1 = _view.findViewById(R.id.linear1);
			final TextView textview3 = _view.findViewById(R.id.textview3);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			final TextView textview2 = _view.findViewById(R.id.textview2);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			final ImageView imageview2 = _view.findViewById(R.id.imageview2);
			final ImageView imageview3 = _view.findViewById(R.id.imageview3);
			
			textview3.setText(new DecimalFormat("000").format(_position + 1));
			textview1.setText(workouts.get((int)_position).get("name").toString());
			textview2.setText(String.valueOf((long)(Double.parseDouble(workouts.get((int)_position).get("duration").toString()) / 1000)).concat(" Sec"));
			imageview1.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(workouts.get((int)_position).get("image").toString(), 1024, 1024));
			if (_position == (workouts.size() - 1)) {
				imageview3.setVisibility(View.GONE);
			}
			else {
				imageview3.setVisibility(View.VISIBLE);
			}
			if (_position == 0) {
				imageview2.setVisibility(View.GONE);
			}
			else {
				imageview2.setVisibility(View.VISIBLE);
			}
			imageview2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					Collections.swap(workouts, (int)(_position), (int)(_position - 1));
					FileUtil.writeFile(pref.getString("path", ""), new Gson().toJson(workouts));
					notifyDataSetChanged();
				}
			});
			imageview3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View _view) {
					Collections.swap(workouts, (int)(_position), (int)(_position + 1));
					FileUtil.writeFile(pref.getString("path", ""), new Gson().toJson(workouts));
					notifyDataSetChanged();
				}
			});
			
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