package com.octo.greenchallenge;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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
