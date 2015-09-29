package org.rob.corruption;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.zip.CRC32;

public class WriteTask implements Runnable {

	private static final int MIN_SIZE = 1024;
	private static final int MAX_SIZE = 10240;

	private final Random random = new Random();

	private final FileEntry fileEntry;

	public WriteTask(FileEntry fileEntry) {
		this.fileEntry = fileEntry;
	}

	@Override
	public void run() {
		int size = nextInt(MIN_SIZE, MAX_SIZE);
		CRC32 crc = new CRC32();
		try (OutputStream os = new BufferedOutputStream(new FileOutputStream(fileEntry.getFile()))) {
			for (int i = 0; i < size; ) {
				for (int rnd = random.nextInt(), n = Math.min(size - i, Integer.SIZE/Byte.SIZE); n-- > 0; rnd >>= Byte.SIZE) {
					crc.update(rnd);
					os.write(rnd);
					i++;
				}
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		fileEntry.setCrc(crc.getValue());
		fileEntry.setSize(size);
	}

	private int nextInt(int min, int max) {
		return min + (int)(random.nextDouble()*(max - min));
	}
}
