import React from "react";
import {connect} from "react-redux";
import Chessboard from "chessboardjsx";
import {
    draggedOverSquare,
    mouseOutSquare,
    mouseOverSquare,
    pieceDropped,
    squareClicked,
    squareRightClicked
} from "./BoardActions";

const GameBoard = (props) => (
    <Chessboard
        id="humanVsAgents"
        width={"350"}
        position={props.position}
        onDrop={(move) => props.onDrop(move)}
        onMouseOverSquare={props.onMouseOverSquare}
        onMouseOutSquare={props.onMouseOutSquare}
        boardStyle={{
            borderRadius: "5px",
            boxShadow: `0 5px 15px rgba(0, 0, 0, 0.5)`
        }}
        squareStyles={props.squareStyles}
        dropSquareStyle={props.dropSquareStyle}
        onDragOverSquare={props.onDragOverSquare}
        onSquareClick={props.onSquareClick}
        onSquareRightClick={props.onSquareRightClick}
    />
);

const mapDispatchToProps = {
    onDrop: pieceDropped,
    onMouseOutSquare: mouseOutSquare,
    onMouseOverSquare: mouseOverSquare,
    onDragOverSquare: draggedOverSquare,
    onSquareClick: squareClicked,
    onSquareRightClick: squareRightClicked,
};

function mapStateToProps(state) {
    return {
        position: state.boardReducer.fen,
        squareStyles: state.boardReducer.squareStyles,
        dropSquareStyle: state.boardReducer.dropSquareStyle,
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(GameBoard)

export const squareStyling = ({pieceSquare, history}) => {
    const sourceSquare = history.length && history[history.length - 1].from;
    const targetSquare = history.length && history[history.length - 1].to;

    return {
        [pieceSquare]: {backgroundColor: "rgba(255, 255, 0, 0.4)"},
        ...(history.length && {
            [sourceSquare]: {
                backgroundColor: "rgba(255, 255, 0, 0.4)"
            }
        }),
        ...(history.length && {
            [targetSquare]: {
                backgroundColor: "rgba(255, 255, 0, 0.4)"
            }
        })
    };
};
