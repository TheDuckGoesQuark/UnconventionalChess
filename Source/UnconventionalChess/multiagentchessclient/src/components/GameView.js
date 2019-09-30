import React, {Component} from "react";

import PropTypes from "prop-types";
import InitialConfiguration from "../commons/InitialConfiguration";
import BoardContainer from "./BoardContainer";
import ChatContainer from "./ChatContainer";
import WebsocketMiddleware from "./WebsocketMiddleware";
import DialogueBox from "./DialogueBox";
import Spinner from "./Spinner";

class GameView extends Component {

    static propTypes = {
        // Initial configuration for game
        initialConfig: PropTypes.instanceOf(InitialConfiguration),
        // Callback to inform parent when player exits game
        onExit: PropTypes.func
    };

    state = {
        timeOrderedMessages: [],
        clientRef: undefined
    };

    isConnected = () => {
        const {clientRef} = this.state.clientRef;

        return clientRef !== undefined && clientRef.isConnected();
    };

    sendMove = (msg) => {
        this.state.clientRef.sendMessage('/app/chat.sendMessage', msg)
    };

    registerClient = (client) => {
        this.setState((prevState) => ({clientRef: client}))
    };

    handleReceivedMessage = (message) => {
        this.setState(prevState => ({
            timeOrderedMessages: [...prevState.timeOrderedMessages, message]
        }));
    };

    render() {
        const {onExit, initialConfig} = this.props;
        const {timeOrderedMessages} = this.state;

        return (
            <div>
                <WebsocketMiddleware
                    onMessage={this.handleReceivedMessage}
                    initialConfig={initialConfig}
                    clientRef={this.registerClient}
                >
                    {this.isConnected() ?
                        <div>
                            <button onClick={onExit}>Exit Game</button>
                            < BoardContainer initialConfig={initialConfig} onMove={this.sendMove}/>
                            <ChatContainer timeOrderedMessages={timeOrderedMessages}/>
                            <DialogueBox message={timeOrderedMessages.last}/>
                        </div>
                        : <Spinner name={"Connection"} loadingImage={""}/>
                    }
                </WebsocketMiddleware>
            </div>
        );
    }
}

export default GameView;
