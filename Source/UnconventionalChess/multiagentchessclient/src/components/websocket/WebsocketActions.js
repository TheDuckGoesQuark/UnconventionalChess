export const WS_CONNECTED = "WS_CONNECTED";
export const WS_DISCONNECTED = "WS_DISCONNECTED";
export const WS_INITIALISED = "WS_INITIALISE";

export const wsConnected = () => ({
    type: WS_CONNECTED
});

export const wsDisconnected = () => ({
    type: WS_DISCONNECTED
});

export const wsInitialised = (clientRef) => ({
    type: WS_INITIALISED,
    payload: {clientRef}
});
