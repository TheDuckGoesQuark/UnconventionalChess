import {ChatMessage, MoveMessage} from "./Chat";

class BaseMessage {
    constructor(type) {
        this.type = type
    }
}

/**
 * Parses plain JS object to instance of model depending on type property
 * @param message message to parse
 * @returns {MoveMessage|ChatMessage|undefined}
 */
export function parseMessage(message) {
    switch (message.type) {
        case ChatMessage.type:
            return new ChatMessage(message.timestamp, message.fromId, message.messageBody);
        case MoveMessage.type:
            return new MoveMessage(message.timestamp, message.fromCoords, message.toCoords);
        default:
            return undefined;
    }
}

export default BaseMessage;