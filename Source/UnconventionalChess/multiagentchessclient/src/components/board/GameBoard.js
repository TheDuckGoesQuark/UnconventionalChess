import React from "react";
import {connect} from "react-redux";
import Chessboard from "chessboardjsx";
import {
    draggedOverSquare,
    mouseOutSquare,
    mouseOverSquare, moveSend,
    pieceDropped,
    squareClicked,
    squareRightClicked
} from "./BoardActions";

/**
 * Validate move, then dispatch move to server if valid
 * @param move move to validate
 * @param game chess instance
 * @param sendMoveCallback dispatch function for move
 */
const handleMove = (move, game, sendMoveCallback) => {
    const {sourceSquare, targetSquare} = move;

    // see if the move is legal
    let moveMade = game.move({
        from: sourceSquare,
        to: targetSquare,
        promotion: "q" // always promote to a queen for example simplicity
    });

    // illegal move
    if (moveMade !== null) {
        // move game back in case as server is source of truth
        game.undo();
        // dispatch move
        sendMoveCallback(move);
    }
};

const GameBoard = (props) => (
    <Chessboard
        id="humanVsAgents"
        width={"500"}
        position={props.position}
        onDrop={(move) => handleMove(move, props.game, props.sendMove)}
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
    sendMove: moveSend,
    onMouseOutSquare: mouseOutSquare,
    onMouseOverSquare: mouseOverSquare,
    onDragOverSquare: draggedOverSquare,
    onSquareClick: squareClicked,
    onSquareRightClick: squareRightClicked,
};

function mapStateToProps(state) {
    return {
        game: state.boardReducer.game,
        position: state.boardReducer.fen,
        squareStyles: state.boardReducer.squareStyles,
        dropSquareStyle: state.boardReducer.dropSquareStyle,
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(GameBoard)

