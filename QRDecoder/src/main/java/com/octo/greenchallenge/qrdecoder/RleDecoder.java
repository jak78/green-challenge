package com.octo.greenchallenge.qrdecoder;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;

public class RleDecoder {

	public int[][] decode(String filename) throws IOException {

		DataInputStream dis = new DataInputStream(new BufferedInputStream(
				new FileInputStream(filename)));

		int width = dis.readInt();
		int height = dis.readInt();
		int[][] data = new int[width][height];

		int x = 0;
		int y = 0;
		int count = 0;
		int rgb = 0;
		boolean EOF = false;
		while (!EOF) {
			try {
				count = dis.readInt();
				rgb = dis.readInt();
				int i = 0;
				while (i < count) {
					if (x == width) {
						x = 0;
						y++;
					}
					data[x][y] = rgb;
					x++;
					i++;
				}
			} catch (EOFException e) {
				EOF = true;
			}
		}
		dis.close();

		return data;
	}
}
