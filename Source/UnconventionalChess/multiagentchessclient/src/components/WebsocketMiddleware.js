import React, {Component} from "react";
import SockJsClient from "react-stomp";
import PropTypes from "prop-types";
import InitialConfiguration from "../commons/InitialConfiguration";

class WebsocketMiddleware extends Component {

    static propTypes = {
        initialConfig: PropTypes.instanceOf(InitialConfiguration),
        onMessage: PropTypes.func,
    };

    render() {
        const {onMessage} = this.props;

        // TODO on connect, send initial configuration
        return (<SockJsClient
            // Base URL for websocket connections
            url='/ws'
            // Topics to subscribe to
            topics={['/topic/chess']}
            // Handler for incoming message from subscribed topics
            onMessage={onMessage}
            // passes up websocket client
            ref={(client) => {this.clientRef = client}}
            // configures logging level
            debug={true}
        />);
    }
}

export default WebsocketMiddleware;
