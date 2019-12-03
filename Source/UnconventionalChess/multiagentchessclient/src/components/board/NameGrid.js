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


const NameGrid = (props) => {
    const width = props.boardWidth + "px";
    const boxWidth = (props.boardWidth / 8) + "px";

    return <div style={{...containerStyle, width: width, height: width}}>
        {getAllPositions().map(row => (
            <div>
                {row.map(coord => <div style={{width: boxWidth, height: boxWidth}} key={coord}>
                    {getPieceNameAtPosition(props.pieceConfigs, coord)}
                </div>)}
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
    alignItems: "stretch"
};

export default connect(mapStateToProps, mapDispatchToProps)(NameGrid)
