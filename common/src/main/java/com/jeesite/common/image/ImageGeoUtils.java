/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.image;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.lang.Rational;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * 图片地理信息获取
 * @author ThinkGem
 * @version 2025-12-07
 * @code pom.xml 中添加依赖：<pre>{@code
 * 		<dependency>
 * 			<groupId>com.drewnoakes</groupId>
 * 			<artifactId>metadata-extractor</artifactId>
 * 			<version>${metadata-extractor.version}</version>
 * 		</dependency>
 * 	}</pre>
 */
public class ImageGeoUtils {

	private static final Logger logger = LoggerFactory.getLogger(ImageGeoUtils.class);

	public static List<Map<String, Object>> getImageGeo(String filename) {
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			File jpegFile = new File(filename);
			Metadata metadata = JpegMetadataReader.readMetadata(jpegFile);
			Collection<GpsDirectory> gpsDirs = metadata.getDirectoriesOfType(GpsDirectory.class);
			gpsDirs.forEach(gpsDir -> {
				Map<String, Object> map = new HashMap<>();
				map.put("alt", 0.0); // 海拔
				map.put("lat", 0.0); // 维度
				map.put("lon", 0.0); // 经度

				Rational[] latPart = gpsDir.getRationalArray(GpsDirectory.TAG_LATITUDE);
				Rational[] lonPart = gpsDir.getRationalArray(GpsDirectory.TAG_LONGITUDE);
				String north = gpsDir.getString(GpsDirectory.TAG_LATITUDE_REF);
				String east = gpsDir.getString(GpsDirectory.TAG_LONGITUDE_REF);

				try {
					double alt = gpsDir.getDouble(GpsDirectory.TAG_ALTITUDE);
					map.put("alt", alt);
				} catch (Exception ex) {
					logger.debug(ex.getMessage());
				}

				double latsign = 1.0d;
				if (north.equalsIgnoreCase("S")) {
					latsign = -1.0d;
				}
				double lonsign = 1.0d;
				if (east.equalsIgnoreCase("W")) {
					lonsign = -1.0d;
				}

				double lat = (Math.abs(latPart[0].doubleValue()) + latPart[1].doubleValue() / 60.0d + latPart[2].doubleValue() / 3600.0d) * latsign;
				double lon = (Math.abs(lonPart[0].doubleValue()) + lonPart[1].doubleValue() / 60.0d + lonPart[2].doubleValue() / 3600.0d) * lonsign;

				if (!Double.isNaN(lat) && !Double.isNaN(lon)) {
					map.put("lat", lat); // 维度
					map.put("lon", lon); // 经度
				}
				list.add(map);
			});
		} catch (Exception ex) {
			logger.info(ex.getMessage());
		}
		return list;
	}

}
