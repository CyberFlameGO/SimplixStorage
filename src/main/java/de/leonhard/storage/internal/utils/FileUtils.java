package de.leonhard.storage.internal.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtils {

	public static boolean hasChanged(final File file, final long timeStamp) {
		return timeStamp < file.lastModified();
	}

	public static File getAndMake(final String name, final String path) {
		return getAndMake(new File(path, name));
	}


	public static void copy(final InputStream inputStream, final Path destination) {
		try {
			Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
		} catch (final Exception exception) {
			System.err.println("Exception while copying to + '" + destination.toAbsolutePath() + "'");
			exception.printStackTrace();
		}
	}

	public static File getAndMake(final File file) {
		try {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}

		} catch (IOException ioException) {
			System.err.println("Error while creating file '" + file.getName() + "'.");
			System.err.println("Path: '" + file.getAbsolutePath() + "'");
			ioException.printStackTrace();
			throw new IllegalStateException();
		}
		return file;
	}

	public static List<String> readAllLines(final File file) throws IOException {
		final byte[] fileBytes = Files.readAllBytes(file.toPath());
		final String asString = new String(fileBytes);
		return new ArrayList<>(Arrays.asList(asString.split("\n")));
	}
}