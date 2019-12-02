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

import Chess from 'chess.js';

const chess = new Chess();

const namesForPieceType = {
    'p': ['Rick Harrison', 'Richard Harrison', 'Austin Russell', 'John Smith', 'Bob', 'Jim', 'Ann', 'Margaret'],
    'n': ['Knight that says Ni', 'Keira Knightly', 'Sean Connery', 'Michael Caine'],
    'b': ['Francis', 'John Bishop'],
    'r': ['Abraham Brook', 'Rook Astley', 'Rook Sanchez', 'Brooke', 'Brooklyn', 'Rooky'],
    'q': ['Victoria', 'Elizabeth', 'Freddie', 'Cleopatra', 'Mary'],
    'k': ['Robert', 'George', 'Robb', ' The King of the Norf', 'Bran', 'Alfred'],
};

const getRandomFromList = (list) => {
    return list[Math.floor(Math.random() * list.length)];
};

const getRandomNameForPiece = (pieceType, currentNames) => {
    let name;
    let attempts = 0;
    // attempt to get a unique name or give up if none are available
    do {
        name = getRandomFromList(namesForPieceType[pieceType.toLowerCase()]);
        attempts++;
    } while (attempts < 8 && currentNames.includes(name));

    return name;
};

const getRandomPersonalityFromOptions = (personalityTypes) => {
    return getRandomFromList(personalityTypes);
};

const getRandomConfigForPiece = (currentConfig, currentNames, position, personalityTypes) => {
    // create if not exist
    if (!currentConfig) currentConfig = {};

    // add name if not exist
    if (!currentConfig.name) {
        const {type} = chess.get(position);
        currentConfig.name = getRandomNameForPiece(type, currentNames);
    }

    // add personality if not set
    if (!currentConfig.personality) {
        currentConfig.personality = getRandomPersonalityFromOptions(personalityTypes);
    }

    return currentConfig;
};

const fillInBlanksRandomly = (pieceConfigs, rowsToCheck, personalityTypes) => {
    const currentNames = new Set();

    rowsToCheck.forEach(row => {
        const coord = 'a' + row;
        if (!pieceConfigs[coord] || !pieceConfigs[coord].name || !pieceConfigs.personality) {
            pieceConfigs[coord] = getRandomConfigForPiece(pieceConfigs[coord], currentNames, coord, personalityTypes);
        }
        currentNames.add(pieceConfigs[coord].name)
    });
};

const fillInPieceConfigBlanks = (state) => {
    // check which pieces we need to fill in random values for
    let {humanPlays, humanPlaysAsWhite, personalityTypes} = state;
    let checkBlack = !humanPlays || humanPlaysAsWhite;
    let checkWhite = !humanPlays || humanPlaysAsWhite;

    let rowsToCheck = [];
    if (checkBlack) rowsToCheck = [...rowsToCheck, 7, 8];
    if (checkWhite) rowsToCheck = [...rowsToCheck, 1, 2];

    state.pieceConfigs = fillInBlanksRandomly(state.pieceConfigs, rowsToCheck, personalityTypes)
};

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
            // ensure all pieces that need to be configured are
            fillInPieceConfigBlanks(state);
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

