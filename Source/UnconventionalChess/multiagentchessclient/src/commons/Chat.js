// Base message that can be rendered
export class TranscriptMessage {
    constructor(timestamp) {
        this.timestamp = timestamp;
    }
}

// Chat message
export class Message extends TranscriptMessage {
    constructor(timestamp, fromId, messageBody) {
        super(timestamp);
        this.fromId = fromId;
        this.messageBody = messageBody;
    }
}

// Record of move made
export class Move extends TranscriptMessage {
    constructor(timestamp, pieceId, fromCoords, toCoords) {
        super(timestamp);
        this.pieceId = pieceId;
        this.fromCoords = fromCoords;
        this.toCoords = toCoords;
    }
}

