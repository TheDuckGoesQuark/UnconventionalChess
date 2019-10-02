export const MOVE_SEND = "MOVE_SEND";
export const MOVE_RECEIVE = "MOVE_RECEIVE";

export const moveSend = (move) => ({
    type: MOVE_SEND,
    payload: {move}
});

export const moveReceive = (move) => ({
    type: MOVE_RECEIVE,
    payload: {move}
});
