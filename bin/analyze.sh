#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

ROOT=$1

for file in $ROOT/*.tar; do
    [ -e "$file" ] || continue
    echo "In $file..."
    tar xf $file --to-command "$DIR/tf.sh -n \$TAR_FILENAME"
done
