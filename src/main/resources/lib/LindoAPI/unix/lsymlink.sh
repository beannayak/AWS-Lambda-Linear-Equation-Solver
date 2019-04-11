#!/bin/sh
#
# make binaries executables
#
chmod 755 liblindo*.*
chmod 755 libmosek*.*
chmod 755 runlindo

source ../../include/lsversion.sh
#
# create symbolic links
#
if [ -f liblindo.so.$LS_MAJOR.$LS_MINOR ]; then
	ln -sf liblindo.so.$LS_MAJOR.$LS_MINOR liblindo.so
	ln -sf liblindojni.so.$LS_MAJOR.$LS_MINOR.1 liblindojni.so
elif [ -f liblindo64.so.$LS_MAJOR.$LS_MINOR ]; then
	ln -sf liblindo64.so.$LS_MAJOR.$LS_MINOR liblindo64.so
	ln -sf liblindojni.so.$LS_MAJOR.$LS_MINOR.1 liblindojni.so
elif [ -f liblindo64.$LS_MAJOR.$LS_MINOR.dylib ]; then
	ln -sf liblindo64.$LS_MAJOR.$LS_MINOR.dylib liblindo64.dylib
elif [ -f liblindo.$LS_MAJOR.$LS_MINOR.dylib ]; then
	ln -sf liblindo.$LS_MAJOR.$LS_MINOR.dylib liblindo.dylib
fi

if [ -f libmosek.so.$MSK_MAJOR.$MSK_MINOR ]; then
	ln -sf libmosek.so.$MSK_MAJOR.$MSK_MINOR libmosek.so
elif [ -f libmosek64.so.$MSK_MAJOR.$MSK_MINOR ]; then
	ln -sf libmosek64.so.$MSK_MAJOR.$MSK_MINOR libmosek64.so
elif [ -f libmosek64.$MSK_MAJOR.$MSK_MINOR.dylib ]; then
	ln -sf libmosek64.$MSK_MAJOR.$MSK_MINOR.dylib libmosek64.dylib	
elif [ -f libmosek.$MSK_MAJOR.$MSK_MINOR.dylib ]; then
	ln -sf libmosek.$MSK_MAJOR.$MSK_MINOR.dylib libmosek.dylib
fi

if [ -f libfsu.so.1 ]; then
	ln -sf libfsu.so.1 libfsu.so
fi
