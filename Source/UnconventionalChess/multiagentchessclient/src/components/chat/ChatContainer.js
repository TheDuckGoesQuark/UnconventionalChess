import React from "react";
import {connect} from "react-redux";

const mapMessageToComponent = (message, index) => {
    if (message.sourceSquare) {
        return <div key={index}
                    style={messageStyle}>Move: {message.sourceSquare},{message.targetSquare}</div>
    } else {
        return <div key={index}
                    style={messageStyle}>{message.entries}</div>
    }
};

const ChatContainer = ({timeOrderedMessages}) => (
    <div>
        {timeOrderedMessages.length > 0
            ? timeOrderedMessages.map((message, index) => mapMessageToComponent(message, index))
            : mapMessageToComponent("no message to show")}
    </div>
);

const messageStyle = {
    backgroundColor: "grey",
    fontcolor: "white",
};

function mapStateToProps(state) {
    return {
        timeOrderedMessages: state.chatReducer.timeOrderedMessages,
    }
}

export default connect(mapStateToProps)(ChatContainer);