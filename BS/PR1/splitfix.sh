#!/bin/bash

#Autor: Steffen Giersch und Maria Luedemann
#Praktikumsgruppe 2
#1. Praktikumstermin

#Gibt den Hilfetext aus
usage() {
  echo "DESCRIPTION
    splitfix.sh [OPTIONS] FILE [FILE ...]
    Split FILE into fixed-size pieces.
    The pieces are, by default, 10 lines long, if FILE is a text file.
    The pieces are, by default, 10 kiB long, if FILE is *not* a text file.
    The last piece may be smaller - it contains the rest of the original file.
    The output files bear the name of the input file with a 4-digit numerical suffix.
    The original file can be reconstructend with the command \"cat FILE.*\".
  
  EXAMPLE:
    splitfix.sh foo.pdf
      splits foo.pdf into the files
      foo.pdf.0000 foo.pdf.0001 etc.
      
  splitfix.sh [-h | --help]     This help text
  splitfix.sh --version        Print version number
  
  OPTIONS:
  -h
    --help    this help text
    
  -s
    --SIZE    size of the pieces
          in lines (for text files)
          in kiBytes (for other files)
  
  -v
    --verbose print debugging messages"
}

#Flags fuer das Speichern der Optionen und deren Argumente
hflag="false"
sflag="false"
vflag="false"
sfound="false"
svalue=10

#Umrechenfaktor fuer Kilobyte zu Byte
byteToKiBi=1000
 
for var in "$@"                         #var wird ueber alle Argumente der Eingabe iteriert
do
    if [ $sfound == "true" ]            #Wenn wir zuvor das sflag gefunden haben, ist das naechste Argument 
    then                                #der Wert fuer s
        svalue=$var
        sfound="false"
    else                                #Ansonsten
        case $var in                    #case fuer alle moegligen Flags
        -v*|--verbose) vflag="true"     #VFlag setzen
                ;;
        
        -h*|--help)    hflag="true"     #HFlag setzen
                ;;

        -s*)           sflag="true"     #SFlag setzen und angeben, dass das SFlag gerade
                       sfound="true"    #gesetzt wurde mit sfound
                ;;
        -*)            echo "Warnung: Unbekanntes Flag $var - dieses wird ignoriert"
        ;;                              #Wenn ein unbekanntes Flag gefunden wird eine Warnung ausgeben
         esac
    fi
done

if [ $vflag == "true" ]                 #Wenn das VFlag gesetzt ist den Debugmodus anschalten
then                                    #und von vorne ausf�hren
     echo "Debugmodus anschalten"
     bash -x $0                         #startet den Debugg Modus und ruft das Programm nochmal auf
     set +x                             #setzt das xFlag wieder zur�ck
     exit 1            
fi

if [ $hflag == "true" -o $# -eq 0 ]      #Wenn das HFlag gesetzt wurde oder keine Argumente mitgegeben 
then                                     #wurden den Hilfetext ausgeben    
    usage
    exit 1
fi

while [ "$1" != "" ]                            #Mit Shift �ber alle Argumente iterieren
do    
    case $1 in
    -s*)    shift                               #Wenn s gefunden wird,�ber s und den Wert nach s shiften
            shift
        ;;
    -*)     shift                               #bei allem anderen dr�ber weg schiften
        ;;
     *)     temp=$(file -b $1 | grep -c "\<text")   #wenn kein Argument gegeben wird  abfragen    
            if [ $temp -eq 0 ]                      #ob es ein Text oder Binaerfile ist
            then
                echo "hallo Welt1"
                split -d -b "$svalue"K -a 4 $1 $1.  #Fuer den Fall eines binearfiles 
            else                                    #wird nach bytes gesplitet
                echo "hallo Welt2"
                split -d -l $svalue -a 4 $1 $1.     #Ein Textfile wird nach lines gesplittet
            fi
            shift
    esac
done

exit 0
