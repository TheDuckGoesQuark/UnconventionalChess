import React, {Component} from "react";

import BoardContainer from "../board/BoardContainer";
import ChatContainer from "../chat/ChatContainer";
import WebsocketMiddleware from "../websocket/WebsocketMiddleware";
import DialogueBox from "../chat/DialogueBox";
import {connect} from "react-redux";

const GameContainer = (props) => (
    <div>
        <WebsocketMiddleware/>
        <div style={gameViewStyle}>
            <button style={exitButtonStyle} onClick={()=>console.log("waddup")}>Exit Game</button>
            <div style={boardContainerStyle}>
                <BoardContainer/>
            </div>
            <div style={dialogBoxContainerStyle}>
                <DialogueBox/>
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

function mapStateToProps(state) {
    return {
        connected: state.started,
    }
}

export default connect(mapStateToProps)(GameContainer);
