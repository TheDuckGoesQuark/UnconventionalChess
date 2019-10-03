import React from "react";
import SockJsClient from "react-stomp";
import {connect} from "react-redux";
import {receiveChatMessage} from "../chat/ChatActions";
import {moveReceive} from "../board/BoardActions";
import {wsConnected, wsDisconnected} from "./WebsocketActions";
import {ChatMessage, MoveMessage} from "../../models/Chat";

const handleMessage = (message, handlerByType) => {
    if (message.type && handlerByType[message.type]) {
        handlerByType[message.type](message);
    } else {
        throw new TypeError("Unknown message type received");
    }
};

// TODO on connect, send initial configuration
const WebsocketMiddleware = (props) => (
    <SockJsClient
        // Base URL for websocket connections
        url='/ws'
        // Topics to subscribe to
        topics={['/topic/chess']}
        // Message handlers
        onMessage={(message) => handleMessage(message, {
            [ChatMessage.TYPE]: props.receiveChatMessage,
            [MoveMessage.TYPE]: props.receiveMoveMessage,
        })}
        // Handlers for connection and disconnection events
        onConnect={props.onConnect}
        onDisconnect={props.onDisconnect}
        // configures logging level
        debug={true}
    />
);

const mapDispatchToProps = {
    onDisconnect: wsDisconnected,
    onConnect: wsConnected,
    receiveChatMessage: receiveChatMessage,
    receiveMoveMessage: moveReceive
};

export default connect(null, mapDispatchToProps)(WebsocketMiddleware);
