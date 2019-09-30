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
        // messages received so far
        timeOrderedMessages: [],
        // reference to websocket client
        clientRef: undefined,
        // current connection status
        connected: false,
    };

    handleConnectionChange = (isConnected) => {
        this.setState({connected: isConnected})
    };

    sendMove = (msg) => {
        this.state.clientRef.sendMessage('/app/chat.sendMessage', msg)
    };

    registerClient = (client) => {
        this.setState({clientRef: client})
    };

    handleReceivedMessage = (message) => {
        this.setState(prevState => ({
            timeOrderedMessages: [...prevState.timeOrderedMessages, message]
        }));
    };

    renderGame = () => {
        const {onExit, initialConfig} = this.props;
        const {timeOrderedMessages} = this.state;

        return (<div>
            <button onClick={onExit}>Exit Game</button>
            <BoardContainer initialConfig={initialConfig} onMove={this.sendMove}/>
            <ChatContainer timeOrderedMessages={timeOrderedMessages}/>
            <DialogueBox message={timeOrderedMessages.last}/>
        </div>);
    };

    renderSpinner = () => {
        return <Spinner name={"Connection"} loadingImage={""}/>
    };

    render() {
        const {initialConfig} = this.props;
        const {connected} = this.state;
        console.log(`rendering ${connected}`);

        return (
            <div>
                <WebsocketMiddleware
                    onMessage={this.handleReceivedMessage}
                    onConnectionChange={this.handleConnectionChange}
                    initialConfig={initialConfig}
                    clientRef={this.registerClient}
                >
                    {connected ? this.renderGame() : this.renderSpinner()}
                </WebsocketMiddleware>
            </div>
        );
    }
}

export default GameView;
