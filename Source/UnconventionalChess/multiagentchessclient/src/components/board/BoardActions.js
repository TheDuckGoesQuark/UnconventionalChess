export const MOVE_SEND = "MOVE_SEND";
export const MOVE_RECEIVE = "MOVE_RECEIVE";
export const GAME_START = "GAME_START";
export const SQUARE_CLICK = "SQUARE_CLICK";
export const SQUARE_RIGHT_CLICK = "SQUARE_RIGHT_CLICK";
export const DRAG_OVER_SQUARE = "DRAG_OVER_SQUARE";
export const MOUSE_OUT_SQUARE = "MOUSE_OUT_SQUARE";
export const MOUSE_OVER_SQUARE = "MOUSE_OVER_SQUARE";
export const PIECE_DROPPED = "PIECE_DROPPED";

export const moveSend = (move) => ({
    type: MOVE_SEND,
    payload: {move}
});

export const receiveMove = (move) => ({
    type: MOVE_RECEIVE,
    payload: {move}
});

export const gameStart = () => ({
    type: GAME_START,
});

export const squareClicked = (square) => ({
    type: SQUARE_CLICK,
    payload: {square}
});

export const squareRightClicked = (square) => ({
    type: SQUARE_RIGHT_CLICK,
    payload: {square}
});

export const draggedOverSquare = (square) => ({
    type: DRAG_OVER_SQUARE,
    payload: {square}
});

export const mouseOverSquare = (square) => ({
    type: MOUSE_OVER_SQUARE,
    payload: {square}
});

export const mouseOutSquare = (square) => ({
    type: MOUSE_OUT_SQUARE,
    payload: {square}
});

export const pieceDropped = ({sourceSquare, targetSquare}) => ({
    type: PIECE_DROPPED,
    payload: {sourceSquare, targetSquare}
});