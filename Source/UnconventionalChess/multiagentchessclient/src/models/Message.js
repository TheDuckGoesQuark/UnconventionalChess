export class Message {
    type;
    body;

    constructor(type, body) {
        this.type = type;
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
    sourceSquare;
    targetSquare;

    constructor(sourceSquare, targetSquare) {
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
    pieceConfigs;
    gameId;

    constructor(humanPlays, humanPlaysAsWhite, gameId, pieceConfigs) {
        this.humanPlays = humanPlays;
        this.humanPlaysAsWhite = humanPlaysAsWhite;
        this.gameId = gameId;
        this.pieceConfigs = pieceConfigs;
    }

    static get TYPE() {
        return "GAME_CONFIGURATION_MESSAGE";
    }
}

