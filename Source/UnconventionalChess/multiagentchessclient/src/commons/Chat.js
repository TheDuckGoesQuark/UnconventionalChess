// Base message that can be rendered
class TranscriptMessage {
    constructor(timestamp) {
        this.timestamp = timestamp;
    }
}

// Chat message
class Message extends TranscriptMessage {
    constructor(timestamp, fromId, messageBody) {
        super(timestamp);
        this.fromId = fromId;
        this.messageBody = messageBody;
    }
}

// Record of move made
class Move extends TranscriptMessage {
    constructor(timestamp, pieceId, fromCoords, toCoords) {
        super(timestamp);
        this.pieceId = pieceId;
        this.fromCoords = fromCoords;
        this.toCoords = toCoords;
    }
}

