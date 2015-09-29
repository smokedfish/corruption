package org.rob.corruption;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Corruption {

	public enum State {
		READING,
		WRITING
	}

	private final int num;
	private final FileEntry[] fileEntries;
	private final ExecutorService executorService;

	public Corruption(int num, int threads) {
		this.num = num;
		this.fileEntries = new FileEntry[num];
		this.executorService = Executors.newFixedThreadPool(threads);
	}

	private void start() throws InterruptedException, ExecutionException {
		for (int i = 0; i < num; i++) {
			fileEntries[i] = new FileEntry(new File("file" + i));
		}

		State state = State.WRITING;

		while (true) {
			List<Future<?>> futures = new ArrayList<>();

			if (state == State.WRITING) {
				for (FileEntry fileEntry : fileEntries) {
					futures.add(executorService.submit(new WriteTask(fileEntry)));
				}
				state = State.READING;
			}
			else if (state == State.READING) {
				for (FileEntry fileEntry : fileEntries) {
					futures.add(executorService.submit(new ReadTask(fileEntry)));
				}
				state = State.WRITING;
			}
			for (Future<?> future : futures) {
				future.get();
			}
		}
	}

	public static void main(String args[]) throws InterruptedException, ExecutionException {
		if (args.length != 2) {
			System.out.println("<num files> <num threads>");
		}
		Corruption corruption = new Corruption(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
		corruption.start();
	}
}
