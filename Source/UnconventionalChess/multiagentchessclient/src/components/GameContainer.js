import React, {Component} from "react";

import ConfigView from "./ConfigView";
import GameView from "./GameView";

class GameContainer extends Component {

    state = {
        // initial configuration for game (player, agent configs)
        initialConfig: undefined
    };

    // On config confirmation, apply new configuration
    onConfirmation = (initialConfig) => {
        this.setState(({initialConfig: initialConfig}));
    };

    // On game exit, clear the configuration
    onGameExit() {
        this.setState({initialConfig: undefined})
    }

    render() {
        const {initialConfig} = this.state;

        const configView = <ConfigView onConfirmation={this.onConfirmation}/>;
        const gameView = <GameView initialConfig={initialConfig} onExit={this.onGameExit}/>;

        return (
            initialConfig ? gameView : configView
        );
    }
}

export default GameContainer;
