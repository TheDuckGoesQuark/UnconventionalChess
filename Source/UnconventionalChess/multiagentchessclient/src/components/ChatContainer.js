import React, {Component} from "react";
import PropTypes from "prop-types";
import {TranscriptMessage} from "../commons/Chat"

class ChatContainer extends Component {
    static propTypes = {
        // List of messages so far
        timeOrderedMessages: PropTypes.arrayOf(TranscriptMessage)
    };

    render() {
        return (<div>I'm a chat container!</div>);
    }
}

export default ChatContainer;