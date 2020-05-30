Embulk::JavaPlugin.register_decoder(
  "unzip", "org.embulk.decoder.unzip.UnzipDecoderPlugin",
  File.expand_path('../../../../classpath', __FILE__))
