/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
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

package com.github.kingargyle.plexappclient.core.services;

import java.util.ArrayList;
import java.util.List;

import com.github.kingargyle.plexapp.model.impl.Directory;
import com.github.kingargyle.plexapp.model.impl.MediaContainer;
import com.github.kingargyle.plexappclient.core.model.CategoryInfo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * Retrieves the available categories for filtering and returns them to the
 * calling service.
 * 
 * @author dcarver
 *
 */
public class MovieCategoryRetrievalIntentService extends
		AbstractPlexRESTIntentService {
	
	private ArrayList<CategoryInfo> categories;
	private String key;
	
	
	public MovieCategoryRetrievalIntentService() {
		super("MovieCategoryRetrievalIntentService");
	}

	/* (non-Javadoc)
	 * @see com.github.kingargyle.plexappclient.core.services.AbstractPlexRESTIntentService#sendMessageResults(android.content.Intent)
	 */
	@Override
	public void sendMessageResults(Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			Messenger messenger = (Messenger) extras.get("MESSENGER");
			Message msg = Message.obtain();
			msg.obj = categories;
			try {
				messenger.send(msg);
			} catch (RemoteException ex) {
				Log.e(getClass().getName(), "Unable to send message", ex);
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		key = intent.getExtras().getString("key");
		populateCategories();
		sendMessageResults(intent);
	}
	
	protected void populateCategories() {
		try {
			MediaContainer mediaContainer = factory.retrieveSections(key);
			List<Directory> dirs = mediaContainer.getDirectories();
			categories = new ArrayList<CategoryInfo>();
			for(Directory dir : dirs) {
				if (!"folder".equals(dir.getKey()) && !"Search...".equals(dir.getTitle())) {
					CategoryInfo category = new CategoryInfo();
					category.setCategory(dir.getKey());
					category.setCategoryDetail(dir.getTitle());
					if (dir.getSecondary() > 0) {
						category.setLevel(dir.getSecondary());
					}
					categories.add(category);
				}
			}
		} catch (Exception e) {
			Log.e(getClass().getName(), e.getMessage(), e);
		}		
	}
	

}
