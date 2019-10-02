export const CHAT_MESSAGE_RECEIVE = "CHAT_MESSAGE_RECEIVE";

export const receiveChatMessage = (message) => ({
    type: CHAT_MESSAGE_RECEIVE,
    payload: {message}
});
