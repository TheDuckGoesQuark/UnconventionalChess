import React, {Component} from "react";

import PropTypes from "prop-types";
import InitialConfiguration from "../commons/InitialConfiguration";
import BoardContainer from "./BoardContainer";
import ChatContainer from "./ChatContainer";
import WebsocketMiddleware from "./WebsocketMiddleware";
import DialogueBox from "./DialogueBox";

class GameView extends Component {

    static propTypes = {
        // Initial configuration for game
        initialConfig: PropTypes.instanceOf(InitialConfiguration),
        // Callback to inform parent when player exits game
        onExit: PropTypes.func
    };

    state = {
        timeOrderedMessages: []
    };

    // TODO allow board to send move via websocket client
    sendMove = (msg) => {
        this.clientRef.sendMessage('/app/chat.sendMessage', msg)
    };

    handleReceivedMessage = (message) => {
        const appendedMessageList = [...this.state.timeOrderedMessages, message];
        this.setState({timeOrderedMessages: appendedMessageList})
    };

    render() {
        const {onExit, initialConfig} = this.props;
        const {timeOrderedMessages} = this.state;

        return (
            <div>
                <WebsocketMiddleware
                    onMessage={this.handleReceivedMessage}
                    initialConfig={initialConfig}
                />
                <BoardContainer initialConfig={initialConfig} onMove={this.sendMove}/>
                <ChatContainer timeOrderedMessages={timeOrderedMessages}/>
                <DialogueBox message={timeOrderedMessages.last}/>
                <button onClick={onExit}>Exit Game</button>
            </div>
        );
    }
}

export default GameView;
