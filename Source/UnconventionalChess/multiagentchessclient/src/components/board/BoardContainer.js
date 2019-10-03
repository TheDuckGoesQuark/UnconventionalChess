import React, {Component} from "react";

import GameBoard from "./GameBoard";

const BoardContainer = () => (
    <div style={boardContainerStyle}>
        <GameBoard/>
    </div>
);

const boardContainerStyle = {
    display: "flex",
    justifyContent: "space-around",
    alignItems: "center",
    flexWrap: "wrap",
    width: "100%",
    height: "100%",
};

export default BoardContainer;
