package com.jiangyifen.ec.util;

import java.io.File;
import java.util.Comparator;

class FileUtils {
	static class CompratorByLastModified implements Comparator<File> {
		public int compare(File file1, File file2) {
			long diff = file1.lastModified() - file2.lastModified();
			if (diff > 0)
				return 1;
			else if (diff == 0)
				return 0;
			else
				return -1;
		}
	}

	static class CompratorBySize implements Comparator<File> {
		public int compare(File file1, File file2) {
			long diff = file1.length() - file2.length();
			if (diff > 0)
				return 1;
			else if (diff == 0)
				return 0;
			else
				return -1;
		}
	}

	/**
	 * File path = new File(path); 
	 * File[] files = path.listFiles();
	 * Arrays.sort(files, new FileUtils.CompratorByLastModified());
	 */

}