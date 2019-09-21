import React, {Component} from "react";
import SockJsClient from 'react-stomp';

import WithMoveValidation from "./integrations/WithMoveValidation";

class Demo extends Component {

    sendMessage = (msg) => {
        this.clientRef.sendMessage('/app/chat.sendMessage', msg)
    };

    render() {
        return (
            <div>
                <div style={boardsContainer}>
                    <WithMoveValidation/>
                </div>
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

export default Demo;

const boardsContainer = {
    display: "flex",
    justifyContent: "space-around",
    alignItems: "center",
    flexWrap: "wrap",
    width: "100vw",
    marginTop: 30,
    marginBottom: 50
};
