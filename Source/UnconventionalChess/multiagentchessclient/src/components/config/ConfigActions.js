export const CONFIG_HUMAN_PLAYS_SET = "CONFIG_HUMAN_PLAYS_SET";
export const CONFIG_HUMAN_PLAYS_AS_WHITE_SET = "HUMAN_PLAYS_AS_WHITE_SET";
export const CONFIG_SUBMITTED = "CONFIG_SUBMITTED";
export const CONFIG_GAME_READY = "CONFIG_GAME_READY";
export const CONFIG_RESET = "CONFIG_RESET";
export const CONFIG_SQUARE_CLICKED = "CONFIG_SQUARE_CLICKED";
export const CONFIG_FETCH_PERSONALITIES = "CONFIG_FETCH_PERSONALITIES";
export const CONFIG_FETCH_PERSONALITIES_SUCCESS = "CONFIG_FETCH_PERSONALITIES_SUCCESS";
export const CONFIG_FETCH_PERSONALITIES_ERROR = "CONFIG_FETCH_PERSONALITIES_ERROR";

/**
 * Set whether human should play as white
 * @param humanPlaysAsWhite boolean value
 * @returns {{payload: {humanPlaysAsWhite: *}, type: *}}
 */
export const setHumanPlaysAsWhite = (humanPlaysAsWhite) => ({
    type: CONFIG_HUMAN_PLAYS_AS_WHITE_SET,
    payload: {humanPlaysAsWhite}
});

/**
 * Submit config for processing
 * @returns {{type: *}}
 */
export const submitConfig = (config) => ({
    type: CONFIG_SUBMITTED,
    payload: {config}
});

/**
 * Set whether a human will play, or if only agents will play
 * @param humanPlays
 * @returns {{type: *}}
 */
export const setHumanPlays = (humanPlays) => ({
    type: CONFIG_HUMAN_PLAYS_SET,
    payload: {humanPlays}
});

/**
 * Config finished processing, game is ready
 * @param gameId id of game that has been prepared
 * @returns {{type: *}}
 */
export const gameReady = (gameId) => ({
    type: CONFIG_GAME_READY,
    payload: {gameId}
});

/**
 * Clear current configuration
 * @returns {{type: *}}
 */
export const resetConfig = () => ({
    type: CONFIG_RESET,
});

/**
 * Register that square on config board was clicked
 * @param square square that was clicked
 * @returns {{payload: {square: *}, type: string}}
 */
export const configSquareClicked = (square) => ({
    type: CONFIG_SQUARE_CLICKED,
    payload: {square}
});

/**
 * Send on load for fetching initial config
 * @returns {{type: string}}
 */
export const fetchPersonalities = () => ({
    type: CONFIG_FETCH_PERSONALITIES,
});

/**
 * Dispatched when initial configuration is fetched successfully
 * @param initialPieceConfigs
 * @returns {{payload: {initialPieceConfigs: *}, type: string}}
 */
export const fetchPersonalityTypesSuccess = (initialPieceConfigs) => ({
    type: CONFIG_FETCH_PERSONALITIES_SUCCESS,
    payload: {initialPieceConfigs}
});

export const fetchPersonalityTypesError = (error) => ({
    type: CONFIG_FETCH_PERSONALITIES_ERROR,
    payload: {error}
});
