package org.rob.corruption;

import java.io.File;

public class FileEntry {
	private final File file;
	private long size;
	private long crc;

	public FileEntry(File file) {
		super();
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getCrc() {
		return crc;
	}

	public void setCrc(long crc) {
		this.crc = crc;
	}
}
