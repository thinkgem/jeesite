package com.thinkgem.jeesite.common.utils;

import com.drew.metadata.*;
import com.drew.metadata.exif.*;
import com.drew.imaging.jpeg.*;
import com.drew.lang.*;
import java.io.*;

public class ImageGeo {
	public double lat = 0.0;
	public double lon = 0.0;
	public double alt = 0.0;
	public boolean error = false;

	public ImageGeo(String filename) {
		try {
			error = false;
			File jpegFile = new File(filename);
			Metadata metadata = JpegMetadataReader.readMetadata(jpegFile);

			GpsDirectory gpsdir = (GpsDirectory) metadata
					.getDirectory(GpsDirectory.class);
			Rational latpart[] = gpsdir
					.getRationalArray(GpsDirectory.TAG_GPS_LATITUDE);
			Rational lonpart[] = gpsdir
					.getRationalArray(GpsDirectory.TAG_GPS_LONGITUDE);
			String northing = gpsdir
					.getString(GpsDirectory.TAG_GPS_LATITUDE_REF);
			String easting = gpsdir
					.getString(GpsDirectory.TAG_GPS_LONGITUDE_REF);

			try {
				alt = gpsdir.getDouble(GpsDirectory.TAG_GPS_ALTITUDE);
			} catch (Exception ex) {
			}

			double latsign = 1.0d;
			if (northing.equalsIgnoreCase("S"))
				latsign = -1.0d;
			double lonsign = 1.0d;
			if (easting.equalsIgnoreCase("W"))
				lonsign = -1.0d;
			lat = (Math.abs(latpart[0].doubleValue())
					+ latpart[1].doubleValue() / 60.0d + latpart[2]
					.doubleValue() / 3600.0d) * latsign;
			lon = (Math.abs(lonpart[0].doubleValue())
					+ lonpart[1].doubleValue() / 60.0d + lonpart[2]
					.doubleValue() / 3600.0d) * lonsign;

			if (Double.isNaN(lat) || Double.isNaN(lon))
				error = true;
		} catch (Exception ex) {
			error = true;
		}
		System.out.println(filename + ": (" + lat + ", " + lon + ")");
	}
	
	public static void main(String[] args) {
		ImageGeo imageGeo = new ImageGeo(ImageGeo.class.getResource("IMAG0068.jpg").getFile());
		System.out.println(imageGeo.lon+","+imageGeo.lat);
	}

}
