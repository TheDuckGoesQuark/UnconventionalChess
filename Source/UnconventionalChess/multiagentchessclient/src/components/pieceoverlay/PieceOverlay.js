import React from "react";
import {
    moveSend,
} from "../board/BoardActions";
import {connect} from "react-redux";

const range = (start, end) => {
    if (start === end) return [start];
    return [start, ...range((start < end ? start + 1 : start - 1), end)];
};

const getAllPositions = () => {
    return range(0, 7).map(row => {
        let letter = (row + 10).toString(36);
        return range(8, 1).map(number => letter + number)
    });
};

const getPieceAtPosition = (pieceConfigs, coord) => {
    return pieceConfigs[coord];
};

const getMostRecentChat = (messages) => {
    for (let message of messages) {
        if (message.fromId) {
            return message;
        }
    }

    return undefined;
};

const renderSpeech = (chat) => {
    const speechStyle = {
        visibility: "visible",
        pointerEvents: "none",
        position: "absolute",
        backgroundColor: "white",
        zIndex: 200,
    };
    return <div style={speechStyle}>
        {chat.messageBody}
    </div>
};

const renderPieceOverlay = (mostRecentMessage, boardWidth, coord, pieceConfigs) => {
    const boxWidth = (boardWidth / 8) + "px";

    const emptyBoxStyle = {
        width: boxWidth,
        height: boxWidth,
        visibility: "hidden", // stops name grid blocking chess detection
    };

    const pieceBoxStyle = {
        ...emptyBoxStyle,
        visibility: "visible", // only show if nametag exists
        alignSelf: "center",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        pointerEvents: "none",
    };

    const nametagStyle = {
        alignSelf: "center",
        backgroundColor: "DarkGrey",
        opacity: 0.8,
        zIndex: 100, // shown nametag above chess pieces
        position: "relative",
    };

    let piece = getPieceAtPosition(pieceConfigs, coord);
    if (piece) {
        return (<div>
            <div style={pieceBoxStyle} key={coord}>
                <div style={nametagStyle}>{piece.name}</div>
            </div>
            {mostRecentMessage && (mostRecentMessage.fromId === piece.name) ? renderSpeech(mostRecentMessage) : null}
        </div>)
    } else {
        return (<div style={emptyBoxStyle} key={coord}/>)
    }
};

const PieceOverlay = (props) => {
    const {boardWidth, pieceConfigs, messages} = props;
    const width = boardWidth + "px";
    let mostRecentChat = getMostRecentChat(messages);

    return <div style={{...containerStyle, width: width, height: width}}>
        {getAllPositions().map((row, idx) => (
            <div key={idx}>
                {row.map(coord => renderPieceOverlay(mostRecentChat, boardWidth, coord, pieceConfigs))}
            </div>
        ))}
    </div>
};

const mapDispatchToProps = {};

function mapStateToProps(state) {
    return {
        boardWidth: state.boardReducer.boardWidth,
        pieceConfigs: state.configReducer.pieceConfigs,
        messages: state.chatReducer.timeOrderedMessages
    }
}

const containerStyle = {
    position: "absolute",
    display: "grid",
    gridGap: 0,
    gridTemplateColumns: "repeat(8, auto)",
    gridAutoFlow: "row",
    alignItems: "stretch",
    visibility: "hidden",
};

export default connect(mapStateToProps, mapDispatchToProps)(PieceOverlay)
