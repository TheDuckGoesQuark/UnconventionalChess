import React from "react";
import {connect} from "react-redux";

import ConfigView from "../config/ConfigView";
import GameContainer from "./GameContainer";

const ViewRouter = (props) => (
    props.started ? <GameContainer/> : <ConfigView/>
);

function mapStateToProps(state) {
    return {
        started: state.started,
    }
}

export default connect(mapStateToProps)(ViewRouter);
