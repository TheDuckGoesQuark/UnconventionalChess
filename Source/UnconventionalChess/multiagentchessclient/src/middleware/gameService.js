import {CONFIG_SUBMITTED, gameReady} from "../components/config/ConfigActions"

const createGame = (config) => {
    fetch('/api/game', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(config)
    }).then(response => {
        return response.json()
    }).then(result => {
        next(gameReady(result.gameId))
    }).catch(err => {
        console.error("Request failed", err);
    });
};

const gameService = store => next => action => {
    // pass all actions through by default
    next(action);

    switch (action.type) {
        case CONFIG_SUBMITTED:
            createGame(action.payload.config);
            break;
        default:
            break;
    }
};

export default gameService