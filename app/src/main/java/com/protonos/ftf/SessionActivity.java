package com.protonos.ftf;

import android.Manifest;
import android.animation.*;
import android.animation.Animator;
import android.animation.ObjectAnimator;
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
import android.media.SoundPool;
import android.net.*;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.webkit.*;
import android.widget.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shawnlin.numberpicker.*;
import java.io.*;
import java.text.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.*;
import org.json.*;

public class SessionActivity extends AppCompatActivity {
	
	private Timer _timer = new Timer();
	
	private double pip = 0;
	private double beep = 0;
	private double finish = 0;
	private double set = 0;
	private boolean controll_visible = false;
	private double tick = 0;
	private double duration = 0;
	private boolean session_running = false;
	private boolean session_paused = false;
	
	private ArrayList<HashMap<String, Object>> execises = new ArrayList<>();
	
	private LinearLayout body;
	private LinearLayout topframe;
	private LinearLayout backframe;
	private LinearLayout playmenu;
	private ProgressBar progressbar1;
	private LinearLayout proggresshead;
	private TextView name;
	private TextView time;
	private TextView sets;
	private ImageView preview;
	private ImageView exit;
	private ImageView pause;
	private ImageView play;
	private ImageView skipB;
	private ImageView skipF;
	
	private TimerTask timerA;
	private SharedPreferences pref;
	private AlertDialog.Builder dialog;
	private SoundPool sound;
	private Calendar cal = Calendar.getInstance();
	private TimerTask Tick;
	private ObjectAnimator anima = new ObjectAnimator();
	private TimerTask Delay;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.session);
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
		body = findViewById(R.id.body);
		topframe = findViewById(R.id.topframe);
		backframe = findViewById(R.id.backframe);
		playmenu = findViewById(R.id.playmenu);
		progressbar1 = findViewById(R.id.progressbar1);
		proggresshead = findViewById(R.id.proggresshead);
		name = findViewById(R.id.name);
		time = findViewById(R.id.time);
		sets = findViewById(R.id.sets);
		preview = findViewById(R.id.preview);
		exit = findViewById(R.id.exit);
		pause = findViewById(R.id.pause);
		play = findViewById(R.id.play);
		skipB = findViewById(R.id.skipB);
		skipF = findViewById(R.id.skipF);
		pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
		dialog = new AlertDialog.Builder(this);
		
		preview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (controll_visible) {
					controll_visible = false;
					anima.setTarget(playmenu);
					anima.setPropertyName("translationY");
					anima.setFloatValues((float)(0));
					anima.setFloatValues((float)(playmenu.getTranslationY()), (float)(0));
					anima.setDuration((int)(600));
					anima.start();
				}
				else {
					controll_visible = true;
					anima.setTarget(playmenu);
					anima.setPropertyName("translationY");
					anima.setFloatValues((float)(0));
					anima.setFloatValues((float)(playmenu.getTranslationY()), (float)(-100));
					anima.setDuration((int)(600));
					anima.start();
				}
			}
		});
		
		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (session_paused || session_running) {
					dialog.setTitle("Exit");
					dialog.setMessage("Proggress will be lost");
					dialog.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							if (session_running) {
								Tick.cancel();
								timerA.cancel();
							}
							finish();
						}
					});
					dialog.setNegativeButton("Stay", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface _dialog, int _which) {
							
						}
					});
					dialog.create().show();
				}
				else {
					finish();
				}
			}
		});
		
		pause.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				session_paused = true;
				session_running = false;
				preview.setAlpha((float)(0.6d));
				body.setBackgroundColor(0xFFBDBDBD);
				time.setText("Paused");
				play.setVisibility(View.VISIBLE);
				pause.setVisibility(View.GONE);
				progressbar1.setVisibility(View.INVISIBLE);
				Tick.cancel();
				timerA.cancel();
			}
		});
		
		play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				session_paused = false;
				session_running = true;
				_start_PreRun();
				pause.setVisibility(View.VISIBLE);
				skipB.setVisibility(View.VISIBLE);
				skipF.setVisibility(View.VISIBLE);
				progressbar1.setVisibility(View.INVISIBLE);
				play.setVisibility(View.GONE);
				preview.setAlpha((float)(1));
			}
		});
		
		skipB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!(set == 0)) {
					if (session_running) {
						pause.performClick();
					}
					set--;
					preview.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(execises.get((int)set).get("image").toString(), 1024, 1024));
					name.setText(execises.get((int)set).get("name").toString());
					sets.setText("Set: ".concat(String.valueOf((long)(set + 1))).concat(" / ".concat(String.valueOf((long)(execises.size())))));
				}
			}
		});
		
		skipF.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if (!((set + 1) == execises.size())) {
					if (session_running) {
						pause.performClick();
					}
					set++;
					preview.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(execises.get((int)set).get("image").toString(), 1024, 1024));
					name.setText(execises.get((int)set).get("name").toString());
					sets.setText("Set: ".concat(String.valueOf((long)(set + 1))).concat(" / ".concat(String.valueOf((long)(execises.size())))));
				}
			}
		});
		
		anima.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator _param1) {
				
			}
			
			@Override
			public void onAnimationEnd(Animator _param1) {
				
			}
			
			@Override
			public void onAnimationCancel(Animator _param1) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animator _param1) {
				
			}
		});
	}
	
	private void initializeLogic() {
		setTitle("FitTheFix - Session");
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		sound = new SoundPool((int)(3), AudioManager.STREAM_MUSIC, 0);
		beep = sound.load(getApplicationContext(), R.raw.beep, 1);
		pip = sound.load(getApplicationContext(), R.raw.blip, 1);
		finish = sound.load(getApplicationContext(), R.raw.finish, 1);
		controll_visible = false;
		session_running = false;
		session_paused = false;
		skipB.setVisibility(View.GONE);
		skipF.setVisibility(View.GONE);
		pause.setVisibility(View.GONE);
		progressbar1.setVisibility(View.INVISIBLE);
		set = 0;
		execises = new Gson().fromJson(FileUtil.readFile(pref.getString("path", "")), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		if (pref.getString("shuffle", "").equals("true")) {
			Collections.shuffle(execises);
		}
		ProgressBar progressBar = findViewById(R.id.progressbar1);
		progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar));
	}
	
	@Override
	public void onBackPressed() {
		exit.performClick();
	}
	public void _do_Tick(final double _phase) {
		if (((cal.getTimeInMillis() / 1000) < 4) && !((cal.getTimeInMillis() / 1000) == 0)) {
			pip = sound.play((int)(2), 1.0f, 1.0f, 1, (int)(0), 1.0f);
		}
		if (_phase == 0) {
			time.setText(new SimpleDateFormat("mm:ss").format(cal.getTime()).concat(" Remaining"));
			progressbar1.setProgress((int)tick);
			progressbar1.setMax((int)duration / 1000);
		}
		if (_phase == 1) {
			time.setText(new SimpleDateFormat("mm:ss").format(cal.getTime()).concat(" Rest"));
		}
		if (_phase == 2) {
			time.setText(new SimpleDateFormat("mm:ss").format(cal.getTime()).concat(" Get ready"));
		}
		cal.add(Calendar.SECOND, (int)(-1));
	}
	
	
	public void _end_session() {
		finish = sound.play((int)(3), 1.0f, 1.0f, 1, (int)(0), 1.0f);
		session_running = false;
		session_paused = false;
		set = 0;
		body.setBackgroundColor(0xFFFFFFFF);
		pause.setVisibility(View.VISIBLE);
		play.setVisibility(View.VISIBLE);
		progressbar1.setVisibility(View.INVISIBLE);
		skipB.setVisibility(View.GONE);
		skipF.setVisibility(View.GONE);
		pause.setVisibility(View.GONE);
		name.setText("");
		time.setText("Session Completed");
		sets.setText("✅️");
		preview.setImageResource(R.drawable.hedgie);
	}
	
	
	public void _start_Pause() {
		beep = sound.play((int)(1), 1.0f, 1.0f, 1, (int)(0), 1.0f);
		preview.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(execises.get((int)set).get("image").toString(), 1024, 1024));
		name.setText(execises.get((int)set).get("name").toString());
		body.setBackgroundColor(0xFF29B6F6);
		progressbar1.setVisibility(View.INVISIBLE);
		duration = Integer.parseInt(pref.getString("rest", ""));
		tick = 0;
		cal.setTimeInMillis((long)(duration));
		Tick = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tick++;
						_do_Tick(1);
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(Tick, (int)(0), (int)(1000));
		timerA = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Tick.cancel();
						time.setText("Go on !");
						_start_Set();
					}
				});
			}
		};
		_timer.schedule(timerA, (int)(duration));
	}
	
	
	public void _start_Set() {
		beep = sound.play((int)(1), 1.0f, 1.0f, 1, (int)(0), 1.0f);
		body.setBackgroundColor(0xFFFFD54F);
		progressbar1.setVisibility(View.VISIBLE);
		preview.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(execises.get((int)set).get("image").toString(), 1024, 1024));
		name.setText(execises.get((int)set).get("name").toString());
		tick = 0;
		duration = Double.parseDouble(execises.get((int)set).get("duration").toString());
		cal.setTimeInMillis((long)(duration));
		sets.setText("Set: ".concat(String.valueOf((long)(set + 1))).concat(" / ".concat(String.valueOf((long)(execises.size())))));
		Tick = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						_do_Tick(0);
						tick++;
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(Tick, (int)(0), (int)(1000));
		timerA = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Tick.cancel();
						if (execises.size() == (set + 1)) {
							_end_session();
						}
						else {
							set++;
							sets.setText("Set: ".concat(String.valueOf((long)(set + 1))).concat(" / ".concat(String.valueOf((long)(execises.size())))));
							time.setText("Rest phase");
							_start_Pause();
						}
					}
				});
			}
		};
		_timer.schedule(timerA, (int)(duration));
	}
	
	
	public void _start_PreRun() {
		body.setBackgroundColor(0xFF9CCC65);
		preview.setImageBitmap(FileUtil.decodeSampleBitmapFromPath(execises.get((int)set).get("image").toString(), 1024, 1024));
		name.setText(execises.get((int)set).get("name").toString());
		sets.setText("🕑");
		cal.setTimeInMillis((long)(6000));
		Tick = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						_do_Tick(2);
					}
				});
			}
		};
		_timer.scheduleAtFixedRate(Tick, (int)(0), (int)(1000));
		timerA = new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Tick.cancel();
						_start_Set();
					}
				});
			}
		};
		_timer.schedule(timerA, (int)(6000));
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