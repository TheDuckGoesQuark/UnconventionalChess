import React from "react";
import {connect} from "react-redux";
import {submitConfig, setHumanPlaysAsWhite, setHumanPlays} from "./ConfigActions";
import BooleanButton from "../BooleanButton";

const ConfigView = (props) => (
    <div style={configStyle}>
        <h3>Choose Game Mode:</h3>
        <BooleanButton
            callback={props.setHumanPlays}
            trueText={"Play against agents"}
            falseText={"Watch agents"}
            isTrue={props.humanPlays}
        />

        <h3>Who do you want to play as:</h3>
        <BooleanButton
            disabled={!props.humanPlays}
            callback={props.setHumanPlaysAsWhite}
            trueText={"White"}
            falseText={"Black"}
            isTrue={props.humanPlaysAsWhite}
        />

        <button onClick={() => props.submitCurrentConfig({
            humanPlaysAsWhite: props.humanPlaysAsWhite,
            humanPlays: props.humanPlays,
        })}>
            Submit
        </button>
    </div>
);

function mapStateToProps(state) {
    return {
        humanPlaysAsWhite: state.configReducer.humanPlaysAsWhite,
        humanPlays: state.configReducer.humanPlays,
    };
}

const mapDispatchToProps = {
    setHumanPlaysAsWhite: setHumanPlaysAsWhite,
    setHumanPlays: setHumanPlays,
    submitCurrentConfig: (config) => submitConfig(config)
};

const configStyle = {
    alignItems: "center",
    justifyContent: "center",
    display: "flex",
};

export default connect(mapStateToProps, mapDispatchToProps)(ConfigView)

