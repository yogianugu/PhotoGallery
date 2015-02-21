package com.mad.gallery;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

@SuppressLint("UseSparseArrays")
public class GalleryActivity extends Activity {
	Bitmap myBitmap;
	List<String> a1,a0;
	ProgressDialog progressDialog;
	@SuppressLint("UseSparseArrays")
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		setTitle("Thumbnails Gallery");
		Bundle b = this.getIntent().getExtras();
		a0=new LinkedList<String>();
		a0=(List<String>) b.get("Thumb");
		 a1=new LinkedList<String>();
		a1=(List<String>) b.get("Photo");
		 progressDialog = ProgressDialog.show(GalleryActivity.this, "",
				"Loading...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);
		
		new DoWork().execute(a0);
		//Toast.makeText(this.getBaseContext(), a0.get(0), 100000).show();
		//Toast.makeText(this.getBaseContext(), a1.get(10), 100000).show();
	}

	class DoWork extends AsyncTask<List<String>, Void, ArrayList<Bitmap>>{

		@Override
		protected ArrayList<Bitmap> doInBackground(
				List<String>... params) {
			List<String> url = params[0];

			ArrayList<Bitmap> images = new ArrayList<Bitmap>();
			for (String entry : url) {
				URL uri = null;
				try {
					uri = new URL(entry.toString().trim());
					HttpURLConnection connection = (HttpURLConnection) uri
							.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					myBitmap = BitmapFactory.decodeStream(input);
					images.add( myBitmap);

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return images;		
			}
		@Override
		protected void onPostExecute(ArrayList<Bitmap> result) {
			super.onPostExecute(result);

		GridView	myGrid = (GridView) findViewById(R.id.gridView1);

		myGrid.setAdapter(new ImageAdapter(result));
			progressDialog.dismiss();
			
myGrid.setOnItemClickListener(new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		//String img_url= a1.get(arg2);
		
		Intent i = new Intent(getBaseContext(),
				ImageViewerActivity.class);
		Bundle b1=new Bundle();
		b1.putSerializable("Pictures",
				(Serializable) a1);
		i.putExtra("ImageUrl", arg2);
		i.putExtras(b1);
		startActivity(i);
	}
});
		}
		public class ImageAdapter extends BaseAdapter {
			private ArrayList<Bitmap> list;

			public ImageAdapter(ArrayList<Bitmap> list) {
				// TODO Auto-generated constructor stub
				this.list = list;
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return list.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ImageView iv;
				if(convertView != null) {
					iv = (ImageView) convertView;
				} else {
					iv = new ImageView(getBaseContext());
					iv.setLayoutParams(new GridView.LayoutParams(200,200));
					iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
					iv.setPadding(1,1,1,1);
				}
				iv.setImageBitmap(list.get(position));
				return iv;
			}

		}
	}

}
