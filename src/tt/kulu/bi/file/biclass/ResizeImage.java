package tt.kulu.bi.file.biclass;

import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.util.Map;

public class ResizeImage {

	/**
	 * @param im
	 *            原始图像
	 * @param resizeTimes
	 *            需要缩小的倍数，缩小2倍为原来的1/2 ，这个数值越大，返回的图片越小
	 * @return 返回处理后的图像
	 */
	public BufferedImage resizeImage(BufferedImage im, int toWidth, int toHeight) {
		/* 原始图像的宽度和高度 */
		int width = im.getWidth();
		int height = im.getHeight();
		double srcScale = (double) height / width;

		if (width > height) {
			toWidth = width / height * toHeight;
		}
		else {
			toHeight = height /width  * toWidth;
		}
		/* 调整后的图片的宽度和高度 */
		// if (width < toWidth) {
		// toWidth = width;
		// }
		// if (height < toHeight) {
		// toHeight = height;
		// }
		/* 新生成结果图片 */
		BufferedImage result = new BufferedImage(toWidth, toHeight,
				BufferedImage.TYPE_INT_RGB);

		result.getGraphics().drawImage(
				im.getScaledInstance(toWidth, toHeight,
						java.awt.Image.SCALE_SMOOTH), 0, 0, null);
		return result;
	}

	/**
	 * @param im
	 *            原始图像
	 * @param resizeTimes
	 *            需要缩小的倍数，缩小2倍为原来的1/2 ，这个数值越大，返回的图片越小
	 * @return 返回处理后的图像
	 */
	public BufferedImage resizeImage(BufferedImage im, float resizeTimes) {
		/* 原始图像的宽度和高度 */
		int width = im.getWidth();
		int height = im.getHeight();

		/* 调整后的图片的宽度和高度 */
		int toWidth = (int) (Float.parseFloat(String.valueOf(width)) / resizeTimes);
		int toHeight = (int) (Float.parseFloat(String.valueOf(height)) / resizeTimes);

		/* 新生成结果图片 */
		BufferedImage result = new BufferedImage(toWidth, toHeight,
				BufferedImage.TYPE_INT_RGB);

		result.getGraphics().drawImage(
				im.getScaledInstance(toWidth, toHeight,
						java.awt.Image.SCALE_SMOOTH), 0, 0, null);
		return result;
	}

	/**
	 * @param im
	 *            原始图像
	 * @param resizeTimes
	 *            倍数,比如0.5就是缩小一半,0.98等等double类型
	 * @return 返回处理后的图像
	 */
	public BufferedImage zoomImage(BufferedImage im, float resizeTimes) {
		/* 原始图像的宽度和高度 */
		int width = im.getWidth();
		int height = im.getHeight();

		/* 调整后的图片的宽度和高度 */
		int toWidth = (int) (Float.parseFloat(String.valueOf(width)) * resizeTimes);
		int toHeight = (int) (Float.parseFloat(String.valueOf(height)) * resizeTimes);

		/* 新生成结果图片 */
		BufferedImage result = new BufferedImage(toWidth, toHeight,
				BufferedImage.TYPE_INT_RGB);

		result.getGraphics().drawImage(
				im.getScaledInstance(toWidth, toHeight,
						java.awt.Image.SCALE_SMOOTH), 0, 0, null);
		return result;
	}

	/**
	 * @param path
	 *            要转化的图像的文件夹,就是存放图像的文件夹路径
	 * @param type
	 *            图片的后缀名组成的数组
	 * @return
	 */
	public List<BufferedImage> getImageList(String path, String[] type)
			throws IOException {
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		for (String s : type) {
			map.put(s, true);
		}
		List<BufferedImage> result = new ArrayList<BufferedImage>();
		File[] fileList = new File(path).listFiles();
		for (File f : fileList) {
			if (f.length() == 0)
				continue;
			if (map.get(getExtension(f.getName())) == null)
				continue;
			result.add(javax.imageio.ImageIO.read(f));
		}
		return result;
	}

