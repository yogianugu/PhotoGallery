package com.mad.gallery;

/*Homework  4
 * Group Members
 Rajashekar reddy Peta
 800836279
 Rakesh Gopishetty Sudershan
 */
import android.graphics.Bitmap;

public class Image {
	public String name;
	public String getName() {
		return name;
	}
	public Image(String name, Bitmap picture) {
		super();
		this.name = name;
		this.picture = picture;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Bitmap getPicture() {
		return picture;
	}
	public void setPicture(Bitmap picture) {
		this.picture = picture;
	}
	public Bitmap picture;
	
	}