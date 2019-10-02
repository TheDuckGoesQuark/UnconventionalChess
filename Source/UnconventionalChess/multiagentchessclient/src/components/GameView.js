import React, {Component} from "react";

import PropTypes from "prop-types";
import InitialConfiguration from "../commons/InitialConfiguration";
import BoardContainer from "./BoardContainer";
import ChatContainer from "./ChatContainer";
import WebsocketMiddleware from "./WebsocketMiddleware";
import DialogueBox from "./DialogueBox";
import Spinner from "./Spinner";
import {ChatMessage, TranscriptMessage} from "../commons/Chat";


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

    componentDidMount() {
        setInterval(() => this.sendMessage(new ChatMessage(new Date(), "the-client", "hey server!")), 5000);
    }

    renderGame = () => {
        const {onExit, initialConfig} = this.props;
        const {timeOrderedMessages} = this.state;

        return (<div style={gameViewStyle}>
            <button style={exitButtonStyle} onClick={onExit}>Exit Game</button>
            <div style={boardContainerStyle}>
                <BoardContainer initialConfig={initialConfig} onMove={this.sendMessage}/>
            </div>
            <div style={dialogBoxContainerStyle}>
                <DialogueBox message={timeOrderedMessages.last}/>
            </div>
            <div style={chatContainerStyle}>
                <ChatContainer timeOrderedMessages={timeOrderedMessages}/>
            </div>
        </div>);
    };

    renderSpinner = () => {
        return <Spinner name={"Connection"} loadingImage={""}/>
    };

    render() {
        const {connected} = this.state;

        return (
            <div>
                <WebsocketMiddleware/>
                {connected ? this.renderGame() : this.renderSpinner()}
            </div>
        );
    }
}

const exitButtonStyle = {
    position: "absolute"
};

const gameViewStyle = {
    margin: "auto",
    width: "100vw",
    height: "100vh",
    backgroundColor: "black",
};

const dialogBoxContainerStyle = {
    width: "60%",
    height: "30%",
    float: "left",
    backgroundColor: "black",
    border: "14px ridge rgba(164,133,81,0.82)",
    boxSizing: "border-box"
};

const boardContainerStyle = {
    width: "60%",
    height: "70%",
    float: "left",
    backgroundColor: "black",
    border: "14px ridge rgba(164,133,81,0.82)",
    boxSizing: "border-box"
};

const chatContainerStyle = {
    width: "40%",
    height: "100%",
    marginLeft: "60%",
    backgroundColor: "black",
    border: "14px ridge rgba(164,133,81,0.82)",
    boxSizing: "border-box"
};

export default GameView;
