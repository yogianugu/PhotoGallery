package com.mad.gallery;

/*Homework  4
 * Group Members
 Rajashekar reddy Peta
 800836279
 Rakesh Gopishetty Sudershan
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
public class MainActivity extends Activity {
	Map<String, String> map;
	List<String> t_comn_map, p_comn_map, t_uncc_map, p_uncc_map, t_ifest_map,
			p_ifest_map, t_sports_map, p_sports_map;
	Map<String, Bitmap> imagemap;
	Resources res;
	ProgressDialog progressDialog;
	InputStream in = null;
	GridView myGrid;
	Bitmap myBitmap;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setTitle("Uncc Photo Gallery");
		findViewById(R.id.Exit_Thread).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});
		res = getResources();
		map = new LinkedHashMap<String, String>();
		t_comn_map = new LinkedList<String>();
		p_comn_map = new LinkedList<String>();
		t_uncc_map = new LinkedList<String>();
		p_uncc_map = new LinkedList<String>();
		t_ifest_map = new LinkedList<String>();
		p_ifest_map = new LinkedList<String>();
		t_sports_map = new LinkedList<String>();
		p_sports_map = new LinkedList<String>();
		map.put(res.getString(R.string.label_ifest),
				res.getString(R.string.ifest_main_image));
		map.put(res.getString(R.string.label_uncc),
				res.getString(R.string.uncc_main_image));
		map.put(res.getString(R.string.label_sports),
				res.getString(R.string.football_main_image));
		map.put(res.getString(R.string.label_commencement),
				res.getString(R.string.commencement_main_image));
		String[] t_comn, p_comn, t_uncc, p_uncc, t_ifest, p_ifest, t_sports, p_sports;
		t_comn = res.getStringArray(R.array.commencement_thumbs);
		p_comn = res.getStringArray(R.array.commencement_photos);
		t_uncc = res.getStringArray(R.array.uncc_thumbs);
		p_uncc = res.getStringArray(R.array.uncc_photos);
		t_ifest = res.getStringArray(R.array.ifest_thumbs);
		p_ifest = res.getStringArray(R.array.ifest_photos);
		p_sports = res.getStringArray(R.array.football_photos);
		t_sports = res.getStringArray(R.array.football_thumbs);
		for (int i = 0; i < p_sports.length; i++) {
			t_sports_map.add(t_sports[i]);
			p_sports_map.add(p_sports[i]);
		}
		for (int i = 0; i < p_comn.length; i++) {
			p_comn_map.add(p_comn[i]);
			t_comn_map.add(t_comn[i]);
		}
		for (int i = 0; i < p_ifest.length; i++) {
			p_ifest_map.add(p_ifest[i]);
			t_ifest_map.add(t_ifest[i]);
		}
		for (int i = 0; i < p_uncc.length; i++) {
			t_uncc_map.add(t_uncc[i]);
			p_uncc_map.add(p_uncc[i]);
		}
		progressDialog = ProgressDialog.show(MainActivity.this, "",
				"Loading...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);
		new DoWork(this.getBaseContext()).execute(map);
	}

	class DoWork extends AsyncTask<Map<String, String>, Void, ArrayList<Image>> {

		Context c;

		public DoWork(Context applicationContext) {
			this.c = applicationContext;
		}
		@Override
		protected ArrayList<Image> doInBackground(Map<String, String>... params) {
			Map<String, String> url = params[0];

			ArrayList<Image> images = new ArrayList<Image>();
			for (Entry<String, String> entry : url.entrySet()) {
				URL uri = null;
				try {
					uri = new URL(entry.getValue().toString().trim());
					HttpURLConnection connection = (HttpURLConnection) uri
							.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					myBitmap = BitmapFactory.decodeStream(input);
					images.add(new Image(entry.getKey(), myBitmap));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return images;
		}

		@Override
		protected void onPostExecute(ArrayList<Image> result) {
			super.onPostExecute(result);
			myGrid = (GridView) findViewById(R.id.gridView1);
			for (Image i : result) {
				Log.d("DEMO", i.getPicture().toString());
			}
			myGrid.setAdapter(new PicsAdapter(c, result));
			progressDialog.dismiss();
			myGrid.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent i = new Intent(getBaseContext(),
							GalleryActivity.class);
					Bundle bundle = new Bundle();
					switch (arg2) {
					case 0:
						bundle.putSerializable("Photo",
								(Serializable) p_ifest_map);
						bundle.putSerializable("Thumb",
								(Serializable) t_ifest_map);
						i.putExtras(bundle);
						startActivity(i);
						break;
					case 1:
						bundle.putSerializable("Photo",
								(Serializable) p_uncc_map);
						bundle.putSerializable("Thumb",
								(Serializable) t_uncc_map);
						i.putExtras(bundle);
						startActivity(i);
						break;
					case 2:
						bundle.putSerializable("Photo",
								(Serializable) p_sports_map);
						bundle.putSerializable("Thumb",
								(Serializable) t_sports_map);
						i.putExtras(bundle);
						startActivity(i);
						break;
					case 3:
						bundle.putSerializable("Photo",
								(Serializable) p_comn_map);
						bundle.putSerializable("Thumb",
								(Serializable) t_comn_map);
						i.putExtras(bundle);
						startActivity(i);
						break;
					}
				}
			});

		}

	}

}
