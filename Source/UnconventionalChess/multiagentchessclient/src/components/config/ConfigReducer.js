import {
    CONFIG_HUMAN_PLAYS_AS_WHITE_SET,
    CONFIG_HUMAN_PLAYS_SET,
    CONFIG_GAME_READY,
    CONFIG_SUBMITTED,
    CONFIG_RESET,
    CONFIG_SQUARE_CLICKED,
    CONFIG_FETCH_PERSONALITIES_ERROR,
    CONFIG_FETCH_PERSONALITIES,
    CONFIG_FETCH_PERSONALITIES_SUCCESS,
    CONFIG_PIECE_NAME_UPDATED,
    CONFIG_PIECE_CONFIG_SAVED,
    CONFIG_PIECE_PERSONALITY_UPDATED,
} from "./ConfigActions";

const initialState = {
    humanPlays: true,
    humanPlaysAsWhite: true,
    configSubmitted: false,
    gameId: undefined,
    configuringSquare: undefined,
    piecePositions: "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
    pieceConfigs: {},
    personalityTypes: [],
    error: null
};

export default function configReducer(state = initialState, action) {

    switch (action.type) {
        case CONFIG_HUMAN_PLAYS_AS_WHITE_SET:
            return {
                ...state,
                humanPlaysAsWhite: action.payload.humanPlaysAsWhite,
                configuringSquare: undefined
            };
        case CONFIG_HUMAN_PLAYS_SET:
            return {
                ...state,
                humanPlays: action.payload.humanPlays,
                configuringSquare: undefined
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
        case CONFIG_RESET:
            return {
                ...state,
                ...initialState
            };
        case CONFIG_SQUARE_CLICKED:
            return {
                ...state,
                configuringSquare: action.payload.square
            };
        case CONFIG_FETCH_PERSONALITIES:
            return state;
        case CONFIG_FETCH_PERSONALITIES_SUCCESS:
            return {
                ...state,
                personalityTypes: action.payload.personalityTypes
            };
        case CONFIG_FETCH_PERSONALITIES_ERROR:
            return {
                ...state,
                error: action.payload.error
            };
        case CONFIG_PIECE_NAME_UPDATED:
            return {
                ...state,
                pieceConfigs: {
                    ...state.pieceConfigs,
                    [state.configuringSquare]: {
                        ...state.pieceConfigs[state.configuringSquare],
                        name: action.payload.newName
                    }
                }
            };
        case CONFIG_PIECE_PERSONALITY_UPDATED:
            // have to copy every nested level of the objects
            // looks a bit nasty, but redux does recommend flat state for this reason
            return {
                ...state,
                pieceConfigs: {
                    ...state.pieceConfigs,
                    [state.configuringSquare]: {
                        ...state.pieceConfigs[state.configuringSquare],
                        personality: action.payload.newPersonality
                    }
                }
            };
        case CONFIG_PIECE_CONFIG_SAVED:
            return {
                ...state,
                configuringSquare: undefined,
            };
        default:
            return state;
    }
}

