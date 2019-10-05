import {CONFIG_SUBMITTED, gameReady} from "../config/ConfigActions";

const gameService = store => next => action => {
    // pass all actions through by default
    next(action);

    switch (action.type) {
        case CONFIG_SUBMITTED:
            const gameId = fetch('/api/game', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(action.payload.config)
            }).then(response => {
                return response.json()
            }).then(result => {
                next(gameReady(result.gameId))
            }).catch(err => {
                console.error("Request failed", err);
            });
            break;
        default:
            break;
    }
};

export default gameService