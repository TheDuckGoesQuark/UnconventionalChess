import React, {Component} from "react";
import PropTypes from "prop-types";
import InitialConfiguration from "../commons/InitialConfiguration";

class ConfigView extends Component {

    static propTypes = {onConfirmation: PropTypes.func};

    playAsWhiteButton = () => {
        return <button onClick={() => this.startGame(true)}>White</button>;
    };

    playAsBlackButton = () => {
        return <button onClick={() => this.startGame(false)}>Black</button>;
    };

    startGame = (humanStarts) => {
        this.props.onConfirmation(new InitialConfiguration(humanStarts));
    };

    render() {
        return <div>
            <h1>Hey you're looking at the config view, good on you buddy!</h1>
            <h3>Who do you want to play as:</h3>
            {this.playAsWhiteButton()}
            {this.playAsBlackButton()}
        </div>
    }
}

export default ConfigView;

