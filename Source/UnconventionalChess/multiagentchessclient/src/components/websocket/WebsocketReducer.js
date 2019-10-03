import {WS_CONNECTED, WS_DISCONNECTED} from "./WebsocketActions";

const initialState = {
    connected: false,
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
        default:
            return state;
    }
}