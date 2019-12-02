import React from "react";
import {connect} from "react-redux";

const mapMessageToComponent = (message, index, nMessages) => {
    let colour = ((nMessages - 1) - index) % 2 === 0 ? "grey" : "silver";
    let style = {
        ...messageStyle,
        backgroundColor: colour
    };
    if (message.sourceSquare) {
        return <div key={index}
                    style={style}>Move: {message.sourceSquare},{message.targetSquare}</div>
    } else {
        return <div key={index}
                    style={style}>{message.fromId}: {message.messageBody}</div>
    }
};

const ChatContainer = ({timeOrderedMessages}) => (
    <div style={containerStyle}>
        {timeOrderedMessages.length > 0
            ? timeOrderedMessages.map((message, index) => mapMessageToComponent(message, index, timeOrderedMessages.length))
            : mapMessageToComponent("no message to show")}
    </div>
);

const messageStyle = {
    fontcolor: "white",
    position: "relative",
    fontSize: "25px",
};

const containerStyle = {
    boxSizing: "border-box",
    height: "100%",
    padding: "4px",
    flexDirection: "column-reverse",
    display: "flex",
    overflowY: "auto",
};

function mapStateToProps(state) {
    return {
        timeOrderedMessages: state.chatReducer.timeOrderedMessages,
    }
}

export default connect(mapStateToProps)(ChatContainer);