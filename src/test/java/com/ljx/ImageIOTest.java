package com.ljx;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.junit.Test;

public class ImageIOTest {

	private static final String CONTEXT = "D:\\imageIOTest\\";
	private static final String SRCIMG = CONTEXT + "9.jpg";

	@Test
	public void test() {
		String[] readerFormatNames = ImageIO.getReaderFormatNames();
		System.out.println(Arrays.asList(readerFormatNames));
		// jpg bmp jpeg wbmp png

		String[] writerFormatNames = ImageIO.getWriterFormatNames();
		System.out.println(Arrays.asList(writerFormatNames));
	}

	@Test
	public void readImage() throws IOException {
		BufferedImage read = ImageIO.read(new File(SRCIMG));
		System.out.println(read.toString());
		ColorModel colorModel = read.getColorModel();
		System.out.println(colorModel);
	}

	@Test
	public void writeImage() throws IOException {
		BufferedImage read = ImageIO.read(new File(SRCIMG));

		File targetFile = new File("D:\\imageIOTest\\99.png");
		boolean write = ImageIO.write(read, "png", targetFile);
		System.out.println(write);
	}

	@Test
	public void imageReaderTest() throws IOException {
		Iterator<ImageReader> irs = ImageIO.getImageReadersByFormatName("jpg");
		ImageReader imageReader = irs.next();

		ImageInputStream imageInputStream = ImageIO
				.createImageInputStream(new File(SRCIMG));

		imageReader.setInput(imageInputStream, true);

		BufferedImage bufferedImage = imageReader.read(0);
		System.out.println(bufferedImage.getWidth());
	}

	@Test
	public void imageReadParam() throws IOException {
		Iterator<ImageReader> itr = ImageIO.getImageReadersByFormatName("jpg");
		ImageReader imageReader = itr.next();

		ImageInputStream imageInputStream = ImageIO
				.createImageInputStream(new File(SRCIMG));

		imageReader.setInput(imageInputStream, true);

		ImageReadParam imageReadParam = imageReader.getDefaultReadParam();
		int halfWidth = imageReader.getWidth(0) / 2;
		int halfHeight = imageReader.getHeight(0) / 2;
		Rectangle rectangle = new Rectangle(0, 0, halfWidth, halfHeight);
		imageReadParam.setSourceRegion(rectangle);

		BufferedImage bufferedImage = imageReader.read(0, imageReadParam);
		System.out.println(bufferedImage.getWidth());

		boolean result = ImageIO.write(bufferedImage, "png", new File(CONTEXT
				+ "999.png"));
		System.out.println(result);
	}

	@Test
	public void imageReadParam2() throws IOException {
		Iterator<ImageReader> itr = ImageIO.getImageReadersByFormatName("jpg");
		ImageReader imageReader = itr.next();

		ImageInputStream imageInputStream = ImageIO
				.createImageInputStream(new File(SRCIMG));
		imageReader.setInput(imageInputStream, true);

		ImageReadParam imageReadParam = imageReader.getDefaultReadParam();
		imageReadParam.setSourceSubsampling(3, 3, 0, 0);

		BufferedImage bufferedImage = imageReader.read(0, imageReadParam);

		boolean b = ImageIO.write(bufferedImage, "jpg", new File(CONTEXT
				+ "1.jpg"));
		System.out.println(b);

	}

	/**
	 * 1、�?过ImageIO获得ImageReader 
	 * 2、�?过ImageReader获得ImageInputStream
	 * 3、指定ImageReader的source 
	 * 4、获得缩略图的数�?
	 * 5、若缩略图大�?，读取缩略图
	 */
	@Test
	public void thumbnailTest() throws IOException {
		Iterator<ImageReader> itr = ImageIO.getImageReadersByFormatName("jpg");
		ImageReader imageReader = itr.next();
		ImageInputStream imageInputStream = ImageIO
				.createImageInputStream(new File(SRCIMG));
		imageReader.setInput(imageInputStream, true);
		int thumbnailNum = imageReader.getNumThumbnails(0);
		System.out.println(thumbnailNum);
		if (thumbnailNum > 0) {
			BufferedImage bufferedImage = imageReader.readThumbnail(0, 0);
			// TODO 图片其它处理
			System.out.println(bufferedImage.getWidth());
		}
	}

	/**
	 * 1、�?过ImageIO获得ImageWriter对象 
	 * 2、�?过ImageIO获得ImageOutputStream对象
	 * 3、�?过给定的ImageOutputStream设定ImageWriter的输出源 
	 * 4、写图片
	 * 
	 * @throws IOException
	 */
	@Test
	public void teatImageWriter() throws IOException {

		// ~:指定配置imageReader
		Iterator<ImageReader> itrReads = ImageIO
				.getImageReadersByFormatName("png");
		ImageReader imageReader = itrReads.next();
		ImageInputStream imageInputSteam = ImageIO
				.createImageInputStream(new File(CONTEXT + "999.png"));
		imageReader.setInput(imageInputSteam, true);
		// ~~

		BufferedImage bufferedImage = imageReader.read(0);

		// ~:指定配置imageWriter
		Iterator<ImageWriter> itr = ImageIO.getImageWritersByFormatName("jpg");
		ImageWriter imageWriter = itr.next();

		ImageOutputStream imageOutputStream = ImageIO
				.createImageOutputStream(new File(CONTEXT + "213.jpg"));
		imageWriter.setOutput(imageOutputStream);
		// ~~

		imageWriter.write(bufferedImage);
	}

	@Test
	public void metaDataTest() throws IOException {
		Iterator<ImageReader> itrR = ImageIO.getImageReadersByFormatName("jpg");
		ImageReader imageReader = itrR.next();
		ImageInputStream imageInputStream = ImageIO
				.createImageInputStream(new File(SRCIMG));
		imageReader.setInput(imageInputStream, true);
		IIOMetadata ioMetadata = imageReader.getImageMetadata(0);
		System.out.println(ioMetadata.toString());

	}

	@Test
	public void generatorThumbnail() throws IOException {
		// Iterator<ImageReader> itrR =
		// ImageIO.getImageReadersByFormatName("jpg");
		// ImageReader imageReader = itrR.next();
		// ImageInputStream imageInputStream =
		// ImageIO.createImageInputStream(new File(SRCIMG));
		// imageReader.setInput(imageInputStream,true);

		BufferedImage image = ImageIO.read(new File(SRCIMG));

		int width = image.getWidth();
		int height = image.getHeight();

		BufferedImage bufferedImage = new BufferedImage(width / 2, height / 2,
				BufferedImage.TYPE_INT_RGB);
		Graphics graphics = bufferedImage.getGraphics();
		graphics.drawImage(image, 0, 0, width / 2, height / 2, null);
		graphics.dispose();

		ImageIO.write(bufferedImage, "jpg", new File(CONTEXT
				+ "1_thumbnail.jpg"));

	}

}
