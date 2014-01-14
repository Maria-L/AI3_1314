#!/bin/sh
module="translate"
device="translate"
mode="666"

# entferne veraltete nodes
rm -f /dev/${device}[0-1]

# entferne das alte modul
/sbin/rmmod $module

#erstelle das neue Modul mit insmod mit allen Parametern die mitgegeben wurden
#wenn das nicht funktioniert hat - brich mit Fehler ab
/sbin/insmod ./$module.ko $* || exit 1

#suche die alte Major-Number aus /proc/devices und erstelle die neuen nodes damit
major=$(grep /proc/devices -e $module | cut -d\  -f1)

mknod /dev/${device}0 c $major 0
mknod /dev/${device}1 c $major 1

echo "Zwei Geraete mit Major-Number $major installiert"