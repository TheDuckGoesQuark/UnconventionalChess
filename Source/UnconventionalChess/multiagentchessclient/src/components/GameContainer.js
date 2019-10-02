import React from "react";
import {connect} from "react-redux";

import ConfigView from "./ConfigView";
import GameView from "./GameView";

const GameContainer = (props) => (
    props.initialConfig ? <GameView/> : <ConfigView/>
);

function mapStateToProps(state) {
    return {
        initialConfig: state.initialConfig
    }
}

export default connect(mapStateToProps)(GameContainer);
