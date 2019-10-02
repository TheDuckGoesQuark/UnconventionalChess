export const CONFIG_HUMAN_PLAYS_FIRST_SET = "HUMAN_PLAYERS_FIRST_SET";
export const CONFIG_SUBMITTED = "CONFIG_SUBMITTED";

export const setHumanPlaysFirst = (humanPlaysFirst) => ({
    type: CONFIG_HUMAN_PLAYS_FIRST_SET,
    payload: {humanPlaysFirst}
});

export const submitConfig = () => ({
    type: CONFIG_SUBMITTED
});
