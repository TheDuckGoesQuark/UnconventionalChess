import React from "react";
import {connect} from "react-redux";
import Chessboard from "chessboardjsx";
import mapStateToProps from "react-redux/lib/connect/mapStateToProps";
import {configSquareClicked} from "./ConfigActions";

const ConfigBoard = (props) => (
    <Chessboard
        id="configBoard"
        width={"500"}
        boardStyle={{
            borderRadius: "5px",
            boxShadow: `0 5px 15px rgba(0, 0, 0, 0.5)`
        }}
        onSquareClick={props.onSquareClick}
    />
);

mapStateToProps = {
    onSquareClick: configSquareClicked
};

export default connect(mapStateToProps)(ConfigBoard)
