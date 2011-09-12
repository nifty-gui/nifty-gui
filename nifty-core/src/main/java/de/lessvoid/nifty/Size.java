package de.lessvoid.nifty;

public class Size {
	private final int m_width;
	private final int m_height;

	public Size() {
		this(0, 0);
	}

	public Size(int width, int height) {
		m_width = width;
		m_height = height;
	}

	public int getWidth() {
		return m_width;
	}

	public int getHeight() {
		return m_height;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + m_height;
		result = prime * result + m_width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Size other = (Size) obj;
		if (m_height != other.m_height)
			return false;
		if (m_width != other.m_width)
			return false;
		return true;
	}
}
