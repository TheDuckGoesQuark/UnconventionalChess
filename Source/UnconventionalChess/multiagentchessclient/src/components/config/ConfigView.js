import React from "react";
import {connect} from "react-redux";
import {submitConfig, setHumanPlaysAsWhite, setHumanPlays} from "./ConfigActions";
import BooleanButton from "../BooleanButton";
import ConfigBoard from "./ConfigBoard";

const ConfigView = (props) => {
    return <div style={configStyle}>
        <form>
            <ul style={listStyle}>
                <li>
                    <label>
                        Game Mode
                    </label>
                    <BooleanButton
                        callback={props.setHumanPlays}
                        trueText={"Play against agents"}
                        falseText={"Watch agents"}
                        isTrue={props.humanPlays}
                    />
                </li>
                <li>
                    <label>
                        Play As:
                    </label>
                    <BooleanButton
                        disabled={!props.humanPlays}
                        callback={props.setHumanPlaysAsWhite}
                        trueText={"White"}
                        falseText={"Black"}
                        isTrue={props.humanPlaysAsWhite}
                    />
                </li>
                <li>
                    <label>
                        Click pieces to configure their personalities.
                    </label>
                    <ConfigBoard/>
                </li>
                <li>
                    <button onClick={() => props.submitCurrentConfig({
                        humanPlaysAsWhite: props.humanPlaysAsWhite,
                        humanPlays: props.humanPlays,
                    })}>
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
    listStyle: "none"
};

export default connect(mapStateToProps, mapDispatchToProps)(ConfigView)

