import {
    CONFIG_HUMAN_PLAYS_AS_WHITE_SET,
    CONFIG_HUMAN_PLAYS_SET,
    CONFIG_GAME_READY,
    CONFIG_SUBMITTED,
} from "./ConfigActions";

const initialState = {
    humanPlays: true,
    humanPlaysAsWhite: true,
    configSubmitted: false,
    gameId: undefined
};

export default function configReducer(state = initialState, action) {
    switch (action.type) {
        case CONFIG_HUMAN_PLAYS_AS_WHITE_SET:
            return {
                ...state,
                humanPlaysAsWhite: action.payload.humanPlaysAsWhite
            };
        case CONFIG_HUMAN_PLAYS_SET:
            return {
                ...state,
                humanPlays: action.payload.humanPlays
            };
        case CONFIG_GAME_READY:
            return {
                ...state,
                gameId: action.payload.gameId
            };
        case CONFIG_SUBMITTED:
            return {
                ...state,
                configSubmitted: true
            };
        default:
            return state;
    }
}