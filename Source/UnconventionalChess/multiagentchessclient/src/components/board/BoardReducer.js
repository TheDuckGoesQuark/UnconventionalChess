import {
    DRAG_OVER_SQUARE,
    GAME_START,
    MOUSE_OUT_SQUARE, MOUSE_OVER_SQUARE,
    MOVE_RECEIVE,
    PIECE_DROPPED,
    SQUARE_CLICK,
    SQUARE_RIGHT_CLICK
} from "./BoardActions";
import Chess from "chess.js"
import {squareStyling} from "./GameBoard";
import {CONFIG_RESET} from "../config/ConfigActions";

const initialState = {
    fen: "start",
    // square styles for active drop square
    dropSquareStyle: {},
    // custom square styles
    squareStyles: {},
    // square with the currently clicked piece
    pieceSquare: "",
    // currently clicked square
    square: "",
    // array of past game moves
    history: [],
    // instance of chess.js
    game: new Chess()
};

const handleSquareClick = (state, action) => {
    const {square} = action.payload;
    const {history, game, pieceSquare} = state;

    // restore styling for pieces
    const result = {
        ...state,
        squareStyles: squareStyling({pieceSquare: square, history}),
        pieceSquare: square
    };

    let move = game.move({
        from: pieceSquare,
        to: square,
        promotion: "q" // always promote to a queen for example simplicity
    });

    // illegal move, return original state
    if (move === null) return state;

    return {
        ...result,
        fen: game.fen(),
        history: game.history({verbose: true}),
        pieceSquare: ""
    }
};

const handleDragOverSquare = (state, action) => {
    const {square} = action.payload;
    return {
        ...state,
        dropSquareStyle:
            square === "e4" || square === "d4" || square === "e5" || square === "d5"
                ? {backgroundColor: "cornFlowerBlue"}
                : {boxShadow: "inset 0 0 1px 4px rgb(255, 255, 0)"}
    };
};

// keep clicked square style and remove hint squares
const removeHighlightSquare = (state) => {
    const {pieceSquare, history} = state;

    return {
        ...state,
        squareStyles: squareStyling({pieceSquare, history})
    }
};

// show possible moves
const highlightSquare = (state, sourceSquare, squaresToHighlight) => {
    const {squareStyles, history, pieceSquare} = state;

    const highlightStyles = [sourceSquare, ...squaresToHighlight].reduce(
        (a, c) => {
            return {
                ...a,
                ...{
                    [c]: {
                        background:
                            "radial-gradient(circle, #fffc00 36%, transparent 40%)",
                        borderRadius: "50%"
                    }
                },
                ...squareStyling({
                    history: history,
                    pieceSquare: pieceSquare
                })
            };
        },
        {}
    );

    return {
        ...state,
        squareStyles: {...squareStyles, ...highlightStyles}
    }
};

const handleMouseOverSquare = (state, action) => {
    const {game} = state;
    const {square} = action.payload;

    // get list of possible moves for this square
    let moves = game.moves({
        square: square,
        verbose: true
    });

    // exit if there are no moves available for this square
    if (moves.length === 0) return state;

    let squaresToHighlight = [];
    for (let i = 0; i < moves.length; i++) {
        squaresToHighlight.push(moves[i].to);
    }

    return highlightSquare(state, square, squaresToHighlight);
};

const handlePieceDropped = (state, action) => {
    return state;
};

const handleReceivedMove = (state, action) => {
    const {game, pieceSquare, history} = state;
    const {sourceSquare, targetSquare} = action.payload.move;

    // see if the move is legal
    let move = game.move({
        from: sourceSquare,
        to: targetSquare,
        promotion: "q" // always promote to a queen for example simplicity
    });

    // illegal move
    if (move === null) return state;
    else return {
        ...state,
        fen: game.fen(),
        history: game.history({verbose: true}),
        squareStyles: squareStyling({pieceSquare, history})
    };
};

export default function boardReducer(state = initialState, action) {
    switch (action.type) {
        case MOVE_RECEIVE:
            return handleReceivedMove(state, action);
        case GAME_START:
        case CONFIG_RESET:
            return {
                ...state,
                ...initialState,
                game: new Chess()
            };
        case SQUARE_CLICK :
            return handleSquareClick(state, action);
        case SQUARE_RIGHT_CLICK :
            return {
                ...state,
                squareStyles: {[action.payload.square]: {backgroundColor: "deepPink"}}
            };
        case DRAG_OVER_SQUARE :
            return handleDragOverSquare(state, action);
        case MOUSE_OUT_SQUARE :
            return removeHighlightSquare(state);
        case MOUSE_OVER_SQUARE :
            return handleMouseOverSquare(state, action);
        case PIECE_DROPPED :
            return handlePieceDropped(state, action);
        default:
            return state;
    }
}