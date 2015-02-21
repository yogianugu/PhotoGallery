package com.mad.gallery;

/*Homework  4
 * Group Members
 Rajashekar reddy Peta
 800836279
 Rakesh Gopishetty Sudershan
 */
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ImageViewerActivity extends Activity {
	ProgressDialog progressDialog;
	OnSwipeImageListener onSwipeTouchListener;
	List<String> Imgs = new LinkedList<String>();;
	ImageView iv;
	Bitmap image = null;
	public int index;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_viewer);
		setTitle("Image Viewer");
		Bundle b = this.getIntent().getExtras();
		Imgs = (List<String>) b.get("Pictures");
		index = getIntent().getExtras().getInt("ImageUrl");
		iv = (ImageView) findViewById(R.id.imageView1);
		DownloadImage(index);
		findViewById(R.id.Back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		findViewById(R.id.Next).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				index++;
				if (index >= Imgs.size())
					index = 0;
				DownloadImage(index);
			}
		});
		findViewById(R.id.Previous).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				index--;
				if (index <= 0)
					index = Imgs.size() - 1;
				DownloadImage(index);
			}
		});
	}

	public void DownloadImage(Integer idx) {
		Log.d("INDEX", String.valueOf(idx));
		new AsyncGetImage(ImageViewerActivity.this).execute(idx);
	}
	public class AsyncGetImage extends AsyncTask<Integer, Void, Bitmap> {
		private InputStream inputStream;
		Context c;

		public AsyncGetImage(Context applicationContext) {
			this.c = applicationContext;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(ImageViewerActivity.this, "",
					"Loading...");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(false);
		}
		@Override
		protected Bitmap doInBackground(Integer... params) {
			Integer imgUrl = params[0];
			try {
				URL url = new URL(Imgs.get(imgUrl).trim());
				inputStream = url.openStream();
				image = BitmapFactory.decodeStream(inputStream);
				inputStream.close();
				return image;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Bitmap result) {
			progressDialog.dismiss();
			if (result == null) {
				result = ((BitmapDrawable) getResources().getDrawable(
						R.drawable.not_found)).getBitmap();
			}
			iv.setImageBitmap(result);
			iv.setOnTouchListener(new OnSwipeImageListener(c) {
				@Override
				public void onSwipeLeft() {
					index--;
					if (index <= 0)
						index = Imgs.size() - 1;
					DownloadImage(index);
				}
				@Override
				public void onSwipeRight() {
					index++;
					if (index >= Imgs.size())
						index = 0;
					DownloadImage(index);
				}
			});
		}
	}
}
