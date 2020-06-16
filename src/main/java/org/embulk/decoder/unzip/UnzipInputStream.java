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
	private ByteArrayInputStream tmpStream;
	
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
		
		int data = readStream();
		if(data != -1)
			return data;
		
		ZipEntry zipEntry = null;
		try {
			zipEntry = zis.getNextEntry();
			if(zipEntry == null)
				return -1;

		} catch (IOException e) {
			System.out.println("error: " + zipFileName + ", " + e.getMessage());
//			zis.closeEntry();
			read();
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
				zis.closeEntry();
			} catch(IOException e) {
				if(e.getMessage().equals("Stream closed"))
					// 正常終了?
					return -1;
				else
					throw new IOException(e);
			}

			this.tmpStream = new ByteArrayInputStream(sb.toString().getBytes("utf-8"));
			read();
		}
		
		return -1;
	}

	private int readStream() {

		if(tmpStream != null) {
			int data = tmpStream.read();
			while(data != -1)
			  return tmpStream.read();
		}

		return -1;
	}
	
}
