package org.embulk.decoder.unzip;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

public class UnzipInputStream extends InputStream {

	private InputStream is;
	
	public UnzipInputStream(InputStream is) {
		this.is = is;
	}

	@Override
	public int read() throws IOException {

		ArchiveInputStream in = null;
		try {
			in = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, is);
		} catch (ArchiveException e) {
			e.printStackTrace();
		}
		return in.read();

//		ZipArchiveEntry entry = (ZipArchiveEntry)in.getNextEntry();
//		entry.
//		OutputStream out = Files.newOutputStream(dir.toPath().resolve(entry.getName()));
//		IOUtils.copy(in, out);
//		out.close();
//		in.close();

	}

}
