import React from "react";
import {connect} from "react-redux";

const mapMessageToComponent = (message) => {
    return <div style={messageStyle}>{message}</div>
};

const ChatContainer = ({timeOrderedMessages}) => (
    <div>
        {timeOrderedMessages.length > 0
            ? timeOrderedMessages.map(mapMessageToComponent)
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