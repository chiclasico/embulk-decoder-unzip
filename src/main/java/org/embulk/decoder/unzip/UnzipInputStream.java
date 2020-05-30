package org.embulk.decoder.unzip;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipInputStream extends InputStream {

	private ZipInputStream zis;
	
	public UnzipInputStream(InputStream is) {
		zis = new ZipInputStream(new BufferedInputStream(is), StandardCharsets.UTF_8);
	}

	@Override
	public int read() throws IOException {

		ZipEntry zipentry = zis.getNextEntry();
		int v = -1;
		if(zipentry != null)
       		v = zis.read();
		return v;

	}

}
