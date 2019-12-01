import React from "react";
import {connect} from "react-redux";
import Chessboard from "chessboardjsx";
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
        position={props.piecePositions}
    />
);

function mapStateToProps(state) {
    return {
        piecePositions: state.configReducer.piecePositions
    };
}

const mapDispatchToProps = {
    onSquareClick: configSquareClicked,
};

export default connect(mapStateToProps, mapDispatchToProps)(ConfigBoard)
