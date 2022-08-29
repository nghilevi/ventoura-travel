package com.Mindelo.Ventoura.UI.Activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import com.Mindelo.Ventoura.Constant.BroadcastConstant;
import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.IMConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Entity.Country;
import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.Entity.Traveller;
import com.Mindelo.Ventoura.Entity.SmallHeadImage;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.ICountryService;
import com.Mindelo.Ventoura.Ghost.IService.IGalleryService;
import com.Mindelo.Ventoura.Ghost.IService.ITravellerService;
import com.Mindelo.Ventoura.Ghost.Service.CountryService;
import com.Mindelo.Ventoura.Ghost.Service.GalleryService;
import com.Mindelo.Ventoura.Ghost.Service.TravellerService;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerProfile;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Adapter.ProfileEditPagerAdapter;
import com.Mindelo.Ventoura.UI.View.BottomPopupMenuAction;
import com.Mindelo.Ventoura.UI.View.DraggableGridView;
import com.Mindelo.Ventoura.UI.View.PopupActionItem;
import com.Mindelo.Ventoura.UI.View.DraggableGridView.OnRearrangeListener;
import com.Mindelo.Ventoura.UI.View.RoundedImageView;
import com.Mindelo.Ventoura.UI.ViewHolder.ProfileEditTabHolder;
import com.Mindelo.Ventoura.Util.BitmapUtil;
import com.Mindelo.Ventoura.Util.ImageUtil;
import com.nineoldandroids.view.ViewHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TravellerProfileEditActivity extends FragmentActivity implements
		ProfileEditTabHolder, ViewPager.OnPageChangeListener {

	private static final String TAG = "TravellerProfileEditActivity";

	/*
	 * progress dialog
	 */
	private ProgressDialog progressDialog;

	/*
	 * utilities
	 */
	private JSONTravellerProfile travellerProfile;
	private SharedPreferences sharedPre;
	private ICountryService countryService;
	private ITravellerService travellerService;
	private IGalleryService galleryService;
	private Country selectedCountry;

	/*
	 * common content field views
	 */
	private Button btn_cancel, btn_save;
	private TextView tv_about, tv_country;

	/*
	 * gallery management views
	 */
	private RoundedImageView bigHeadImage;
	private ProfileEditPagerAdapter pagerAdapter;
	private DraggableGridView imageGVSelector;
	private ViewPager viewPager;
	private View vHeaderSelector;

	private List<SmallHeadImage> smallHeadImages = new ArrayList<>();
	private static final int NUMOFIMAGESELECTOR = 4;
	// this variable is used for updating the gallery images, the first element is the portal image
	private GalleryImage[] galleryImages = new GalleryImage[NUMOFIMAGESELECTOR + 1]; 
	private List<Long> deletedImageIds = new ArrayList<Long>();
	private long originalPortalId = -1;

	/*
	 * children layouts
	 */
	public static View vContent;

	private int minHeaderHeight;
	private int headerHeight;
	private int minHeaderTranslation;

	private BottomPopupMenuAction bottomMenuAction;
	private static final int ID_BOTTOM_MENU_SET_PROFILE_IMAGE = 1;
	private static final int ID_BOTTOM_MENU_DELETE = 2;

	
	/*
	 * global variables 
	 */
	private int touchIndex = -1;

	/*
	 * request code for staring a new activity and back
	 */
	public static final int SELECT_PHOTO = 0x1111;
	public static final int CROP_PHOTO = 0x1112;
	public static final int SELECT_COUNTRY = 0x1113;
	public static final int DESCRIPTION_INPUT_CONTENT = 0x1114;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_traveller_profile_edit);

		/*
		 * initialize utilities
		 */
		sharedPre = getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);
		travellerProfile = (JSONTravellerProfile) getIntent()
				.getSerializableExtra(VentouraConstant.INTENT_TRAVELLER_PROFILE);
		countryService = new CountryService(this);
		travellerService = new TravellerService(this);
		galleryService = new GalleryService(this);

		/*
		 * the layout of traveller_profile_edit contains two big part: content
		 * and the gallery management
		 */
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		vContent = inflater.inflate(
				R.layout.activity_traveller_profile_edit_content, null);

		minHeaderHeight = getResources().getDimensionPixelSize(
				R.dimen.min_header_height);
		headerHeight = getResources().getDimensionPixelSize(
				R.dimen.profile_head_height);
		minHeaderTranslation = -minHeaderHeight;

		/*
		 * gallery image management view initialize
		 */
		vHeaderSelector = findViewById(R.id.profile_head_img_layout);
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setOffscreenPageLimit(1);

		initPopupBottomMenu(); // deal with image delete and set as profile

		imageGVSelector = (DraggableGridView) this
				.findViewById(R.id.draggable_grid_view);
		imageGVSelector.setOnRearrangeListener(new OnRearrangeListener() {
			@Override
			public void onRearrange(int oldIndex, int newIndex) {
				SmallHeadImage moveImage = smallHeadImages.remove(oldIndex);
				if (oldIndex < newIndex)
					smallHeadImages.add(newIndex, moveImage);
				else
					smallHeadImages.add(newIndex, moveImage);
			}
		});

		/*
		 * initialize gallery image views data
		 */
		for(int i=0; i<=NUMOFIMAGESELECTOR; i++){
			galleryImages[i] = new GalleryImage();
		}
		
		bigHeadImage = (RoundedImageView) this
				.findViewById(R.id.traveller_rounded_profile_image);
		
		setProfileImageViews();
	

		imageGVSelector.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i(TAG, "onItemClick position = " + position);
				touchIndex = position;
			}
		});

		imageGVSelector.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i(TAG, "OnClickListener");
				if (touchIndex != -1) {
					SmallHeadImage shi = smallHeadImages.get(touchIndex);
					// already set the image
					if (shi.isAdd() == true) {
						bottomMenuAction.show(v);
					} else {// not set the image
						pickImageFromGallery();
					}
				}
			}
		});

		pagerAdapter = new ProfileEditPagerAdapter(getSupportFragmentManager(),
				UserRole.TRAVELLER);
		pagerAdapter.setTabHolderScrollingContent(this);
		viewPager.setAdapter(pagerAdapter);

		/*
		 * initialize common content views
		 */
		tv_about = (TextView) vContent
				.findViewById(R.id.traveller_profile_edit_description_tv);
		tv_country = (TextView) vContent
				.findViewById(R.id.traveller_profile_edit_country_tv);

		tv_about.setOnClickListener(new CommonContextOnClickListener());
		tv_country.setOnClickListener(new CommonContextOnClickListener());

		setTextFieldViews();


		/*
		 * menu buttons
		 */
		btn_cancel = (Button) this.findViewById(R.id.btn_back);
		btn_save = (Button) this.findViewById(R.id.btn_action);
		btn_cancel.setText("Cancel");
		btn_save.setText("Save");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		// select the photo from the gallery
		case SELECT_PHOTO:
			if (resultCode == RESULT_OK && data != null) {
				Uri selectedImage = data.getData();
				if (checkImageSize(selectedImage)) {
					Intent intent = new Intent(
							TravellerProfileEditActivity.this,
							CropImageActivity.class);
					intent.setData(selectedImage);
					Bundle extras = new Bundle();
					extras.putBoolean("return-data", true);
					intent.putExtras(extras);
					startActivityForResult(intent, CROP_PHOTO);
				} else {
					Toast.makeText(getApplicationContext(),
							"the image too small", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		// crop the photo to the right size
		case CROP_PHOTO:
			if (resultCode == RESULT_OK && data != null) {

				// refresh the view
				Bitmap b = data.getParcelableExtra("data");
				SmallHeadImage shi = smallHeadImages.get(touchIndex);
				shi.getView().setImageBitmap(b);
				shi.setHeadImage(b);
				shi.setAdd(true);

				// mark as added
				if (touchIndex + 1 <= NUMOFIMAGESELECTOR) {
					galleryImages[touchIndex + 1].setImageContent(b);
				}

			} else if (resultCode == RESULT_CANCELED) {
				Log.i(TAG, "CROP_PHOTO RESULT_CANCELED");
			}
			break;
		case DESCRIPTION_INPUT_CONTENT:
			if (resultCode == RESULT_OK && data != null) {
				tv_about.setText(data.getStringExtra("inputContent"));
			}
			break;
		case SELECT_COUNTRY:
			if (resultCode == RESULT_OK && data != null) {
				selectedCountry = (Country) data
						.getSerializableExtra("CountrySelected");
				if (selectedCountry != null) {
					tv_country.setText(selectedCountry.getCountryName());
				}
			}
			break;
		}

	}

	private void initPopupBottomMenu() {
		PopupActionItem setItem = new PopupActionItem(
				ID_BOTTOM_MENU_SET_PROFILE_IMAGE, "Set As Profile");
		PopupActionItem deleteItem = new PopupActionItem(ID_BOTTOM_MENU_DELETE,
				"Delete Image");

		bottomMenuAction = new BottomPopupMenuAction(this);
		bottomMenuAction.addActionItem(setItem);
		bottomMenuAction.addActionItem(deleteItem);

		bottomMenuAction
				.setOnActionItemClickListener(new BottomPopupMenuAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(BottomPopupMenuAction source,
							int pos, int actionId) {
						SmallHeadImage shi = smallHeadImages.get(touchIndex);
						RoundedImageView view = shi.getView();

						Bitmap b = null;
						switch (actionId) {
						case ID_BOTTOM_MENU_DELETE:

							// restore to its original state
							shi.setAdd(false);
							b = BitmapUtil.ReadBitmapById(
									TravellerProfileEditActivity.this,
									R.drawable.btn_circle_add_new_ventoura_color);
							shi.setHeadImage(b);
							view.setImageBitmap(b);

							// mark this image as delete
							if (touchIndex + 1 <= NUMOFIMAGESELECTOR
									&& galleryImages[touchIndex + 1].galleryId != -1) {
								galleryImages[touchIndex + 1]
										.setImageContent(null);
								deletedImageIds.add(galleryImages[touchIndex + 1].galleryId);
								galleryImages[touchIndex + 1].setGalleryId(-1);
							}

							break;
						case ID_BOTTOM_MENU_SET_PROFILE_IMAGE:
							/*
							 * swap the head image with the small images
							 */
							Bitmap bitMapTemp = ((BitmapDrawable) bigHeadImage
									.getDrawable()).getBitmap();
							b = shi.getHeadImage();
							bigHeadImage.setImageBitmap(b);
							shi.setHeadImage(bitMapTemp);
							view.setImageBitmap(bitMapTemp);
							bitMapTemp = null;
							/*
							 * mark the new image as portal
							 */
							if (touchIndex + 1 <= NUMOFIMAGESELECTOR) {
								GalleryImage galleryImageTemp = galleryImages[0];
								galleryImages[0] = galleryImages[touchIndex + 1];
								galleryImages[touchIndex + 1] = galleryImageTemp;
							}
							break;
						}
					}
				});

		bottomMenuAction
				.setOnDismissListener(new BottomPopupMenuAction.OnDismissListener() {
					@Override
					public void onDismiss() {
					}
				});
	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}

	/*
	 * The back button click to go to profile page
	 */
	public void btnBackOnClick(View view) {
		
		finish();
		onBackPressed();
	}

	public void btnActionOnClick(View view) {
		UpdateTravellerProfileTask updateTravellerProfileTask = new UpdateTravellerProfileTask();
		updateTravellerProfileTask.execute();
	}

	private class CommonContextOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			case R.id.traveller_profile_edit_description_tv:
				intent = new Intent(TravellerProfileEditActivity.this,
						PureInputActivity.class);
				intent.putExtra(
						PureInputActivity.TOTAL_ALLOWED_NUMBER_CHARACTERS, 200);
				intent.putExtra(PureInputActivity.ORIGINAL_CONTENT, tv_about
						.getText().toString());
				startActivityForResult(intent, DESCRIPTION_INPUT_CONTENT);
				break;
			case R.id.traveller_profile_edit_country_tv:
				intent = new Intent(TravellerProfileEditActivity.this,
						CountrySelectorActivity.class);
				startActivityForResult(intent, SELECT_COUNTRY);
				break;
			}

		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// nothing
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// nothing
	}

	@Override
	public void onPageSelected(int position) {
		SparseArrayCompat<ProfileEditTabHolder> scrollTabHolders = pagerAdapter
				.getScrollTabHolders();
		ProfileEditTabHolder currentHolder = scrollTabHolders.valueAt(position);
		currentHolder
				.adjustScroll((int) (vHeaderSelector.getHeight() + ViewHelper
						.getTranslationY(vHeaderSelector)));
	}

	@Override
	public void adjustScroll(int scrollHeight) {
		// do nothing
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount, int pagePosition) {
		if (viewPager.getCurrentItem() == pagePosition) {
			int scrollY = getScrollY(view);
			ViewHelper.setTranslationY(vHeaderSelector,
					Math.max(-scrollY, minHeaderTranslation));
		}
	}

	public int getScrollY(AbsListView view) {
		View c = view.getChildAt(0);
		if (c == null) {
			return 0;
		}

		int firstVisiblePosition = view.getFirstVisiblePosition();
		int top = c.getTop();

		int mheaderHeight = 0;
		if (firstVisiblePosition >= 1) {
			mheaderHeight = headerHeight;
		}

		return -top + firstVisiblePosition * c.getHeight() + mheaderHeight;
	}

	/**
	 * Network tasks for update a new user's profile
	 */
	private class UpdateTravellerProfileTask extends
			AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(
					TravellerProfileEditActivity.this);
			progressDialog.setTitle("Updating profile...");
			progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean successUpdateAllField = true;
			/*
			 * update traveller common fields
			 */
			Traveller traveller = new Traveller();
			traveller.setId(travellerProfile.getId());
			traveller.setTextBiography(tv_about.getText().toString());

			if (selectedCountry != null) {
				traveller.setCountry(selectedCountry.getId());
			}
			if (!travellerService.updateTravellerProfile(traveller)) {
				successUpdateAllField = false;
			}
			
			/*
			 * upload the updates to remote server gallery management
			 */
			if (deletedImageIds.size() > 0 && !galleryService.deleteBatchTravellerGallery(travellerProfile.getId(), deletedImageIds)) {
				successUpdateAllField = false;
			}
			if(galleryImages[0].getGalleryId() == -1 && galleryImages[0].getImageContent() != null){
				long galleryId = galleryService.uploadSingleTravellerGallery(travellerProfile.getId(), BitmapUtil
								.bitMapToByteArray(galleryImages[0]
										.getImageContent()));
				if(galleryId != -1){
					if(galleryService.setTravellerPortalImage(travellerProfile.getId(), galleryId) != true){
						successUpdateAllField = false;
					}
				}else{
					successUpdateAllField = false;
				}
			}else if(galleryImages[0].getGalleryId() != originalPortalId){
				if(galleryService.setTravellerPortalImage(travellerProfile.getId(), galleryImages[0].getGalleryId()) != true){
					successUpdateAllField = false;
				}
			}
			for (int i = 1; i <= NUMOFIMAGESELECTOR; i++) {
				if (galleryImages[i].getGalleryId() == -1
						&& galleryImages[i].getImageContent() != null) {
					galleryService.uploadSingleTravellerGallery(
							travellerProfile.getId(), BitmapUtil
									.bitMapToByteArray(galleryImages[i]
											.getImageContent()));
				}
			}
			
			return successUpdateAllField;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dismissProgressDialog();
			if (result != true) {
				Toast.makeText(TravellerProfileEditActivity.this,
						"Error happened when updating!", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(TravellerProfileEditActivity.this,
						"Update profile succeeded!", Toast.LENGTH_SHORT).show();
				/*
				 * broad cast to update profile page
				 */
				Intent intent = new Intent(
						BroadcastConstant.USER_PROFILE_IMAGES_UPDATED_ACTION);
				sendBroadcast(intent);
				
				// go back to profile page
				btnBackOnClick(null);
			}
		}
	}

	/*****************************************************************/
	/*************** Helping functions ***************/
	/*****************************************************************/

	/**
	 * this method is used to set the four profile images
	 */
	private void setProfileImageViews() {
		
		for (int i = 0; i < NUMOFIMAGESELECTOR; i++) {
			RoundedImageView view = (RoundedImageView) LayoutInflater.from(
					getApplicationContext()).inflate(
					R.layout.activity_profile_edit_round_image, null);
			SmallHeadImage smallHeadImage = new SmallHeadImage();
			// Empoty place holder
			smallHeadImage.setAdd(false);
			smallHeadImage.setView(view);
			smallHeadImages.add(smallHeadImage);
			imageGVSelector.addView(view);
		}

		List<ImageProfile> imageProfiles = travellerService.getTravellerGalleryImagesFromDB(travellerProfile.getId());
		for(ImageProfile image : imageProfiles){
			if(image.isPortal()){
				originalPortalId = image.getId();
				galleryImages[0].setGalleryId(image.getId());
				galleryImages[0].setImageContent(BitmapUtil.byteArrayToBitMap(image.getImageContent(),
						ConfigurationConstant.NORMAL_USER_PORTAL_IMAGE_WIDTH,
						ConfigurationConstant.NORMAL_USER_PORTAL_IMAGE_HEIGHT));
				bigHeadImage.setImageBitmap(galleryImages[0].getImageContent());
				break;
			}
		}

		int galleryIndex = 1;
		for(ImageProfile image : imageProfiles){
			if(!image.isPortal()){
				Bitmap bitmap_galleryImage_small = BitmapUtil.byteArrayToBitMap(image.getImageContent(),
						ConfigurationConstant.SMALL_USER_PORTAL_IMAGE_WIDTH,
						ConfigurationConstant.SMALL_USER_PORTAL_IMAGE_HEIGHT);
				// set the small image views
				SmallHeadImage shi = smallHeadImages.get(galleryIndex - 1);
				shi.getView().setImageBitmap(bitmap_galleryImage_small);
				shi.setHeadImage(bitmap_galleryImage_small);
				shi.setAdd(true);

				// manage the gallery images
				galleryImages[galleryIndex].setGalleryId(image.getId());
				galleryImages[galleryIndex]
						.setImageContent(bitmap_galleryImage_small);
				galleryIndex++;		
			}
		}
	}

	/**
	 * ready to go to galley to pick an image
	 */
	private void pickImageFromGallery() {
		Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
		pickImageIntent.setType("image/*");
		startActivityForResult(pickImageIntent, SELECT_PHOTO);

	}

	private void setTextFieldViews() {
		if (travellerProfile != null) {

			if (travellerProfile.getTextBiography() != null
					&& travellerProfile.getTextBiography().length() > 0) {
				tv_about.setText(travellerProfile.getTextBiography() + "");
			}

			Country country = countryService.getCountryById(travellerProfile
					.getCountry());
			if (country != null) {
				tv_country.setText(country.getCountryName());
			}

		}
	}

	private boolean checkImageSize(Uri selectedImage) {
		String imgPath = ImageUtil.getImageRealPathFromURI(selectedImage,
				getContentResolver());
		File f = new File(imgPath);
		long length = f.length();
		if (ConfigurationConstant.MINCROPIMAGESIZE > length)
			return false;
		else
			return true;
	}

	/*
	 * helper to handle the dismiss progress dialog
	 */
	private void dismissProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		progressDialog = null;
	}

	@Data
	private class GalleryImage {
		long galleryId;
		Bitmap imageContent;

		public GalleryImage() {
			galleryId = -1;
			imageContent = null;
		}
	}
}
