import {
    CHAT_MESSAGE_RECEIVE,
} from "./ChatActions"

import {
    MOVE_RECEIVE
} from "../board/BoardActions";

const initialState = {
    timeOrderedMessages: []
};

export default function chatReducer(state = initialState, action) {
    switch (action.type) {
        case CHAT_MESSAGE_RECEIVE:
            console.log(action);

            return {
                ...state,
                timeOrderedMessages: [action.payload.message, ...state.timeOrderedMessages]
            };
        case MOVE_RECEIVE:
            return {
                ...state,
                timeOrderedMessages: [action.payload.move, ...state.timeOrderedMessages]
            };
        default:
            return state;
    }
}
