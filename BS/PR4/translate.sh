#!/bin/sh
module="translate"
device="translate"
mode="664"

# remoce stale nodes
rm -f /dev/${device}[0-1]

# remove old module
/sbin/rmmod $module

#invoke insmod with all arguments we got
#end use a pathname as newer modules dont look in . by default
/sbin/insmod ./$module.ko $* || exit 1

major=$(grep /proc/devices -e $module | cut -d\ -f1)

mknod /dev/${device}0 c $major 0
mknod /dev/${device}1 c $major 1

echo "Installed two devices with major number $major"