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
            return {
                ...state,
                timeOrderedMessages: [...state.timeOrderedMessages, action.payload.message]
            };
        case MOVE_RECEIVE:
            return {
                ...state,
                timeOrderedMessages: [...state.timeOrderedMessages, action.payload.move]
            };
        default:
            return state;
    }
}
