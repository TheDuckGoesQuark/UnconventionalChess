import BaseMessage from "./BaseMessage";

// Base message that can be rendered
export class TranscriptMessage extends BaseMessage {
    constructor(timestamp, type) {
        super(type);

        // enforce this class an abstract type
        if (new.target === TranscriptMessage) {
            throw new TypeError("Cannot construct TranscriptMessage instances directly");
        }

        this.timestamp = timestamp;
    }
}

// Chat message
export class ChatMessage extends TranscriptMessage {
    constructor(timestamp, fromId, messageBody) {
        super(timestamp);
        this.fromId = fromId;
        this.messageBody = messageBody;
    }

    static get TYPE() {
        return "ChatMessage";
    }
}

// Record of move made
export class MoveMessage extends TranscriptMessage {
    constructor(timestamp, sourceSquare, targetSquare, piece) {
        super(timestamp);
        this.piece = piece;
        this.sourceSquare = sourceSquare;
        this.targetSquare = targetSquare;
    }

    static get TYPE() {
        return "MoveMessage";
    }
}

