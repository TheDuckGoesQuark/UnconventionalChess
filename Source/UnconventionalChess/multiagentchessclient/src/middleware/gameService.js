import {CONFIG_SUBMITTED, gameReady} from "../components/config/ConfigActions"
import {GameConfigurationMessage, Message, MoveMessage} from "../models/Message";
import {MOVE_SEND} from "../components/board/BoardActions";

import Chess from 'chess.js';

const chess = new Chess();

const namesForPieceType = {
    'p': ['Rick-Harrison', 'Richard-Harrison', 'Austin-Russell', 'John-Smith', 'Bob', 'Jim', 'Ann', 'Margaret'],
    'n': ['Knight-that-says-Ni', 'Keira-Knightly', 'Sean-Connery', 'Michael-Caine'],
    'b': ['Francis', 'John-Bishop'],
    'r': ['Abraham-Brook', 'Rook-Astley', 'Rook-Sanchez', 'Brooke', 'Brooklyn', 'Rooky'],
    'q': ['Victoria', 'Elizabeth', 'Freddie', 'Cleopatra', 'Mary', 'Daenerys'],
    'k': ['Robert', 'George', 'Robb', ' The-King-of-the-Norf', 'Bran', 'Alfred', 'Viserys'],
};

const getRandomFromList = (list) => {
    return list[Math.floor(Math.random() * list.length)];
};

const getRandomNameForPiece = (pieceType, currentNames) => {
    let lowercasePieceType = pieceType.toLowerCase();
    let namesForType = namesForPieceType[lowercasePieceType];
    // get a unique name
    let name = getRandomFromList(namesForType);
    // remove name since its now in use
    for (let i = 0; i < namesForType.length; i++) {
        if (namesForType[i] === name) {
            namesForType.splice(i, 1);
            break;
        }
    }
    // update list
    namesForPieceType[lowercasePieceType] = namesForType;

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
        for (let i = 0; i <= 7; i++) {
            // iterate over files (columns)
            let coord = (i + 10).toString(36) + row;
            // if any field is incomplete
            if (!pieceConfigs[coord] || !pieceConfigs[coord].name || !pieceConfigs.personality) {
                pieceConfigs[coord] = getRandomConfigForPiece(pieceConfigs[coord], currentNames, coord, personalityTypes);
            }
            currentNames.add(pieceConfigs[coord].name)
        }
    });

    return pieceConfigs;
};

const fillInPieceConfigBlanks = (state) => {
    // check which pieces we need to fill in random values for
    let {humanPlays, humanPlaysAsWhite, personalityTypes} = state;
    let checkBlack = !humanPlays || humanPlaysAsWhite;
    let checkWhite = !humanPlays || !humanPlaysAsWhite;

    let rowsToCheck = [];
    if (checkBlack) rowsToCheck = [...rowsToCheck, 7, 8];
    if (checkWhite) rowsToCheck = [...rowsToCheck, 1, 2];

    state.pieceConfigs = fillInBlanksRandomly(state.pieceConfigs, rowsToCheck, personalityTypes)
};

const createGame = (config, dispatch) => {
    fillInPieceConfigBlanks(config);
    console.log(config);

    const configurationMessage = new GameConfigurationMessage(
        config.humanPlays,
        config.humanPlaysAsWhite,
        undefined,
        config.pieceConfigs,
    );
    const message = new Message(
        GameConfigurationMessage.TYPE,
        configurationMessage
    );

    const body = JSON.stringify(message);

    fetch('/game', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: body
    }).then(response => {
        return response.json()
    }).then(result => {
        dispatch(gameReady(result.body.gameId));
    }).catch(err => {
        console.error("Request failed", err);
    });
};

const sendMove = (move, state) => {
    const {clientRef} = state.websocketReducer;
    const {gameId} = state.configReducer;
    const {sourceSquare, targetSquare} = move;

    if (!clientRef) return state;

    const moveMessage = new MoveMessage(sourceSquare.toUpperCase(), targetSquare.toUpperCase());
    const message = new Message(MoveMessage.TYPE, moveMessage);

    const json = JSON.stringify(message);

    clientRef.sendMessage(`/app/game.${gameId}.move`, json);
};

const gameService = store => next => action => {
    // pass all actions through by default
    next(action);

    switch (action.type) {
        case CONFIG_SUBMITTED:
            createGame(action.payload.config, next);
            break;
        case MOVE_SEND:
            sendMove(action.payload.move, store.getState());
            break;
        default:
            break;
    }
};

export default gameService