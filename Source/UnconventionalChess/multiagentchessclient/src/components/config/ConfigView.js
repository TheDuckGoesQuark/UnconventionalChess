import React from "react";
import {connect} from "react-redux";
import {setIsConfigured, setHumanPlaysFirst} from "./ConfigActions";


const ConfigView = (props) => (
    <div>
        <h1>Hey you're looking at the config view, good on you buddy!</h1>
        <h3>Who do you want to play as:</h3>
        <button style={props.humanPlaysFirst ? chosenButton : null}
                onClick={() => props.setHumanPlaysFirst(true)}>White
        </button>
        <button style={!props.humanPlaysFirst ? chosenButton : null}
                onClick={() => props.setHumanPlaysFirst(false)}>Black
        </button>
        <button onClick={() => props.submitConfig()}>Submit</button>
    </div>
);

function mapStateToProps(state) {
    return {
        humanPlaysFirst: state.configReducer.humanPlaysFirst,
    };
}

const mapDispatchToProps = {
    setHumanPlaysFirst: setHumanPlaysFirst,
    submitConfig: () => setIsConfigured(true)
};

const chosenButton = {
    backgroundColor: "green"
};

export default connect(mapStateToProps, mapDispatchToProps)(ConfigView)

