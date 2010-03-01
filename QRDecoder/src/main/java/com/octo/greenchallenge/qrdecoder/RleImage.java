package com.octo.greenchallenge.qrdecoder;

import java.io.IOException;

import jp.sourceforge.qrcode.data.QRCodeImage;

class RleImage implements QRCodeImage {
	int[][] image;

	public RleImage(String filename) throws IOException {
		RleDecoder rleDecoder = new RleDecoder();
		this.image = rleDecoder.decode(filename);
	}

	public int getWidth() {
		return image.length;
	}

	public int getHeight() {
		return image[0].length;
	}

	public int getPixel(int x, int y) {
		return image[x][y];
	}

}