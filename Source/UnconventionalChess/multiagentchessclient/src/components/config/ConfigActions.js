export const CONFIG_HUMAN_PLAYS_SET = "CONFIG_HUMAN_PLAYS_SET";
export const CONFIG_HUMAN_PLAYS_AS_WHITE_SET = "HUMAN_PLAYS_AS_WHITE_SET";
export const CONFIG_SUBMITTED = "CONFIG_SUBMITTED";
export const CONFIG_GAME_READY = "CONFIG_GAME_READY";
export const CONFIG_RESET = "CONFIG_RESET";

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
