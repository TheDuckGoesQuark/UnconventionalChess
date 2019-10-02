export const CONFIG_HUMAN_PLAYS_FIRST_SET = "HUMAN_PLAYERS_FIRST_SET";
export const CONFIG_IS_SET = "CONFIG_IS_SET";

export const setHumanPlaysFirst = (humanPlaysFirst) => ({
    type: CONFIG_HUMAN_PLAYS_FIRST_SET,
    payload: {humanPlaysFirst}
});

export const setIsConfigured = (isConfigured) => ({
    type: CONFIG_IS_SET,
    payload: {isConfigured}
});
