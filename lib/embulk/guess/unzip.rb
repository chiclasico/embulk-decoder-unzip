module Embulk
  module Guess

    # TODO implement guess plugin to make this command work:
    #      $ embulk guess -g "unzip" partial-config.yml

    # class Unzip < GuessPlugin
    #   Plugin.register_guess("unzip", self)
    #
    #   FOO_BAR_HEADER = "\x1f\x8b".force_encoding('ASCII-8BIT').freeze
    #
    #   def guess(config, sample_buffer)
    #     if sample_buffer[0,2] == FOO_BAR_HEADER
    #       return {"decoders" => [{"type" => "unzip"}]}
    #     end
    #     return {}
    #   end
    # end

  end
end
