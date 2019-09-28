import React, {Component} from "react";

import WithMoveValidation from "../integrations/WithMoveValidation";
import SockJsClient from "react-stomp";
import PropTypes from "prop-types";
import InitialConfiguration from "../commons/InitialConfiguration";
import BoardContainer from "./BoardContainer";

class GameView extends Component {

    static propTypes = {
        // Initial configuration for game
        initialConfig: PropTypes.instanceOf(InitialConfiguration),
        // Callback to inform parent when player exits game
        onExit: PropTypes.func
    };

    sendMessage = (msg) => {
        this.clientRef.sendMessage('/app/chat.sendMessage', msg)
    };

    render() {
        return (
            <div>
                <BoardContainer/>
                <SockJsClient url='/ws' topics={['/topic/chess']}
                              onMessage={(msg) => {
                                  console.log(msg)
                              }}
                              ref={(client) => {
                                  this.clientRef = client
                              }}
                              debug={true}
                />
                <button onClick={() => this.sendMessage("hello world")}>Press me!</button>
            </div>
        );
    }
}


export default GameView;
