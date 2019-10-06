import {WS_CONNECTED, WS_DISCONNECTED, WS_INITIALISED} from "./WebsocketActions";
import {MOVE_SEND} from "../board/BoardActions";
import {Message, MoveMessage} from "../../models/Message";

const initialState = {
    connected: false,
    clientRef: null,
};

const sendMove = (state, action) => {
    const {clientRef} = state;
    const {sourceSquare, targetSquare, piece, gameId} = action.payload.move;

    if (!clientRef) return state;

    const move = new MoveMessage(sourceSquare, targetSquare, piece);
    const message = new Message(MoveMessage.TYPE, gameId, move);
    clientRef.sendMessage(`/game.${gameId}.move`, JSON.stringify(message));

    return state;
};

export default function websocketReducer(state = initialState, action) {
    switch (action.type) {
        case WS_CONNECTED:
            return {
                ...state,
                connected: true
            };
        case WS_DISCONNECTED:
            return {
                ...state,
                connected: false
            };
        case MOVE_SEND:
            return sendMove(state, action);
        case WS_INITIALISED:
            return {
                ...state,
                clientRef: action.payload.clientRef,
            };
        default:
            return state;
    }
}