package com.Mindelo.VentouraServer.Controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Mindelo.VentouraServer.Constant.ConfigurationConstant;
import com.Mindelo.VentouraServer.Constant.HttpFieldConstant;
import com.Mindelo.VentouraServer.Entity.GuideGallery;
import com.Mindelo.VentouraServer.Entity.GuideLastUpdatedLog;
import com.Mindelo.VentouraServer.IService.IGuideService;
import com.Mindelo.VentouraServer.IService.ILastUpdatedService;
import com.Mindelo.VentouraServer.JSONEntity.JSONKeyValueMessage;
import com.Mindelo.VentouraServer.Util.DateTimeUtil;

@Controller
@RequestMapping(value = "/guide")
public class GuideGalleryController {

	@Autowired
	private IGuideService guideService;
	@Autowired
	private ILastUpdatedService luService;

	@RequestMapping(value = "/uploadSingleGuideGallery", method = RequestMethod.POST)
	public @ResponseBody JSONKeyValueMessage<String, String> uploadSingleGuideGalleryGallery(
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		JSONKeyValueMessage<String, String> message = new JSONKeyValueMessage<String, String>();

		try {

			@SuppressWarnings("unchecked")
			List<FileItem> items = new ServletFileUpload(
					new DiskFileItemFactory()).parseRequest(request);

			List<GuideGallery> guideGalleries = loadTravellerGallery(items);
			if (guideGalleries.size() >= 0) {
				GuideGallery guideGallery = guideGalleries.get(0);
				guideService.saveGuideGallery(guideGallery);
				message.setKey(HttpFieldConstant.SERVER_GUIDE_GALLERY_ID);
				message.setValue(guideGallery.getId() + "");

				/*
				 * set update log
				 */
				Date time_modified = new Date();
				response.setHeader(
						ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
						DateTimeUtil.fromDateToString_GMT(time_modified));
				luService.setGuideGalleryLastUpdated(time_modified.getTime(),
						guideGallery.getGuideId());
			}

			return message;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@RequestMapping(value = "/uploadAllGuideGallery", method = RequestMethod.POST)
	public void uploadAllGuideGalleryGallery(HttpServletRequest request,
			HttpServletResponse response, Model model) {

		try {
			@SuppressWarnings("unchecked")
			List<FileItem> items = new ServletFileUpload(
					new DiskFileItemFactory()).parseRequest(request);

			List<GuideGallery> guideGalleries = loadTravellerGallery(items);
			guideService.saveGuideGallery(guideGalleries);

			if (guideGalleries.size() >= 0) {
				GuideGallery guideGallery = guideGalleries.get(0);
				/*
				 * set update log
				 */
				Date time_modified = new Date();
				response.setHeader(
						ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
						DateTimeUtil.fromDateToString_GMT(time_modified));
				luService.setGuideGalleryLastUpdated(time_modified.getTime(),
						guideGallery.getGuideId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<GuideGallery> loadTravellerGallery(List<FileItem> items)
			throws IOException {
		List<GuideGallery> guideGalleries = new ArrayList<GuideGallery>();
		Map<String, String> commonField = new HashMap<String, String>();
		for (FileItem item : items) {
			if (item.isFormField()) {
				// Process regular form field (input
				// type="text|radio|checkbox|etc", select, etc).
				String fieldname = item.getFieldName();
				String fieldvalue = item.getString("UTF-8").trim();
				commonField.put(fieldname, fieldvalue);

			} else {
				GuideGallery gallery = new GuideGallery();
				byte[] imageFile = IOUtils.toByteArray(item.getInputStream());
				gallery.setImage(imageFile);
				guideGalleries.add(gallery);
			}
		}

		long guideId = Long.valueOf(commonField
				.get(HttpFieldConstant.SERVER_GUIDE_ID));

		for (GuideGallery guideGallery : guideGalleries) {
			guideGallery.setGuideId(guideId);
		}
		return guideGalleries;
	}

	@RequestMapping(value = "/setGuidePortalGallery/{guideId}/{galleryId}", method = RequestMethod.GET)
	public void setGuidePortalGallery(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("galleryId") final long galleryId,
			@PathVariable("guideId") final long guideId) {
		guideService.setGuidePortalGallery(guideId, galleryId);
		/*
		 * set update log
		 */
		Date time_modified = new Date();
		response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
				DateTimeUtil.fromDateToString_GMT(time_modified));
		luService.setGuideGalleryLastUpdated(time_modified.getTime(), guideId);
	}

	@RequestMapping(value = "/deleteBatchGallery/{travellerId}", method = RequestMethod.POST)
	public void deleteTravellerGallery(HttpServletRequest request,
			HttpServletResponse response) {
		Enumeration<String> parameters = request.getParameterNames();

		List<Long> galleryIds = new ArrayList<Long>();
		while (parameters.hasMoreElements()) {
			String parameter = (String) parameters.nextElement();
			galleryIds.add(Long.valueOf(parameter));
		}

		if (galleryIds.size() >= 1) {
			GuideGallery gallery = guideService.getGuideGalleryById(galleryIds
					.get(0));
			/*
			 * set update log
			 */
			Date time_modified = new Date();
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
					DateTimeUtil.fromDateToString_GMT(time_modified));
			luService.setGuideGalleryLastUpdated(time_modified.getTime(),
					gallery.getGuideId());

			guideService.deleteBatchGallery(galleryIds);
		}
	}

	@RequestMapping(value = "/deleteGuideGallery/{galleryId}", method = RequestMethod.GET)
	public void deleteGuideGallery(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("galleryId") final long galleryId) {

		GuideGallery gallery = guideService.getGuideGalleryById(galleryId);
		guideService.deleteGuideGallery(galleryId);

		/*
		 * set update log
		 */
		Date time_modified = new Date();
		response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
				DateTimeUtil.fromDateToString_GMT(time_modified));
		luService.setGuideGalleryLastUpdated(time_modified.getTime(),
				gallery.getGuideId());
	}

	@RequestMapping(value = "/getGuidePortalGallery/{guideId}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<byte[]> getGuidePortalGallery(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("guideId") final long guideId) {

		/*
		 * no changes in the server
		 */
		GuideLastUpdatedLog guideLastUpdatedLog = luService
				.getGuideLastUpdatedLogByGuideId(guideId);
		try {
			String modifiedSince = request
					.getHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE);
			if (modifiedSince != null) {

				long lastUpdatedTime = DateTimeUtil.fromStringToDate_GMT(
						modifiedSince).getTime();
				if (guideLastUpdatedLog != null
						&& lastUpdatedTime == guideLastUpdatedLog
								.getGalleryLastUpdated()) {
					response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
					return null;
				}
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}

		/*
		 * some changes happened in the server
		 */
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		if (guideLastUpdatedLog != null) {
			Date date_modified = new Date();
			date_modified.setTime(guideLastUpdatedLog.getGalleryLastUpdated());
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
					DateTimeUtil.fromDateToString_GMT(date_modified));
		}

		/*
		 * return the databody
		 */
		try {

			final HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_PNG);

			GuideGallery gallery = guideService
					.getGuidePortalGalleryByGuideId(guideId);
			if (gallery != null) {
				return new ResponseEntity<byte[]>(gallery.getImage(), headers,
						HttpStatus.CREATED);
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return null;
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
		}

	}

	@RequestMapping(value = "/getAllGalleryImages/{guideId}", method = RequestMethod.GET)
	public void doDownload(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("guideId") final long guideId) {
		/*
		 * no changes in the server
		 */
		GuideLastUpdatedLog guideLastUpdatedLog = luService
				.getGuideLastUpdatedLogByGuideId(guideId);
		try {
			String modifiedSince = request
					.getHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE);
			if (modifiedSince != null) {

				long lastUpdatedTime = DateTimeUtil.fromStringToDate_GMT(
						modifiedSince).getTime();
				if (guideLastUpdatedLog != null
						&& lastUpdatedTime == guideLastUpdatedLog
								.getGalleryLastUpdated()) {
					response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				}
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		/*
		 * some changes happened in the server
		 */
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		if (guideLastUpdatedLog != null) {
			Date date_modified = new Date();
			date_modified.setTime(guideLastUpdatedLog.getGalleryLastUpdated());
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
					DateTimeUtil.fromDateToString_GMT(date_modified));
		}

		/*
		 * return the databody
		 */
		int size = 0;
		List<GuideGallery> guideGalleries = guideService
				.getAllGalleryByGuideId(guideId);
		for (GuideGallery gallery : guideGalleries) {
			size += gallery.getImage().length;
		}

		// set headers for the response
		response.setContentType("application/zip");
		response.setContentLength(size);
		response.setHeader("Content-Disposition",
				"attachment;filename=temp_user_images.zip");

		try {
			// get output stream of the response
			OutputStream outStream = response.getOutputStream();
			zipFile(guideGalleries, outStream);
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void zipFile(final List<GuideGallery> files, OutputStream os) {
		try {
			ZipOutputStream zos = new ZipOutputStream(os);
			for (int i = 0; i < files.size(); i++) {
				byte[] currentFile = files.get(i).getImage();
				ZipEntry entry;
				if (files.get(i).isPortalPhoto()) {
					entry = new ZipEntry("gp_" + files.get(i).getId() + "_"
							+ files.get(i).getGuideId() + ".png");
				} else {
					entry = new ZipEntry("g_" + files.get(i).getId() + "_"
							+ files.get(i).getGuideId() + ".png");
				}
				entry.setSize(currentFile.length);
				zos.putNextEntry(entry);
				zos.write(currentFile);
				zos.closeEntry();
			}
			zos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
