package com.wy.word;

import org.apache.poi.xwpf.usermodel.Document;

/**
 * Word文档中的图片类型
 *
 * @author 飞花梦影
 * @date 2021-03-06 00:23:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public enum PicType {

	EMF(Document.PICTURE_TYPE_EMF),
	WMF(Document.PICTURE_TYPE_WMF),
	PICT(Document.PICTURE_TYPE_PICT),
	JPEG(Document.PICTURE_TYPE_JPEG),
	PNG(Document.PICTURE_TYPE_PNG),
	DIB(Document.PICTURE_TYPE_DIB),
	GIF(Document.PICTURE_TYPE_GIF),
	TIFF(Document.PICTURE_TYPE_TIFF),
	EPS(Document.PICTURE_TYPE_EPS),
	WPG(Document.PICTURE_TYPE_WPG);

	private final int value;

	PicType(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}