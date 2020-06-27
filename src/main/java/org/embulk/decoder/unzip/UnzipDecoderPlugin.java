package org.embulk.decoder.unzip;

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

public class UnzipDecoderPlugin
        implements DecoderPlugin
{
    public interface PluginTask
            extends Task
    {
        @Config("format")
        @ConfigDefault("\"\"")
        public String getFormat();

        @Config("decompress_concatenated")
        @ConfigDefault("true")
        public boolean getDecompressConcatenated();

        @Config("match_name")
        @ConfigDefault("\"\"")
        public String getMatchName();

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
    	String zipFileName = fileInput.hintOfCurrentInputFileNameForLogging().get();
    	System.out.println(zipFileName);
        final PluginTask task = taskSource.loadTask(PluginTask.class);

        final FileInputInputStream files = new FileInputInputStream(fileInput);
        return new CommonsCompressFileInput(task.getBufferAllocator(),
                new CommonsCompressProvider(task, files));
    }

}
