import React, {Component} from "react";

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
        const gameView = <GameView onExit={this.onGameExit}/>;
        const view = initialConfig ? gameView : configView;

        // TODO place ternary operator in return (couldn't remember syntax)
        return (
            {view}
        );
    }
}

export default GameContainer;
