import {WS_CONNECTED, WS_DISCONNECTED, WS_INITIALISED} from "./WebsocketActions";
import {MOVE_SEND} from "../board/BoardActions";

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
        case WS_INITIALISED:
            return {
                ...state,
                clientRef: action.payload.clientRef,
            };
        case MOVE_SEND:
        default:
            return state;
    }
}