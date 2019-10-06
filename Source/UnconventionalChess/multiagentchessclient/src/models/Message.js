export class Message {
    type;
    gameId;
    body;

    constructor(type, gameId, body) {
        this.type = type;
        this.gameId = gameId;
        this.body = body;
    }
}

// Chat message
export class ChatMessage {
    fromId;
    toId;
    messageBody;

    constructor(timestamp, fromId, toId, messageBody) {
        this.fromId = fromId;
        this.toId = toId;
        this.messageBody = messageBody;
    }

    static get TYPE() {
        return "CHAT_MESSAGE";
    }
}

// Record of move made
export class MoveMessage {
    piece;
    sourceSquare;
    targetSquare;

    constructor(sourceSquare, targetSquare, piece) {
        this.piece = piece;
        this.sourceSquare = sourceSquare;
        this.targetSquare = targetSquare;
    }

    static get TYPE() {
        return "MOVE_MESSAGE";
    }
}

// initial game setup
export class GameConfigurationMessage {
    humanPlays;
    humanPlaysAsWhite;
    gameId;

    constructor(humanPlays, humanPlaysAsWhite, gameId) {
        this.humanPlays = humanPlays;
        this.humanPlaysAsWhite = humanPlaysAsWhite;
        this.gameId = gameId;
    }

    static get TYPE() {
        return "GAME_CONFIGURATION_MESSAGE";
    }
}

