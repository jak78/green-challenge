package com.octo.greenchallenge;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

/**
 * Read an image from the disk, encode it in RLE and save it in a new file (qrc
 * extension)
 * 
 * @author Henri Tremblay
 * 
 */
public class Image2rle {

	public static void main(String[] args) {
		if (args.length != 1) {
			showUsage("Missing image file name");
			return;
		}

		String filename = args[0];
		File file = new File(filename);
		if (!file.exists()) {
			showUsage("File " + filename + " not found");
			return;
		}

		try {
			BufferedImage image = ImageIO.read(file);

			int[] result = RleEncoder.encode(image);

			String rleFile = filename.substring(0, filename.lastIndexOf('.'));
			DataOutputStream out = new DataOutputStream(
					new BufferedOutputStream(new FileOutputStream(rleFile + ".qrc")));
			for (int i : result) {
				out.writeInt(i);
			}

			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			showUsage(e.toString());
			return;
		}
	}

	private static void showUsage(String msg) {
		System.err.println("Error: " + msg);
		System.err.println();
		System.err.println("Usage: " + Image2rle.class.getSimpleName() + " filename");
		System.err.println("\tThe RLE file will be in the same directory and have the .qrc extension");
		System.err.println();
		System.exit(1);
	}
}
