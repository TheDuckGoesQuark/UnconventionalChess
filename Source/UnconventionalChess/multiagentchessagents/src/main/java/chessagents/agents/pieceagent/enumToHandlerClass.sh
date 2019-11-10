#!/bin/bash

enumfile=$1

# Get everything between brackets
contents=$(awk '/\}/{f=0} f; /\{/{f=1}' "$enumfile")
# Remove any indentation/whitespace
contents=${contents// /}
# Remove newlines
contents=${contents//$'\n'/}
# Lowercase everything
contents=$(echo "$contents" | tr '[:upper:]' '[:lower:]')
# Uppercase after underscore
contents=$(echo "$contents" | sed 's/_\(.\)/\U\1/g')
# Uppercase after comma
contents=$(echo "$contents" | sed 's/,\(.\)/ \U\1/g')
# Uppercase first character
contents=$(echo "$contents" | sed 's/^\(.\)/\U\1/g')

# Change to array
# shellcheck disable=SC2206
classnames=($contents)

# Create handler class for each
for eventName in "${classnames[@]}"; do
  event="${eventName}Event"
  classname="${event}Handler"
  file="./eventhandlers/${classname}.java"
  classContents=$(
    cat <<END_HEREDOC
package chessagents.agents.pieceagent.eventhandlers;

import chessagents.agents.pieceagent.events.${event};
import chessagents.agents.pieceagent.pieces.PieceAgent;

public class ${classname} extends PieceEventHandler<${event}> {

    public void apply(${event} event, PieceAgent agent) {
        // do nothing
    }
}

END_HEREDOC
  )
  echo "$classContents" >"$file"
done

# Create event class for each event
for eventName in "${classnames[@]}"; do
  classname="${eventName}Event"
  file="./events/${classname}.java"
  classContents=$(
    cat <<END_HEREDOC
package chessagents.agents.pieceagent.events;

public class ${classname} implements Event {

}

END_HEREDOC
  )
  echo "$classContents" >"$file"
done
