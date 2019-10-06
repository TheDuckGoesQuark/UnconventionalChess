import {CONFIG_SUBMITTED, gameReady} from "../components/config/ConfigActions"
import {GameConfigurationMessage, Message} from "../models/Message";

const createGame = (config, dispatch) => {
    const configurationMessage = new GameConfigurationMessage(
        config.humanPlays,
        config.humanPlaysAsWhite,
        0,
    );
    const message = new Message(
        GameConfigurationMessage.TYPE,
        0,
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

const gameService = store => next => action => {
    // pass all actions through by default
    next(action);

    switch (action.type) {
        case CONFIG_SUBMITTED:
            createGame(action.payload.config, next);
            break;
        default:
            break;
    }
};

export default gameService