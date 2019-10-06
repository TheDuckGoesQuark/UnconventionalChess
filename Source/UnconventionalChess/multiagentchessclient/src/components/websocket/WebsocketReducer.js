import {WS_CONNECTED, WS_DISCONNECTED, WS_INITIALISED} from "./WebsocketActions";
import {MOVE_SEND} from "../board/BoardActions";
import {MoveMessage} from "../../models/Chat";

const initialState = {
    connected: false,
    clientRef: null,
};

const sendMove = (state, action) => {
    const {clientRef, gameId} = state;
    const {sourceSquare, targetSquare, piece} = action.payload.move;

    if (!clientRef) return state;

    clientRef.sendMessage(`/app/game.${gameId}.move`, JSON.stringify(new MoveMessage(sourceSquare, targetSquare, piece)));

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