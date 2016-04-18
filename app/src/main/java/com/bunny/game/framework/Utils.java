package com.bunny.game.framework;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class Utils {

	private static Utils instance = new Utils();

	private Utils() {
		// TODO Auto-generated constructor stub
	}

	public Utils getInstance() {
		return instance;
	}

	public static Bitmap getBitmap(int id, BitmapFactory.Options ops, Context ctx){
		return BitmapFactory.decodeResource(ctx.getResources(), id, ops);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap getBitmap(Resources res, int resId) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;
		// Bitmap bmp = BitmapFactory.decodeResource(res, resId, options);

		return BitmapFactory.decodeResource(res, resId, options);
	}

	// cubic bezier
	public static ArrayList<Point> createSetPoints(Point p0, Point p1,
			Point p2, Point p3) {
		ArrayList<Point> ptArrayList = new ArrayList<Point>();
		float lt = 0.0f;
		int tx, ty;

		while (lt <= 1) {
			tx = (int) (((1 - lt) * (1 - lt) * (1 - lt)) * p0.x + 3 * (1 - lt)
					* (1 - lt) * lt * p1.x + 3 * (1 - lt) * lt * lt * p2.x + (lt
					* lt * lt * p3.x));
			ty = (int) (((1 - lt) * (1 - lt) * (1 - lt)) * p0.y + 3 * (1 - lt)
					* (1 - lt) * lt * p1.y + 3 * (1 - lt) * lt * lt * p2.y + (lt
					* lt * lt * p3.y));

			lt += 0.05f;
			ptArrayList.add(new Point((int) Math.round((double) tx), (int) Math
					.round((double) ty)));
		}

		return ptArrayList;
	}

	// cubic bezier
	public static ArrayList<Point> createSetPoints(Point p0, Point p1,
			Point p2, Point p3, float interval) {
		ArrayList<Point> ptArrayList = new ArrayList<Point>();
		float lt = 0.0f;
		int tx, ty;

		while (lt <= 1) {
			tx = (int) (((1 - lt) * (1 - lt) * (1 - lt)) * p0.x + 3 * (1 - lt)
					* (1 - lt) * lt * p1.x + 3 * (1 - lt) * lt * lt * p2.x + (lt
					* lt * lt * p3.x));
			ty = (int) (((1 - lt) * (1 - lt) * (1 - lt)) * p0.y + 3 * (1 - lt)
					* (1 - lt) * lt * p1.y + 3 * (1 - lt) * lt * lt * p2.y + (lt
					* lt * lt * p3.y));

			lt += interval;
			ptArrayList.add(new Point((int) Math.round((double) tx), (int) Math
					.round((double) ty)));
		}

		return ptArrayList;
	}

	// linear interpolation :)
	public static ArrayList<Point> createSetPoints(Point p0, Point p1) {
		ArrayList<Point> ptArrayList = new ArrayList<Point>();
		float lt = 0.0f;
		int tx, ty;

		while (lt <= 1) {
			ty = (int) ((1 - lt) * p0.y + lt * p1.y);
			tx = (int) ((1 - lt) * p0.x + lt * p1.x);

			lt += 0.05f;
			ptArrayList.add(new Point((int) Math.round((double) tx), (int) Math
					.round((double) ty)));
		}

		return ptArrayList;
	}

}
