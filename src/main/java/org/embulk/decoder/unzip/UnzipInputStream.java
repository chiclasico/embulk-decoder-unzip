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

	private InputStream is;
	private BufferedInputStream bis;
	private ZipInputStream zis;
	
	public UnzipInputStream(InputStream is) {
   		this.is = is;
   		this.bis = new BufferedInputStream(is);
   		this.zis = new ZipInputStream(bis, StandardCharsets.UTF_8);
	}

	@Override
	public void close() throws IOException {
		zis.close();
		bis.close();
		is.close();
	}
	
	@Override
	public int read() throws IOException {

		ZipEntry zipEntry = zis.getNextEntry();
		StringBuilder sb = new StringBuilder();

		if (zipEntry != null) {
			System.out.println(String.format("Entry: %s len %d", zipEntry.getName(), zipEntry.getSize()));

	        BufferedReader br = new BufferedReader(new InputStreamReader(zis, "UTF-8"));
	        String line;
	        while ((line = br.readLine()) != null) {
	        	sb.append(line);
	        	System.out.println(line);
	        }
	        br.close();
	    } else 
	    	return -1;
		
		return new ByteArrayInputStream(sb.toString().getBytes("utf-8")).read();
	}
	
}
