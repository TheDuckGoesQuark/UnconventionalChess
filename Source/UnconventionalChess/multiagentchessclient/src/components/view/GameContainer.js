import React from "react";

import BoardContainer from "../board/BoardContainer";
import ChatContainer from "../chat/ChatContainer";
import WebsocketMiddleware from "../websocket/WebsocketMiddleware";
import {connect} from "react-redux";
import {resetConfig} from "../config/ConfigActions";

const GameContainer = (props) => (
    <div>
        <WebsocketMiddleware/>
        <div style={gameViewStyle}>
            <button style={exitButtonStyle} onClick={props.exit}>Exit Game</button>
            <div style={boardContainerStyle}>
                <BoardContainer/>
            </div>
            <div style={chatContainerStyle}>
                <ChatContainer/>
            </div>
        </div>
    </div>
);

const exitButtonStyle = {
    position: "absolute"
};

const gameViewStyle = {
    margin: "auto",
    width: "100vw",
    height: "100vh",
    backgroundColor: "black",
};

const boardContainerStyle = {
    width: "100%",
    height: "70%",
    float: "left",
    backgroundColor: "black",
    border: "14px ridge rgba(164,133,81,0.82)",
    boxSizing: "border-box"
};

const chatContainerStyle = {
    width: "100%",
    height: "30%",
    backgroundColor: "black",
    border: "14px ridge rgba(164,133,81,0.82)",
    boxSizing: "border-box"
};

const mapDispatchToProps = {
    exit: resetConfig
};

function mapStateToProps(state) {
    return {
        configured: state.configReducer.configured,
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(GameContainer);
