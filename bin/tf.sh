#!/bin/bash


pdfname=
input=

while getopts n:h name; do
    case $name in
        n)    pdfname=${OPTARG};;
    esac
done


TXT=$(pdftotext $pdfname - | egrep -i '^arxiv')

echo "$pdfname: $TXT"
