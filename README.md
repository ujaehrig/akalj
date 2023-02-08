# akalj

A Spring Boot Java service to get the `Abfallkalender Stadtwerke Karlsruhe` as either JSON or iCal.

# Basic Workflow

Get HTML file from: 
'https://web6.karlsruhe.de/service/abfall/akal/akal.php?strasse=Neureuter%20Hauptstrasse&hausnr=1'

* find element with id='nfoo'
* Starting from here, find all elements with class_='row'
* Filter all elements with a child class_='col_3-3'
* find all strings 

