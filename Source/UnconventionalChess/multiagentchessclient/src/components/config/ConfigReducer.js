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
import {MOVE_RECEIVE} from "../board/BoardActions";

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

const adjacent = (sourceSquare, targetSquare) => {
    let sameCol = sourceSquare.charCodeAt(0) === targetSquare.charCodeAt(0);
    let sameRow = sourceSquare.charCodeAt(1) === targetSquare.charCodeAt(1);
    let colsAdjacent = Math.abs(sourceSquare.charCodeAt(0) - targetSquare.charCodeAt(0)) === 1;
    let rowsAdjacent = Math.abs(sourceSquare.charCodeAt(1) - targetSquare.charCodeAt(1)) === 1;
    return (sameCol && rowsAdjacent)
        || (sameRow && colsAdjacent)
        || (colsAdjacent && rowsAdjacent);
};

const castlingOccurred = (configAtSource, move) => {
    return (configAtSource.type === 'k' || configAtSource.type === 'K')
        && !adjacent(move.sourceSquare, move.targetSquare)
};

const applyCastingToRook = (kingConfig, kingMove, pieceConfigs) => {
    let {targetSquare} = kingMove;
    let rookSource;
    let rookTarget;

    // white king uppercase
    if (kingConfig.type === 'K') {
        if (targetSquare === 'g1') {
            rookSource = 'h1';
            rookTarget = 'f1';
        } else {
            rookSource = 'a1';
            rookTarget = 'd1';
        }
    } else {
        if (targetSquare === 'g8') {
            rookSource = 'h8';
            rookTarget = 'f8';
        } else {
            rookSource = 'a8';
            rookTarget = 'd8';
        }
    }

    return movePiece(rookSource, rookTarget, pieceConfigs[rookSource], pieceConfigs)
};

const applyMoveToPieceConfigs = (pieceConfigs, move) => {
    let {sourceSquare, targetSquare} = move;
    let configAtSource = pieceConfigs[sourceSquare];
    let newConfig = movePiece(sourceSquare, targetSquare, configAtSource, pieceConfigs);

    if (configAtSource && castlingOccurred(configAtSource, move)) {
        return applyCastingToRook(configAtSource, move);
    } else {
        return newConfig;
    }
};

const movePiece = (sourceSquare, targetSquare, configAtSource, pieceConfigs) => {
    return {
        ...pieceConfigs,
        [targetSquare]: {
            ...configAtSource
        },
        [sourceSquare]: undefined,
    };
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
        case MOVE_RECEIVE:
            return {
                ...state,
                pieceConfigs: applyMoveToPieceConfigs(state.pieceConfigs, action.payload.move)
            };
        default:
            return state;
    }
}

