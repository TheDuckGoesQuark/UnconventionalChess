import {CONFIG_HUMAN_PLAYS_FIRST_SET, CONFIG_IS_SET} from "./ConfigActions";

const initialState = {
    humanPlaysFirst: true,
    configured: false,
};

export default function configReducer(state = initialState, action) {
    switch (action.type) {
        case CONFIG_HUMAN_PLAYS_FIRST_SET:
            console.log(action);
            return {
                ...state,
                humanPlaysFirst: action.payload.humanPlaysFirst
            };
        case CONFIG_IS_SET:
            return {
                ...state,
                configured: action.payload.isConfigured,
            };
        default:
            return state;
    }
}