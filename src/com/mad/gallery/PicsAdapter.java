package com.mad.gallery;

/*Homework  4
 * Group Members
 Rajashekar reddy Peta
 800836279
 Rakesh Gopishetty Sudershan
 */
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class PicsAdapter extends BaseAdapter {

	ArrayList<Image> imageList;
	@SuppressWarnings("unused")
	private LayoutInflater mInflater;
	Context context;

	public PicsAdapter(Context c2, ArrayList<Image> result) {
		this.context = c2;
		this.mInflater = LayoutInflater.from(context);
		this.imageList = result;
	}

	@Override
	public int getCount() {
		return imageList.size();
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View convertView, ViewGroup viewGroup) {
		ImageTextHolder holder = null;
		View myView = convertView;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			myView = inflater.inflate(R.layout.single_item, viewGroup, false);
			holder = new ImageTextHolder();
			holder.name = (TextView) myView.findViewById(R.id.textView1);
			holder.picture = (ImageView) myView.findViewById(R.id.imageView1);
			myView.setTag(holder);
		} else {
			holder = (ImageTextHolder) myView.getTag();
		}
		Image image = imageList.get(i);
		holder.picture.setImageBitmap(image.getPicture());
		holder.name.setText(image.getName());
		return myView;
	}
	static class ImageTextHolder {
		TextView name;
		ImageView picture;
	}

}