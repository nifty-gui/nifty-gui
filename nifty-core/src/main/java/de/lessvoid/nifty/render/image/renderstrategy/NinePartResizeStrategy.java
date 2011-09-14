package de.lessvoid.nifty.render.image.renderstrategy;

import java.util.logging.Logger;

import de.lessvoid.nifty.layout.Box;
import de.lessvoid.nifty.spi.render.RenderDevice;
import de.lessvoid.nifty.spi.render.RenderImage;
import de.lessvoid.nifty.tools.Color;

public class NinePartResizeStrategy implements RenderStrategy {
	private static Logger log = Logger.getLogger(NinePartResizeStrategy.class.getName());

	private static final int NINE_PART_RESIZE_ARGS_COUNT = 12;

	private NinePartResizeRow m_row0;
	private NinePartResizeRow m_row1;
	private NinePartResizeRow m_row2;
	private Box box = new Box();

	@Override
	public void setParameters(String parameters) {
		String[] args = getArguments(parameters);

		m_row0 = new NinePartResizeRow(args, 0);
		m_row1 = new NinePartResizeRow(args, 4);
		m_row2 = new NinePartResizeRow(args, 8);
	}

	private String[] getArguments(String parameters) {
		String[] args = null;
		if (parameters != null) {
			args = parameters.split(",");
		}

		if ((args == null) || (args.length != NINE_PART_RESIZE_ARGS_COUNT)) {
			int argCount = (args == null) ? 0 : args.length;
			throw new IllegalArgumentException("Trying to parse [" + this.getClass().getName()
					+ "] : wrong parameter count (" + argCount
					+ "). Expected [w1,w2,w3,h1,w4,w5,w6,h2,w7,w8,w9,h3], found [" + parameters + "].");
		}
		return args;
	}

	@Override
	public void render(RenderDevice device, RenderImage image, Box sourceArea, int x, int y, int width, int height,
			Color color, float scale) {
		final int cX = x + width / 2;
		final int cY = y + height / 2;

		final int srcX = sourceArea.getX();
		final int srcW = sourceArea.getWidth();

		if ((m_row0.getWidth() > srcW) || (m_row1.getWidth() > srcW) || (m_row2.getWidth() > srcW)) {
			log.warning("Defined nine-part resize strategy goes out of source area's bounds.");
		}

		final int srcH0 = m_row0.getHeight();
		final int srcH1 = m_row1.getHeight();
		final int srcH2 = m_row2.getHeight();

		final int srcY0 = sourceArea.getY();
		final int srcY1 = srcY0 + srcH0;
		final int srcY2 = srcY1 + srcH1;

		final int midlH = height - srcH0 - srcH2;

		final int y0 = y;
		final int y1 = y0 + srcH0;
		final int y2 = y1 + midlH;

		box.setX(srcX);
    box.setY(srcY0);
    box.setWidth(srcW);
    box.setHeight(srcH0);
		renderRow(device, image, m_row0, box, x, y0, width, srcH0, color, scale, cX, cY);

    box.setX(srcX);
    box.setY(srcY1);
    box.setWidth(srcW);
    box.setHeight(srcH1);
		renderRow(device, image, m_row1, box, x, y1, width, midlH, color, scale, cX, cY);

    box.setX(srcX);
    box.setY(srcY2);
    box.setWidth(srcW);
    box.setHeight(srcH2);
		renderRow(device, image, m_row2, box, x, y2, width, srcH2, color, scale, cX, cY);
	}

	private void renderRow(final RenderDevice renderDevice, final RenderImage image, final NinePartResizeRow row,
			final Box sourceArea, final int x, final int y, final int width, final int height, final Color color,
			final float scale, final int centerX, final int centerY) {
		final int srcY = sourceArea.getY();
		final int srcH = sourceArea.getHeight();

		final int srcW0 = row.getLeftWidth();
		final int srcW1 = row.getMiddleWidth();
		final int srcW2 = row.getRightWidth();

		final int srcX0 = sourceArea.getX();
		final int srcX1 = srcX0 + srcW0;
		final int srcX2 = srcX1 + srcW1;

		final int midlW = width - srcW0 - srcW2;

		final int x0 = x;
		final int x1 = x0 + srcW0;
		final int x2 = x1 + midlW;

		renderDevice.renderImage(image, x0, y, srcW0, height, srcX0, srcY, srcW0, srcH, color, scale, centerX, centerY);
		renderDevice.renderImage(image, x1, y, midlW, height, srcX1, srcY, srcW1, srcH, color, scale, centerX, centerY);
		renderDevice.renderImage(image, x2, y, srcW2, height, srcX2, srcY, srcW2, srcH, color, scale, centerX, centerY);
	}

	private static class NinePartResizeRow {
		private final int m_leftWidth;
		private final int m_middleWidth;
		private final int m_rightWidth;
		private final int m_height;

		public NinePartResizeRow(String[] data, int dataOffset) {
			m_leftWidth = Integer.valueOf(data[dataOffset]);
			m_middleWidth = Integer.valueOf(data[dataOffset + 1]);
			m_rightWidth = Integer.valueOf(data[dataOffset + 2]);
			m_height = Integer.valueOf(data[dataOffset + 3]);
		}

		public int getLeftWidth() {
			return m_leftWidth;
		}

		public int getMiddleWidth() {
			return m_middleWidth;
		}

		public int getRightWidth() {
			return m_rightWidth;
		}

		public int getWidth() {
			return m_leftWidth + m_middleWidth + m_rightWidth;
		}

		public int getHeight() {
			return m_height;
		}
	}
}
