import React from "react";
import SockJsClient from "react-stomp";
import {connect} from "react-redux";
import {receiveChatMessage} from "../chat/ChatActions";
import {receiveMove} from "../board/BoardActions";
import {wsConnected, wsDisconnected, wsInitialised} from "./WebsocketActions";
import {ChatMessage, MoveMessage} from "../../models/Message";


const MESSAGE_DELAY_MS = 2000;

let lastMessageSent = new Date().getTime();

const handleMessage = (message, handlerByType) => {
    let currentTime = new Date().getTime();
    let delay;

    // throttle messages so they seem realistic
    if (lastMessageSent > currentTime) {
        delay = (lastMessageSent - currentTime) + MESSAGE_DELAY_MS;
        lastMessageSent = currentTime + delay;
    } else {
        delay = 0;
        lastMessageSent = currentTime + MESSAGE_DELAY_MS;
    }

    setTimeout(() => {
        if (message.type && handlerByType[message.type]) {
            handlerByType[message.type](message.body);
        } else {
            throw new TypeError("Unknown message type received");
        }
    }, delay)
};

const reformatMoveMessage = (move) => {
    move.sourceSquare = move.sourceSquare.toLowerCase();
    move.targetSquare = move.targetSquare.toLowerCase();
    return move;
};

const WebsocketMiddleware = (props) => (
    <SockJsClient
        // Base URL for websocket connections
        url='/ws'
        // Topics to subscribe to
        topics={[`/topic/game.${props.gameId}`]}
        // Message handlers
        onMessage={(message) => handleMessage(message, {
            [ChatMessage.TYPE]: props.receiveChatMessage,
            [MoveMessage.TYPE]: (message) => props.receiveMoveMessage(reformatMoveMessage(message)),
        })}
        // Handlers for connection and disconnection events
        onConnect={props.onConnect}
        onDisconnect={props.onDisconnect}
        // configures logging level
        debug={true}
        // store client reference to send messages
        ref={props.socketEstablished}
    />
);

function mapStateToProps(state) {
    return {
        gameId: state.configReducer.gameId
    }
}

const mapDispatchToProps = {
    onDisconnect: wsDisconnected,
    onConnect: wsConnected,
    receiveChatMessage: receiveChatMessage,
    receiveMoveMessage: receiveMove,
    socketEstablished: wsInitialised
};

export default connect(mapStateToProps, mapDispatchToProps)(WebsocketMiddleware);
