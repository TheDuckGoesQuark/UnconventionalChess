import React, {Component} from "react";

import WithMoveValidation from "./WithMoveValidation";

const BoardContainer = () => (
    <div style={boardContainerStyle}>
        <WithMoveValidation/>
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
