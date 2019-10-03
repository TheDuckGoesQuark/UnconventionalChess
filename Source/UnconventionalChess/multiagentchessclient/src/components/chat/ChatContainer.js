import React, {Component} from "react";
import PropTypes from "prop-types";
import {TranscriptMessage} from "../../models/Chat"


class ChatContainer extends Component {
    static propTypes = {
        // List of messages so far
        timeOrderedMessages: PropTypes.arrayOf(PropTypes.instanceOf(TranscriptMessage))
    };

    render() {
        return (<div>I'm a chat container!</div>);
    }
}

const messageStyle = {
    backgroundColor: "grey",
    fontcolor: "white",
};

export default ChatContainer;