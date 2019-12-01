import React, {useEffect} from "react";
import {connect} from "react-redux";
import Chessboard from "chessboardjsx";
import {configSquareClicked, fetchPersonalities} from "./ConfigActions";
import PieceConfig from "./PieceConfig";

const ConfigBoard = (props) => {
    useEffect(props.fetchPersonalityTypes, []);
    return <div>
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
        {props.configuringSquare ? <PieceConfig/> : null}
        {props.error}
    </div>
};

function mapStateToProps(state) {
    return {
        configuringSquare: state.configReducer.configuringSquare,
        piecePositions: state.configReducer.piecePositions,
        error: state.configReducer.error,
    };
}

const mapDispatchToProps = {
    onSquareClick: configSquareClicked,
    fetchPersonalityTypes: fetchPersonalities
};

export default connect(mapStateToProps, mapDispatchToProps)(ConfigBoard)
