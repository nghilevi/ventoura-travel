package com.Mindelo.Ventoura.UI.Activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import com.Mindelo.Ventoura.Constant.BroadcastConstant;
import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Entity.City;
import com.Mindelo.Ventoura.Entity.Guide;
import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.Entity.SmallHeadImage;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.ICityService;
import com.Mindelo.Ventoura.Ghost.IService.IGalleryService;
import com.Mindelo.Ventoura.Ghost.IService.IGuideService;
import com.Mindelo.Ventoura.Ghost.Service.CityService;
import com.Mindelo.Ventoura.Ghost.Service.GalleryService;
import com.Mindelo.Ventoura.Ghost.Service.GuideService;
import com.Mindelo.Ventoura.JSONEntity.JSONGuideAttraction;
import com.Mindelo.Ventoura.JSONEntity.JSONGuideProfile;
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

public class GuideProfileEditActivity extends FragmentActivity implements
		ProfileEditTabHolder, ViewPager.OnPageChangeListener {

	private static final String TAG = "GuideProfileEditActivity";

	/*
	 * progress dialog
	 */
	private ProgressDialog progressDialog;

	/*
	 * utilities
	 */
	private JSONGuideProfile guideProfile;
	private SharedPreferences sharedPre;
	private ICityService cityService;
	private IGuideService guideService;
	private IGalleryService galleryService;
	private City selectedCity;
	private List<String> newAttractions = new ArrayList<String>();
	List<JSONGuideAttraction> jsonAttractions = new ArrayList<JSONGuideAttraction>();

	/*
	 * common content field views
	 */
	private Button btn_cancel, btn_save;
	private TextView tv_about, tv_tour_price, tv_tour_length, tv_city,
			tv_attractions, tv_tour_type;

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
	// this variable is used for updating the gallery images, the first element
	// is the portal image
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
	public static final int SELECT_TOUR_TYPE = 0x1113;
	public static final int SELECT_CITY = 0x1114;
	public static final int DESCRIPTION_INPUT_CONTENT = 0x1115;
	public static final int TOUR_LENGTH_INPUT_CONTENT = 0x1116;
	public static final int EDIT_ATTRACTIONS = 0x1117;
	public static final int SET_TOUR_PRICE = 0x1118;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_guide_profile_edit);

		/*
		 * initialize utilities
		 */
		sharedPre = getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);
		guideProfile = (JSONGuideProfile) getIntent().getSerializableExtra(VentouraConstant.INTENT_GUIDE_PROFILE);
		galleryService = new GalleryService(this);
		cityService = new CityService(this);
		guideService = new GuideService(this);

		/*
		 * the layout of guide_profile_edit contains two big part: content and
		 * the gallery management
		 */
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		vContent = inflater.inflate(
				R.layout.activity_guide_profile_edit_content, null);

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
		for (int i = 0; i <= NUMOFIMAGESELECTOR; i++) {
			galleryImages[i] = new GalleryImage();
		}

		bigHeadImage = (RoundedImageView) this
				.findViewById(R.id.guide_rounded_profile_image);

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
				UserRole.GUIDE);
		pagerAdapter.setTabHolderScrollingContent(this);
		viewPager.setAdapter(pagerAdapter);

		/*
		 * initialize common content views
		 */
		tv_tour_type = (TextView) vContent
				.findViewById(R.id.guide_profile_edit_tour_type_tv);
		tv_about = (TextView) vContent
				.findViewById(R.id.guide_profile_edit_description_tv);
		tv_tour_price = (TextView) vContent
				.findViewById(R.id.guide_profile_edit_tour_price_tv);
		tv_tour_length = (TextView) vContent
				.findViewById(R.id.guide_profile_edit_tour_length_tv);
		tv_city = (TextView) vContent
				.findViewById(R.id.guide_profile_edit_city_tv);
		tv_attractions = (TextView) vContent
				.findViewById(R.id.guide_profile_edit_attractions_tv);

		tv_tour_type.setOnClickListener(new CommonContextOnClickListener());
		tv_about.setOnClickListener(new CommonContextOnClickListener());
		tv_tour_price.setOnClickListener(new CommonContextOnClickListener());
		tv_tour_length.setOnClickListener(new CommonContextOnClickListener());
		tv_city.setOnClickListener(new CommonContextOnClickListener());
		tv_attractions.setOnClickListener(new CommonContextOnClickListener());

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
					Intent intent = new Intent(GuideProfileEditActivity.this,
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
		case SELECT_TOUR_TYPE:
			if (resultCode == RESULT_OK && data != null) {
				tv_tour_type.setText(data.getStringExtra("tourTypeSelected"));
			}
			break;
		case TOUR_LENGTH_INPUT_CONTENT:
			if (resultCode == RESULT_OK && data != null) {
				tv_tour_length.setText(data.getStringExtra("inputContent"));
			}
			break;
		case DESCRIPTION_INPUT_CONTENT:
			if (resultCode == RESULT_OK && data != null) {
				tv_about.setText(data.getStringExtra("inputContent"));
			}
			break;
		case SELECT_CITY:
			if (resultCode == RESULT_OK && data != null) {
				selectedCity = (City) data.getSerializableExtra("CitySelected");
				if (selectedCity != null) {
					tv_city.setText(selectedCity.getCityName());
				}
			}
			break;
		case EDIT_ATTRACTIONS:
			if (resultCode == RESULT_OK && data != null) {
				newAttractions = data.getStringArrayListExtra("newAttractions");
				String attractions = "";
				for (String attraction : newAttractions) {
					attractions += attraction + "\n";
				}
				tv_attractions.setText(attractions);
			}
			break;
		case SET_TOUR_PRICE:
			if (resultCode == RESULT_OK && data != null) {
				tv_tour_length.setText(data.getIntExtra(
						VentouraConstant.INTENT_LOCAL_SET_TOUR_PRICE, 0) + "");
			}
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
							b = BitmapUtil
									.ReadBitmapById(
											GuideProfileEditActivity.this,
											R.drawable.btn_circle_add_new_ventoura_color);
							shi.setHeadImage(b);
							view.setImageBitmap(b);

							// mark this image as delete
							if (touchIndex + 1 <= NUMOFIMAGESELECTOR
									&& galleryImages[touchIndex + 1].galleryId != -1) {
								galleryImages[touchIndex + 1]
										.setImageContent(null);
								deletedImageIds
										.add(galleryImages[touchIndex + 1].galleryId);
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
		UpdateGuideProfileTask updateGuideProfileTask = new UpdateGuideProfileTask();
		updateGuideProfileTask.execute();
	}

	private class CommonContextOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent;
			switch (v.getId()) {
			case R.id.guide_profile_edit_description_tv:
				intent = new Intent(GuideProfileEditActivity.this,
						PureInputActivity.class);
				intent.putExtra(
						PureInputActivity.TOTAL_ALLOWED_NUMBER_CHARACTERS, 200);
				intent.putExtra(PureInputActivity.ORIGINAL_CONTENT, tv_about
						.getText().toString());
				startActivityForResult(intent, DESCRIPTION_INPUT_CONTENT);
				break;
			case R.id.guide_profile_edit_city_tv:
				intent = new Intent(GuideProfileEditActivity.this,
						CitySelectorActivity.class);
				startActivityForResult(intent, SELECT_CITY);
				break;
			case R.id.guide_profile_edit_tour_length_tv:
				intent = new Intent(GuideProfileEditActivity.this,
						PureInputActivity.class);
				intent.putExtra(
						PureInputActivity.TOTAL_ALLOWED_NUMBER_CHARACTERS, 10);
				intent.putExtra(PureInputActivity.ORIGINAL_CONTENT,
						tv_tour_length.getText().toString());

				startActivityForResult(intent, TOUR_LENGTH_INPUT_CONTENT);
				break;
			case R.id.guide_profile_edit_tour_type_tv:
				intent = new Intent(GuideProfileEditActivity.this,
						GuideProfileEditTourTypeSelectorActivity.class);
				startActivityForResult(intent, SELECT_TOUR_TYPE);
				break;
			case R.id.guide_profile_edit_attractions_tv:
				intent = new Intent(getApplicationContext(),
						GuideProfileEditAttractionsTagActivity.class);
				ArrayList<String> attractionsArray = new ArrayList<String>();
				for (JSONGuideAttraction attraction : jsonAttractions) {
					attractionsArray.add(attraction.getAttractionName());
				}
				intent.putStringArrayListExtra("attractionsArray",
						attractionsArray);
				startActivityForResult(intent, EDIT_ATTRACTIONS);

				break;
			case R.id.guide_profile_edit_tour_price_tv:
				intent = new Intent(GuideProfileEditActivity.this,
						GuideSetTourPriceActivity.class);
				intent.putExtra(
						VentouraConstant.INTENT_LOCAL_SET_TOUR_PAYMENT_METHOD,
						guideProfile.getPaymentMethod().getNumVal());
				startActivityForResult(intent, SELECT_TOUR_TYPE);
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
	private class UpdateGuideProfileTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(GuideProfileEditActivity.this);
			progressDialog.setTitle("Updating profile...");
			progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			boolean successUpdateAllField = true;
			/*
			 * update guide common fields
			 */
			Guide guide = new Guide();
			guide.setId(guideProfile.getId());
			guide.setTextBiography(tv_about.getText().toString());

			guide.setTourLength(tv_tour_length.getText().toString());
			guide.setTourType(tv_tour_type.getText().toString());
			guide.setTourPrice(Double.valueOf(tv_tour_price.getText()
					.toString()));
			guide.setMoneyType(-1); // TODO fix money type
			if (selectedCity != null) {
				guide.setCity(selectedCity.getId());
				guide.setCountry(selectedCity.getCountryId());
			}
			if (!guideService.updateGuideProfile(guide)) {
				successUpdateAllField = false;
			}

			/*
			 * handle deleted guide attractions
			 */
			List<Long> deletedAttractions = new ArrayList<Long>();
			for (JSONGuideAttraction jsonAttraction : jsonAttractions) {
				boolean deletedFlag = true;
				for (String newAttraction : newAttractions) {
					if (jsonAttraction.getAttractionName()
							.equals(newAttraction)) {
						deletedFlag = false;
						break;
					}
				}
				if (deletedFlag == true) {
					deletedAttractions.add(jsonAttraction.getId());
				}
			}
			if (deletedAttractions.size() > 0
					&& !guideService
							.batchDeleteGuideAttraction(deletedAttractions)) {
				successUpdateAllField = false;
			}

			/*
			 * handle new added guide attractions
			 */
			List<String> addedAttractions = new ArrayList<String>();
			for (String newAttractionName : newAttractions) {
				boolean addedFlag = true;
				for (JSONGuideAttraction jsonAttraction : jsonAttractions) {
					if (jsonAttraction.getAttractionName().equals(
							newAttractionName)) {
						addedFlag = false;
						break;
					}
				}
				if (addedFlag == true) {
					addedAttractions.add(newAttractionName);
				}
			}
			if (addedAttractions.size() > 0
					&& !guideService.batchUploadGuideAttraction(
							guideProfile.getId(), addedAttractions)) {
				successUpdateAllField = false;
			}

			/*
			 * upload the updates to remote server gallery management
			 */
			if (deletedImageIds.size() > 0
					&& !galleryService.deleteBatchGuideGallery(
							guideProfile.getId(), deletedImageIds)) {
				successUpdateAllField = false;
			}
			if (galleryImages[0].getGalleryId() == -1
					&& galleryImages[0].getImageContent() != null) {
				long galleryId = galleryService.uploadSingleGuideGallery(
						guideProfile.getId(), BitmapUtil
								.bitMapToByteArray(galleryImages[0]
										.getImageContent()));
				if (galleryId != -1) {
					if (galleryService.setGuidePortalImage(
							guideProfile.getId(), galleryId) != true) {
						successUpdateAllField = false;
					}
				} else {
					successUpdateAllField = false;
				}
			} else if (galleryImages[0].getGalleryId() != originalPortalId) {
				if (galleryService.setGuidePortalImage(guideProfile.getId(),
						galleryImages[0].getGalleryId()) != true) {
					successUpdateAllField = false;
				}
			}
			for (int i = 1; i <= NUMOFIMAGESELECTOR; i++) {
				if (galleryImages[i].getGalleryId() == -1
						&& galleryImages[i].getImageContent() != null) {
					galleryService.uploadSingleGuideGallery(guideProfile
							.getId(), BitmapUtil
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
				Toast.makeText(GuideProfileEditActivity.this,
						"Error happened when updating!", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(GuideProfileEditActivity.this,
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

		List<ImageProfile> imageProfiles = guideService
				.getGuideGalleryImagesFromDB(guideProfile.getId());
		for (ImageProfile image : imageProfiles) {
			if (image.isPortal()) {
				originalPortalId = image.getId();
				galleryImages[0].setGalleryId(image.getId());
				galleryImages[0].setImageContent(BitmapUtil.byteArrayToBitMap(
						image.getImageContent(),
						ConfigurationConstant.NORMAL_USER_PORTAL_IMAGE_WIDTH,
						ConfigurationConstant.NORMAL_USER_PORTAL_IMAGE_HEIGHT));
				bigHeadImage.setImageBitmap(galleryImages[0].getImageContent());
				break;
			}
		}

		int galleryIndex = 1;
		for (ImageProfile image : imageProfiles) {
			if (!image.isPortal()) {
				Bitmap bitmap_galleryImage_small = BitmapUtil
						.byteArrayToBitMap(
								image.getImageContent(),
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
		if (guideProfile != null) {

			tv_tour_length.setText(guideProfile.getTourLength() + "");
			tv_tour_price.setText(guideProfile.getTourPrice() + "");
			tv_tour_type.setText(guideProfile.getTourType() + "");

			if (guideProfile.getTextBiography() != null
					&& guideProfile.getTextBiography().length() > 0) {
				tv_about.setText(guideProfile.getTextBiography() + "");
			}

			City city = cityService.getCityById(guideProfile.getCity());
			if (city != null) {
				tv_city.setText(city.getCityName());
			}

			String attractions = "";
			jsonAttractions = guideProfile.getAttractions();
			if (jsonAttractions != null) {
				for (JSONGuideAttraction attraction : jsonAttractions) {
					attractions += attraction.getAttractionName() + "\n";
				}
				tv_attractions.setText(attractions);
			} else {
				// make sure it is not null
				jsonAttractions = new ArrayList<JSONGuideAttraction>();
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
