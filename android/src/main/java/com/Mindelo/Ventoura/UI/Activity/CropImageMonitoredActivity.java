package com.Mindelo.Ventoura.UI.Activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

public class CropImageMonitoredActivity extends Activity {

	private final ArrayList<LifeCycleListener> mListeners = new ArrayList<LifeCycleListener>();

	public static interface LifeCycleListener {
		public void onActivityCreated(CropImageMonitoredActivity activity);

		public void onActivityDestroyed(CropImageMonitoredActivity activity);

		public void onActivityPaused(CropImageMonitoredActivity activity);

		public void onActivityResumed(CropImageMonitoredActivity activity);

		public void onActivityStarted(CropImageMonitoredActivity activity);

		public void onActivityStopped(CropImageMonitoredActivity activity);
	}

	public static class LifeCycleAdapter implements LifeCycleListener {
		public void onActivityCreated(CropImageMonitoredActivity activity) {
		}

		public void onActivityDestroyed(CropImageMonitoredActivity activity) {
		}

		public void onActivityPaused(CropImageMonitoredActivity activity) {
		}

		public void onActivityResumed(CropImageMonitoredActivity activity) {
		}

		public void onActivityStarted(CropImageMonitoredActivity activity) {
		}

		public void onActivityStopped(CropImageMonitoredActivity activity) {
		}
	}

	public void addLifeCycleListener(LifeCycleListener listener) {
		if (mListeners.contains(listener))
			return;
		mListeners.add(listener);
	}

	public void removeLifeCycleListener(LifeCycleListener listener) {
		mListeners.remove(listener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		for (LifeCycleListener listener : mListeners) {
			listener.onActivityCreated(this);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		for (LifeCycleListener listener : mListeners) {
			listener.onActivityDestroyed(this);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		for (LifeCycleListener listener : mListeners) {
			listener.onActivityStarted(this);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		for (LifeCycleListener listener : mListeners) {
			listener.onActivityStopped(this);
		}
	}
}