	/**
	 * 把图片写到磁盘上
	 * 
	 * @param im
	 * @param path
	 *            eg: C://home// 图片写入的文件夹地址
	 * @param fileName
	 *            DCM1987.jpg 写入图片的名字
	 * @return
	 */
	public boolean writeToDisk(BufferedImage im, String fileEx, File f) {
		try {
			ImageIO.write(im, fileEx, f);
			im.flush();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean writeHighQuality(BufferedImage im, String fileFullPath) {
		try {
			/* 输出到文件流 */
			FileOutputStream newimage = new FileOutputStream(fileFullPath
					+ System.currentTimeMillis() + ".jpg");
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);
			JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(im);
			/* 压缩质量 */
			jep.setQuality(1f, true);
			encoder.encode(im, jep);
			/* 近JPEG编码 */
			newimage.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 返回文件的文件后缀名
	 * 
	 * @param fileName
	 * @return
	 */
	public String getExtension(String fileName) {
		try {
			return fileName.split("\\.")[fileName.split("\\.").length - 1];
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 对图片裁剪，并把裁剪完蛋新图片保存 。
	 * 
	 * @param srcpath
	 *            源图片路径
	 * @param subpath
	 *            剪切图片存放路径
	 * @throws IOException
	 */
	public boolean cut(File in, String fileEx, int newW, int newH)
			throws IOException {
		boolean isOk = true;
		ImageInputStream iis = null;
		try {
			if (fileEx == null || fileEx.trim().equals("")) {
				fileEx = "png";
			}
			// 先压缩
			this.writeToDisk(this.resizeImage(ImageIO.read(in), newW, newH),
					fileEx, in);
			// 获取图片流
			Iterator iterator = ImageIO.getImageReadersByFormatName(fileEx);
			ImageReader reader = (ImageReader) iterator.next();
			iis = ImageIO.createImageInputStream(in);
			/*
			 * <p>iis:读取源.true:只向前搜索 </p>.将它标记为 ‘只向前搜索’。
			 * 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 reader 避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
			 */
			reader.setInput(iis, true);

			/*
			 * <p>描述如何对流进行解码的类<p>.用于指定如何在输入时从 Java Image I/O
			 * 框架的上下文中的流转换一幅图像或一组图像。用于特定图像格式的插件 将从其 ImageReader 实现的
			 * getDefaultReadParam 方法中返回 ImageReadParam 的实例。
			 */
			ImageReadParam param = reader.getDefaultReadParam();

			/*
			 * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
			 * 的左上顶点的坐标（x，y）、宽度和高度可以定义这个区域。
			 */
			int x = 0, y = 0;
			int w = reader.getWidth(0);
			int h = reader.getHeight(0);
			if (w != h) {
				if (w > h) {// 宽
					x = (w - h) / 2;
					w = h;
				} else {
					y = (h - w) / 2;
					h = w;
				}
				Rectangle rect = new Rectangle(x, y, w, h);

				// 提供一个 BufferedImage，将其用作解码像素数据的目标。
				param.setSourceRegion(rect);
				/*
				 * 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象，并将 它作为一个完整的
				 * BufferedImage 返回。
				 */
				BufferedImage bi = reader.read(0, param);

				// 保存新图片

				ImageIO.write(bi, fileEx, in);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (iis != null)
				iis.close();
		}
		return isOk;
	}

	/**
	 * 对图片裁剪，并把裁剪完蛋新图片保存 。
	 * 
	 * @param srcpath
	 *            源图片路径
	 * @param subpath
	 *            剪切图片存放路径
	 * @throws IOException
	 */
	public FileInputStream resizeImage(File in, String fileEx, int newW,
			int newH) throws IOException {
		ImageInputStream iis = null;
		FileInputStream newIn = null;
		try {
			if (fileEx == null || fileEx.trim().equals("")) {
				fileEx = "png";
			}

			// 获取图片流
			Iterator iterator = ImageIO.getImageReadersByFormatName(fileEx);
			ImageReader reader = (ImageReader) iterator.next();
			iis = ImageIO.createImageInputStream(in);
			/*
			 * <p>iis:读取源.true:只向前搜索 </p>.将它标记为 ‘只向前搜索’。
			 * 此设置意味着包含在输入源中的图像将只按顺序读取，可能允许 reader 避免缓存包含与以前已经读取的图像关联的数据的那些输入部分。
			 */
			reader.setInput(iis, true);

			/*
			 * 图片裁剪区域。Rectangle 指定了坐标空间中的一个区域，通过 Rectangle 对象
			 * 的左上顶点的坐标（x，y）、宽度和高度可以定义这个区域。
			 */
			int x = 0, y = 0;
			int w = reader.getWidth(0);
			int h = reader.getHeight(0);
			if (w > h) {// 宽，处理高
				newH = newW * h / w;
			} else {
				newW = newH * w / h;
			}
			// 压缩
			this.writeToDisk(this.resizeImage(ImageIO.read(in), newW, newH),
					fileEx, in);
			// 读取
			newIn = new FileInputStream(in);
		} finally {
			if (iis != null)
				iis.close();
		}
		return newIn;
	}

	public static void main(String[] args) throws Exception {

		String oldFile = "C:\\nodejs\\old.jpg";
		/* 这儿填写你存放要缩小图片的文件夹全地址 */
		String outputFolder = "C:\\nodejs\\new.jpg";
		/* 这儿填写你转化后的图片存放的文件夹 */
		float times = 0.5f;
		/* 这个参数是要转化成的倍数,如果是1就是转化成1倍 */

		ResizeImage r = new ResizeImage();
		r.cut(new File(oldFile), "jpg", 200, 200);
	}
}