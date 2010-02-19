package com.octo.greenchallenge;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Encode an image in RLE.
 * <p>
 * 
 * The format is the following: <b>width height
 * (number_of_pixel_of_the_following_color color)*</b>
 * <p>
 * 
 * All data is on 4 bytes. The color is RGBA (in order from the less to the most
 * significative byte)
 * 
 * @author Henri Tremblay
 */
public class RleEncoder {

	public static int[] encode(BufferedImage image) {
		int width = image.getWidth(null);
		int height = image.getHeight(null);

		List<Integer> rle = new ArrayList<Integer>(2 * width * height + 2); // worst
																			// size
																			// possible

		rle.add(width);
		rle.add(height);

		int previousPixel = image.getRGB(0, 0);
		int count = 0;

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = image.getRGB(x, y);
				if (rgb == previousPixel) {
					count++;
				} else {
					rle.add(count);
					rle.add(previousPixel);
					count = 1;
					previousPixel = rgb;
				}
			}
		}

		// write the last pixel
		rle.add(count);
		rle.add(image.getRGB(width - 1, height - 1));

		int[] result = new int[rle.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = rle.get(i);
		}
		return result;
	}
}
