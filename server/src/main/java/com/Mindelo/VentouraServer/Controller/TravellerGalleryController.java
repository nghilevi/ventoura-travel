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
import com.Mindelo.VentouraServer.Entity.GuideLastUpdatedLog;
import com.Mindelo.VentouraServer.Entity.TravellerGallery;
import com.Mindelo.VentouraServer.Entity.TravellerLastUpdatedLog;
import com.Mindelo.VentouraServer.IService.ILastUpdatedService;
import com.Mindelo.VentouraServer.IService.ITravellerService;
import com.Mindelo.VentouraServer.JSONEntity.JSONKeyValueMessage;
import com.Mindelo.VentouraServer.Util.DateTimeUtil;

@Controller
@RequestMapping(value = "/traveller")
public class TravellerGalleryController {

	@Autowired
	private ITravellerService travellerService;
	@Autowired
	private ILastUpdatedService luService;

	@RequestMapping(value = "/getTravellerPortalGallery/{travellerId}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<byte[]> getTravellerPortalImage(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("travellerId") final long travellerId) {

		/*
		 * no changes in the server
		 */
		TravellerLastUpdatedLog travellerLastUpdatedLog = luService
				.getTravellerLastUpdatedLogByTravellerId(travellerId);
		try {
			String modifiedSince = request
					.getHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE);
			if (modifiedSince != null) {

				long lastUpdatedTime = DateTimeUtil.fromStringToDate_GMT(
						modifiedSince).getTime();
				if (travellerLastUpdatedLog != null
						&& lastUpdatedTime == travellerLastUpdatedLog
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
		if (travellerLastUpdatedLog != null) {
			Date date_modified = new Date();
			date_modified.setTime(travellerLastUpdatedLog
					.getGalleryLastUpdated());
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
					DateTimeUtil.fromDateToString_GMT(date_modified));
		}

		/*
		 * return the databody
		 */
		try {
			final HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_PNG);
			TravellerGallery travellerGallery = travellerService
					.getTravellerPortalGalleryByTravellerId(travellerId);
			if (travellerGallery != null) {
				return new ResponseEntity<byte[]>(travellerGallery.getImage(),
						headers, HttpStatus.CREATED);
			}
			return null;

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

	@RequestMapping(value = "/getAllGalleryImages/{travellerId}", method = RequestMethod.GET)
	public void doDownload(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("travellerId") final long travellerId) {

		/*
		 * no changes in the server
		 */
		TravellerLastUpdatedLog travellerLastUpdatedLog = luService
				.getTravellerLastUpdatedLogByTravellerId(travellerId);
		try {
			String modifiedSince = request
					.getHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE);
			if (modifiedSince != null) {

				long lastUpdatedTime = DateTimeUtil.fromStringToDate_GMT(
						modifiedSince).getTime();
				if (travellerLastUpdatedLog != null
						&& lastUpdatedTime == travellerLastUpdatedLog
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
		if (travellerLastUpdatedLog != null) {
			Date date_modified = new Date();
			date_modified.setTime(travellerLastUpdatedLog
					.getGalleryLastUpdated());
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
					DateTimeUtil.fromDateToString_GMT(date_modified));
		}

		/*
		 * return the databody
		 */
		int size = 0;
		List<TravellerGallery> travellerGalleries = travellerService
				.getAllGalleryByTravellerId(travellerId);
		for (TravellerGallery gallery : travellerGalleries) {
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
			zipFile(travellerGalleries, outStream);
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/uploadSingleTravellerGallery", method = RequestMethod.POST)
	public @ResponseBody JSONKeyValueMessage<String, String> uploadSingleTravellerGallery(
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		JSONKeyValueMessage<String, String> message = new JSONKeyValueMessage<String, String>();

		try {

			@SuppressWarnings("unchecked")
			List<FileItem> items = new ServletFileUpload(
					new DiskFileItemFactory()).parseRequest(request);

			List<TravellerGallery> travellerGalleries = loadTravellerGallery(items);
			if (travellerGalleries.size() > 0) {
				TravellerGallery gallery = travellerGalleries.get(0);
				travellerService.saveTravellerGallery(gallery);
				message.setKey(HttpFieldConstant.SERVER_TRAVELLER_GALLERY_ID);
				message.setValue(gallery.getId() + "");

				/*
				 * set update log
				 */
				Date time_modified = new Date();
				response.setHeader(
						ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
						DateTimeUtil.fromDateToString_GMT(time_modified));
				luService.setTravellerGalleryLastUpdated(
						time_modified.getTime(), gallery.getTravellerId());
			}

			return message;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/uploadAllTravellerGallery", method = RequestMethod.POST)
	public void uploadAllTravellerGallery(HttpServletRequest request,
			HttpServletResponse response, Model model) {

		try {

			@SuppressWarnings("unchecked")
			List<FileItem> items = new ServletFileUpload(
					new DiskFileItemFactory()).parseRequest(request);

			List<TravellerGallery> travellerGalleries = loadTravellerGallery(items);
			travellerService.saveTravellerGallery(travellerGalleries);

			if (travellerGalleries.size() > 0) {
				TravellerGallery gallery = travellerGalleries.get(0);
				/*
				 * set update log
				 */
				Date time_modified = new Date();
				response.setHeader(
						ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
						DateTimeUtil.fromDateToString_GMT(time_modified));
				luService.setTravellerGalleryLastUpdated(
						time_modified.getTime(), gallery.getTravellerId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<TravellerGallery> loadTravellerGallery(List<FileItem> items)
			throws IOException {
		List<TravellerGallery> travellerGalleries = new ArrayList<TravellerGallery>();
		Map<String, String> commonField = new HashMap<String, String>();
		for (FileItem item : items) {
			if (item.isFormField()) {
				// Process regular form field (input
				// type="text|radio|checkbox|etc", select, etc).
				String fieldname = item.getFieldName();
				String fieldvalue = item.getString("UTF-8").trim();
				commonField.put(fieldname, fieldvalue);

			} else {
				TravellerGallery gallery = new TravellerGallery();
				byte[] imageFile = IOUtils.toByteArray(item.getInputStream());
				gallery.setImage(imageFile);
				travellerGalleries.add(gallery);
			}
		}

		long travellerId = Long.valueOf(commonField
				.get(HttpFieldConstant.SERVER_TRAVELLER_ID));

		for (TravellerGallery travellerGallery : travellerGalleries) {
			travellerGallery.setTravellerId(travellerId);
		}
		return travellerGalleries;
	}

	@RequestMapping(value = "/setTravellerPortalGallery/{travellerId}/{galleryId}", method = RequestMethod.GET)
	public void setGuidePortalGallery(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("galleryId") final long galleryId,
			@PathVariable("travellerId") final long travellerId) {
		travellerService.setTravellerPortalGallery(travellerId, galleryId);
		/*
		 * set update log
		 */
		Date time_modified = new Date();
		response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
				DateTimeUtil.fromDateToString_GMT(time_modified));
		luService.setTravellerGalleryLastUpdated(time_modified.getTime(),
				travellerId);
	}

	@RequestMapping(value = "/deleteBatchGallery", method = RequestMethod.POST)
	public void deleteBatchGallery(HttpServletRequest request,
			HttpServletResponse response) {
		Enumeration<String> parameters = request.getParameterNames();

		List<Long> galleryIds = new ArrayList<Long>();
		while (parameters.hasMoreElements()) {
			String parameter = (String) parameters.nextElement();
			galleryIds.add(Long.valueOf(parameter));
		}

		if (galleryIds.size() >= 1) {
			TravellerGallery gallery = travellerService
					.getTravellerGalleryById(galleryIds.get(0));
			/*
			 * set update log
			 */
			Date time_modified = new Date();
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
					DateTimeUtil.fromDateToString_GMT(time_modified));
			luService.setTravellerGalleryLastUpdated(time_modified.getTime(),
					gallery.getTravellerId());

			travellerService.deleteBatchGallery(galleryIds);
		}
	}

	@RequestMapping(value = "/deleteTravellerGallery/{galleryId}", method = RequestMethod.GET)
	public void deleteTravellerGallery(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("galleryId") final long galleryId) {
		TravellerGallery gallery = travellerService
				.getTravellerGalleryById(galleryId);
		travellerService.deleteTravellerGallery(galleryId);

		/*
		 * set update log
		 */
		Date time_modified = new Date();
		response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
				DateTimeUtil.fromDateToString_GMT(time_modified));
		luService.setTravellerGalleryLastUpdated(time_modified.getTime(),
				gallery.getTravellerId());
	}

	private void zipFile(final List<TravellerGallery> files, OutputStream os) {
		try {
			ZipOutputStream zos = new ZipOutputStream(os);
			for (int i = 0; i < files.size(); i++) {
				byte[] currentFile = files.get(i).getImage();

				ZipEntry entry;
				if (files.get(i).isPortalPhoto()) {
					entry = new ZipEntry("tp_" + files.get(i).getId() + "_"
							+ files.get(i).getTravellerId() + ".png");
				} else {
					entry = new ZipEntry("t_" + files.get(i).getId() + "_"
							+ files.get(i).getTravellerId() + ".png");
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
