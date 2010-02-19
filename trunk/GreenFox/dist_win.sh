#!/bin/sh

cd `dirname $0`



package="system-monitor"
version="0.3"


if [[ ! -a "chrome.manifest" ]]; then 
  cat chrome.manifest.in | sed -e "s/@PACKAGE_NAME@/$package/g" > chrome.manifest
fi

if [[ ! -a "install.rdf" ]]; then 
  cat install.rdf.in | sed -e "s/@PACKAGE_VERSION@/$version/g" > install.rdf
fi

build_libraries="components/SystemMonitor/Release/SystemMonitor.dll"
platform_component_directory="WINNT_x86-msvc"
component_xpt="components/*.xpt"

xpcom_name=system-monitor
xpi=$xpcom_name-$version.xpi

cp -f $build_libraries platform/$platform_component_directory/components/
xpi_contents="content locale skin defaults $component_xpt platform chrome.manifest install.rdf"

rm -f $xpi

zip -q -r -9 $xpi $xpi_contents -x \*/.git/\* || exit 1


