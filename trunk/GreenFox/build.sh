#!/bin/sh
mkdir -p build
rm -fr build/*
cd srcExtension
zip -r ../build/greenfox.xpi * -x '*/.svn/*' -x '*~'

