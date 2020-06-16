package org.embulk.decoder.unzip;

import java.io.InputStream;
import java.io.IOException;

import org.embulk.config.Config;
import org.embulk.config.ConfigDefault;
import org.embulk.config.ConfigInject;
import org.embulk.config.ConfigSource;
import org.embulk.config.Task;
import org.embulk.config.TaskSource;
import org.embulk.spi.BufferAllocator;
import org.embulk.spi.DecoderPlugin;
import org.embulk.spi.FileInput;
import org.embulk.spi.util.FileInputInputStream;
import org.embulk.spi.util.InputStreamFileInput;

public class UnzipDecoderPlugin
        implements DecoderPlugin
{
    public interface PluginTask
            extends Task
    {
//        @Config("skip_on_error")
//        @ConfigDefault("true")
//        public boolean skipOnError();

        @ConfigInject
        public BufferAllocator getBufferAllocator();
    }

    @Override
    public void transaction(ConfigSource config, DecoderPlugin.Control control)
    {
        PluginTask task = config.loadConfig(PluginTask.class);

        control.run(task.dump());
    }

    @Override
    public FileInput open(TaskSource taskSource, FileInput fileInput)
    {
    	System.out.println("file: " + fileInput.hintOfCurrentInputFileNameForLogging());
        final PluginTask task = taskSource.loadTask(PluginTask.class);

        final FileInputInputStream files = new FileInputInputStream(fileInput);

        InputStreamFileInput isfi = null;
        try {
	        isfi = new InputStreamFileInput(
	                task.getBufferAllocator(),
	                new InputStreamFileInput.Provider() {
	                    public InputStream openNext() throws IOException
	                    {
	                        if (!files.nextFile()) {
	                            return null;
	                        }
	                        return newDecoderInputStream(task, files);
	                    }
	        
	                    public void close() throws IOException
	                    {
	                        files.close();
	                    }
	                });
        } catch (Exception e) {
//        	if(task.skipOnError()) {
//        		System.out.println("skip: " + isfi.hintOfCurrentInputFileNameForLogging());
//        		return null;
//        	} else
       		throw new RuntimeException(e);
        }
        return isfi;
    }

    private static InputStream newDecoderInputStream(PluginTask task, InputStream file) throws IOException
    {
        return new UnzipInputStream(file);
    }
}
