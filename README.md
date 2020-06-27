Original code is copied from https://github.com/hata/embulk-decoder-commons-compress

diffrence is below.
* upgrade common-compress from 1.13 to 1.20.
* add error handling for ZipException and IllegalArgumentException when ArchiveInputStream#getNextEntry() called.
