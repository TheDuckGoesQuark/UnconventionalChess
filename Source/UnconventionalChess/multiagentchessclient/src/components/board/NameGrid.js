import React from "react";
import {
    moveSend,
} from "./BoardActions";
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

const getPieceNameAtPosition = (pieceConfigs, coord) => {
    return pieceConfigs[coord] ? pieceConfigs[coord].name : undefined;
};

const renderNameTag = (boardWidth, coord, pieceConfigs) => {
    const boxWidth = (boardWidth / 8) + "px";

    const emptyBoxStyle = {
        width: boxWidth,
        height: boxWidth,
        visibility: "hidden", // stops name grid blocking chess detection
    };

    const pieceBoxStyle = {
        ...emptyBoxStyle,
        visibility: "visible", // only show if nametag exists
    };

    const pieceName = getPieceNameAtPosition(pieceConfigs, coord);
    if (pieceName) {
        return (<div style={pieceBoxStyle} key={coord}>
            {getPieceNameAtPosition(pieceConfigs, coord)}
        </div>)
    } else {
        return (<div style={emptyBoxStyle} key={coord}/>)
    }

};

const NameGrid = (props) => {
    const {boardWidth, pieceConfigs} = props;
    const width = boardWidth + "px";

    return <div style={{...containerStyle, width: width, height: width}}>
        {getAllPositions().map((row, idx) => (
            <div key={idx}>
                {row.map(coord => renderNameTag(boardWidth, coord, pieceConfigs))}
            </div>
        ))}
    </div>
};

const mapDispatchToProps = {
    sendMove: moveSend,
};

function mapStateToProps(state) {
    return {
        boardWidth: state.boardReducer.boardWidth,
        pieceConfigs: state.configReducer.pieceConfigs
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

export default connect(mapStateToProps, mapDispatchToProps)(NameGrid)
