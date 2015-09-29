package org.rob.corruption;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CRC32;

public class ReadTask implements Runnable {

	private final FileEntry fileEntry;

	public ReadTask(FileEntry fileEntry) {
		this.fileEntry = fileEntry;
	}

	@Override
	public void run() {
		CRC32 crc = new CRC32();
		try (InputStream inputStream = new BufferedInputStream(new FileInputStream(fileEntry.getFile()))) {
			int cnt;
			while ((cnt = inputStream.read()) != -1) {
				crc.update(cnt);
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		if (crc.getValue() != fileEntry.getCrc()) {
			System.out.println(fileEntry.getFile() + " has crc " + crc.getValue() + " expecting " + fileEntry.getCrc());
		}
	}
}
