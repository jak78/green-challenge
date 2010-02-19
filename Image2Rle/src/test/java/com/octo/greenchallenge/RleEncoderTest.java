package com.octo.greenchallenge;


import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Henri Tremblay
 */
public class RleEncoderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testColor() throws Exception {

		BufferedImage image = ImageIO.read(new File("colors.png"));

		int[] actual = RleEncoder.encode(image);
		int[] expected = new int[] { 2, 2, 1, 0xffffffff, 1, 0xffff0000, 1, 0xff00ff00, 1, 0xff0000ff };

		printCompare(actual, expected);
		assertArrayEquals(expected, actual);

	}

	@Test
	public void testQR() throws Exception {

		BufferedImage image = ImageIO.read(new File("test.png"));

		int[] actual = RleEncoder.encode(image);
		int[] expected = new int[] { 6, 3, 2, 0xff000000, 2, -1, 3, 0xff000000, 2, -1, 1, 0xff000000, 4, -1, 4,
				0xff000000 };

		printCompare(actual, expected);
		assertArrayEquals(expected, actual);
	}

	private void printCompare(int[] actual, int[] expected) {
		for (int i = 0; i < actual.length; i++) {
			System.out.print(Integer.toHexString(actual[i]) + ",");
		}
		System.out.println();
		for (int i = 0; i < expected.length; i++) {
			System.out.print(Integer.toHexString(expected[i]) + ",");
		}
		System.out.println();
	}
}
