import {CONFIG_SUBMITTED, gameReady} from "../components/config/ConfigActions"
import {GameConfigurationMessage, Message, MoveMessage} from "../models/Message";
import {MOVE_SEND} from "../components/board/BoardActions";

const createGame = (config, dispatch) => {
    const configurationMessage = new GameConfigurationMessage(
        config.humanPlays,
        config.humanPlaysAsWhite,
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
    const {sourceSquare, targetSquare, piece} = move;

    if (!clientRef) return state;

    const moveMessage = new MoveMessage(sourceSquare, targetSquare, piece);
    const message = new Message(MoveMessage.TYPE, moveMessage);

    const json = JSON.stringify(message);
    console.log(json);

    clientRef.sendMessage(`/game.${gameId}.move`, json);
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