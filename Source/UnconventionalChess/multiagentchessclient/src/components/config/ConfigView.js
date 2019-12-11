import React from "react";
import {connect} from "react-redux";
import {submitConfig, setHumanPlaysAsWhite, setHumanPlays} from "./ConfigActions";
import BooleanButton from "../BooleanButton";
import ConfigBoard from "./ConfigBoard";

const ConfigView = (props) => {
    return <div style={configStyle}>
        <h1>Unconventional Chess</h1>
        <form style={{textAlign: "center"}}>
            <ul style={listStyle}>
                <li>
                    <h2>Game Configuration</h2>
                </li>
                <li>
                    <label>
                        Game Mode
                        <BooleanButton
                            callback={props.setHumanPlays}
                            trueText={"Play against agents"}
                            falseText={"Watch agents"}
                            isTrue={props.humanPlays}
                        />
                    </label>
                </li>
                <li>
                    <label>
                        Play As:
                        <BooleanButton
                            disabled={!props.humanPlays}
                            callback={props.setHumanPlaysAsWhite}
                            trueText={"White"}
                            falseText={"Black"}
                            isTrue={props.humanPlaysAsWhite}
                        />
                    </label>
                </li>
                <li>
                    <h2>Piece Configuration</h2>
                    <label>
                        Click pieces to configure their personalities.
                        <ConfigBoard/>
                    </label>
                </li>
                <li>
                    <button type="button" onClick={() => props.submitCurrentConfig(props.currentConfig)}>
                        Submit
                    </button>
                </li>
            </ul>
        </form>
    </div>
};

function mapStateToProps(state) {
    return {
        humanPlaysAsWhite: state.configReducer.humanPlaysAsWhite,
        humanPlays: state.configReducer.humanPlays,
        currentConfig: state.configReducer
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

const listStyle = {
    display: "inline-block",
    listStyle: "none"
};

export default connect(mapStateToProps, mapDispatchToProps)(ConfigView)

