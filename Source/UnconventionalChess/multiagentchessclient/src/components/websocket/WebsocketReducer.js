import {WS_CONNECTED, WS_DISCONNECTED, WS_INITIALISED} from "./WebsocketActions";
import {MOVE_SEND} from "../board/BoardActions";
import {Message, MoveMessage} from "../../models/Message";

const initialState = {
    connected: false,
    clientRef: null,
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
            return state;
        case WS_INITIALISED:
            return {
                ...state,
                clientRef: action.payload.clientRef,
            };
        default:
            return state;
    }
}