package com.hjw.webService.xyyyService.util;

import java.awt.image.BufferedImage;

/***
 * 图片旋转
 * @author Administrator
 *
 */
public class ImgXuanzhuan {
	//逆时针旋转90度（通过交换图像的整数像素RGB 值）
		public static BufferedImage rotateCounterclockwise90(BufferedImage bi) {
			int width = bi.getWidth();
			int height = bi.getHeight();
			BufferedImage bufferedImage = new BufferedImage(height, width, bi.getType());
			for (int i = 0; i < width; i++)
				for (int j = 0; j < height; j++)
					bufferedImage.setRGB(j, i, bi.getRGB(i, j));
			return bufferedImage;
		}
}
