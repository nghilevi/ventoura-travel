package com.Mindelo.Ventoura.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CommonUtil {
	/**
	 * @param sourceFile
	 *            the filename of zip file including the absolute path
	 * @param destFileFolder
	 *            the folder in which the zipped files will be put in
	 */
	public static boolean unzipFile(InputStream inputStream,
			String destFileFolder) {
		try {
			File zipFile = new File(destFileFolder);
			if (!zipFile.exists())
				zipFile.mkdirs();
			ZipInputStream zipFileStream = new ZipInputStream(inputStream);
			ZipEntry entry = zipFileStream.getNextEntry();
			while (entry != null) {
				String fileName = entry.getName();
				File newFile = new File(destFileFolder + File.separator
						+ fileName);
				if (entry.isDirectory())
					new File(newFile.getParent()).mkdirs();
				else {
					FileOutputStream out = new FileOutputStream(newFile);
					IOUtils.copy(zipFileStream, out);
				}
				entry = zipFileStream.getNextEntry();
			}
			zipFileStream.closeEntry();

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param sourceFile
	 *            the filename of zip file including the absolute path
	 * @return the files in the zip file
	 * 
	 *         The zip file only allow contains images without any folders
	 */
	public static Map<String, Bitmap> unzipImageFilesIntoBitmaps(InputStream inputStream) {

		Map<String, Bitmap> unzippedFiles = new HashMap<String, Bitmap>();

		try {
			ZipInputStream zipFileStream = new ZipInputStream(inputStream);
			ZipEntry entry = zipFileStream.getNextEntry();
			while (entry != null) {
				String fileName = entry.getName();
				Bitmap mIcon11 = null;
				if (!entry.isDirectory()) {
					mIcon11 = BitmapFactory.decodeStream(zipFileStream);
					unzippedFiles.put(fileName, mIcon11);
				}
				entry = zipFileStream.getNextEntry();
			}
			zipFileStream.closeEntry();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return unzippedFiles;
	}
	
	/**
	 * @param sourceFile
	 *            the filename of zip file including the absolute path
	 * @return the files in the zip file
	 *         The zip file only allow contains images without any folders
	 */
	public static Map<String, byte[]> unzipImageFilesIntoBytes(InputStream inputStream) {

		Map<String, byte[]> unzippedFiles = new HashMap<String, byte[]>();

		try {
			ZipInputStream zipFileStream = new ZipInputStream(inputStream);
			ZipEntry entry = zipFileStream.getNextEntry();
			while (entry != null) {
				String fileName = entry.getName();
				if (!entry.isDirectory()) {
					unzippedFiles.put(fileName, IOUtils.toByteArray(zipFileStream));
				}
				entry = zipFileStream.getNextEntry();
			}
			zipFileStream.closeEntry();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return unzippedFiles;
	}

	/**
	 * delete a file, if is a folder, delete all files recursively
	 */
	public static void deleteFile(File file) throws IOException {
		if (!file.exists()) {
			return;
		}
		if (file.isDirectory()) {
			if (file.list().length == 0) {
				file.delete();
			} else {
				// list all the directory contents
				String files[] = file.list();
				for (String temp : files) {
					File fileDelete = new File(file, temp);
					deleteFile(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
				}
			}
		} else {
			file.delete();
		}
	}
	
	
}
