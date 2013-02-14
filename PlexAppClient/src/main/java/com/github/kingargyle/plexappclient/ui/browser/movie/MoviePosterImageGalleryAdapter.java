/**
 * The MIT License (MIT)
 * Copyright (c) 2013 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.kingargyle.plexappclient.ui.browser.movie;

import java.util.List;

import com.github.kingargyle.plexappclient.R;
import com.github.kingargyle.plexappclient.core.model.VideoContentInfo;
import com.github.kingargyle.plexappclient.core.services.MoviesRetrievalIntentService;
import com.github.kingargyle.plexappclient.ui.adapters.AbstractPosterImageGalleryAdapter;
import com.github.kingargyle.plexappclient.ui.views.SerenityPosterImageView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * 
 * @author dcarver
 * 
 */
public class MoviePosterImageGalleryAdapter extends
		AbstractPosterImageGalleryAdapter {

	protected static MoviePosterImageGalleryAdapter notifyAdapter;
	protected static ProgressDialog pd;
	private Handler posterGalleryHandler;

	public MoviePosterImageGalleryAdapter(Context c, String key, String category) {
		super(c, key, category);
		pd = ProgressDialog.show(c, "Movie Retrieval", "Retrieving Movies");
		notifyAdapter = this;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		VideoContentInfo pi = posterList.get(position);
		SerenityPosterImageView mpiv = new SerenityPosterImageView(context, pi);
		if (pi.getPosterURL() != null) {
			mpiv.setTag(imageTagFactory.build(pi.getPosterURL(), context));
		} else {
			mpiv.setTag(imageTagFactory.build(factory.baseURL()
					+ ":/resources/movie-fanart.jpg", context));
		}
		mpiv.setScaleType(ImageView.ScaleType.FIT_XY);
		mpiv.setLayoutParams(new Gallery.LayoutParams(200,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		imageManager.getLoader().load(mpiv);

		return mpiv;
	}

	protected void fetchDataFromService() {
		posterGalleryHandler = new MoviePosterHandler();
		Messenger messenger = new Messenger(posterGalleryHandler);
		Intent intent = new Intent(context, MoviesRetrievalIntentService.class);
		intent.putExtra("MESSENGER", messenger);
		intent.putExtra("key", key);
		intent.putExtra("category", category);
		context.startService(intent);
	}
	
	private static class MoviePosterHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			posterList = (List<VideoContentInfo>) msg.obj;
			Gallery posterGallery = (Gallery) context.findViewById(R.id.moviePosterGallery);
			notifyAdapter.notifyDataSetChanged();
			posterGallery.requestFocus();
			pd.dismiss();
		}

	}

}
