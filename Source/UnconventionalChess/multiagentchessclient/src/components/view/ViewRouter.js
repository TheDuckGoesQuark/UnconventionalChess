import React from "react";
import {connect} from "react-redux";

import ConfigView from "../config/ConfigView";
import GameContainer from "./GameContainer";

const ViewRouter = (props) => (
    props.gameReady ? <GameContainer/> : <ConfigView/>
);

function mapStateToProps(state) {
    return {
        gameReady: state.configReducer.gameId !== undefined,
    }
}

export default connect(mapStateToProps)(ViewRouter);
