package org.embulk.decoder.unzip;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipInputStream extends InputStream {

	private ZipInputStream zis;
	private String zipFileName;
	
	public UnzipInputStream(InputStream is, String zipFileName) {
		this.zipFileName = zipFileName;
   		this.zis = new ZipInputStream(new BufferedInputStream(is), StandardCharsets.UTF_8);
	}

	@Override
	public void close() throws IOException {
		zis.close();
	}
	
	@Override
	public int read() throws IOException {
		
		ZipEntry zipEntry;
		try {
			zipEntry = zis.getNextEntry();
		} catch (IOException e) {
			System.out.println("error: " + zipFileName + ", " + e.getMessage());
			return -1;
		}

		StringBuilder sb = new StringBuilder();
		if (zipEntry != null) {
			System.out.println(String.format("Entry: %s len %d", zipEntry.getName(), zipEntry.getSize()));

			try (BufferedReader br = new BufferedReader(new InputStreamReader(zis, "UTF-8"));) {
	        
		        String line;
		        while ((line = br.readLine()) != null) {
		        	sb.append(line);
		        	System.out.println(line);
		        }
			}
	    	zis.closeEntry();

		} else {
	    	return -1;
	    }
		
		return new ByteArrayInputStream(sb.toString().getBytes("utf-8")).read();
	}
	
}
