import React, {Component} from "react";
import SockJsClient from "react-stomp";
import {connect} from "react-redux";

// TODO on connect, send initial configuration
const WebsocketMiddleware = (props) => (
    <SockJsClient
        // Base URL for websocket connections
        url='/ws'
        // Topics to subscribe to
        topics={['/topic/chess']}
        // Handler for incoming message from subscribed topics
        onMessage={props.onMessage}
        // Handlers for connection and disconnection events
        onConnect={props.onConnect}
        onDisconnect={props.onDisconnect}
        // configures logging level
        debug={true}
    />
);

const mapDispatchToProps = {
    onDisconnect: null,
    onConnect: null,
    onMessage: null,
};

export default connect(null, mapDispatchToProps)(WebsocketMiddleware);
