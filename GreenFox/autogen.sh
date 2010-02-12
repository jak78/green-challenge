#!/bin/sh

srcdir=`dirname $0`
test -z "$srcdir" && srcdir=.

run()
{
    $@
    if test $? -ne 0; then
	echo "Failed $@"
	exit 1
    fi
}

run ${ACLOCAL:-aclocal} -I m4macros $ACLOCAL_OPTIONS
case `uname` in
	Darwin)
    run ${LIBTOOLIZE:-glibtoolize} --copy --force
    ;;
  *)
		run ${LIBTOOLIZE:-libtoolize} --copy --force
		;;
esac
run ${AUTOHEADER:-autoheader}
run ${AUTOMAKE:-automake} --add-missing --foreign --copy
run ${AUTOCONF:-autoconf}
