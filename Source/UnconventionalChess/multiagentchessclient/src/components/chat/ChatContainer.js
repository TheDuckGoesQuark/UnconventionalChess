import React from "react";
import {connect} from "react-redux";

const mapMessageToComponent = (message) => {
    if (!message) return (<div>SAD</div>);
    return <div key={message.timestamp ? message.timestamp : "key"} style={messageStyle}>{message}</div>
};

const ChatContainer = ({timeOrderedMessages}) => (
    <div>
        {timeOrderedMessages.length > 0
            ? timeOrderedMessages.map((message)=>{
                console.log(message);
                return mapMessageToComponent(message)
            })
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