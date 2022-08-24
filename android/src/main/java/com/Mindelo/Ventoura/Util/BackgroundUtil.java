package com.Mindelo.Ventoura.Util;

import com.Mindelo.Ventoura.UI.Activity.CropImageMonitoredActivity;

import android.os.Handler;
import android.util.Log;

/**
 * @author need a explaination what this class is used for ? TODO 
 */
public class BackgroundUtil {

	private BackgroundUtil() {

	}

	private static class BackgroundJob extends
			CropImageMonitoredActivity.LifeCycleAdapter implements Runnable {

		private final CropImageMonitoredActivity mActivity;
		private final Runnable mJob;
		private final Handler mHandler;
		private final Runnable mCleanupRunner = new Runnable() {
			public void run() {
				mActivity.removeLifeCycleListener(BackgroundJob.this);
			}
		};

		public BackgroundJob(CropImageMonitoredActivity activity, Runnable job,
				Handler handler) {
			mActivity = activity;
			mJob = job;
			mActivity.addLifeCycleListener(this);
			mHandler = handler;
		}

		public void run() {
			try {
				mJob.run();
			} finally {
				mHandler.post(mCleanupRunner);
			}
		}

		@Override
		public void onActivityDestroyed(CropImageMonitoredActivity activity) {
			// We get here only when the onDestroyed being called before
			// the mCleanupRunner. So, run it now and remove it from the queue
			Log.i("BackgroundUtil ", "onActivityDestroyed");
			mCleanupRunner.run();
			mHandler.removeCallbacks(mCleanupRunner);
		}

		@Override
		public void onActivityStopped(CropImageMonitoredActivity activity) {

		}

		@Override
		public void onActivityStarted(CropImageMonitoredActivity activity) {

		}
	}

	public static void startBackgroundJob(CropImageMonitoredActivity activity,
			String title, String message, Runnable job, Handler handler) {
		// Make the progress dialog uncancelable, so that we can gurantee
		new Thread(new BackgroundJob(activity, job, handler)).start();
	}
}
