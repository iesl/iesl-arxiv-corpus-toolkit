#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

ROOT=$1

find $ROOT -type f -name '*.pdf' -exec ./bin/tf.sh -n {} ';'

# for file in $ROOT/*.tar; do
#     [ -e "$file" ] || continue
#     echo "In $file..."
#     tar xf $file --to-command "$DIR/tf.sh -n \$TAR_FILENAME"
# done
